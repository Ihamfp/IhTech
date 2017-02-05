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
			
			// dust => ingot in furnace
			if (mat.has("dust") && mat.has("ingot") && mat.resourceType == ResourceType.METAL && mat.meltingPoint <= 1600) {
				GameRegistry.addSmelting(mat.getItemFor("dust"), mat.getItemFor("ingot"), 1);
			}
		}
	}
}
