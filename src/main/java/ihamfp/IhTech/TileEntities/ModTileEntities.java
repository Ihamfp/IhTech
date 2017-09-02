package ihamfp.IhTech.TileEntities;

import java.util.ArrayList;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.machines.TileEntityElectricFurnace;
import ihamfp.IhTech.TileEntities.machines.TileEntityElectricGrinder;
import ihamfp.IhTech.TileEntities.machines.TileEntityMachine;
import ihamfp.IhTech.TileEntities.machines.TileEntitySteamGrinder;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
	
	public static ArrayList<Class<? extends TileEntityMachine>> machinesList = new ArrayList<Class<? extends TileEntityMachine>>();
	
	public static void registerTileEntities() {
		ModIhTech.logger.info("Registered TileEntities");
		GameRegistry.registerTileEntity(TileEntityEnergyStorage.class, ModIhTech.MODID + "_EnergyStorage");
		GameRegistry.registerTileEntity(TileEntityItemEnergyGenerator.class, ModIhTech.MODID + "_FuelEnergyGenerator");
		GameRegistry.registerTileEntity(TileEntitySolarEnergyGenerator.class, ModIhTech.MODID + "_SolarEnergyGenerator");
		GameRegistry.registerTileEntity(TileEntityBatteryRack.class, ModIhTech.MODID + "_BatteryRack");
		GameRegistry.registerTileEntity(TileEntityEnergyCable.class, ModIhTech.MODID + "_EnergyCable");
		registerMachineTileEntities();
	}
	
	public static void registerMachineTileEntities() {
		machinesList.add(TileEntityElectricFurnace.class);
		machinesList.add(TileEntityElectricGrinder.class);
		machinesList.add(TileEntitySteamGrinder.class);
		
		for (Class c : machinesList) {
			GameRegistry.registerTileEntity(c, ModIhTech.MODID + "_" + c.getSimpleName().replaceFirst("TileEntity", ""));
		}
	}
}
