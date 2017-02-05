package ihamfp.IhTech.TileEntities;

import java.util.ArrayList;
import java.util.List;

import scala.actors.threadpool.Arrays;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage.EnumEnergySideTypes;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityItemEnergyGenerator extends TileEntityEnergyStorage implements ITileEntityInteractable {
	//EnergyStorage energy;
	public int ticksLeft = 0; // fuel ticks left
	public int ticksMax = 1; // max ticks left for the last item used
	private int energyLevel = 80; // fuel's energy level
	private static final int FUEL_SLOT = 0;
	
	public TileEntityItemEnergyGenerator() {}
	
	private ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			TileEntityItemEnergyGenerator.this.markDirty();
		}
	};
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)itemStackHandler;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return !isInvalid() && player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
	
	private int getTicksPerItem(ItemStack item) {
		return TileEntityFurnace.getItemBurnTime(item);
	}
	
	@Override
	public void update() {
		super.update();
		if (this.getWorld().isRemote) return;
		
		if (this.energyStorage == null || this.getWorld().isRemote) {
			return;
		}
		if (this.ticksLeft > 0) {
			this.energyStorage.receiveEnergy(this.energyLevel, false);
			this.ticksLeft--;
			this.updateToClient();
			this.markDirty();
		}
		ItemStack itemStack = this.itemStackHandler.getStackInSlot(FUEL_SLOT);
		if (this.ticksLeft == 0 && itemStack != null && getTicksPerItem(itemStack) > 0 && this.energyStorage.getEnergyStored() < this.energyStorage.getMaxEnergyStored()) {
			this.ticksLeft = getTicksPerItem(itemStack);
			this.ticksMax = this.ticksLeft;
			this.itemStackHandler.extractItem(FUEL_SLOT, 1, false);
			this.markDirty();
		}
	}
	
	@Override
	public EnumEnergySideTypes getEnergySideType(EnumFacing face) {
		return EnumEnergySideTypes.SEND;
	}
	
	// NBT
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("ticksLeft")) {
			this.ticksLeft = compound.getInteger("ticksLeft");
		}
		if (compound.hasKey("items")) {
			itemStackHandler.deserializeNBT((NBTTagCompound)compound.getTag("items"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("ticksLeft", this.ticksLeft);
		compound.setTag("items", itemStackHandler.serializeNBT());
		return compound;
	}
}
