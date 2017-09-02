package ihamfp.IhTech.TileEntities.machines;

import ihamfp.IhTech.recipes.RecipesGrinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityElectricGrinder extends TileEntityElectricMachine {

	protected ItemStackHandler itemStackHandler = new MachineItemStackHandler(3);
	
	@Override
	protected ItemStackHandler getStackHandler() {
		return this.itemStackHandler;
	}

	@Override
	protected ItemStack getOutputStack(ItemStack[] input, int outputIndex) {
		return RecipesGrinding.getResults(input[0])[outputIndex].copy();
	}

	@Override
	protected float getOutputProbability(ItemStack[] input, int outputIndex) {
		return RecipesGrinding.getProbabilities(input[0])[outputIndex];
	}

	@Override
	protected int getProcessTime(ItemStack[] input) {
		return RecipesGrinding.getTicksNeeded(input[0]);
	}

	@Override
	protected boolean hasOutput(ItemStack[] input) {
		return RecipesGrinding.hasResult(input[0]);
	}
}
