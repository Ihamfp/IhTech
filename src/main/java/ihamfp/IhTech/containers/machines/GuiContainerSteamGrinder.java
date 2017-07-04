package ihamfp.IhTech.containers.machines;

import java.awt.Color;

import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.CapabilityEnergy;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.machines.TileEntityElectricGrinder;
import ihamfp.IhTech.TileEntities.machines.TileEntitySteamGrinder;
import ihamfp.IhTech.containers.GuiContainerDecorated;

public class GuiContainerSteamGrinder extends GuiContainerDecorated {
	public static final int WIDTH = 176;
	public static final int HEIGHT = 168;
	
	public static final ResourceLocation background = new ResourceLocation(ModIhTech.MODID, "textures/gui/generic_single.png");
	
	protected TileEntitySteamGrinder te;

	public GuiContainerSteamGrinder(TileEntitySteamGrinder te, Container inventorySlotsIn) {
		super(inventorySlotsIn);
		this.te = te;
		
		xSize = WIDTH;
		ySize = HEIGHT;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		drawSlot(guiLeft+61, guiTop+33); // input
		drawLargeSlot(guiLeft+93, guiTop+29); // output
		drawSlot(guiLeft+5, guiTop+40); // steam
		drawVertProgressBar(guiLeft+80, guiTop+33, te.processTime, (te.processTimeLeft>0)?te.processTime-te.processTimeLeft:0, Color.YELLOW.getRGB()); // cooking bar
		this.drawLiquidStorageLevel(guiLeft+4, guiTop+4, mouseX, mouseY, te.getEnergyStorage()); // steam
	}

}
