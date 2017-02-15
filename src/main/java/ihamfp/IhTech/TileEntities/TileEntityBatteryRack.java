package ihamfp.IhTech.TileEntities;

import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage.EnumEnergySideTypes;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityBatteryRack extends TileEntityEnergyStorage implements ITileEntityInteractable {
	private static int BATT_SLOT = 0;
	
	private ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			TileEntityBatteryRack.this.markDirty();
			TileEntityBatteryRack.this.updateToClient();
		}
	};
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY && this.getEnergySideType(facing) != EnumEnergySideTypes.BLOCKED) {
			if (this.itemStackHandler.getStackInSlot(BATT_SLOT) != null && this.itemStackHandler.getStackInSlot(BATT_SLOT).hasCapability(capability, facing)) {
				return true;
			} else {
				return false;
			}
		} else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY && this.getEnergySideType(facing) != EnumEnergySideTypes.BLOCKED) {
			if (this.itemStackHandler.getStackInSlot(BATT_SLOT) != null && this.itemStackHandler.getStackInSlot(BATT_SLOT).hasCapability(capability, facing)) {
				return this.itemStackHandler.getStackInSlot(BATT_SLOT).getCapability(capability, facing);
			} else {
				return null;
			}
		} else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)itemStackHandler;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public IEnergyStorage getEnergyStorage() {
		ItemStack battStack = this.itemStackHandler.getStackInSlot(BATT_SLOT);
		if (battStack != null && battStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			return battStack.getCapability(CapabilityEnergy.ENERGY, null);
		}
		return null;
	}
	
	@Override
	public void update() {
		if (this.getEnergyStorage() != null) {
			this.updateGlobalEnergySharing();
			if (this.getEnergyStorage().getEnergyStored() != this.lastEnergy) {
				this.lastEnergy = this.getEnergyStorage().getEnergyStored();
				this.updateToClient();
			}
		}
	}
	
	// NBT
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBTBypassable(compound, true);
		if (compound.hasKey("items")) {
			itemStackHandler.deserializeNBT((NBTTagCompound)compound.getTag("items"));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBTBypassable(compound, true);
		compound.setTag("items", itemStackHandler.serializeNBT());
		return compound;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return !isInvalid() && player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
}
