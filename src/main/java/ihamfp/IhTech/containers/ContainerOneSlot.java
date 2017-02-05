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

public class ContainerOneSlot extends ContainerBase<TileEntity> {
	private static final int SLOTS_COUNT = 1;

	public ContainerOneSlot(IInventory playerInventory, TileEntity te) {
		super(playerInventory, te);
	}

	protected void addOwnSlots() {
		if (!this.te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			ModIhTech.logger.error("Tile at " + te.getPos() + " has no ITEM_HANDLER_CAPABILIY !!! This is an error !!!");
			return;
		}
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		addSlotToContainer(new SlotItemHandler(itemHandler, 0, 80, 36));
	}
}
