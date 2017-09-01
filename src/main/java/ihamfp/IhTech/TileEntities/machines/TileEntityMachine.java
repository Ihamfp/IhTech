package ihamfp.IhTech.TileEntities.machines;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.ModTileEntities;
import ihamfp.IhTech.TileEntities.TileEntityEnergyStorage;
import ihamfp.IhTech.blocks.machines.BlockMachineBase;
import ihamfp.IhTech.common.PacketHandler;
import ihamfp.IhTech.common.packets.PacketMachineSimpleUpdate;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage.EnumEnergySideTypes;

public abstract class TileEntityMachine extends TileEntity implements ITileEntityInteractable, ITickable {
	protected class MachineItemStackHandler extends ItemStackHandler {
		public MachineItemStackHandler(int size) {
			super(size);
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			TileEntityMachine.this.markDirty();
		}
	}
	
	// TODO support this (low priority)
	public enum EnumSideConfig {
		ENERGY,
		INPUT,
		OUTPUT,
		FLINPUT,
		FLOUTPUT,
	}
	public Map<EnumFacing, EnumMap<EnumSideConfig, Boolean>> sideConfig = new EnumMap<EnumFacing, EnumMap<EnumSideConfig, Boolean>>(EnumFacing.class);
	
	// processing variables
	public int processTimeLeft = 0;
	public int processTime = 1;
	public ItemStack cookingItems[] = new ItemStack[this.getInputSlots().length];
	
	// slots configuration (default)
	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_SLOT = 1;
	
	// Block/world interaction
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return (oldState.getBlock() != newState.getBlock());
	}
	
	protected void setBlockStateActive(boolean active) {
		IBlockState blockState = this.world.getBlockState(this.pos);
		this.world.setBlockState(this.pos, blockState.withProperty(BlockMachineBase.ACTIVE, active));
	}
	
	protected boolean getBlockStateActive() {
		IBlockState blockState = this.world.getBlockState(this.pos);
		return blockState.getValue(BlockMachineBase.ACTIVE);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return !isInvalid() && player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
	
	//@SideOnly(Side.CLIENT)
	public void simpleUpdate(int value) {
		this.processTimeLeft = value;
	}
	
	//@SideOnly(Side.SERVER)
	public void sendSimpleUpdate(int value) {
		PacketHandler.INSTANCE.sendToAllAround(new PacketMachineSimpleUpdate(this.pos,  this.processTimeLeft), new TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64.0));
	}
	
	// indexes start from 0
	protected abstract ItemStackHandler getStackHandler();
	protected abstract boolean hasOutput(ItemStack[] input);
	protected abstract ItemStack getOutputStack(ItemStack[] input, int outputIndex);
	protected abstract float getOutputProbability(ItemStack[] input, int outputIndex);
	protected abstract int getProcessTime(ItemStack[] input);
	protected abstract int getEnergyUsage(ItemStack[] input);
	
	protected abstract int getEnergyStored();
	protected abstract void extractEnergy(int amount, boolean simulate);
	
	// Used for upgrades
	protected int getSpeedMultiplier() {
		return 1;
	}
	
	protected float getProbabilityMultiplier() {
		return 1.0f;
	}
	
	// Slots/stacks manipulation
	protected int[] getInputSlots() {
		return new int[]{this.INPUT_SLOT};
	}
	
	protected ItemStack[] getInputStacks() {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		ItemStackHandler sh = this.getStackHandler();
		int[] is = this.getInputSlots();
		
		for (int i=0;i<is.length;i++) {
			stacks.add(sh.getStackInSlot(is[i]));
		}
		return stacks.toArray(new ItemStack[stacks.size()]);
	}
	
	protected void consumeInputItems(ItemStack[] input) { // consume items on the input for 1 "cycle"
		this.getStackHandler().extractItem(this.getInputSlots()[0], 1, false);
		//input[0].splitStack(1);
	}
	
	protected int[] getOutputSlots() {
		return new int[]{this.OUTPUT_SLOT};
	}
	
	protected boolean hasInputChanged() {
		ItemStackHandler sh = this.getStackHandler();
		int[] is = this.getInputSlots();
		for (int i=0;i<is.length;i++) {
			if (cookingItems[i] == ItemStack.EMPTY && sh.getStackInSlot(is[i]) == ItemStack.EMPTY) continue; // don't care if they are both null
			if (cookingItems[i] == ItemStack.EMPTY || sh.getStackInSlot(is[i]) == ItemStack.EMPTY) return true; // the precedent statement implies that only one of them is null
			if (!ItemStack.areItemsEqual(cookingItems[i], sh.getStackInSlot(is[i]))) return true;
		}
		return false;
	}
	
	// returns true if a stack is cleared
	protected boolean clearEmptyStacks() {
		boolean hasCleaned = false;
		ItemStackHandler sh = this.getStackHandler();
		int[] is = this.getInputSlots();
		for (int i=0;i<is.length;i++) {
			if (sh.getStackInSlot(is[i]) != ItemStack.EMPTY && sh.getStackInSlot(is[i]).getCount() == 0) {
				sh.setStackInSlot(is[i], ItemStack.EMPTY);
				hasCleaned = true;
			}
		}
		return hasCleaned;
	}
	
	protected void refreshCookingItems() {
		ItemStackHandler sh = this.getStackHandler();
		int[] is = this.getInputSlots();
		
		this.cookingItems = new ItemStack[this.getInputSlots().length];
		
		for (int i=0;i<is.length;i++) {
			ItemStack iStack = sh.getStackInSlot(is[i]);
			if (iStack == ItemStack.EMPTY) {
				this.cookingItems[i] = ItemStack.EMPTY;
				continue;
			}
			this.cookingItems[i] = new ItemStack(iStack.getItem(), 64, iStack.getMetadata());
		}
	}
	
	protected void clearCookingItems() {
		this.cookingItems = new ItemStack[this.getInputSlots().length];
		for (int i=0;i<this.cookingItems.length;i++) {
			this.cookingItems[i] = ItemStack.EMPTY;
		}
	}
	
	// Big machine code
	public void update() {
		ItemStack[] inputStacks = this.getInputStacks();
		
		if (this.world.isRemote) {
			if (this.hasInputChanged()) {
				if (this.hasOutput(inputStacks)) {
					this.processTime = this.getProcessTime(inputStacks);
				} else {
					this.processTimeLeft = 0;
					this.processTime = 1;
				}
				this.refreshCookingItems();
			}
			return;
		}
		
		if (this.hasInputChanged()) { // the input item changed !
			this.clearCookingItems();
			this.processTimeLeft = 0;
			if (!this.hasOutput(inputStacks)) { // and it's not even processable !
				this.setBlockStateActive(false);
				this.markDirty();
			}
		}
		
		if (this.processTimeLeft > 0 && this.getEnergyStored() >= this.getEnergyUsage(cookingItems) && !this.hasInputChanged()) { // process items
			this.processTimeLeft -= getSpeedMultiplier();
			this.extractEnergy(this.getEnergyUsage(cookingItems), false);
			if (this.processTimeLeft <= 0) { // wow it's finished
				this.consumeInputItems(inputStacks);
				for (int i=0;i<getOutputSlots().length;i++) { // in all output slots ...
					ItemStack processed =  this.getOutputStack(inputStacks, i);
					if (processed == ItemStack.EMPTY) break;
					if (this.world.rand.nextFloat() <= (this.getOutputProbability(cookingItems, i) * this.getProbabilityMultiplier())) // checking for probabilities
						this.getStackHandler().insertItem(OUTPUT_SLOT, processed.copy(), false);
				}
				
				this.clearCookingItems(); // reset craft
				if (this.clearEmptyStacks()) {
					this.setBlockStateActive(false);
				}
				sendSimpleUpdate(0); // no process time left = 0
				this.markDirty();
				return;
			}
		} else if (this.processTimeLeft > 0 && cookingItems[0] != ItemStack.EMPTY) { // Not enough energy, item changed or is not present
			this.processTimeLeft = this.getProcessTime(cookingItems);
			sendSimpleUpdate(this.processTimeLeft); // sent as integer as it won't make a huge difference on the client side.
			this.markDirty();
			return;
		}
		
		if (this.processTimeLeft == 0 && inputStacks[0] != ItemStack.EMPTY && this.hasOutput(inputStacks)) { // start processing
			if (this.getStackHandler().insertItem(OUTPUT_SLOT, this.getOutputStack(inputStacks, 0), true) != ItemStack.EMPTY) return;
			this.refreshCookingItems();
			this.processTime = this.getProcessTime(cookingItems);
			this.processTimeLeft = this.processTime;
			this.setBlockStateActive(true);
			this.markDirty();
		}
	}
	
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
