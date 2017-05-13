package ihamfp.IhTech.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesGrinding {
	public static class GrindingRecipe {
		public ItemStack[] itemsOut;
		public int ticksNeeded;
		public int meta = -1; // for the input item, -1 to ignore
	}
	
	public static Map<Item, List<GrindingRecipe>> recipes = new HashMap<Item, List<GrindingRecipe>>();
	
	public static void registerGrinding(ItemStack itemIn, ItemStack[] itemsOut, int ticksNeeded, boolean inputMeta) {
		GrindingRecipe r = new GrindingRecipe();
		r.itemsOut = itemsOut;
		r.ticksNeeded = ticksNeeded;
		if (inputMeta) r.meta = itemIn.getMetadata();
		if (!recipes.containsKey(itemIn.getItem())) recipes.put(itemIn.getItem(), new ArrayList<GrindingRecipe>());
		recipes.get(itemIn.getItem()).add(r);
	}
	
	public static void registerGrinding(ItemStack itemIn, ItemStack[] itemsOut, int ticksNeeded) {
		registerGrinding(itemIn, itemsOut, ticksNeeded, false);
	}

	private static GrindingRecipe getRecipe(ItemStack itemIn) {
		if (recipes.containsKey(itemIn.getItem())) {
			List<GrindingRecipe> list = recipes.get(itemIn.getItem());
			for (GrindingRecipe recipe : list) {
				if (recipe.meta == -1) {
					return recipe;
				} else if (itemIn.getMetadata()==recipe.meta) {
					return recipe;
				}
			}
		}
		return null;
	}
	
	public static ItemStack[] getResult(ItemStack itemIn) {
		return getRecipe(itemIn).itemsOut;
	}
	
	public static int getTicksNeeded(ItemStack itemIn) {
		return getRecipe(itemIn).ticksNeeded;
	}

	public static boolean hasResult(ItemStack itemIn) {
		return (getRecipe(itemIn) != null);
	}
}
