package ihamfp.IhTech.TileEntities.machines;

import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import ihamfp.IhTech.recipes.RecipesGrinding;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityElectricGrinder extends TileEntityElectricMachine {	
	protected ItemStackHandler itemStackHandler = new ItemStackHandler(3) {
		@Override
		protected void onContentsChanged(int slot) {
			TileEntityElectricGrinder.this.markDirty();
		}
	};
	
	@Override
	protected ItemStackHandler getStackHandler() {
		return this.itemStackHandler;
	}

	@Override
	protected ItemStack getOutputStack(ItemStack input, int outputIndex) {
		return RecipesGrinding.getResults(input)[outputIndex].copy();
	}

	@Override
	protected float getOutputProbability(ItemStack input, int outputIndex) {
		return RecipesGrinding.getProbabilities(input)[outputIndex];
	}

	@Override
	protected int getProcessTime(ItemStack input) {
		return RecipesGrinding.getTicksNeeded(input);
	}

	@Override
	protected boolean hasOutput(ItemStack input) {
		return RecipesGrinding.hasResult(input);
	}

}
