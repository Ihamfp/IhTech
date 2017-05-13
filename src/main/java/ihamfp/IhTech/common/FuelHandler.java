package ihamfp.IhTech.common;

import java.util.HashMap;
import java.util.Map;

import ihamfp.IhTech.Materials;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.items.ItemGenericResource;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {
	
	private Map<String,Double> multipliers = new HashMap<String,Double>() {{
		put("ingot", 1.0);
		put("block", 9.1);
		put("gem", 1.0);
		put("nugget", 0.1);
		put("dust", 0.9);
		put("plate", 1.0);
		put("rod", 0.5);
	}};
	
	@Override
	public int getBurnTime(ItemStack fuel) {
		if (fuel.getItem() instanceof ItemGenericResource) {
			int base = Materials.materials.get(fuel.getMetadata()).burningEnergy;
			if (base == 0) return 0;
			ItemGenericResource item = (ItemGenericResource)fuel.getItem();
			if (multipliers.containsKey(item.type)) {
				return (int) (base*multipliers.get(item.type));
			}
		}
		return 0;
	}
}
