package ihamfp.IhTech.common;

import java.awt.Color;
import java.util.Hashtable;

import org.apache.logging.log4j.Level;

import ihamfp.IhTech.Materials;
import ihamfp.IhTech.ModIhTech;
import net.minecraftforge.common.config.Configuration;

public class Config {
	// Categories
	private static final String CAT_GENERAL = "general";
	private static final String CAT_MATCOLORS = "materialcolors";
	private static final String CAT_INTEGRATION = "integration";
	private static final String CAT_ELECTRIC = "electric";
	
	// general
	public static String energyUnitName = "FU"; // Name of the Forge Energy unit name used in the mod (client only)
	public static String tempUnitName = "C"; // Name of the Temperature unit used in the mod (client only, will be scaled if F or K)
	public static boolean alwaysAddResources = false;
	public static boolean limitEnergyCompatiblity = false;
	public static boolean showCapes = true;
	
	// materials colors
	public static Hashtable<String, Integer> materialsColors = new Hashtable<String, Integer>();
	
	// integration
	public static boolean TOPIntegration = true;
	public static boolean WailaIntegration = true;
	public static boolean TConstructIntegration = true;
	
	// electric machines
	public static int simple_energyBuffer = 10000; // energy buffer for simple electric machines
	
	public static void readConfig() {
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			initGeneral(cfg);
			initMatColors(cfg);
			initIntegration(cfg);
			initElectric(cfg);
		} catch (Exception e) {
			ModIhTech.logger.log(Level.ERROR, "Can't load the config file!", e);
		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}
	
	private static void initGeneral(Configuration cfg) {
		energyUnitName = cfg.getString("energyUnitName", CAT_GENERAL, energyUnitName, "Can be anything you want, really");
		tempUnitName = cfg.getString("temperatureUnitName", CAT_GENERAL, tempUnitName, "Can be either C, K or F, for Celsius, Kelvin and Fahrenheit\nScaling is automatic and client-only (server uses Kelvins)");
		alwaysAddResources = cfg.getBoolean("alwaysAddResources", CAT_GENERAL, alwaysAddResources, "Set to true to force adding materials to the OreDict");
		limitEnergyCompatiblity = cfg.getBoolean("limitEnergyCompatibility", CAT_GENERAL, limitEnergyCompatiblity, "Set to true to force checking that a tile is from this mod to send it energy.\nCan limit compatibility.");
		showCapes = cfg.getBoolean("show capes", CAT_GENERAL, showCapes, "Please leave it to true");
	}
	
	private static void initMatColors(Configuration cfg) {
		if (!cfg.getBoolean("customColors", CAT_MATCOLORS, false, "Set to true to enable custom colors")) return;
		for (int i=0; i<Materials.materials.size(); ++i) {
			ResourceMaterial mat = Materials.materials.get(i);
			if (mat.name == "Undefined") continue;
			
			mat.color = Color.decode(cfg.getString(mat.name, CAT_MATCOLORS, "0x"+Integer.toHexString(mat.color).substring(2), "")).getRGB();
		}
	}
	
	private static void initIntegration(Configuration cfg) {
		TOPIntegration = cfg.getBoolean("TOP", CAT_INTEGRATION, TOPIntegration, "");
		WailaIntegration = cfg.getBoolean("Waila", CAT_INTEGRATION, WailaIntegration, "");
		TConstructIntegration = cfg.getBoolean("TConstruct", CAT_INTEGRATION, TConstructIntegration, "");
	}
	
	private static void initElectric(Configuration cfg) {
		simple_energyBuffer = cfg.getInt("simple_energyBuffer", CAT_ELECTRIC, simple_energyBuffer, 1, Integer.MAX_VALUE, "energy buffer for simple machines");
	}
}
