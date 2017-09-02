package ihamfp.IhTech.TileEntities.machines;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityElectricFurnace extends TileEntityElectricMachine {
	protected ItemStackHandler itemStackHandler = new MachineItemStackHandler(3);

	@Override
	protected ItemStackHandler getStackHandler() {
		return this.itemStackHandler;
	}

	@Override
	protected ItemStack getOutputStack(ItemStack[] input, int outputIndex) {
		return FurnaceRecipes.instance().getSmeltingResult(input[0]).copy();
	}

	@Override
	protected float getOutputProbability(ItemStack[] input, int outputIndex) {
		return 1.0f;
	}

	@Override
	protected int getProcessTime(ItemStack[] input) {
		return 185;
	}

	@Override
	protected boolean hasOutput(ItemStack[] input) {
		for (int i=0;i<input.length;i++) {
			if (input[i] == null || input[i] == ItemStack.EMPTY) return false;
		}
		return (FurnaceRecipes.instance().getSmeltingResult(input[0]) != ItemStack.EMPTY);
	}
}
