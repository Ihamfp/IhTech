package ihamfp.IhTech.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesGrinding {
	
	public static class GrindingRecipe {
		public ItemStack itemIn;
		public ItemStack[] itemsOut;
		public float[] probabilities; // 1.0 = 100%
		public int ticksNeeded = 1;
		public boolean meta = false; // for the input item, false to ignore
		public boolean requiresDiamond = false; // TODO implement this in the grinders (low priority)
		
		public GrindingRecipe requiresDiamond() {
			this.requiresDiamond = true;
			return this;
		}
	}
	
	public static Map<Item, List<GrindingRecipe>> recipes = new HashMap<Item, List<GrindingRecipe>>();
	
	public static GrindingRecipe registerGrinding(ItemStack itemIn, ItemStack[] itemsOut, float[] probabilities, int ticksNeeded, boolean inputMeta) {
		GrindingRecipe r = new GrindingRecipe();
		r.itemIn = itemIn;
		r.itemsOut = itemsOut;
		r.ticksNeeded = ticksNeeded;
		r.probabilities = probabilities;
		r.meta = inputMeta;
		if (!recipes.containsKey(itemIn.getItem())) recipes.put(itemIn.getItem(), new ArrayList<GrindingRecipe>());
		recipes.get(itemIn.getItem()).add(r);
		return r;
	}
	
	public static GrindingRecipe registerGrinding(ItemStack itemIn, ItemStack[] itemsOut, float[] probabilities, int ticksNeeded) {
		return registerGrinding(itemIn, itemsOut, probabilities, ticksNeeded, false);
	}
	
	public static GrindingRecipe registerGrinding(ItemStack itemIn, ItemStack[] itemsOut, int ticksNeeded, boolean inputMeta) {
		float[] probabilities = new float[itemsOut.length];
		Arrays.fill(probabilities, 1.0f);
		return registerGrinding(itemIn, itemsOut, probabilities, ticksNeeded, inputMeta);
	}
	
	public static GrindingRecipe registerGrinding(ItemStack itemIn, ItemStack[] itemsOut, int ticksNeeded) {
		return registerGrinding(itemIn, itemsOut, ticksNeeded, false);
	}

	private static GrindingRecipe getRecipe(ItemStack itemIn) {
		if (itemIn == null) return null;
		if (recipes.containsKey(itemIn.getItem())) {
			List<GrindingRecipe> list = recipes.get(itemIn.getItem());
			for (GrindingRecipe recipe : list) {
				if (!recipe.meta) {
					return recipe;
				} else if (itemIn.getMetadata()==recipe.itemIn.getMetadata()) {
					return recipe;
				}
			}
		}
		return null;
	}
	
	public static ItemStack[] getResults(ItemStack itemIn) {
		return getRecipe(itemIn).itemsOut;
	}
	
	public static float[] getProbabilities(ItemStack itemIn) {
		return getRecipe(itemIn).probabilities;
	}
	
	public static int getTicksNeeded(ItemStack itemIn) {
		return getRecipe(itemIn).ticksNeeded;
	}

	public static boolean hasResult(ItemStack itemIn) {
		return (getRecipe(itemIn) != null);
	}
}
