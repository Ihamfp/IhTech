package ihamfp.IhTech.containers;

import java.util.ArrayList;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.common.Config;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidTank;

public class GuiContainerDecorated extends GuiContainer {
	
	public static final ResourceLocation decoration = new ResourceLocation(ModIhTech.MODID, "textures/gui/decoration.png");
	
	public GuiContainerDecorated(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		
	}
	
	public void drawEnergyStorageLevel(int x, int y, int mouseX, int mouseY, IEnergyStorage es) {
		if (es == null) return;
		if (es.getMaxEnergyStored() == 0) return;
		int energyLevel = 33*es.getEnergyStored()/es.getMaxEnergyStored();
		mc.getTextureManager().bindTexture(decoration);
		drawTexturedModalRect(x, y, 0, 0, 20, 35); // draw the decoration background
		drawTexturedModalRect(x+2, y+33-energyLevel, 20, 33-energyLevel, 16, energyLevel); // draw the filling bar
		
		if ((mouseX >= x && mouseX <= x+20) && (mouseY >= y && mouseY <= y+35)) {
			ArrayList<String> text = new ArrayList<String>();
			text.add(es.getEnergyStored() + "/" + es.getMaxEnergyStored() + " " + Config.energyUnitName);
			drawHoveringText(text, mouseX, mouseY);
		}
	}
	
	public void drawLiquidStorageLevel(int x, int y, int mouseX, int mouseY, FluidTank fs) {
		int fluidLevel = 33*fs.getFluidAmount()/fs.getCapacity();
		mc.getTextureManager().bindTexture(decoration);
		drawTexturedModalRect(x, y, 40, 0, 20, 35);
		// draw fluid texture here
		if (fs.getFluidAmount() > 0) {
			TextureAtlasSprite fluidTexture = mc.getTextureMapBlocks().getTextureExtry(fs.getFluid().getFluid().getStill().toString());
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			drawTexturedModalRect(x+2, y+33-fluidLevel, fluidTexture, 16, fluidLevel);
			
			mc.getTextureManager().bindTexture(decoration);
			drawTexturedModalRect(x+2, y+33-fluidLevel, 60, 33-fluidLevel, 16, fluidLevel);
		}
		
		if ((mouseX >= x && mouseX <= x+20) && (mouseY >= y && mouseY <= y+35)) {
			ArrayList<String> text = new ArrayList<String>();
			if (fs.getFluidAmount() > 0)
				text.add(fs.getFluid().getLocalizedName());
			text.add(fs.getFluidAmount() + "/" + fs.getCapacity() + " mb");
			drawHoveringText(text, mouseX, mouseY);
		}
	}
	
	public void drawBurningLevel(int x, int y, int level, int levelMax) {
		int burnLevel = 13*level/levelMax;
		mc.getTextureManager().bindTexture(decoration);
		drawTexturedModalRect(x+1, y+1, 20, 41, 13, 13);
		drawTexturedModalRect(x, y+13-burnLevel, 34, 53-burnLevel, 13, burnLevel);
	}
	
	public void drawSlot(int x, int y) {
		mc.getTextureManager().bindTexture(decoration);
		drawTexturedModalRect(x, y, 1, 36, 18, 18);
	}
	
	public void drawLargeSlot(int x, int y) {
		mc.getTextureManager().bindTexture(decoration);
		drawTexturedModalRect(x, y, 49, 36, 26, 26);
	}
	
	public void drawVertProgressBar(int x, int y, int progressMax, int progress, int color) {
		int level = 16*progress/progressMax;
		mc.getTextureManager().bindTexture(decoration);
		drawTexturedModalRect(x, y, 81, 36, 3, 18);
		if (progressMax > 0 && progress > 0)
			drawTexturedModalRect(x+1, y+17-level, 84, 53-level, 1, level); // TODO: add color to this
	}
	
	public void drawLargeVertProgressBar(int x, int y, int progressMax, int progress, int color) {
		int level = 24*progress/progressMax;
		mc.getTextureManager().bindTexture(decoration);
		drawTexturedModalRect(x, y, 76, 36, 3, 26);
		if (progressMax > 0 && progress > 0)
			drawTexturedModalRect(x+1, y+25-level, 77, 60-level, 1, level);
	}
	
	/** Temperature in Kelvins */
	public void drawTemperature(int x, int y, int mouseX, int mouseY, int temperature, int tempMin, int tempScale) {
		int tempLevel = (temperature-tempMin)/tempScale;
		if (tempLevel < 0) tempLevel = 0;
		if (tempLevel > 33) tempLevel = 33;
		mc.getTextureManager().bindTexture(decoration);
		drawTexturedModalRect(x, y, 75, 0, 5, 35);
		drawTexturedModalRect(x+1, y+34-tempLevel, 80, 33-tempLevel, 3, tempLevel);
		if ((mouseX >= x && mouseX <= x+5) && (mouseY >= y && mouseY <= y+35)) {
			ArrayList<String> text = new ArrayList<String>();
			if (Config.tempUnitName == "K") {
				text.add(temperature + " K");
			} else if (Config.tempUnitName == "F") {
				text.add(((temperature*9/5)-460) + "°F");
			} else {
				text.add((temperature - 273) + "°C");
			}
			drawHoveringText(text, mouseX, mouseY);
		}
	}
	
	/** Standard thermometer, ranges from 273K to 603K (0°C to 330°C) */
	public void drawTemperature(int x, int y, int mouseX, int mouseY, int temperature) {
		drawTemperature(x, y, mouseX, mouseY, temperature, 273, 10);
	}
}
