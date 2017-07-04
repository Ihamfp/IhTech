package ihamfp.IhTech.TileEntities.machines;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityHPSteamMachine extends TileEntitySteamMachine {
	
	public static final int minPressureLevel = 100; // minimal heat to start processing
	public static final int maxPressureLevel = 150; // level at which the heat stops increasing, thus decreasing steam consumption
	public static final int heatingSteamUsage = 50;
	
	public int pressureLevel = 0; // increased by 1 each tick of activity
	
	@Override
	public void update() {
		if (this.getBlockStateActive() && this.pressureLevel < maxPressureLevel && super.getEnergyStored() >= heatingSteamUsage) {
			this.pressureLevel++;
		} else if (this.pressureLevel > 0 && (!this.getBlockStateActive() || this.getEnergyStored() < heatingSteamUsage)) {
			this.pressureLevel--;
		}
		super.update();
	}
	
	@Override
	protected int getEnergyUsage(ItemStack[] input) {
		return 40;
	}

	@Override
	protected int getEnergyStored() {
		return (this.pressureLevel >= this.minPressureLevel)?super.getEnergyStored():0;
	}
}
