package ihamfp.IhTech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockMachineCasing extends Block {
	public static enum CasingType {
		PRIMITIVE("Primitive", ""),
		BASIC("Basic", "Iron"),
		AGRICULTURE("Agriculture", "Plastic"),
		PRESSURIZED("Pressurized", "Bronze"),
		HIGHLY_PRESSURIZED("Highly_pressurized", "Cast iron"),
		ADVANCED("Advanced", "Steel"),
		REINFORCED("Reinforced", ""),
		LIGHT("Light", "Aluminium"),
		HEAVY("Heavy", "Tungsten"),
		TOP_TIER("Top_tier", "Iridium"),
		QUANTIC("Quantic", "Enderium"),
		BLACK("Black", "Black matter");
		
		private String name;
		private String material;
		
		// Leave material blank to not add crafting
		private CasingType(String name, String material) {
			this.name = name;
			this.material = material;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getMaterial() {
			return this.material;
		}
	}
	
	public static final PropertyInteger TYPE = PropertyInteger.create("Type", 0, CasingType.values().length);
	
	public BlockMachineCasing() {
		super(Material.IRON);
	}

}
