package ihamfp.IhTech.TileEntities;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.blocks.BlockEnergyStorage;
import ihamfp.IhTech.common.Config;
import ihamfp.IhTech.common.PacketHandler;
import ihamfp.IhTech.common.packets.PacketEnergyChange;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import net.minecraft.block.Block;
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
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;


/* Rules for an energy system:
 *  - Do NOT ask for energy, the provider block must send you the energy
 *  - As much as possible, update on neighbor block/TE change
 *   - Use worldObj.getTotalWorldTime() to prevent updating multiple times per tick
 *  - Only markDirty()/updateToClient() if something really changed
 */

/* Any function declared here dealing with energyStorage _MUST_ perform a null-check, as this value can be null.
 * Also, prefer using getEnergyStorage() over just this.energyStorage.
 */
public class TileEntityEnergyStorage extends TileEntity implements ITileEntityEnergyStorage, ITickable {
	public int capacity = 10000; // default capacity for all electric blocks
	
	protected EnergyStorage energyStorage = new EnergyStorage(capacity);
	
	
	public void setCapacity(int cap) {
		this.capacity = cap;
	}
	
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
		return this.energyStorage;
	}
	
	@Override
	public EnumEnergySideTypes getEnergySideType(EnumFacing face) {
		return EnumEnergySideTypes.IO;
	}
	
	/***
	 * Sends energy to TEs around which can receive it.
	 */
	public void updateGlobalEnergySharing() {
		if (this.getWorld().isRemote) return;
		IEnergyStorage thisStorage = this.getEnergyStorage();
		if (thisStorage == null) return;
		if (thisStorage.getEnergyStored() == 0) return;
		boolean changed = false;
		for (EnumFacing face : EnumFacing.values()) {
			if (!this.hasCapability(CapabilityEnergy.ENERGY, face)) continue;
			TileEntity te = this.world.getTileEntity(this.getPos().offset(face));
			if (te == null || te.isInvalid() || !te.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())) continue;
			if (Config.limitEnergyCompatiblity && !(te instanceof ITileEntityEnergyStorage)) continue;
			if (this.getEnergySideType(face) == EnumEnergySideTypes.BLOCKED || this.getEnergySideType(face) == EnumEnergySideTypes.RECEIVE) continue;
			
			IEnergyStorage teStorage = (IEnergyStorage)te.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
			if (teStorage == null) continue;
			if (!teStorage.canReceive()) continue;
			
			// if the other tile is from this mod, check its energy sides types, otherwise just try to inject.
			if (te instanceof ITileEntityEnergyStorage && ((ITileEntityEnergyStorage)te).getEnergySideType(face.getOpposite()) == EnumEnergySideTypes.SEND) continue;
			
			if (te instanceof ITileEntityEnergyStorage) {
				ITileEntityEnergyStorage te2 = (ITileEntityEnergyStorage)te;
				switch (this.getEnergySideType(face)) {
				case IO:
					switch (te2.getEnergySideType(face.getOpposite())) {
					case IO:
						this.balanceEnergy(te2.getEnergyStorage());
						te.markDirty();
						break;
					
					case RECEIVE:
						this.sendMaxEnergy(te2.getEnergyStorage());
						te.markDirty();
						break;
					
					default: // SEND
						break;
					}
				case SEND:
					this.sendMaxEnergy(te2.getEnergyStorage());
					changed = true;
					te.markDirty();
					break;
				
				default: // RECEIVE
					break;
				}
			} else {
				int maxGive = thisStorage.extractEnergy(thisStorage.getMaxEnergyStored(), true);
				int given = teStorage.receiveEnergy(maxGive, false);
				thisStorage.extractEnergy(given, false);
				if (given == 0) continue;
				
				te.markDirty();
				changed = true;
			}
			
			if (te instanceof ITileEntityEnergyStorage) {
				((ITileEntityEnergyStorage)te).updateToClient();
			}
		}
		if (changed) {
			this.markDirty();
			this.updateToClient();
		}
	}
	
	// Energy sharing utils
	public void sendMaxEnergy(IEnergyStorage to) {
		IEnergyStorage from = this.getEnergyStorage();
		if (!(to.canReceive() && from.canExtract())) return;
		int maxGive = from.extractEnergy(from.getMaxEnergyStored(), true);
		int given = to.receiveEnergy(maxGive, false);
		from.extractEnergy(given, false);
	}
	
	public void balanceEnergy(IEnergyStorage with) {
		IEnergyStorage from = this.getEnergyStorage();
		if (!(with.canReceive() && from.canExtract())) return;
		int toGive = (from.getEnergyStored() - with.getEnergyStored())/2;
		if (toGive <= 0) return;
		
		int maxGive = from.extractEnergy(toGive, true);
		int given = with.receiveEnergy(maxGive, false);
		from.extractEnergy(given, false);
	}
	
	
	// Everything update-related
	int lastEnergy = 0;
	@Override
	public void update() {
		//ModIhTech.logger.info("update() from TEEnergyStorage");
		IEnergyStorage thisStorage = this.getEnergyStorage();
		if ((thisStorage == null || thisStorage.getMaxEnergyStored() == 0) && !this.getWorld().isAirBlock(this.getPos())) {
			ModIhTech.logger.debug("Tile at " + this.getPos() + " has no energy storage.");
			Block thisBlock = this.getWorld().getBlockState(this.getPos()).getBlock();
			if (thisBlock instanceof BlockEnergyStorage) {
				this.energyStorage = new EnergyStorage(this.capacity);
			} else {
				ModIhTech.logger.debug("Could not set energyStorage: block is not a BlockEnergyStorage");
				return;
			}
		}
		this.updateGlobalEnergySharing();
		if (this.getEnergyStorage().getEnergyStored() != this.lastEnergy) {
			this.lastEnergy = thisStorage.getEnergyStored();
			this.updateToClient();
		}
	}
	
	@Override
	public void updateToClient() {
		if (this.world.isRemote)
			return;
		
		/*if (this.getEnergyStorage() != null) {
			PacketEnergyChange packet = new PacketEnergyChange(this.getPos(), this.getEnergyStorage().getEnergyStored());
			PacketHandler.INSTANCE.sendToAllAround(packet, new TargetPoint(this.worldObj.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64));
		}*/
		IBlockState bs = this.world.getBlockState(this.getPos());
		this.world.notifyBlockUpdate(this.getPos(), bs, bs, 0);
	}
	
	// NBT stuff
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
