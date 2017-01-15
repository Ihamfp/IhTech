package ihamfp.IhTech.common;

import ihamfp.IhTech.TileEntities.TileEntityItemEnergyGenerator;
import ihamfp.IhTech.containers.ContainerOneSlot;
import ihamfp.IhTech.containers.GuiContainerOneSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		if (te instanceof TileEntityItemEnergyGenerator) {
			return new ContainerOneSlot<TileEntityItemEnergyGenerator>(player.inventory, (TileEntityItemEnergyGenerator)te);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		if (te instanceof TileEntityItemEnergyGenerator) {
			TileEntityItemEnergyGenerator te2 = (TileEntityItemEnergyGenerator)te;
			return new GuiContainerOneSlot<TileEntityItemEnergyGenerator>(te2, new ContainerOneSlot(player.inventory, te2));
		}
		return null;
	}

}
