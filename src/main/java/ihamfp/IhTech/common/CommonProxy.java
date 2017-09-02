package ihamfp.IhTech.common;

import java.io.File;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TweakPolarBear;
import ihamfp.IhTech.blocks.ModBlocks;
import ihamfp.IhTech.compatibility.TConstructIntegration;
import ihamfp.IhTech.compatibility.TOPCompatibility;
import ihamfp.IhTech.compatibility.WailaCompatibility;
import ihamfp.IhTech.fluids.ModFluids;
import ihamfp.IhTech.interfaces.IProxy;
import ihamfp.IhTech.items.ModItems;
import ihamfp.IhTech.recipes.ModRecipes;
import net.minecraftforge.common.MinecraftForge;
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
		
		ModBlocks.preInit();
		ModItems.preInit();
		ModFluids.preInit();
		ModAchievements.preInit();
		ModRecipes.preInit();
		PacketHandler.registerMessages();
		if (Loader.isModLoaded("Waila") && Config.WailaIntegration) {
			WailaCompatibility.register();
		}
		if (Loader.isModLoaded("theoneprobe") && Config.TOPIntegration) {
			TOPCompatibility.register();
		}
		if (Loader.isModLoaded("tconstruct") && Config.TConstructIntegration) {
			TConstructIntegration.moltenIntegration();
		} // TODO fix this
	}

	@Override
	public void init(FMLInitializationEvent event) {		
		NetworkRegistry.INSTANCE.registerGuiHandler(ModIhTech.instance, new GuiHandler());
		ModBlocks.init();
		
		if (Loader.isModLoaded("tconstruct") && Config.TConstructIntegration) {
			TConstructIntegration.materialsIntegration();
		}
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new TweakPolarBear());
		ModBlocks.postInit();
		if (config.hasChanged()) {
			config.save();
		}
	}
}
