package ihamfp.IhTech.items;

import java.awt.Color;
import java.util.Random;

import ihamfp.IhTech.interfaces.IItemColored;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemColoredColor implements IItemColor {
	private static int randomColor = Color.decode("0x52414E").getRGB(); /* set the material color to this for a random color (hex for "RAN")
	                                                                     * Warning: the color changes EACH FRAME and is independent for every item */
	Random random = new Random();
	
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		if (tintIndex != 0) return Color.WHITE.getRGB();
		
		Item item = stack.getItem();
		if (item instanceof IItemColored) {
			if (((IItemColored)item).getColor(stack) == randomColor) {
				return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)).getRGB();
			}
			return ((IItemColored)item).getColor(stack);
		}
		return Color.BLACK.getRGB();
	}

}
