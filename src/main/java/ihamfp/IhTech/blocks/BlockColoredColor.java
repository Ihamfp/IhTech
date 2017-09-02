package ihamfp.IhTech.blocks;

import java.awt.Color;

import ihamfp.IhTech.Materials;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockColoredColor implements IBlockColor {

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
		Block block = state.getBlock();
		if (block instanceof BlockGenericResource && tintIndex == 0) {
			return Materials.materials.get(((BlockGenericResource)block).material).color;
		}
		return Color.BLACK.getRGB();
	}

}
