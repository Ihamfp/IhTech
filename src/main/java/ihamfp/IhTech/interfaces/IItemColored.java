package ihamfp.IhTech.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemColored {
	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack);
}
