package ihamfp.IhTech.items;

import java.util.List;

import slimeknights.tconstruct.TConstruct;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.common.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemBattery extends ItemBase {
	int capacity = 3000;
	int maxSend = 1;
	int maxReceive = 1;
	
	public ItemBattery(String name, int capacity, int maxReceive, int maxSend) {
		super(name);
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxSend = maxSend;
		this.setMaxDamage(1);
		this.setMaxStackSize(1);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		if (nbt != null && nbt.hasKey("energyStored")) {
			return new CapabilityEnergyProvider(this.capacity, this.maxReceive, this.maxSend, nbt.getInteger("energyStored"));
		}
		return new CapabilityEnergyProvider(this.capacity, this.maxReceive, this.maxSend);
	}
	
	/*@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		if (!stack.hasCapability(CapabilityEnergy.ENERGY, null)) return;
		IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
		tooltip.add("Energy stored: " + storage.getEnergyStored() + "/" + storage.getMaxEnergyStored() + " " + Config.energyUnitName);
	}*/
	
	@Override
	public String getHighlightTip(ItemStack item, String displayName) {
		IEnergyStorage energy = item.getCapability(CapabilityEnergy.ENERGY, null);
		if (energy == null) return displayName;
		return (displayName + " (" + energy.getEnergyStored() + "/" + energy.getMaxEnergyStored() + " " + Config.energyUnitName + ")");
	}
	
	/*** Capability provider for items containing EnergyStorage.
	 */
	public static class CapabilityEnergyProvider implements ICapabilitySerializable<NBTTagCompound> {
		private EnergyStorage energyStorage = null;
		
		public CapabilityEnergyProvider(int capacity, int maxReceive, int maxSend) {
			this.energyStorage = new EnergyStorage(capacity, maxReceive, maxSend);
		}
		
		public CapabilityEnergyProvider(int capacity, int maxReceive, int maxSend, int energyStored) {
			this.energyStorage = new EnergyStorage(capacity, maxReceive, maxSend);
			this.energyStorage.receiveEnergy(energyStored, false);
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			if (capability == CapabilityEnergy.ENERGY) {
				return true;
			}
			return false;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == CapabilityEnergy.ENERGY) {
				return (T)this.energyStorage;
			}
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("energy", CapabilityEnergy.ENERGY.writeNBT(this.energyStorage, null));
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			if (nbt.hasKey("energy")) {
				CapabilityEnergy.ENERGY.readNBT(this.energyStorage, null, nbt.getTag("energy"));
			}
		}
		
	}
}
