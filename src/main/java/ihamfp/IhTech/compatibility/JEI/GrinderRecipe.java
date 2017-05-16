package ihamfp.IhTech.compatibility.JEI;

import java.util.Collections;
import java.util.List;

import scala.actors.threadpool.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import ihamfp.IhTech.recipes.RecipesGrinding.GrindingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;

public class GrinderRecipe extends BlankRecipeWrapper {

	private GrindingRecipe recipe; // just keep a link to the true recipe
	
	public GrinderRecipe(GrindingRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, this.recipe.itemIn);
		ingredients.setOutputs(ItemStack.class, Arrays.asList(recipe.itemsOut));
	}
	
	/*@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
	}*/

}
