package ihamfp.IhTech.interfaces;

import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage.EnumEnergySideTypes;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.EnergyStorage;

public interface ITileEntityEnergyStorage {
	enum EnumEnergySideTypes {
		BLOCKED,
		SEND,
		RECEIVE,
		IO
	}
	
	EnergyStorage getEnergyStorage();
	void setEnergyStorage(EnergyStorage energy);
	void setEnergyParameters(int capacity, int maxReceive, int maxExtract);
	void updateToClient();
	EnumEnergySideTypes getEnergySideType(EnumFacing face);
	void updateGlobalEnergySharing();
}
