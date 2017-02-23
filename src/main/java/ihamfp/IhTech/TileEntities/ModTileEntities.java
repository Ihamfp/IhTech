package ihamfp.IhTech.TileEntities;

import ihamfp.IhTech.ModIhTech;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
	public static void preInit() {
		GameRegistry.registerTileEntity(TileEntityEnergyStorage.class, ModIhTech.MODID + "_EnergyStorage");
		GameRegistry.registerTileEntity(TileEntityItemEnergyGenerator.class, ModIhTech.MODID + "_FuelEnergyGenerator");
		GameRegistry.registerTileEntity(TileEntitySolarEnergyGenerator.class, ModIhTech.MODID + "_SolarEnergyGenerator");
		GameRegistry.registerTileEntity(TileEntityBatteryRack.class, ModIhTech.MODID + "_BatteryRack");
		GameRegistry.registerTileEntity(TileEntityEnergyCable.class, ModIhTech.MODID + "_EnergyCable");
	}
}
