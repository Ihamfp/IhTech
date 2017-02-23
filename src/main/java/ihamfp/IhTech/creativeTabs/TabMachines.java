package ihamfp.IhTech.creativeTabs;

import ihamfp.IhTech.blocks.ModBlocks;
import ihamfp.IhTech.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TabMachines extends CreativeTabs {
	public TabMachines() {
		super("ihtech.machines");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return new ItemBlock(ModBlocks.blockElectricFurnace);
	}
}
