package ihamfp.IhTech.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ihamfp.IhTech.Materials;
import ihamfp.IhTech.creativeTabs.ModCreativeTabs;

public class ItemMulti extends ItemBase {
	
	protected String[] itemsList;
	
	public ItemMulti(String name, String[] itemsList) {
		super(name);
		this.itemsList = itemsList;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	public String[] getItemsList() {
		return this.itemsList;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int i=1; i<getItemsList().length; ++i) {
			ItemStack stack = new ItemStack(itemIn, 1, i);
			subItems.add(stack);
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getMetadata();
		return super.getUnlocalizedName(stack) + "." + getItemsList()[meta];
	}
}
