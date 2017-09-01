package ihamfp.IhTech.blocks.machines;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ihamfp.IhTech.TileEntities.machines.TileEntityElectricGrinder;
import ihamfp.IhTech.common.GuiHandler.EnumGUIs;

public class BlockMachineElectricGrinder extends BlockMachineBase<TileEntityElectricGrinder> {
	public static int GUI_ID = EnumGUIs.GUI_ELGRINDER.ordinal();

	public BlockMachineElectricGrinder(String name) {
		super(name, new TileEntityElectricGrinder());
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,  EnumFacing side, float hitX, float hitY, float hitZ) {
		return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ) || tryOpenGUI(world, pos, player, this.GUI_ID);
	}

}
