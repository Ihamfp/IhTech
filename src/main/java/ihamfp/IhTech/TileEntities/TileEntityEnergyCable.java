package ihamfp.IhTech.TileEntities;

import java.util.List;

import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public class TileEntityEnergyCable extends TileEntityEnergyStorage {
	
	enum EnumCableSideRenderType {
		NONE, // not connected
		CABLE, // connected to a cable
		BLOCK, // connected to an energy-compatible block
	}
	
	EnergyStorage energyStorage;
	
	public TileEntityEnergyCable() {
		
	}

	@Override
	public EnumEnergySideTypes getEnergySideType(EnumFacing face) {
		return EnumEnergySideTypes.IO;
	}
	
	private EnumCableSideRenderType getSideRenderType(EnumFacing face) {
		TileEntity otherTile = this.worldObj.getTileEntity(this.getPos().offset(face));
		if (otherTile instanceof TileEntityEnergyCable) {
			return EnumCableSideRenderType.CABLE;
		} else if (otherTile.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())) {
			return EnumCableSideRenderType.BLOCK;
		} else {
			return EnumCableSideRenderType.NONE;
		}
	}

}
