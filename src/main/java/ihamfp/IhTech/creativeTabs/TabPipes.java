package ihamfp.IhTech.creativeTabs;

import java.util.Date;
import java.util.Random;

import ihamfp.IhTech.blocks.ModBlocks;
import ihamfp.IhTech.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TabPipes extends CreativeTabs {
	
	public TabPipes() {
		super("ihtech.pipes");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Item getTabIconItem() {
		return ModItems.wire;
	}
}
