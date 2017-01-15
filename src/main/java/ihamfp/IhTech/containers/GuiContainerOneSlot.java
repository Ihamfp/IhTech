package ihamfp.IhTech.containers;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.TileEntityItemEnergyGenerator;
import ihamfp.IhTech.fluids.FluidSteam;
import ihamfp.IhTech.fluids.ModFluids;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class GuiContainerOneSlot<T extends TileEntity & ITileEntityInteractable> extends GuiContainerDecorated {
	public static final int WIDTH = 176;
	public static final int HEIGHT = 168;
	
	public static final ResourceLocation background = new ResourceLocation(ModIhTech.MODID, "textures/gui/generic_1.png");
	
	private T te;
	
	public GuiContainerOneSlot(T te, Container inventorySlotsIn) {
		super(inventorySlotsIn);
		
		this.te = te;
		
		xSize = WIDTH;
		ySize = HEIGHT;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		if (te instanceof ITileEntityEnergyStorage) {
			EnergyStorage es = ((ITileEntityEnergyStorage) te.getWorld().getTileEntity(te.getPos())).getEnergyStorage(); // We need to get the actual TE
			drawEnergyStorageLevel(guiLeft+5, guiTop+5, mouseX, mouseY, es);
		}
		if (te instanceof TileEntityItemEnergyGenerator) {
			TileEntityItemEnergyGenerator ieg = (TileEntityItemEnergyGenerator)te;
			if (ieg.ticksLeft > ieg.ticksMax) ieg.ticksMax = ieg.ticksLeft;
			drawBurningLevel(guiLeft+81, guiTop+54, ieg.ticksLeft, ieg.ticksMax);
		}
	}
	
}
