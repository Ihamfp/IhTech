package ihamfp.IhTech.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ihamfp.IhTech.TileEntities.machines.TileEntityElectricFurnace;
import ihamfp.IhTech.blocks.BlockBase;
import ihamfp.IhTech.blocks.BlockEnergyStorage;
import ihamfp.IhTech.common.Config;
import ihamfp.IhTech.common.GuiHandler.EnumGUIs;
import ihamfp.IhTech.creativeTabs.ModCreativeTabs;

public class BlockMachineElectricFurnace extends BlockMachineElectricBase<TileEntityElectricFurnace> {
	public static int GUI_ID = EnumGUIs.GUI_ELFURNACE.ordinal();
	//static final int ticksPerSmelt = 185; // default vanilla value
	
	public BlockMachineElectricFurnace(String name) {
		super(name, Config.simple_energyBuffer, new TileEntityElectricFurnace());
	}
}
