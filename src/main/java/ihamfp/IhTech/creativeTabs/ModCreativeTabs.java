package ihamfp.IhTech.creativeTabs;

import ihamfp.IhTech.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ModCreativeTabs {
	public static CreativeTabs RESOURCES = new CreativeTabs("ihtech.resources") {
		
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.ingot, 1, 9);
		}
	}; //new TabResources();
	public static CreativeTabs PIPES = new CreativeTabs("ihtech.pipes") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.wire);
		}
	};
	public static CreativeTabs MACHINES = new TabMachines();
}
