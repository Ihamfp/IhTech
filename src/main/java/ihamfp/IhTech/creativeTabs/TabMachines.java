package ihamfp.IhTech.creativeTabs;

import ihamfp.IhTech.blocks.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TabMachines extends CreativeTabs {
	private static ItemStack display = new ItemStack(Item.getItemFromBlock(ModBlocks.blockElectricFurnace));
	
	public TabMachines() {
		super("ihtech.machines");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() {
		return this.display;
	}
}
