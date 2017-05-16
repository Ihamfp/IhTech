package ihamfp.IhTech.compatibility;

import net.minecraft.item.ItemStack;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.blocks.ModBlocks;
import ihamfp.IhTech.compatibility.JEI.GrinderRecipeCategory;
import ihamfp.IhTech.compatibility.JEI.GrinderRecipeHandler;
import ihamfp.IhTech.compatibility.JEI.GrinderRecipeMaker;
import ihamfp.IhTech.containers.machines.ContainerElectricGrinder;
import ihamfp.IhTech.items.ModItems;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;

@JEIPlugin
public class JEIIntegration implements IModPlugin {
	
	public static enum CategoryUid {
		GRINDER("Grinder");
		
		private final String name;
		private CategoryUid(String s) {
			name = s;
		}
		
		public String toString() {
			return ModIhTech.MODID + this.name;
		}
	}
	
	public static int recipesCounter = 0;
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {
		
	}

	@Override
	public void register(IModRegistry registry) {
		
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		registry.addRecipeCategories(
			new GrinderRecipeCategory(guiHelper)
		);
		
		registry.addRecipeHandlers(
			new GrinderRecipeHandler()
		);
		
		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		
		recipeTransferRegistry.addRecipeTransferHandler(ContainerElectricGrinder.class, CategoryUid.GRINDER.toString(), 0, 1, ContainerElectricGrinder.SLOTS_COUNT, 36);
		
		registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockElectricGrinder), CategoryUid.GRINDER.toString());
		
		registry.addRecipes(GrinderRecipeMaker.getGrinderRecipe(jeiHelpers));
		
		ModIhTech.logger.info("Registered " + recipesCounter + " recipes in JEI.");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		
	}

}
