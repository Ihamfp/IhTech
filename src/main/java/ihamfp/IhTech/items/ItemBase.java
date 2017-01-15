package ihamfp.IhTech.items;

import ihamfp.IhTech.ModIhTech;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBase extends Item {	
	public ItemBase(String name) {
		setRegistryName(ModIhTech.MODID, name);
		setUnlocalizedName(getRegistryName().toString());
	}
	
	public void register() {
		GameRegistry.register(this);
	}
}
