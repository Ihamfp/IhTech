package ihamfp.IhTech.TileEntities.machines;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
import ihamfp.IhTech.TileEntities.TileEntityEnergyStorage;
import ihamfp.IhTech.blocks.machines.BlockMachineElectricBase;
import ihamfp.IhTech.common.PacketHandler;
import ihamfp.IhTech.common.packets.PacketMachineSimpleUpdate;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage.EnumEnergySideTypes;

public abstract class TileEntityElectricMachine extends TileEntityEnergyStorage implements ITileEntityInteractable {
	
	public int processTimeLeft = 0;
	public int processTime = 0;
	
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
