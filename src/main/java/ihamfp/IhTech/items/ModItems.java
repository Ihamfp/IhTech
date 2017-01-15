package ihamfp.IhTech.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.creativeTabs.ModCreativeTabs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ModItems {
	//public static ItemWrench wrench1 = new ItemWrench("wrench1");
	
	public static ItemGenericResource ingot = new ItemGenericResource("ingot");
	public static ItemGenericResource nugget = new ItemGenericResource("nugget");
	public static ItemGenericResource dust = new ItemGenericResource("dust");
	public static ItemGenericResource gem = new ItemGenericResource("gem");
	public static ItemGenericResource plate = new ItemGenericResource("plate");
	
	public static void preInit() {
		//wrench1.register();
		
		ingot.register();
		nugget.register();
		dust.register();
		gem.register();
		plate.register();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		ingot.initModel();
		nugget.initModel();
		dust.initModel();
		gem.initModel();
		plate.initModel();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initColors() {
		ItemColoredColor colorHandler = new ItemColoredColor();
		ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
		itemColors.registerItemColorHandler(colorHandler, ingot);
		itemColors.registerItemColorHandler(colorHandler, nugget);
		itemColors.registerItemColorHandler(colorHandler, dust);
		itemColors.registerItemColorHandler(colorHandler, gem);
		itemColors.registerItemColorHandler(colorHandler, plate);
	}
}
