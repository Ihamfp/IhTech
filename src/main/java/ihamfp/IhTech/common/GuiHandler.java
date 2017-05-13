package ihamfp.IhTech.common;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.TileEntityBatteryRack;
import ihamfp.IhTech.TileEntities.TileEntityItemEnergyGenerator;
import ihamfp.IhTech.TileEntities.machines.TileEntityElectricFurnace;
import ihamfp.IhTech.TileEntities.machines.TileEntityElectricGrinder;
import ihamfp.IhTech.containers.ContainerOneSlot;
import ihamfp.IhTech.containers.GuiContainerBurningGenerator;
import ihamfp.IhTech.containers.GuiContainerOneSlot;
import ihamfp.IhTech.containers.machines.ContainerElectricFurnace;
import ihamfp.IhTech.containers.machines.ContainerElectricGrinder;
import ihamfp.IhTech.containers.machines.GuiContainerElectricFurnace;
import ihamfp.IhTech.containers.machines.GuiContainerElectricGrinder;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public static enum EnumGUIs {
		GUI_NONE,
		GUI_ONESLOT,
		GUI_ELFURNACE,
		GUI_ELGRINDER,
		// add GUIs here
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		ModIhTech.logger.debug("Gui ID was: " + EnumGUIs.values()[ID].toString());
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		
		switch(EnumGUIs.values()[ID]) {
		case GUI_ONESLOT:
			return new ContainerOneSlot(player.inventory, te);
		
		case GUI_ELFURNACE:
			return new ContainerElectricFurnace(player.inventory, (TileEntityElectricFurnace)te);
		
		case GUI_ELGRINDER:
			return new ContainerElectricGrinder(player.inventory, (TileEntityElectricGrinder)te);
		
		default:
			return null;
		
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		ModIhTech.logger.debug("Gui ID was: " + EnumGUIs.values()[ID].toString());
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		
		switch(EnumGUIs.values()[ID]) {
		case GUI_ONESLOT:
			return new GuiContainerOneSlot(te, new ContainerOneSlot(player.inventory, te));
		
		case GUI_ELFURNACE:
			return new GuiContainerElectricFurnace((TileEntityElectricFurnace)te, new ContainerElectricFurnace(player.inventory, (TileEntityElectricFurnace)te));
		
		case GUI_ELGRINDER:
			return new GuiContainerElectricGrinder((TileEntityElectricGrinder)te, new ContainerElectricGrinder(player.inventory, (TileEntityElectricGrinder)te));
		
		default:
			return null;
		}
	}

}
