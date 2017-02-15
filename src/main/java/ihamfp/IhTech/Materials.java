package ihamfp.IhTech;

import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.common.ResourceMaterial.ResourceType;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
		add(new ResourceMaterial("Wood", "0x979702").setHas("dust").setHas("plate").setItemFor("block", new ItemStack(Blocks.LOG)).setItemFor("rod", new ItemStack(Items.STICK)));
		add(new ResourceMaterial("Stone", "0x6F6F6F").setHas("dust").setHas("plate").setItemFor("block", new ItemStack(Blocks.COBBLESTONE)));
		add(new ResourceMaterial("Clay", "0x9F9F9F").setHas("dust").setItemFor("block", new ItemStack(Blocks.CLAY)));
		add(new ResourceMaterial("Bricks", "0x706013").setHas("ingot").setHas("dust").setItemFor("ingot", new ItemStack(Items.BRICK)));
		add(new ResourceMaterial("Sand", "0xFFAF9F").setHas("dust").setItemFor("block", new ItemStack(Blocks.SAND)));
		add(new ResourceMaterial("Glass", "0xF7F7FF").setHas("ingot").setHas("dust").setHas("plate").setItemFor("block", new ItemStack(Blocks.GLASS)));
		add(new ResourceMaterial("Coal", "0x3B3B3B").setHas("dust").setItemFor("gem", new ItemStack(Items.COAL)).setItemFor("ore", new ItemStack(Blocks.COAL_ORE)).setItemFor("block", new ItemStack(Blocks.COAL_BLOCK)));
		add(new ResourceMaterial("Coal coke", "0x7A7A7A").setHas("dust").setHas("gem"));
		add(new ResourceMaterial("Iron", "0xC8C8C8").setType(ResourceType.METAL).setItemFor("ingot", new ItemStack(Items.IRON_INGOT)).setItemFor("block", new ItemStack(Blocks.IRON_BLOCK)).setItemFor("ore", new ItemStack(Blocks.IRON_ORE)).setHas("nugget").setHas("dust").setHas("plate").setMeltingPoint(1811));
		// Iron age
		add(new ResourceMaterial("Copper", "0xAF7F00").setType(ResourceType.METAL).setMeltingPoint(1357));
		add(new ResourceMaterial("Tin", "0xAFAFB7").setType(ResourceType.METAL).setMeltingPoint(505));
		add(new ResourceMaterial("Zinc", "0xA4A4A4").setType(ResourceType.METAL).setMeltingPoint(693));
		add(new ResourceMaterial("Lead", "0x574472").setType(ResourceType.METAL).setMeltingPoint(600));
		add(new ResourceMaterial("Silver", "0xDFDFEF").setType(ResourceType.METAL).setMeltingPoint(2435));
		add(new ResourceMaterial("Gold", "0xFAFA1F").setType(ResourceType.METAL).setItemFor("ingot", new ItemStack(Items.GOLD_INGOT)).setItemFor("nugget", new ItemStack(Items.GOLD_NUGGET)).setItemFor("block", new ItemStack(Blocks.GOLD_BLOCK)).setItemFor("ore", new ItemStack(Blocks.GOLD_ORE)).setMeltingPoint(1337));
		add(new ResourceMaterial("Nickel", "0x4D9A58").setType(ResourceType.METAL).setMeltingPoint(1728));
		add(new ResourceMaterial("Diamond", "0xC6C6FF").setType(ResourceType.CRYSTAL).setItemFor("gem", new ItemStack(Items.DIAMOND)).setHas("dust").setHas("plate").setItemFor("block", new ItemStack(Blocks.DIAMOND_BLOCK)).setItemFor("ore", new ItemStack(Blocks.DIAMOND_ORE)));
		add(new ResourceMaterial("Obsidian", "0x270252").setItemFor("block", new ItemStack(Blocks.OBSIDIAN)).setHas("dust").setHas("plate"));
		add(new ResourceMaterial("Redstone", "0xFF0000").setType(ResourceType.DUST).setItemFor("dust", new ItemStack(Items.REDSTONE)).setItemFor("block", new ItemStack(Blocks.REDSTONE_BLOCK)).setItemFor("ore", new ItemStack(Blocks.REDSTONE_ORE)).setHas("plate"));
		add(new ResourceMaterial("Lapis lazuli", "0x0202FF").setItemFor("gem", new ItemStack(Items.DYE, 1, 4)).setItemFor("block", new ItemStack(Blocks.LAPIS_BLOCK)).setItemFor("ore", new ItemStack(Blocks.LAPIS_ORE)).setHas("plate"));
		add(new ResourceMaterial("Bronze", "0xF7DF1B").setType(ResourceType.METAL).setMeltingPoint(1158));
		add(new ResourceMaterial("Brass", "0xF7D717").setType(ResourceType.METAL).setMeltingPoint(1177));
		// Nether
		add(new ResourceMaterial("Quartz", "0xFFFFFF").setType(ResourceType.CRYSTAL).setItemFor("gem", new ItemStack(Items.QUARTZ)).setHas("plate"));
		add(new ResourceMaterial("Glowstone", "0xFFFF00").setType(ResourceType.DUST).setItemFor("dust", new ItemStack(Items.GLOWSTONE_DUST)).setItemFor("block", new ItemStack(Blocks.GLOWSTONE)).setHas("plate"));
		add(new ResourceMaterial("Soul sand", "0xA27F7F").setItemFor("block", new ItemStack(Blocks.SOUL_SAND)).setHas("dust"));
		add(new ResourceMaterial("Netherrack", "0x7F0000").setItemFor("block", new ItemStack(Blocks.NETHERRACK)).setHas("dust"));
		// End
		add(new ResourceMaterial("End stone", "0x909090").setItemFor("block", new ItemStack(Blocks.END_STONE)).setHas("dust"));
		// Bronze age
		add(new ResourceMaterial("Cast iron", "0x353333").setType(ResourceType.METAL).setMeltingPoint(1421));
		add(new ResourceMaterial("Steel", "0xCACACA").setType(ResourceType.METAL).setMeltingPoint(1700));
		// Steel age
		add(new ResourceMaterial("Plastic", "0xCDCDCD").setHas("ingot").setHas("nugget").setHas("dust").setHas("plate"));
		add(new ResourceMaterial("Aluminium", "0xCACADF").setType(ResourceType.METAL).setMeltingPoint(932));
		add(new ResourceMaterial("Tungsten", "0x82858C").setType(ResourceType.METAL).setMeltingPoint(3695).setToolProperties(1080, 5, 8, 3));
		add(new ResourceMaterial("Platinum", "0xFAFAFA").setType(ResourceType.METAL).setMeltingPoint(2041));
		add(new ResourceMaterial("Silicon", "0x646871").setType(ResourceType.METAL).setMeltingPoint(1687));
		add(new ResourceMaterial("Glass fiber", "0xF7F7FF").setHas("plate"));
		add(new ResourceMaterial("Electrum", "0xFAFA4F").setType(ResourceType.METAL));
		// Plastic age
		add(new ResourceMaterial("Meat", "0xAF1414").setHas("ingot").setHas("nugget").setHas("plate"));
		add(new ResourceMaterial("Diamond-plated steel", "0xC3C3F7").setHas("ingot").setHas("plate"));
		// Nuclear age
		add(new ResourceMaterial("Uranium", "0x12AF12").setType(ResourceType.METAL).setMeltingPoint(1408));
		add(new ResourceMaterial("Iridium", "0xFAFAFA").setType(ResourceType.METAL).setMeltingPoint(2719));
		add(new ResourceMaterial("Plutonium", "0xAFAF12").setType(ResourceType.METAL).setMeltingPoint(913));
		add(new ResourceMaterial("Supraconductor", "0xFF1414").setType(ResourceType.METAL));
		// Fusion age
		add(new ResourceMaterial("Black hole matter", "0x010101").setHas("gem"));
		add(new ResourceMaterial("Unbibinium", "0x12345601").setHas("plate"));
		// Black hole age
		add(new ResourceMaterial("Black matter", "0x000000").setHas("ingot").setHas("nugget").setHas("dust").setHas("gem").setHas("block").setToolProperties(Integer.MAX_VALUE/2, 6, 50.0f, 1000.0f));
	}};
	
	public static HashMap<String, String[]> oreDictAliasses = new HashMap<String, String[]>() {{
		put("Aluminium", new String[]{"Aluminum"});
	}};
}
