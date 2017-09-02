package ihamfp.IhTech.items;

import ihamfp.IhTech.ModIhTech;
import net.minecraft.item.Item;

public class ItemBase extends Item {	
	public ItemBase(String name) {
		setRegistryName(ModIhTech.MODID, name);
		setUnlocalizedName(getRegistryName().toString());
	}
	
	public void register() {
		
	}
}
