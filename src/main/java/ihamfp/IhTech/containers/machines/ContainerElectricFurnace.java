package ihamfp.IhTech.containers.machines;

import ihamfp.IhTech.TileEntities.machines.TileEntityElectricFurnace;
import ihamfp.IhTech.containers.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class ContainerElectricFurnace extends ContainerBase<TileEntityElectricFurnace> {
	private static final int SLOT_COUNT = 2;
	
	public ContainerElectricFurnace(IInventory playerInventory, TileEntityElectricFurnace te) {
		super(playerInventory, te);
	}

	@Override
	protected void addOwnSlots() {
		// TODO Auto-generated method stub
		
	}

}
