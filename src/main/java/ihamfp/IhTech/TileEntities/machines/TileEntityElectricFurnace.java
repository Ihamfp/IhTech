package ihamfp.IhTech.TileEntities.machines;

import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.TileEntityEnergyStorage;
import ihamfp.IhTech.TileEntities.TileEntityItemEnergyGenerator;
import ihamfp.IhTech.common.PacketHandler;
import ihamfp.IhTech.common.packets.PacketMachineSimpleUpdate;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage.EnumEnergySideTypes;

public class TileEntityElectricFurnace extends TileEntityElectricMachine {
	public static final int cookTime = 185;
	public static final int energyUsage = 80; // will put this in config later
	public Item cookingItem = null;
	
	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;
	private static final int BATT_SLOT = 2; // Not working atm
	
	protected ItemStackHandler itemStackHandler = new ItemStackHandler(3) {
		@Override
		protected void onContentsChanged(int slot) {
			TileEntityElectricFurnace.this.markDirty();
		}
	};
	
	//public TileEntityElectricFurnace() {}
	
	@Override
	public void update() {
		super.update();
		if (this.worldObj.isRemote) return;
		
		ItemStack itemStack = this.itemStackHandler.getStackInSlot(INPUT_SLOT);
		
		if (itemStack != null && itemStack.getItem() != this.cookingItem) { // Someone changed the item ! reset everything
			this.cookingItem = null;
			this.processTimeLeft = 0;
			if (FurnaceRecipes.instance().getSmeltingResult(itemStack) == null) { // If it's not even cookable, just disable the cooking (logic)
				this.setBlockStateActive(false);
				this.markDirty();
			}
		}
		
		if (this.processTimeLeft > 0 && this.getEnergyStorage().getEnergyStored() >= this.energyUsage && itemStack != null && itemStack.getItem() == this.cookingItem) { // cook item
			this.processTimeLeft--;
			this.getEnergyStorage().extractEnergy(this.energyUsage, false);
			if (this.processTimeLeft == 0) { // wow it's finished
				ItemStack cooked = FurnaceRecipes.instance().getSmeltingResult(itemStack.splitStack(1)).copy();
				this.itemStackHandler.insertItem(OUTPUT_SLOT, cooked, false);
				this.cookingItem = null;
				if (itemStack.stackSize == 0) { // nothing left in slot
					this.itemStackHandler.setStackInSlot(INPUT_SLOT, null);
					this.setBlockStateActive(false);
				}
			}
			sendSimpleUpdate(this.processTimeLeft);
			this.markDirty();
			return;
		} else if (this.processTimeLeft > 0) { // Not enough energy, item changed or is not present
			this.processTimeLeft = this.cookTime; // reset cooking
			sendSimpleUpdate(this.processTimeLeft);
			this.markDirty();
			return;
		}
		
		if (processTimeLeft == 0 && itemStack != null && FurnaceRecipes.instance().getSmeltingResult(itemStack) != null) { // start cooking
			if (this.itemStackHandler.insertItem(OUTPUT_SLOT, FurnaceRecipes.instance().getSmeltingResult(itemStack).copy(), true) != null) return; // do not cook if output slot full/incompatible
			//this.cookingItem = itemStack.splitStack(1);
			this.cookingItem = itemStack.getItem();
			this.processTimeLeft = this.cookTime;
			this.setBlockStateActive(true);
			this.markDirty();
		}
	}

	@Override
	protected ItemStackHandler getStackHandler() {
		return this.itemStackHandler;
	}

}
