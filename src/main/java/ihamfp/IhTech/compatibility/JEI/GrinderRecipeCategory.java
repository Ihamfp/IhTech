package ihamfp.IhTech.compatibility.JEI;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.compatibility.JEIIntegration;
import net.minecraft.util.ResourceLocation;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;

public class GrinderRecipeCategory extends BlankRecipeCategory<GrinderRecipe> {
	protected static final int inputSlot = 0;
	protected static final int outputSlot = 1;
	
	private final IDrawable background;
	private final String title;
	
	public GrinderRecipeCategory(IGuiHelper guiHelper) {
		ResourceLocation backgroundLocation = new ResourceLocation(ModIhTech.MODID, "textures/gui/jei/grinder1.png");
		this.background = guiHelper.createDrawable(backgroundLocation, 0, 0, 82, 54);
		this.title = Translator.translateToLocal("gui.ihtech.jei.category.grinder");
	}
	
	@Override
	public String getUid() {
		return JEIIntegration.CategoryUid.GRINDER.toString();
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, GrinderRecipe recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		
		guiItemStacks.init(inputSlot, true, 0, 18);
		guiItemStacks.init(outputSlot, false, 60, 18);
		
		guiItemStacks.set(ingredients);
	}
}
