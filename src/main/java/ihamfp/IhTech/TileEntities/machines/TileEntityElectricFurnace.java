package ihamfp.IhTech.TileEntities.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.ItemStackHandler;
import ihamfp.IhTech.TileEntities.TileEntityEnergyStorage;
import ihamfp.IhTech.TileEntities.TileEntityItemEnergyGenerator;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage.EnumEnergySideTypes;

public class TileEntityElectricFurnace extends TileEntityEnergyStorage implements ITileEntityInteractable {
	public int cookTicksLeft = 0;
	public static final int cookTime = 185;
	
	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;
	
	private ItemStackHandler itemStackHandler = new ItemStackHandler(2) {
		@Override
		protected void onContentsChanged(int slot) {
			TileEntityElectricFurnace.this.markDirty();
		}
	};
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return !isInvalid() && player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
	
	@Override
	public EnumEnergySideTypes getEnergySideType(EnumFacing face) {
		return EnumEnergySideTypes.RECEIVE;
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("cookLeft")) {
			this.cookTicksLeft = compound.getInteger("cookLeft");
		}
		if (compound.hasKey("items")) {
			itemStackHandler.deserializeNBT((NBTTagCompound)compound.getTag("items"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("items", itemStackHandler.serializeNBT());
		return compound;
	}

}
