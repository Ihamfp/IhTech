package ihamfp.IhTech.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (!tab.equals(this.getCreativeTab())) return;
		for (int i=1; i<getItemsList().length; ++i) {
			ItemStack stack = new ItemStack(this, 1, i);
			subItems.add(stack);
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getMetadata();
		return super.getUnlocalizedName(stack) + "." + getItemsList()[meta];
	}
}
