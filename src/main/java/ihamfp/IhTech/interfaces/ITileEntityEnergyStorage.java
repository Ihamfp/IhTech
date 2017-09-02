package ihamfp.IhTech.interfaces;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

public interface ITileEntityEnergyStorage {
	enum EnumEnergySideTypes {
		BLOCKED,
		SEND,
		RECEIVE,
		IO
	}
	
	IEnergyStorage getEnergyStorage();
	void updateToClient();
	EnumEnergySideTypes getEnergySideType(EnumFacing face);
	void updateGlobalEnergySharing();
}
