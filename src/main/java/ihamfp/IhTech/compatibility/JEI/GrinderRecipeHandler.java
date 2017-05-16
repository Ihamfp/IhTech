package ihamfp.IhTech.compatibility.JEI;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.compatibility.JEIIntegration;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.plugins.vanilla.furnace.SmeltingRecipe;

public class GrinderRecipeHandler implements IRecipeHandler<GrinderRecipe> {

	@Override
	public Class<GrinderRecipe> getRecipeClass() {
		return GrinderRecipe.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return JEIIntegration.CategoryUid.GRINDER.toString();
	}

	@Override
	public String getRecipeCategoryUid(GrinderRecipe recipe) {
		return this.getRecipeCategoryUid();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(GrinderRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(GrinderRecipe recipe) {
		return true;
	}

}
