package ihamfp.IhTech.blocks;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.TileEntityBatteryRack;
import ihamfp.IhTech.common.GuiHandler.EnumGUIs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBatteryRack extends BlockEnergyStorage {
	
	public static int GUI_ID = EnumGUIs.GUI_ONESLOT.ordinal();
	
	public BlockBatteryRack() {
		super("blockBatteryRack", Material.IRON);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityBatteryRack te = new TileEntityBatteryRack();
		return te;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		if (player.isSneaking()) {
			return true;
		}
		player.openGui(ModIhTech.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
}
