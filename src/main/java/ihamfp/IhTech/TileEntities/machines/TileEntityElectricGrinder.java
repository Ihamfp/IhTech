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
	public int grindTime = 1;
	public static final int energyUsage = 80;
	public Item grindingItem = null;
	public int grindingMeta = -1;
	
	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;
	private static final int BATT_SLOT = 2; // Not working atm
	
	protected ItemStackHandler itemStackHandler = new ItemStackHandler(3) {
		@Override
		protected void onContentsChanged(int slot) {
			TileEntityElectricGrinder.this.markDirty();
		}
	};
	
	@Override
	public void update() {
		super.update();
		ItemStack itemStack = this.itemStackHandler.getStackInSlot(INPUT_SLOT);
		
		if (this.worldObj.isRemote) {
			if (itemStack != null && (itemStack.getItem() != this.grindingItem || itemStack.getMetadata() != this.grindingMeta) && RecipesGrinding.hasResult(itemStack)) {
				this.grindTime = RecipesGrinding.getTicksNeeded(itemStack);
				this.grindingItem = itemStack.getItem();
				this.grindingMeta = itemStack.getMetadata();
			}
			return;
		}
		
		if (itemStack != null && (itemStack.getItem() != this.grindingItem || itemStack.getMetadata() != this.grindingMeta)) { // Someone changed the item ! reset everything
			this.grindingItem = null;
			this.grindingMeta = -1;
			this.processTimeLeft = 0;
			if (!RecipesGrinding.hasResult(itemStack)) { // If it's not even cookable, just disable the cooking (logic)
				this.setBlockStateActive(false);
				this.markDirty();
			}
		}
		
		if (this.processTimeLeft > 0 && this.getEnergyStorage().getEnergyStored() >= this.energyUsage && itemStack != null && itemStack.getItem() == this.grindingItem && itemStack.getMetadata() == this.grindingMeta) { // cook item
			this.processTimeLeft--;
			this.getEnergyStorage().extractEnergy(this.energyUsage, false);
			if (this.processTimeLeft == 0) { // wow it's finished
				ItemStack cooked = RecipesGrinding.getResults(itemStack.splitStack(1))[0].copy();
				if (this.worldObj.rand.nextFloat() <= RecipesGrinding.getProbabilities(itemStack)[0]) // this grinder only outputs the first item of a recipe
					this.itemStackHandler.insertItem(OUTPUT_SLOT, cooked, false);
				this.grindingItem = null;
				this.grindingMeta = -1;
				if (itemStack.stackSize == 0) { // nothing left in slot
					this.itemStackHandler.setStackInSlot(INPUT_SLOT, null);
					this.setBlockStateActive(false);
				}
			}
			sendSimpleUpdate(this.processTimeLeft);
			this.markDirty();
			return;
		} else if (this.processTimeLeft > 0) { // Not enough energy, item changed or is not present
			this.processTimeLeft = this.grindTime; // reset cooking
			sendSimpleUpdate(this.processTimeLeft);
			this.markDirty();
			return;
		}
		
		if (processTimeLeft == 0 && itemStack != null && RecipesGrinding.hasResult(itemStack)) { // start cooking
			if (this.itemStackHandler.insertItem(OUTPUT_SLOT, RecipesGrinding.getResults(itemStack)[0].copy(), true) != null) return; // do not cook if output slot full/incompatible
			this.grindingItem = itemStack.getItem();
			this.grindingMeta = itemStack.getMetadata();
			this.grindTime = RecipesGrinding.getTicksNeeded(itemStack);
			this.processTimeLeft = this.grindTime;
			this.setBlockStateActive(true);
			this.markDirty();
		}
	}
	
	@Override
	protected ItemStackHandler getStackHandler() {
		return this.itemStackHandler;
	}

}
