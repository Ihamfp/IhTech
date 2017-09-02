package ihamfp.IhTech.containers.machines;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.machines.TileEntityElectricFurnace;
import ihamfp.IhTech.containers.ContainerBase;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerElectricFurnace extends ContainerBase<TileEntityElectricFurnace> {
	public static final int SLOTS_COUNT = 3;
	
	public ContainerElectricFurnace(IInventory playerInventory, TileEntityElectricFurnace te) {
		super(playerInventory, te);
	}

	@Override
	protected void addOwnSlots() {
		if (!this.te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			ModIhTech.logger.error("Tile at " + te.getPos() + " has no ITEM_HANDLER_CAPABILIY !!! This is an error !!!");
			return;
		}
		IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if (itemHandler.getSlots() < SLOTS_COUNT) {
			ModIhTech.logger.error("Tile at " + te.getPos() + " has not enough slots (" + itemHandler.getSlots() + " out of " + SLOTS_COUNT + ") !!! This is an error !!!");
			return;
		}
		addSlotToContainer(new SlotItemHandler(itemHandler, 0, 62, 34));
		addSlotToContainer(new SlotItemHandler(itemHandler, 1, 98, 34));
		addSlotToContainer(new SlotItemHandler(itemHandler, 2, 6, 41));
	}
	
	@Override
	protected int getOwnSlotsCount() {
		return this.SLOTS_COUNT;
	}

}
