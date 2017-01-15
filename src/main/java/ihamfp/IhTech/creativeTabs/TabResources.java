package ihamfp.IhTech.creativeTabs;

import ihamfp.IhTech.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TabResources extends CreativeTabs {

	public TabResources() {
		super("ihtech.resources");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return ModItems.ingot;
	}

}
