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
	
	public static String energyUnitName = "FU"; // Name of the Forge Energy unit name used in the mod
	public static boolean alwaysAddResources = false;
	public static boolean showCapes = true;
	
	public static Hashtable<String, Integer> materialsColors = new Hashtable<String, Integer>();
	
	public static void readConfig() {
		Configuration cfg = CommonProxy.config;
		try {
			cfg.load();
			initGeneral(cfg);
			initMatColors(cfg);
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
		alwaysAddResources = cfg.getBoolean("alwaysAddResources", CAT_GENERAL, alwaysAddResources, "Set to true to force adding materials to the OreDict");
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
}
