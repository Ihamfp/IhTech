package ihamfp.IhTech.recipes;

import ihamfp.IhTech.Materials;
import ihamfp.IhTech.blocks.BlockGenericResource;
import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.common.ResourceMaterial.ResourceType;
import ihamfp.IhTech.items.ItemGenericResource;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {
	public static void addMaterialRecipes() {
		for (int i=0;i<Materials.materials.size();++i) {
			ResourceMaterial mat = Materials.materials.get(i);
			
			// 9 nuggets <=> ingot
			if (mat.has("ingot") && mat.has("nugget") && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || mat.getItemFor("nugget").getItem() instanceof ItemGenericResource)) {
				GameRegistry.addRecipe(mat.getItemFor("ingot"), "nnn", "nnn", "nnn", 'n', mat.getItemFor("nugget"));
				ItemStack nineNuggets = ItemStack.copyItemStack(mat.getItemFor("nugget"));
				nineNuggets.stackSize = 9;
				GameRegistry.addRecipe(nineNuggets, "i", 'i', mat.getItemFor("ingot"));
			}
			
			// 9 ingots <=> block
			if (mat.has("ingot") && mat.has("block") && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || ((ItemBlock)(mat.getItemFor("block").getItem())).getBlock() instanceof BlockGenericResource)) {
				GameRegistry.addRecipe(mat.getItemFor("block"), "iii", "iii", "iii", 'i', mat.getItemFor("ingot"));
				ItemStack nineIngots = ItemStack.copyItemStack(mat.getItemFor("ingot"));
				nineIngots.stackSize = 9;
				GameRegistry.addRecipe(nineIngots, "b", 'b', mat.getItemFor("block"));
			}
			
			// dust => ingot in furnace
			if (mat.has("dust") && mat.has("ingot") && mat.resourceType == ResourceType.METAL && mat.meltingPoint <= 1600 && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || mat.getItemFor("dust").getItem() instanceof ItemGenericResource)) {
				GameRegistry.addSmelting(mat.getItemFor("dust"), mat.getItemFor("ingot"), 1);
			}
			
			// ingot => dust grinding
			if (mat.has("dust") && mat.has("ingot") && mat.resourceType == ResourceType.METAL) {
				RecipesGrinding.registerGrinding(mat.getItemFor("ingot"), new ItemStack[] {mat.getItemFor("dust")}, 185);
			}
			
			// ore => ingot in furnace
			if (mat.has("ore") && mat.has("ingot") && mat.resourceType == ResourceType.METAL && mat.meltingPoint <= 1600 && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || ((ItemBlock)(mat.getItemFor("ore").getItem())).getBlock() instanceof BlockGenericResource)) {
				GameRegistry.addSmelting(mat.getItemFor("ore"), mat.getItemFor("ingot"), 1);
			}
		}
	}
}
