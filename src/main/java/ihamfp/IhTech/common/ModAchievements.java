package ihamfp.IhTech.common;

import java.util.ArrayList;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.blocks.BlockMachineCasing;
import ihamfp.IhTech.blocks.ModBlocks;
import ihamfp.IhTech.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.oredict.OreDictionary;

public class ModAchievements {
	private static ArrayList<Achievement> achievements = new ArrayList<Achievement>(); 
	
	private static class ModAchievement extends Achievement {
		public ModAchievement(String id, int x, int y, Block blockIn, Achievement parent) {
			this(id, x, y, ItemBlock.getItemFromBlock(blockIn), parent);
		}
		
		public ModAchievement(String id, int x, int y, Item itemIn, Achievement parent) {
			this(id, x, y, new ItemStack(itemIn), parent);
		}
		
		public ModAchievement(String id, int x, int y, ItemStack stack, Achievement parent) {
			super(ModIhTech.MODID + ".achievement." + id, id, x, y, stack, parent);
			this.registerStat();
			achievements.add(this);
		}
	}
	
	// Wood age
	public static Achievement tank = new ModAchievement("tank", -3, -2, Blocks.PLANKS, AchievementList.MINE_WOOD);
	public static Achievement barrel = new ModAchievement("barrel", -3, -1, Blocks.STONE, AchievementList.MINE_WOOD);
	public static Achievement pipe = new ModAchievement("pipe", -3, 0, Blocks.ACACIA_FENCE, AchievementList.MINE_WOOD);
	public static Achievement tube = new ModAchievement("tube", -3, 1, Blocks.ACACIA_FENCE_GATE, AchievementList.MINE_WOOD);
	
	// bronze age
	public static Achievement bronze = new ModAchievement("bronze", 0, 0, OreDictionary.getOres("ingotBronze").get(0), AchievementList.ACQUIRE_IRON);
	public static Achievement grinder = new ModAchievement("grinder", 1, -1, ModBlocks.blockElectricGrinder, bronze);
	public static Achievement boiler = new ModAchievement("boiler", 1, -2, Blocks.BRICK_BLOCK, bronze);
	public static Achievement betterWorkbench = new ModAchievement("betterWorkbench", 1, -3, Blocks.CRAFTING_TABLE, bronze);
	public static Achievement lesserFurnace = new ModAchievement("lesserFurnace", 1, 1, Blocks.FURNACE, bronze);
	public static Achievement greaterFurnace = new ModAchievement("greaterFurnace", 1, 2, Blocks.FIRE, bronze);
	public static Achievement alloyFurnace = new ModAchievement("alloyFurnace", 2, 2, Blocks.FIRE, greaterFurnace);
	public static Achievement autoHammer = new ModAchievement("autoHammer", 1, 3, Blocks.BEDROCK, bronze);
	
	public static Achievement blastFurnace = new ModAchievement("blastFurnace", 2, 0, ModBlocks.blockElectricFurnace, bronze);
	public static Achievement highPressure = new ModAchievement("highPressure", 3, -1, new ItemStack(ModBlocks.blockMachineCasing, 1, BlockMachineCasing.CasingType.HIGHLY_PRESSURIZED.getID()), blastFurnace);
	
	// steel age
	public static Achievement steel = new ModAchievement("steel", 4, 0, Items.IRON_INGOT, blastFurnace);
	public static Achievement oil = new ModAchievement("oil", 5, -1, Blocks.COAL_BLOCK, steel);
	public static Achievement fuel = new ModAchievement("fuel", 5, -2, Blocks.WOOL, oil);
	public static Achievement plastic = new ModAchievement("plastic", 5, -3, OreDictionary.getOres("ingotPlastic").get(0), oil);
	
	public static Achievement electricGenerator = new ModAchievement("electricGenerator", 6, 0, ModBlocks.blockCables.get(1), steel);
	public static Achievement electricBlastFurnace = new ModAchievement("electricBlastFurnace", 8, 0, Blocks.BED, electricGenerator);
	public static Achievement aluminium = new ModAchievement("aluminium", 8, -2, OreDictionary.getOres("ingotAluminium").get(0), electricGenerator);

	public static Achievement tungsten = new ModAchievement("tungsten", 10, 0, OreDictionary.getOres("ingotTungsten").get(0), electricBlastFurnace);
	public static Achievement silicon = new ModAchievement("silicon", 10, 2, OreDictionary.getOres("ingotSilicon").get(0), electricBlastFurnace);
	
	// Nuclear age
	public static Achievement uranium = new ModAchievement("uranium", 12, 0, OreDictionary.getOres("ingotUranium").get(0), tungsten);
	public static Achievement supraConductor = new ModAchievement("supraConductor", 14, 0, ModBlocks.blockCables.get(4), uranium);
	
	// Fusion age
	public static Achievement fusion = new ModAchievement("fusion", 16, 0, Blocks.TNT, supraConductor);
	public static Achievement unbibium = new ModAchievement("unbibium", 17, 1, OreDictionary.getOres("plateUnbibium").get(0), fusion);
	
	// Black hole age
	public static Achievement blackHoleMatter = new ModAchievement("blackHoleMatter", 18, 0, OreDictionary.getOres("gemBlack matter").get(0), fusion);
	public static Achievement universeFactory = new ModAchievement("universeFactory", 19, 1, Blocks.BOOKSHELF, blackHoleMatter);
	public static Achievement blackMatter = new ModAchievement("blackMatter", 20, 1, Blocks.ANVIL, universeFactory);
	public static Achievement jump = new ModAchievement("jump", 20, 2, Blocks.COBBLESTONE, universeFactory);
	
	public static void preInit() {
		Achievement[] list = new Achievement[achievements.size()];
		list = achievements.toArray(list);
		AchievementPage.registerAchievementPage(new AchievementPage("IhTech", list));
	}
}
