package ihamfp.IhTech.containers;

import ihamfp.IhTech.ModIhTech;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class GuiContainerOneSlot extends GuiContainerDecorated {
	public static final int WIDTH = 176;
	public static final int HEIGHT = 168;
	
	public static final ResourceLocation background = new ResourceLocation(ModIhTech.MODID, "textures/gui/generic_1.png");
	
	protected TileEntity te;
	
	public GuiContainerOneSlot(TileEntity te, Container inventorySlotsIn) {
		super(inventorySlotsIn);
		
		this.te = te;
		
		xSize = WIDTH;
		ySize = HEIGHT;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		if (te.hasCapability(CapabilityEnergy.ENERGY, null)) {
			//IEnergyStorage es = te.getWorld().getTileEntity(te.getPos()).getCapability(CapabilityEnergy.ENERGY, null);
			IEnergyStorage es = te.getCapability(CapabilityEnergy.ENERGY, null);
			drawEnergyStorageLevel(guiLeft+5, guiTop+5, mouseX, mouseY, es);
		}

	}
	
}
