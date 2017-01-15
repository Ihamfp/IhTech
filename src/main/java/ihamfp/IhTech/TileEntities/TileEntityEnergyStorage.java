package ihamfp.IhTech.TileEntities;

import ihamfp.IhTech.blocks.BlockEnergyStorage;
import ihamfp.IhTech.common.PacketHandler;
import ihamfp.IhTech.common.packets.PacketEnergyChange;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;


/*
 * Rules for an energy system:
 *  - Do NOT ask for energy, the provider block must send you the energy
 *  - As much as possible, update on neighbor block/TE change
 *   - Use worldObj.getTotalWorldTime() to prevent updating multiple times per tick
 *  - Only markDirty()/updateToClient() if something really changed
 */
public class TileEntityEnergyStorage extends TileEntity implements ITileEntityEnergyStorage, ITickable {
	EnergyStorage energy;
	
	@Override
	public EnergyStorage getEnergyStorage() {
		return energy;
	}
	
	@Override
	public void setEnergyStorage(EnergyStorage energy) {
		this.energy = energy;
		markDirty();
	}
	
	@Override
	public void setEnergyParameters(int capacity, int maxReceive, int maxExtract) {
		EnergyStorage newEnergyStorage = new EnergyStorage(capacity, maxReceive, maxExtract);
		this.energy = newEnergyStorage;
	}
	
	@Override
	public EnumEnergySideTypes getEnergySideType(EnumFacing face) {
		return EnumEnergySideTypes.IO;
	}
	
	@Override
	public void updateGlobalEnergySharing() {
		if (this.getWorld().isRemote)
			return;
		EnergyStorage thisStorage = this.getEnergyStorage();
		boolean changed = false;
		for (EnumFacing face : EnumFacing.values()) {
			TileEntity te = this.worldObj.getTileEntity(getPos().offset(face));
			if (!(te instanceof ITileEntityEnergyStorage) || te.isInvalid())
				continue;
			if (this.getEnergySideType(face) == EnumEnergySideTypes.BLOCKED || this.getEnergySideType(face) == EnumEnergySideTypes.RECEIVE)
				continue;
			
			ITileEntityEnergyStorage teEnergy = (TileEntityEnergyStorage)te;
			EnergyStorage storage = teEnergy.getEnergyStorage();
			if (!storage.canReceive())
				continue;
			
			switch (teEnergy.getEnergySideType(face.getOpposite())) {
			case IO:
			case RECEIVE: // send energy to this TE
				int maxGive = thisStorage.extractEnergy(thisStorage.getMaxEnergyStored(), true); // check how much it can extract
				int given = storage.receiveEnergy(maxGive, false);
				thisStorage.extractEnergy(given, false);
				
				te.markDirty();
				teEnergy.updateToClient();
				changed = true;
				break;
				
			default:
				break;
			}
		}
		if (changed) {
			this.markDirty();
			this.updateToClient();
		}
	}

	@Override
	public void update() {
		this.updateGlobalEnergySharing();
	}
	
	@Override
	public void updateToClient() {
		if (this.worldObj.isRemote)
			return;
		PacketEnergyChange packet = new PacketEnergyChange(this.getPos(), this.getEnergyStorage().getEnergyStored());
		PacketHandler.INSTANCE.sendToAllAround(packet, new TargetPoint(this.worldObj.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64));
		IBlockState bs = this.worldObj.getBlockState(this.getPos());
		this.worldObj.notifyBlockUpdate(this.getPos(), bs, bs, 0);
	}
	
	// NBT stuff
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		int energyStored = compound.getInteger("energyStored");
		int energyCapacity = compound.getInteger("energyCapacity");
		if (this.energy == null) {
			this.energy = new EnergyStorage(energyCapacity);
		}
		if (energy.getEnergyStored() < energyStored) {
			energy.receiveEnergy(energyStored-energy.getEnergyStored(), false);
		} else if (energy.getEnergyStored() > energyStored) {
			energy.extractEnergy(energy.getEnergyStored()-energyStored, false);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("energyStored", energy.getEnergyStored());
		compound.setInteger("energyCapacity", energy.getMaxEnergyStored());
		return compound;
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
