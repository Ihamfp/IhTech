package ihamfp.IhTech.blocks;

import ihamfp.IhTech.Materials;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockColoredColor implements IItemColor {

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		Block block = ((ItemBlock)stack.getItem()).getBlock();
		if (block instanceof BlockGenericResource && tintIndex == 0) {
			return Materials.materials.get(((BlockGenericResource)block).material).color;
		}
		return Color.WHITE.getRGB();
	}
}