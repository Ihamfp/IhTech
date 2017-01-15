package ihamfp.IhTech.containers;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import io.netty.handler.logging.LogLevel;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerOneSlot<T extends TileEntity & ITileEntityInteractable> extends Container {
	private static final int SLOTS_COUNT = 1;
	
	private T te;

	public ContainerOneSlot(IInventory playerInventory, T te) {
		this.te = te;

		addOwnSlots();
		addPlayerSlots(playerInventory);
	}

	private void addPlayerSlots(IInventory playerInventory) {
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				int x = 8 + col * 18;
				int y = 86 + row * 18;
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
			}
		}
		
		for (int row = 0; row < 9; ++row) {
			int x = 8 + row * 18;
			this.addSlotToContainer(new Slot(playerInventory, row, x, 144));
		}
	}

	private void addOwnSlots() {
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlotToContainer(new SlotItemHandler(itemHandler, 0, 80, 36));
	}

	@Nullable
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);
		int slotsCount = this.inventorySlots.size()-1;
		
		if (slot == null || !slot.getHasStack()) return null;
		ItemStack itemStack1 = slot.getStack();
		ItemStack itemStack = itemStack1.copy();

		if (playerIn.worldObj.isRemote) {
			return null;
		}
		
		if (index < SLOTS_COUNT) { // TE to inv.
			if (!mergeItemStack(itemStack1, SLOTS_COUNT, slotsCount, false)) {
				return null;
			}
		} else { // inv to TE
			if (!mergeItemStack(itemStack1, 0, SLOTS_COUNT, false)) {
				return null;
			}
		}
		
		if (itemStack1.stackSize == 0) {
			slot.putStack((ItemStack)null);
		} else if (itemStack.stackSize == itemStack1.stackSize) {
			return null;
		} else {
			slot.onSlotChanged();
		}

		return itemStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return te.canInteractWith(player);
	}

}
