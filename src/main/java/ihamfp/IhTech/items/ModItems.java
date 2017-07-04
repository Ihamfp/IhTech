package ihamfp.IhTech.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemElytra;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.creativeTabs.ModCreativeTabs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import slimeknights.tconstruct.library.TinkerRegistry;

public class ModItems {
	//public static ItemWrench wrench1 = new ItemWrench("wrench1");
	
	public static ItemGenericResource ingot = new ItemGenericResource("ingot");
	public static ItemGenericResource nugget = new ItemGenericResource("nugget");
	public static ItemGenericResource dust = new ItemGenericResource("dust");
	public static ItemGenericResource dustTiny = new ItemGenericResource("dustTiny", "dust");
	public static ItemGenericResource gem = new ItemGenericResource("gem");
	public static ItemGenericResource plate = new ItemGenericResource("plate");
	public static ItemGenericResource compressedPlate = new ItemGenericResource("compressedPlate", "plate");
	public static ItemGenericResource gear = new ItemGenericResource("gear", "plate");
	public static ItemGenericResource rod = new ItemGenericResource("rod");
	public static ItemGenericResource wire = new ItemGenericResource("wire");
	public static ItemGenericResource coil = new ItemGenericResource("coil", "wire");
	
	public static ItemMulti upgrade = (ItemMulti) new ItemMulti("upgrade", new String[]{
			"speedup",
			"slowdown",
			"eefficiency",
			"fefficiency",
			"fortune",
			"void",
			"blast",
			
			"diamondGrind",
			"nitroxygen",
			"alimentary",
			
			"coil1",
			"coil2",
			"coil3",
	}).setCreativeTab(ModCreativeTabs.MACHINES);
	
	public static ItemMulti elecTube = new ItemMulti("elecTube", new String[]{
			"diode",
			"logic",
	});
	
	public static ItemMulti elecChip = (ItemMulti) new ItemMulti("elecChip", new String[]{
			"diode",
			"transistor",
			"logic",
			"analogic",
			"powerRegulator",
			"rom",
			"ram",
			"eeprom",
			"alu",
			"controlUnit",
			"microcontroller",
			"cpu",
			"gpu",
			"fpga",
			"quantumProcessor",
	}).setCreativeTab(ModCreativeTabs.RESOURCES);
	
	public static ItemMulti elecDisplay = (ItemMulti) new ItemMulti("elecDisplay", new String[]{
			"vfd",
			"crt",
			"lcd",
			"holo",
			"holo2",
	}).setCreativeTab(ModCreativeTabs.RESOURCES);
	
	public static ItemMulti elecPackage = (ItemMulti) new ItemMulti("elecPackage", new String[]{
			"ceramic",
			"whiteCeramic",
			"plastic",
			"metal",
	}).setCreativeTab(ModCreativeTabs.RESOURCES);
	
	/*public static ItemBattery batPotato = new ItemBattery("PotatoBattery", 12, 0, 1);
	public static ItemBattery batSaline = new ItemBattery("SalineBattery", 2500, 0, 20);
	public static ItemBattery batAlkaline = new ItemBattery("AlkalineBattery", 5000, 0, 40);
	public static ItemBattery batRedstone = new ItemBattery("RedstoneBattery", 1000, 100, 100);
	public static ItemBattery batLeadAcid = new ItemBattery("LeadAcidBattery", 50000, 500, 500);
	public static ItemBattery batNickelZinc = new ItemBattery("NickelZincBattery", 7500, 1000, 1000);
	public static ItemBattery batLithium = new ItemBattery("LithiumBattery", 12500, 1000, 2000);
	public static ItemBattery batSupercap = new ItemBattery("Supercapacitor", 10000, 10000, 10000);*/
	
	public static void preInit() {
		//wrench1.register();
		
		ingot.register();
		nugget.register();
		dust.register();
		dustTiny.register();
		gem.register();
		plate.register();
		compressedPlate.register();
		gear.register();
		rod.register();
		wire.register();
		coil.register();
		
		upgrade.register();
		
		elecTube.register();
		elecChip.register();
		elecDisplay.register();
		elecPackage.register();
		
		/*batPotato.register();
		batSaline.register();
		batAlkaline.register();
		batRedstone.register();
		batLeadAcid.register();
		batNickelZinc.register();
		batLithium.register();
		batSupercap.register();*/
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		ingot.initModel();
		nugget.initModel();
		dust.initModel();
		dustTiny.initModel();
		gem.initModel();
		plate.initModel();
		compressedPlate.initModel();
		gear.initModel();
		rod.initModel();
		wire.initModel();
		coil.initModel();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initColors() {
		ItemColoredColor colorHandler = new ItemColoredColor();
		ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
		itemColors.registerItemColorHandler(colorHandler, ingot);
		itemColors.registerItemColorHandler(colorHandler, nugget);
		itemColors.registerItemColorHandler(colorHandler, dust);
		itemColors.registerItemColorHandler(colorHandler, dustTiny);
		itemColors.registerItemColorHandler(colorHandler, gem);
		itemColors.registerItemColorHandler(colorHandler, plate);
		itemColors.registerItemColorHandler(colorHandler, compressedPlate);
		itemColors.registerItemColorHandler(colorHandler, gear);
		itemColors.registerItemColorHandler(colorHandler, rod);
		itemColors.registerItemColorHandler(colorHandler, wire);
		itemColors.registerItemColorHandler(colorHandler, coil);
	}
}
