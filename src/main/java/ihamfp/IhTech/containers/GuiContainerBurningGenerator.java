package ihamfp.IhTech.containers;

import ihamfp.IhTech.TileEntities.TileEntityItemEnergyGenerator;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;

public class GuiContainerBurningGenerator<T extends TileEntity & ITileEntityInteractable> extends GuiContainerOneSlot {

	public GuiContainerBurningGenerator(TileEntity te, Container inventorySlotsIn) {
		super(te, inventorySlotsIn);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		if (te instanceof TileEntityItemEnergyGenerator) {
			TileEntityItemEnergyGenerator ieg = (TileEntityItemEnergyGenerator)te;
			if (ieg.ticksLeft > ieg.ticksMax) ieg.ticksMax = ieg.ticksLeft;
			drawBurningLevel(guiLeft+81, guiTop+54, ieg.ticksLeft, ieg.ticksMax);
		}
	}
}
