package ihamfp.IhTech.TileEntities.machines;

import scala.actors.threadpool.Arrays;
import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.TileEntityEnergyStorage;
import ihamfp.IhTech.TileEntities.TileEntityItemEnergyGenerator;
import ihamfp.IhTech.common.PacketHandler;
import ihamfp.IhTech.common.Utils;
import ihamfp.IhTech.common.packets.PacketMachineSimpleUpdate;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage.EnumEnergySideTypes;

public class TileEntityElectricFurnace extends TileEntityElectricMachine {
	protected ItemStackHandler itemStackHandler = new MachineItemStackHandler(3);

	@Override
	protected ItemStackHandler getStackHandler() {
		return this.itemStackHandler;
	}

	@Override
	protected ItemStack getOutputStack(ItemStack[] input, int outputIndex) {
		return FurnaceRecipes.instance().getSmeltingResult(input[0]).copy();
	}

	@Override
	protected float getOutputProbability(ItemStack[] input, int outputIndex) {
		return 1.0f;
	}

	@Override
	protected int getProcessTime(ItemStack[] input) {
		return 185;
	}

	@Override
	protected boolean hasOutput(ItemStack[] input) {
		for (int i=0;i<input.length;i++) {
			if (input[i] == null || input[i] == ItemStack.EMPTY) return false;
		}
		return (FurnaceRecipes.instance().getSmeltingResult(input[0]) != ItemStack.EMPTY);
	}
}
