package ihamfp.IhTech.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class RecipesGrinding {
	public static class GrindingRecipe {
		public ItemStack itemIn;
		public ItemStack[] itemsOut;
		public int ticksNeeded;
	}
	
	public static ArrayList<GrindingRecipe> recipes = new ArrayList<GrindingRecipe>();
	
	public static void registerGrinding(ItemStack itemIn, ItemStack[] itemsOut, int ticksNeeded) {
		GrindingRecipe r = new GrindingRecipe();
		r.itemIn = itemIn;
		r.itemsOut = itemsOut;
		r.ticksNeeded = ticksNeeded;
		
		recipes.add(r);
	}
}
