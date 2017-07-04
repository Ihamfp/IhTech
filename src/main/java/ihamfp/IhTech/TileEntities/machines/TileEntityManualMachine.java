package ihamfp.IhTech.TileEntities.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityManualMachine extends TileEntityMachine {
	
	public int energy = 0;
	
	@Override
	protected int getEnergyUsage(ItemStack[] input) {
		return 1;
	}

	@Override
	protected int getEnergyStored() {
		return energy;
	}

	@Override
	protected void extractEnergy(int amount, boolean simulate) {
		if (!simulate) energy -= amount;
	}

}
