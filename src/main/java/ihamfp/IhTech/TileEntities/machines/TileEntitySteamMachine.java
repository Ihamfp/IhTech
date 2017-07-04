package ihamfp.IhTech.TileEntities.machines;

import ihamfp.IhTech.fluids.ModFluids;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntitySteamMachine extends TileEntityMachine {
	public static final int STEAM_SLOT = 2;
	public static final int capacity = 8000;
	
	protected FluidTank steamTank;
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T)getEnergyStorage();
		}
		return super.getCapability(capability, facing);
	}
	
	int lastEnergy = 0;
	@Override
	public void update() {
		if (!this.worldObj.isRemote && this.getEnergyStorage().canFill()) {
			ItemStack steamStack = this.getStackHandler().getStackInSlot(STEAM_SLOT);
			if (steamStack != null && steamStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) { // bucket
				IFluidHandler itemFluid = steamStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
				if (this.getEnergyStorage().canFillFluidType(itemFluid.drain(this.capacity, false))) {
					int spaceLeft = this.getEnergyStorage().getCapacity() - this.getEnergyStorage().getFluidAmount();
					FluidStack extracted = itemFluid.drain(spaceLeft, true);
					this.getEnergyStorage().fill(extracted, true);
					this.markDirty();
				}
			}
		}
		super.update();
		if (!this.worldObj.isRemote && this.getEnergyStored() != lastEnergy) {
			this.updateToClient();
			lastEnergy = this.getEnergyStored();
		}
	}
	
	public void updateToClient() {
		if (this.worldObj.isRemote) return;
		
		IBlockState bs = this.worldObj.getBlockState(this.getPos());
		this.worldObj.notifyBlockUpdate(this.getPos(), bs, bs, 0);
	}
	
	public FluidTank getEnergyStorage() {
		if (this.steamTank == null) {
			this.steamTank = new FluidTank(this.capacity) {
				@Override
				public boolean canFillFluidType(FluidStack fluid) {
					return (fluid != null && super.canFillFluidType(fluid) && fluid.getFluid() == ModFluids.steam);
				}
			};
			steamTank.setCanDrain(false);
			steamTank.setTileEntity(this);
		}
		return this.steamTank;
	}
	
	@Override
	protected int getEnergyUsage(ItemStack[] input) {
		return 20;
	}

	@Override
	protected int getEnergyStored() {
		return this.getEnergyStorage().getFluidAmount();
	}

	@Override
	protected void extractEnergy(int amount, boolean simulate) {
		this.getEnergyStorage().drainInternal(amount, !simulate);
	}
	
	// saving/networking
	protected void readFromNBTBypassable(NBTTagCompound compound, boolean bypass) { // Yes, that is ugly
		super.readFromNBT(compound);
		if (!bypass) {
			if (compound.hasKey("energy") && this.getEnergyStorage() != null && this.getEnergyStorage() == this.steamTank) {
				CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(this.getEnergyStorage(), null, compound.getTag("energy"));
				if (this.getWorld() != null && !this.getWorld().isRemote) this.updateToClient();
			}
		}
	}
	
	protected NBTTagCompound writeToNBTBypassable(NBTTagCompound compound, boolean bypass) { // tht too
		super.writeToNBT(compound);
		if (!bypass) {
			if (this.getEnergyStorage() != null)
				compound.setTag("energy", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(this.getEnergyStorage(), null));
		}
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.readFromNBTBypassable(compound, false);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return this.writeToNBTBypassable(compound, false);
	}
	
	// Networking stuff
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
}
