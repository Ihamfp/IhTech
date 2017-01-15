package ihamfp.IhTech.TileEntities;

import java.util.List;

import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.EnergyStorage;

public class TileEntityEnergyCable extends TileEntity implements ITileEntityEnergyStorage {
	
	enum EnumCableSideRenderType {
		NONE, // not connected
		CABLE, // connected to a cable
		BLOCK, // connected to an energy-compatible block
	}
	
	EnergyStorage energyStorage;
	List<BlockPos> nodesList; // Add to this list ONLY if the block can exchange energy
	boolean isMaster = false; /* there should be only 1 master which updates every node
	                           * if the master is destroyed, just pick a random one in
	                           * the nodesList. */
	
	public TileEntityEnergyCable() {
		
	}

	@Override
	public EnergyStorage getEnergyStorage() {
		return this.energyStorage;
	}

	@Override
	public void setEnergyStorage(EnergyStorage energy) {
		this.energyStorage = energy;
	}

	@Override
	public void setEnergyParameters(int capacity, int maxReceive, int maxExtract) {
		
	}

	@Override
	public void updateToClient() {
		if (!isMaster) return;
	}

	@Override
	public EnumEnergySideTypes getEnergySideType(EnumFacing face) {
		return EnumEnergySideTypes.IO;
	}
	
	private EnumCableSideRenderType getSideRenderType(EnumFacing face) {
		TileEntity otherTile = this.worldObj.getTileEntity(this.getPos().offset(face));
		if (otherTile instanceof TileEntityEnergyCable) {
			return EnumCableSideRenderType.CABLE;
		} else if (otherTile instanceof ITileEntityEnergyStorage) {
			return EnumCableSideRenderType.BLOCK;
		} else {
			return EnumCableSideRenderType.NONE;
		}
	}
	
	@Override
	public void updateGlobalEnergySharing() {
		// update the node here
	}

}
