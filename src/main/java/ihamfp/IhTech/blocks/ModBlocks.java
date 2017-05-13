package ihamfp.IhTech.blocks;

import java.awt.List;
import java.util.ArrayList;

import ihamfp.IhTech.Materials;
import ihamfp.IhTech.blocks.machines.BlockMachineElectricFurnace;
import ihamfp.IhTech.blocks.machines.BlockMachineElectricGrinder;
import ihamfp.IhTech.models.CableModelsLoader;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ModBlocks {
	// Blocks
	public static BlockSolidFuelEnergyGenerator blockGen = new BlockSolidFuelEnergyGenerator("blockGen", Material.IRON);
	public static BlockSolarPanel blockPanel = new BlockSolarPanel("blockPanel", Material.GLASS);
	public static BlockBatteryRack blockBattRack = new BlockBatteryRack();
	
	public static ArrayList<BlockGenericResource> blockResources = new ArrayList<BlockGenericResource>(Materials.materials.size());
	public static ArrayList<BlockGenericResource> blockOres = new ArrayList<BlockGenericResource>(Materials.materials.size());
	public static ArrayList<BlockEnergyCable> blockCables = new ArrayList<BlockEnergyCable>(Materials.materials.size());
	
	// machines
	public static BlockMachineElectricFurnace blockElectricFurnace = new BlockMachineElectricFurnace("blockElectricFurnace");
	public static BlockMachineElectricGrinder blockElectricGrinder = new BlockMachineElectricGrinder("blockElectricGrinder");
	
	//public static BlockEnergyCable blockCable = new BlockEnergyCable("blockCable", Material.IRON);
	
	public static void preInit() {		
		for (int i=0; i<Materials.materials.size();++i) {
			if (Materials.materials.get(i).has("block") && Materials.materials.get(i).getItemFor("block") == null) {
				blockResources.add(i, new BlockGenericResource("blockStorage", i, Material.IRON));
				blockResources.get(i).register();
				Materials.materials.get(i).setItemFor("block", new ItemStack(blockResources.get(i), 1));
				OreDictionary.registerOre("block" + Materials.materials.get(i).name, blockResources.get(i));
			} else {
				blockResources.add(i, null);
			}
			
			if (Materials.materials.get(i).has("ore") && Materials.materials.get(i).getItemFor("ore") == null) {
				blockOres.add(i, new BlockGenericResource("blockOre", i, Material.ROCK));
				blockOres.get(i).register();
				Materials.materials.get(i).setItemFor("ore", new ItemStack(blockOres.get(i), 1));
				OreDictionary.registerOre("ore" + Materials.materials.get(i).name, blockOres.get(i));
			} else {
				blockOres.add(i, null);
			}
			
			if (Materials.materials.get(i).energyCableCapacity > 0) {
				blockCables.add(new BlockEnergyCable("blockCable", i));
				blockCables.get(i).register();
			} else {
				blockCables.add(i, null);
			}
			
		}
		
		blockGen.register();
		blockPanel.register();
		blockBattRack.register();
		
		blockElectricFurnace.register();
		blockElectricGrinder.register();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		ModelLoaderRegistry.registerLoader(new CableModelsLoader());
		
		blockGen.initModel();
		blockPanel.initModel();
		blockBattRack.initModel();
		
		blockElectricFurnace.initModel();
		blockElectricGrinder.initModel();
		
		//blockCable.initModel();
		
		for (int i=0;i<Materials.materials.size();++i) {
			if (blockOres.get(i) != null) {
				blockOres.get(i).initModel();
			}
			if (blockResources.get(i) != null) {
				blockResources.get(i).initModel();
			}
			if (blockCables.get(i) != null) {
				blockCables.get(i).initModel();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void initItemModels()	{
		for (int i=0;i<Materials.materials.size();++i) {
			if (blockCables.get(i)!=null) {
				blockCables.get(i).initItemModel();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void initColors() {
		BlockColoredColor colorHandler = new BlockColoredColor();
		ItemBlockColoredColor itemColorHandler = new ItemBlockColoredColor();
		BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
		for (int i=0;i<Materials.materials.size();++i) {
			if (blockOres.get(i) != null) {
				blockColors.registerBlockColorHandler(colorHandler, blockOres.get(i));
				itemColors.registerItemColorHandler(itemColorHandler, blockOres.get(i));
			}
			if (blockResources.get(i) != null) {
				blockColors.registerBlockColorHandler(colorHandler, blockResources.get(i));
				itemColors.registerItemColorHandler(itemColorHandler, blockResources.get(i));
			}
			if (blockCables.get(i) != null) {
				itemColors.registerItemColorHandler(itemColorHandler, blockCables.get(i));
			}
		}
	}
}
