package ihamfp.IhTech.common;

import java.io.File;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TOPCompatibility;
import ihamfp.IhTech.WailaCompatibility;
import ihamfp.IhTech.blocks.ModBlocks;
import ihamfp.IhTech.creativeTabs.ModCreativeTabs;
import ihamfp.IhTech.creativeTabs.TabResources;
import ihamfp.IhTech.fluids.ModFluids;
import ihamfp.IhTech.interfaces.IProxy;
import ihamfp.IhTech.items.ModItems;
import ihamfp.IhTech.recipes.ModRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy implements IProxy {
	public static Configuration config;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(new File(event.getModConfigurationDirectory().getPath(), "ihtech.cfg"));
		Config.readConfig();
		
		ModCreativeTabs.preInit();
		ModBlocks.preInit();
		ModItems.preInit();
		ModFluids.preInit();
		PacketHandler.registerMessages();
		if (Loader.isModLoaded("Waila")) {
			WailaCompatibility.register();
		}
		if (Loader.isModLoaded("theoneprobe")) {
			TOPCompatibility.register();
		}
	}

	@Override
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(ModIhTech.instance, new GuiHandler());
		ModRecipes.addMaterialRecipes();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		if (config.hasChanged()) {
			config.save();
		}
	}
}
