package ihamfp.IhTech.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ihamfp.IhTech.ModIhTech;
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
		super(name, new TileEntityElectricFurnace());
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		return tryOpenGUI(world, pos, player, this.GUI_ID);
	}
}
