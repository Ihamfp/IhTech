package ihamfp.IhTech.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;

public class RecipesGrinding {
	public static class GrindingRecipe {
		public ItemStack[] itemsOut;
		public int ticksNeeded;
	}
	
	public static HashMap<ItemStack, GrindingRecipe> recipes = new HashMap<ItemStack, GrindingRecipe>();
	
	public static void registerGrinding(ItemStack itemIn, ItemStack[] itemsOut, int ticksNeeded) {
		GrindingRecipe r = new GrindingRecipe();
		r.itemsOut = itemsOut;
		r.ticksNeeded = ticksNeeded;
		
		recipes.put(itemIn, r);
	}
}
