package ihamfp.IhTech.TileEntities.machines;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.TileEntityEnergyStorage;
import ihamfp.IhTech.blocks.machines.BlockMachineElectricBase;
import ihamfp.IhTech.common.PacketHandler;
import ihamfp.IhTech.common.packets.PacketMachineSimpleUpdate;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage.EnumEnergySideTypes;

public abstract class TileEntityElectricMachine extends TileEntityEnergyStorage implements ITileEntityInteractable {
	
	public int processTimeLeft = 0;
	public int processTime = 1;
	public ItemStack cookingItem = null;
	
	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	public static final int BATT_SLOT = 2; // Not working atm
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return (oldState.getBlock() != newState.getBlock());
	}
	
	protected void setBlockStateActive(boolean active) {
		IBlockState blockState = this.worldObj.getBlockState(this.pos);
		this.worldObj.setBlockState(this.pos, blockState.withProperty(BlockMachineElectricBase.ACTIVE, active));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return !isInvalid() && player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
	
	@Override
	public EnumEnergySideTypes getEnergySideType(EnumFacing face) {
		return EnumEnergySideTypes.RECEIVE;
	}
	
	//@SideOnly(Side.CLIENT)
	public void simpleUpdate(int value) {
		this.processTimeLeft = value;
	}
	
	//@SideOnly(Side.SERVER)
	public void sendSimpleUpdate(int value) {
		PacketHandler.INSTANCE.sendToAllAround(new PacketMachineSimpleUpdate(this.pos,  this.processTimeLeft), new TargetPoint(this.worldObj.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64.0));
	}
	
	// indexes start from 0
	protected abstract boolean hasOutput(ItemStack input);
	protected abstract ItemStack getOutputStack(ItemStack input, int outputIndex);
	protected abstract float getOutputProbability(ItemStack input, int outputIndex);
	protected abstract int getProcessTime(ItemStack input);
	
	protected int getEnergyUsage(ItemStack input) {
		return 80;
	}
	
	public void update() {
		super.update();
		
		ItemStack itemStack = this.getStackHandler().getStackInSlot(INPUT_SLOT);
		
		if (this.worldObj.isRemote) {
			if (itemStack != null && !ItemStack.areItemsEqual(itemStack, cookingItem) && this.hasOutput(itemStack)) {
				this.processTime = this.getProcessTime(itemStack);
				this.cookingItem = new ItemStack(itemStack.getItem(), 64, itemStack.getMetadata());
			}
			return;
		}
		
		if (itemStack != null && !ItemStack.areItemsEqual(itemStack, cookingItem)) { // the input item changed !
			this.cookingItem = null;
			this.processTimeLeft = 0;
			if (!this.hasOutput(itemStack)) { // and it's not even processable !
				this.setBlockStateActive(false);
				this.markDirty();
			}
		}
		
		if (this.processTimeLeft > 0 && this.getEnergyStorage().getEnergyStored() > this.getEnergyUsage(cookingItem) && itemStack != null && ItemStack.areItemsEqual(itemStack, cookingItem)) { // process items
			this.processTimeLeft--;
			this.energyStorage.extractEnergy(this.getEnergyUsage(cookingItem), false);
			if (this.processTimeLeft == 0) { // wow it's finished
				ItemStack processed =  this.getOutputStack(itemStack.splitStack(1), 0).copy();
				if (this.worldObj.rand.nextFloat() <= this.getOutputProbability(cookingItem, 0))
					this.getStackHandler().insertItem(OUTPUT_SLOT, processed, false);
				this.cookingItem = null;
				if (itemStack.stackSize == 0) {
					this.getStackHandler().setStackInSlot(INPUT_SLOT, null);
					this.setBlockStateActive(false);
				}
				sendSimpleUpdate(this.processTimeLeft);
				this.markDirty();
				return;
			}
		} else if (this.processTimeLeft > 0 && cookingItem != null) { // Not enough energy, item changed or is not present
			this.processTimeLeft = this.getProcessTime(cookingItem);
			sendSimpleUpdate(this.processTimeLeft);
			this.markDirty();
			return;
		}
		
		if (this.processTimeLeft == 0 && itemStack != null && this.hasOutput(itemStack)) { // start processing
			if (this.getStackHandler().insertItem(OUTPUT_SLOT, this.getOutputStack(itemStack, 0), true) != null) return;
			this.cookingItem = new ItemStack(itemStack.getItem(), 64, itemStack.getMetadata());
			this.processTime = this.getProcessTime(cookingItem);
			this.processTimeLeft = this.processTime;
			this.setBlockStateActive(true);
			this.markDirty();
		}
	}
	
	// I have no idea why this could be useful
	protected void normalUpdate() {
		super.update();
	}
	
	protected abstract ItemStackHandler getStackHandler();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)this.getStackHandler();
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("cookLeft")) {
			this.processTimeLeft = compound.getInteger("cookLeft");
		}
		if (compound.hasKey("items")) {
			getStackHandler().deserializeNBT((NBTTagCompound)compound.getTag("items"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items", getStackHandler().serializeNBT());
		if (this.processTimeLeft > 0) compound.setInteger("cookLeft", this.processTimeLeft);
		return compound;
	}
}
