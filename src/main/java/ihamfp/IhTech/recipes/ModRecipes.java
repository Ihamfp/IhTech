package ihamfp.IhTech.recipes;

import ihamfp.IhTech.Materials;
import ihamfp.IhTech.blocks.BlockGenericResource;
import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.common.ResourceMaterial.ResourceType;
import ihamfp.IhTech.items.ItemGenericResource;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {
	public static void addMaterialRecipes() {
		for (int i=0;i<Materials.materials.size();++i) {
			ResourceMaterial mat = Materials.materials.get(i);
			
			// 9 nuggets <=> ingot
			if (mat.has("ingot") && mat.has("nugget") && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || mat.getItemFor("nugget").getItem() instanceof ItemGenericResource)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(mat.getItemFor("ingot"), "nnn", "nnn", "nnn", 'n', "nugget"+mat.name));
				
				ItemStack nineNuggets = ItemStack.copyItemStack(mat.getItemFor("nugget"));
				nineNuggets.stackSize = 9;
				GameRegistry.addRecipe(nineNuggets, "i", 'i', mat.getItemFor("ingot"));
			}
			
			// 9 ingots <=> block
			if (mat.has("ingot") && mat.has("block") && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || ((ItemBlock)(mat.getItemFor("block").getItem())).getBlock() instanceof BlockGenericResource)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(mat.getItemFor("block"), "iii", "iii", "iii", 'i', "ingot"+mat.name));
				
				ItemStack nineIngots = ItemStack.copyItemStack(mat.getItemFor("ingot"));
				nineIngots.stackSize = 9;
				GameRegistry.addRecipe(nineIngots, "b", 'b', mat.getItemFor("block"));
			}
			
			// 9 gems <=> block
			if (mat.has("gem") && mat.has("block") && (mat.getItemFor("gem").getItem() instanceof ItemGenericResource || ((ItemBlock)(mat.getItemFor("block").getItem())).getBlock() instanceof BlockGenericResource)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(mat.getItemFor("block"), "iii", "iii", "iii", 'i', "gem"+mat.name));
				
				ItemStack nineIngots = ItemStack.copyItemStack(mat.getItemFor("gem"));
				nineIngots.stackSize = 9;
				GameRegistry.addRecipe(nineIngots, "b", 'b', mat.getItemFor("block"));
			}
			
			// 9 tiny dusts <=> dust
			if (mat.has("dust") && (mat.getItemFor("dust").getItem() instanceof ItemGenericResource || mat.getItemFor("dustTiny").getItem() instanceof ItemGenericResource)) {
				GameRegistry.addRecipe(new ShapedOreRecipe(mat.getItemFor("dust"), "ttt", "ttt", "ttt", 't', "dustTiny"+mat.name));
				
				ItemStack nineTinyDusts = ItemStack.copyItemStack(mat.getItemFor("dustTiny"));
				nineTinyDusts.stackSize = 9;
				GameRegistry.addRecipe(nineTinyDusts, "d", 'd', mat.getItemFor("dust"));
			}
			
			// dust => ingot in furnace
			if (mat.has("dust") && mat.has("ingot") && mat.resourceType == ResourceType.METAL && mat.meltingPoint <= 2000 && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || mat.getItemFor("dust").getItem() instanceof ItemGenericResource)) {
				GameRegistry.addSmelting(mat.getItemFor("dust"), mat.getItemFor("ingot"), 1);
			}
			
			// ore => ingot in furnace
			if (mat.has("ore") && mat.has("ingot") && mat.resourceType == ResourceType.METAL && mat.meltingPoint <= 1600 && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || ((ItemBlock)(mat.getItemFor("ore").getItem())).getBlock() instanceof BlockGenericResource)) {
				GameRegistry.addSmelting(mat.getItemFor("ore"), mat.getItemFor("ingot"), 1);
			}
			
			// ingot => dust grinding
			if (mat.has("dust") && mat.has("ingot") && mat.resourceType == ResourceType.METAL) {
				RecipesGrinding.registerGrinding(mat.getItemFor("ingot"), new ItemStack[] {mat.getItemFor("dust")}, 185, true);
			}
			
			// gem => dust grinding
			if (mat.has("dust") && mat.has("gem")) {
				switch (mat.resourceType) {
				case COAL:
					RecipesGrinding.registerGrinding(mat.getItemFor("gem"), new ItemStack[] {mat.getItemFor("dust")}, 185, true);
					break;
				
				case CRYSTAL:
					RecipesGrinding.registerGrinding(mat.getItemFor("gem"), new ItemStack[] {mat.getItemFor("dust")}, 185, true).requiresDiamond();
					break;
				
				default:
					break;
				}
			}
			
			// block => dust grinding
			if (mat.has("dust") && mat.has("block")) {
				ItemStack dusts = ItemStack.copyItemStack(mat.getItemFor("dust"));
				switch (mat.resourceType) {
				case METAL:
				case COAL:
					dusts.stackSize = 9;
					RecipesGrinding.registerGrinding(mat.getItemFor("block"), new ItemStack[] {dusts}, 185*8, true);
					break;
				
				case CRYSTAL:
					dusts.stackSize = 9;
					RecipesGrinding.registerGrinding(mat.getItemFor("block"), new ItemStack[] {dusts}, 185*8, true).requiresDiamond();
					break;
				
				default:
					if (mat.name == "Wood") {
						dusts.stackSize = 4;
						RecipesGrinding.registerGrinding(mat.getItemFor("block"), new ItemStack[] {dusts}, 185*2);
					}
					break;
				}
			}
		}
	}
}
