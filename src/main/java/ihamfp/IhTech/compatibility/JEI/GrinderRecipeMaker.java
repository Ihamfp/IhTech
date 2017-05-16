package ihamfp.IhTech.compatibility.JEI;

import ihamfp.IhTech.compatibility.JEIIntegration;
import ihamfp.IhTech.recipes.RecipesGrinding;
import ihamfp.IhTech.recipes.RecipesGrinding.GrindingRecipe;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;

public class GrinderRecipeMaker {
	public static List<GrinderRecipe> getGrinderRecipe(IJeiHelpers helpers) {
		IStackHelper stackHelper = helpers.getStackHelper();
		
		List<GrinderRecipe> recipes = new ArrayList<GrinderRecipe>();
		for (List<GrindingRecipe> recipeList : RecipesGrinding.recipes.values()) {
			for (GrindingRecipe recipe : recipeList) {
				recipes.add(new GrinderRecipe(recipe));
				JEIIntegration.recipesCounter++;
			}
		}
		
		return recipes;
	}
}
