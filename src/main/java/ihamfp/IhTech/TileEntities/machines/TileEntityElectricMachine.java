package ihamfp.IhTech.TileEntities.machines;

import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage.EnumEnergySideTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityElectricMachine extends TileEntityMachine implements ITileEntityEnergyStorage {

	public static final int BATT_SLOT = 2; // Not working atm
	public static final int capacity = 10000;
	
	protected EnergyStorage energyStorage = null;
	
	// Simplified TEEnergyStorage
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY && this.getEnergySideType(facing) != EnumEnergySideTypes.BLOCKED) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY && this.getEnergySideType(facing) != EnumEnergySideTypes.BLOCKED) {
			return (T)this.getEnergyStorage();
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public IEnergyStorage getEnergyStorage() {
		if (this.energyStorage == null) {
			this.energyStorage = new EnergyStorage(capacity);
		}
		return this.energyStorage;
	}
	
	int lastEnergy = 0;
	@Override
	public void update() {
		super.update();
		if (this.getEnergyStored() != this.lastEnergy) {
			this.lastEnergy = this.getEnergyStored();
			this.updateToClient();
		}
	}

	@Override
	public void updateToClient() {
		if (this.worldObj.isRemote) return;
		
		IBlockState bs = this.worldObj.getBlockState(this.getPos());
		this.worldObj.notifyBlockUpdate(this.getPos(), bs, bs, 0);
	}

	@Override
	public void updateGlobalEnergySharing() {
		return; // We only receive energy, nothing to do here
	}

	@Override
	public EnumEnergySideTypes getEnergySideType(EnumFacing face) {
		return EnumEnergySideTypes.RECEIVE;
	}
	
	// Default energy things
	@Override
	protected int getEnergyUsage(ItemStack[] input) {
		return 80;
	}

	@Override
	protected int getEnergyStored() {
		return this.getEnergyStorage().getEnergyStored();
	}
	
	@Override
	protected void extractEnergy(int amount, boolean simulate) {
		this.getEnergyStorage().extractEnergy(amount, simulate);
	}
	
	protected void readFromNBTBypassable(NBTTagCompound compound, boolean bypass) { // Yes, that is ugly
		super.readFromNBT(compound);
		if (!bypass) {
			if (this.getEnergyStorage() == null) {
				this.energyStorage = new EnergyStorage(this.capacity);
			}
			if (compound.hasKey("energy") && this.getEnergyStorage() != null && this.getEnergyStorage() == this.energyStorage) {
				CapabilityEnergy.ENERGY.readNBT(this.getEnergyStorage(), null, compound.getTag("energy"));
				if (this.getWorld() != null && !this.getWorld().isRemote) this.updateToClient();
			}
		}
	}
	
	protected NBTTagCompound writeToNBTBypassable(NBTTagCompound compound, boolean bypass) { // tht too
		super.writeToNBT(compound);
		if (!bypass) {
			if (this.getEnergyStorage() != null)
				compound.setTag("energy", CapabilityEnergy.ENERGY.writeNBT(this.getEnergyStorage(), null));
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
