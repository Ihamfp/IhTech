package ihamfp.IhTech.blocks;

import java.awt.List;
import java.util.ArrayList;

import ihamfp.IhTech.Materials;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.ModTileEntities;
import ihamfp.IhTech.blocks.machines.BlockMachineElectricFurnace;
import ihamfp.IhTech.blocks.machines.BlockMachineElectricGrinder;
import ihamfp.IhTech.blocks.machines.BlockMachineSteamGrinder;
import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.models.CableModelsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
	public static BlockMachineCasing blockMachineCasing = new BlockMachineCasing();
	
	public static BlockMachineElectricFurnace blockElectricFurnace = new BlockMachineElectricFurnace("blockElectricFurnace");
	public static BlockMachineElectricGrinder blockElectricGrinder = new BlockMachineElectricGrinder("blockElectricGrinder");
	public static BlockMachineSteamGrinder blockSteamGrinder = new BlockMachineSteamGrinder("blockSteamGrinder");
	
	// TODO empty on postLoad
	private static ArrayList<Block> modBlocks = new ArrayList<Block>(); // will be emptied when the mod is loaded, only used for registration
	
	//public static BlockEnergyCable blockCable = new BlockEnergyCable("blockCable", Material.IRON);
	
	public static void preInit() {
		for (int i=0; i<Materials.materials.size();++i) {
			if (Materials.materials.get(i).has("block") && Materials.materials.get(i).getItemFor("block") == null) {
				blockResources.add(i, new BlockGenericResource("blockStorage", i, Material.IRON));
				Materials.materials.get(i).setItemFor("block", new ItemStack(blockResources.get(i), 1));
				modBlocks.add(blockResources.get(i));
			} else {
				blockResources.add(i, null);
			}
			
			if (Materials.materials.get(i).has("ore") && Materials.materials.get(i).getItemFor("ore") == null) {
				blockOres.add(i, new BlockGenericResource("blockOre", i, Material.ROCK));
				Materials.materials.get(i).setItemFor("ore", new ItemStack(blockOres.get(i), 1));
				modBlocks.add(blockOres.get(i));
			} else {
				blockOres.add(i, null);
			}
			
			if (Materials.materials.get(i).energyCableCapacity > 0) {
				blockCables.add(new BlockEnergyCable("blockCable", i));
				blockCables.get(i).register();
				modBlocks.add(blockCables.get(i));
			} else {
				blockCables.add(i, null);
			}
		}
		
		modBlocks.add(blockMachineCasing);
		modBlocks.add(blockPanel);
		modBlocks.add(blockGen);
		
		modBlocks.add(blockBattRack);
		
		modBlocks.add(blockElectricFurnace);
		modBlocks.add(blockElectricGrinder);
		modBlocks.add(blockSteamGrinder);
		
		MinecraftForge.EVENT_BUS.register(new ModBlocks());
	}
	
	public static void init() {
		
	}
	
	public static void postInit() {
		modBlocks.clear();
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		for (Block b : modBlocks) {
			event.getRegistry().register(b);
		}
		ModTileEntities.registerTileEntities();
		
		ModIhTech.logger.info("Loaded and registered blocks");
	}

	static boolean oredicted = false;
	
	@SubscribeEvent
	public void registerItemBlocks(RegistryEvent.Register<Item> event) {
		for (Block b : modBlocks) {
			event.getRegistry().register(new ItemBlock(b).setRegistryName(b.getRegistryName()));
			if (!oredicted) {
				if (blockResources.contains(b)) { // OreDict for blocks
					ResourceMaterial m = Materials.materials.get(((BlockGenericResource)b).material);
					OreDictionary.registerOre("block" + m.name, b);
				} else if (blockOres.contains(b)) { // OreDict for ores
					ResourceMaterial m = Materials.materials.get(((BlockGenericResource)b).material);
					OreDictionary.registerOre("ore" + m.name, b);
				}
			}
		}
		oredicted = true;
		ModIhTech.logger.info("Loaded and registered item blocks");
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event) {
		this.initModels();
		this.initItemModels();
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
