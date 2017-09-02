package ihamfp.IhTech;

import java.util.ArrayList;
import java.util.HashMap;

import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.common.ResourceMaterial.ResourceType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Materials {
	public static ArrayList<ResourceMaterial> materials = new ArrayList<ResourceMaterial>() {{
		add(new ResourceMaterial("Undefined", "0xFFFFFF") {//"0x52414E") {
			@Override
			public boolean has(String thing) {
				return true; // I HAZ EVERYTHING
			}
		});
		
		// Wood/stone age
		add(new ResourceMaterial("Wood", "0x979702").setHas("dust").setHas("plate").setItemFor("block", new ItemStack(Blocks.PLANKS)).setItemFor("rod", new ItemStack(Items.STICK)).setBurningEnergy(Item.getItemFromBlock(Blocks.PLANKS), 0.25f));
		add(new ResourceMaterial("Stone", "0x6F6F6F").setHas("dust").setHas("plate").setItemFor("block", new ItemStack(Blocks.COBBLESTONE)));
		add(new ResourceMaterial("Clay", "0x9F9F9F").setHas("dust").setItemFor("block", new ItemStack(Blocks.CLAY)));
		add(new ResourceMaterial("Bricks", "0x706013").setHas("ingot").setHas("dust").setItemFor("ingot", new ItemStack(Items.BRICK)));
		add(new ResourceMaterial("Sand", "0xFFAF9F").setHas("dust").setItemFor("block", new ItemStack(Blocks.SAND)));
		add(new ResourceMaterial("Glass", "0xF7F7FF").setHas("ingot").setHas("dust").setHas("plate").setItemFor("block", new ItemStack(Blocks.GLASS)));
		add(new ResourceMaterial("Coal", "0x3B3B3B").setType(ResourceType.COAL).setItemFor("gem", new ItemStack(Items.COAL)).setItemFor("ore", new ItemStack(Blocks.COAL_ORE)).setItemFor("block", new ItemStack(Blocks.COAL_BLOCK)).setBurningEnergy(Items.COAL, 1));
		add(new ResourceMaterial("Coal coke", "0x7A7A7A").setType(ResourceType.COAL).setBurningEnergy(Items.COAL, 2));
		add(new ResourceMaterial("Iron", "0xD8D8D8").setType(ResourceType.METAL).setItemFor("ingot", new ItemStack(Items.IRON_INGOT)).setItemFor("block", new ItemStack(Blocks.IRON_BLOCK)).setItemFor("ore", new ItemStack(Blocks.IRON_ORE)).setHas("nugget").setHas("dust").setHas("plate").setMeltingPoint(1811).setEnergyCableCapacity(192));
		// Iron age
		add(new ResourceMaterial("Copper", "0xAF7F00").setType(ResourceType.METAL).setHas("ore").setMeltingPoint(1357).setEnergyCableCapacity(800));
		add(new ResourceMaterial("Tin", "0xAFAFB7").setType(ResourceType.METAL).setHas("ore").setMeltingPoint(505).setEnergyCableCapacity(128));
		add(new ResourceMaterial("Zinc", "0xA4A4A4").setType(ResourceType.METAL).setHas("ore").setMeltingPoint(693).setEnergyCableCapacity(192));
		add(new ResourceMaterial("Lead", "0x574472").setType(ResourceType.METAL).setHas("ore").setMeltingPoint(600).setEnergyCableCapacity(64));
		add(new ResourceMaterial("Silver", "0xDFDFEF").setType(ResourceType.METAL).setHas("ore").setMeltingPoint(2435).setEnergyCableCapacity(1024));
		add(new ResourceMaterial("Gold", "0xFFFF8B").setType(ResourceType.METAL).setItemFor("ingot", new ItemStack(Items.GOLD_INGOT)).setItemFor("nugget", new ItemStack(Items.GOLD_NUGGET)).setItemFor("block", new ItemStack(Blocks.GOLD_BLOCK)).setItemFor("ore", new ItemStack(Blocks.GOLD_ORE)).setMeltingPoint(1337).setEnergyCableCapacity(768));
		add(new ResourceMaterial("Nickel", "0x4D9A58").setType(ResourceType.METAL).setHas("ore").setMeltingPoint(1728).setEnergyCableCapacity(232));
		add(new ResourceMaterial("Chromium", "0xE7E3E0").setType(ResourceType.METAL).setHas("ore").setMeltingPoint(2180));
		add(new ResourceMaterial("Diamond", "0xC6C6FF").setType(ResourceType.CRYSTAL).setItemFor("gem", new ItemStack(Items.DIAMOND)).setHas("dust").setHas("plate").setItemFor("block", new ItemStack(Blocks.DIAMOND_BLOCK)).setItemFor("ore", new ItemStack(Blocks.DIAMOND_ORE)));
		add(new ResourceMaterial("Obsidian", "0x270252").setItemFor("block", new ItemStack(Blocks.OBSIDIAN)).setHas("dust").setHas("plate"));
		add(new ResourceMaterial("Redstone", "0xFF0000").setType(ResourceType.DUST).setItemFor("dust", new ItemStack(Items.REDSTONE)).setItemFor("block", new ItemStack(Blocks.REDSTONE_BLOCK)).setItemFor("ore", new ItemStack(Blocks.REDSTONE_ORE)).setHas("plate").setEnergyCableCapacity(1));
		add(new ResourceMaterial("Lapis lazuli", "0x5A82E2").setType(ResourceType.CRYSTAL).setItemFor("gem", new ItemStack(Items.DYE, 1, 4)).setItemFor("block", new ItemStack(Blocks.LAPIS_BLOCK)).setItemFor("ore", new ItemStack(Blocks.LAPIS_ORE)).setHas("plate"));
		add(new ResourceMaterial("Sapphire", "0x3289F7").setType(ResourceType.CRYSTAL).setHas("ore").setMeltingPoint(2323));
		add(new ResourceMaterial("Ruby", "0x9D2A51").setType(ResourceType.CRYSTAL).setHas("ore").setMeltingPoint(2323));
		add(new ResourceMaterial("Bronze", "0xF7DF1B").setType(ResourceType.METAL).setMeltingPoint(1158).setEnergyCableCapacity(96));
		add(new ResourceMaterial("Brass", "0xF7D717").setType(ResourceType.METAL).setMeltingPoint(1177).setEnergyCableCapacity(192));
		// Nether
		add(new ResourceMaterial("Quartz", "0xFFFFFF").setType(ResourceType.CRYSTAL).setItemFor("gem", new ItemStack(Items.QUARTZ)).setItemFor("block", new ItemStack(Blocks.QUARTZ_BLOCK)).setHas("plate"));
		add(new ResourceMaterial("Glowstone", "0xFFFF00").setType(ResourceType.DUST).setItemFor("dust", new ItemStack(Items.GLOWSTONE_DUST)).setItemFor("block", new ItemStack(Blocks.GLOWSTONE)).setHas("plate"));
		add(new ResourceMaterial("Soul sand", "0xA27F7F").setItemFor("block", new ItemStack(Blocks.SOUL_SAND)).setHas("dust"));
		add(new ResourceMaterial("Manganese", "0x4D566E").setType(ResourceType.METAL).setMeltingPoint(1519).setEnergyCableCapacity(12));
		add(new ResourceMaterial("Netherrack", "0x7F0000").setItemFor("block", new ItemStack(Blocks.NETHERRACK)).setHas("dust"));
		// End
		add(new ResourceMaterial("End stone", "0x909090").setItemFor("block", new ItemStack(Blocks.END_STONE)).setHas("dust"));
		// Bronze age
		add(new ResourceMaterial("Cast iron", "0x353333").setType(ResourceType.METAL).setMeltingPoint(1421).setEnergyCableCapacity(64));
		add(new ResourceMaterial("Steel", "0xCACACA").setType(ResourceType.METAL).setMeltingPoint(1700).setEnergyCableCapacity(72));
		// Steel age
		add(new ResourceMaterial("Plastic", "0xCDCDCD").setHas("ingot").setHas("nugget").setHas("dust").setHas("plate").setHas("rod"));
		add(new ResourceMaterial("Aluminium", "0xCACADF").setHas("ore").setType(ResourceType.METAL).setMeltingPoint(932).setEnergyCableCapacity(512));
		add(new ResourceMaterial("Tungsten", "0x82858C").setHas("ore").setType(ResourceType.METAL).setMeltingPoint(3695).setToolProperties(1080, 5, 8, 3).setEnergyCableCapacity(128));
		add(new ResourceMaterial("Platinum", "0xFAFAFA").setHas("ore").setType(ResourceType.METAL).setMeltingPoint(2041).setEnergyCableCapacity(150));
		add(new ResourceMaterial("Silicon", "0x646871").setHas("ore").setType(ResourceType.METAL).setMeltingPoint(1687));
		add(new ResourceMaterial("Glass fiber", "0xF7F7FF").setHas("plate"));
		add(new ResourceMaterial("Electrum", "0xFAFA4F").setType(ResourceType.METAL).setEnergyCableCapacity(1280));
		// Plastic age
		add(new ResourceMaterial("Meat", "0xAF1414").setHas("ingot").setHas("nugget").setHas("plate"));
		add(new ResourceMaterial("Diamond-plated steel", "0xC3C3F7").setHas("ingot").setHas("plate"));
		// Nuclear age
		add(new ResourceMaterial("Uranium", "0x12AF12").setType(ResourceType.METAL).setHas("ore").setMeltingPoint(1408).setEnergyCableCapacity(256));
		add(new ResourceMaterial("Iridium", "0xFAFAFA").setType(ResourceType.METAL).setMeltingPoint(2719));
		add(new ResourceMaterial("Plutonium", "0xAFAF12").setType(ResourceType.METAL).setMeltingPoint(913));
		add(new ResourceMaterial("Enderium", "0x258273").setType(ResourceType.METAL).setMeltingPoint(3000).setEnergyCableCapacity(1536));
		add(new ResourceMaterial("Supraconductor", "0xFF1414").setType(ResourceType.METAL).setEnergyCableCapacity(Integer.MAX_VALUE/8));
		// Fusion age
		add(new ResourceMaterial("Black hole matter", "0x010101").setHas("gem"));
		add(new ResourceMaterial("Unbibium", "0x12345601").setHas("plate"));
		// Black hole age
		add(new ResourceMaterial("Black matter", "0x000000").setHas("ingot").setHas("nugget").setHas("dust").setHas("gem").setHas("block").setToolProperties(Integer.MAX_VALUE/2, 6, 50.0f, 1000.0f));
		add(new ResourceMaterial("Gregtekium", "0x000078")); // hidden material
	}};
	
	public static HashMap<String, String[]> oreDictAliasses = new HashMap<String, String[]>() {{
		put("Aluminium", new String[]{"Aluminum"});
	}};
}
