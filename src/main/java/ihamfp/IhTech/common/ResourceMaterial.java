package ihamfp.IhTech.common;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class ResourceMaterial {
	public static enum OreDrop {
		ORE, // the ore itself
		DUST,
		GEM,
		NUGGET,
	}
	
	public static enum StorageBlockType {
		FROM_INGOT,
		FROM_DUST,
		FROM_GEM,
		FROM_PLATE,
		FROM_NOTHING, // No craft will be added for the storage block
	}
	
	public static enum ResourceType {
		METAL,
		DUST,
		COAL,
		CRYSTAL,
		ALL,
		NONE, // will not add recipes
	}
	
	public String name;
	public int color;
	
	// Mainly used for TConstruct
	public int toolDurability = 0;
	public int miningLevel = 0;
	public float miningSpeed = 1;
	public float attackDamages = 0;
	public float handleModifier = 1.0f;
	public int meltingPoint = 773; // 500°C
	
	// Ore and storage block
	public OreDrop oreDrop;
	public int oreLevel = 1; // mining level required to mine the ore
	public StorageBlockType storageBlockType;
	public ResourceType resourceType = ResourceType.NONE;
	public float hardness = 0.1f;
	public float resistance = 0.1f;
	
	// Things
	private Hashtable<String, Boolean> hasThings = new Hashtable<String, Boolean>();
	public Hashtable<String, ItemStack> items = new Hashtable<String, ItemStack>();
	public Hashtable<String, String> customRenders = new Hashtable<String, String>();
	
	// Energy
	public int energyCableCapacity = 0;
	public int burningEnergy = 0; // in ticks
	
	
	// Please capitalize the first letter of the name
	public ResourceMaterial(String name, String colorEncoded) {
		this.name = name;
		color = Color.decode(colorEncoded).getRGB();
		oreDrop = OreDrop.ORE;
	}
	
	public ResourceMaterial setType(ResourceType type) {
		this.resourceType = type;
		switch (type) {
		case METAL: // ex: iron
			this.setHas("ingot");
			this.setHas("nugget");
			this.setHas("dust");
			this.setHas("plate");
			this.setHas("rod");
			this.setHas("block");
			break;
		
		case DUST: // ex: redstone, glowstone
			this.setHas("dust");
			this.setHas("block");
			break;
		
		case COAL: // ex: coal, coke...
		case CRYSTAL: // ex: diamond, emerald
			this.setHas("gem");
			this.setHas("dust");
			this.setHas("block");
			break;
		
		case ALL:
			this.setHas("ingot");
			this.setHas("nugget");
			this.setHas("dust");
			this.setHas("gem");
			this.setHas("plate");
			this.setHas("rod");
			this.setHas("block");
			
		case NONE:
		default:
			break;
		}
		
		return this;
	}
	
	public boolean has(String thing) {
		if (this.hasThings.containsKey(thing)) {
			return true;
		}
		
		return false;
	}
	
	public ResourceMaterial setHas(String thing) {
		this.hasThings.put(thing, true);
		return this;
	}
	
	public ItemStack getItemFor(String thing) {
		return this.items.get(thing);
	}
	
	public ResourceMaterial setItemFor(String thing, ItemStack item) {
		if (!this.hasThings.containsKey(thing)) {
			this.hasThings.put(thing, true);
		}
		this.items.put(thing, item);
		
		return this;
	}
	
	public ResourceMaterial setToolProperties(int durability, int miningLevel, float miningSpeed, float attackDamages) {
		this.toolDurability = durability;
		this.miningLevel = miningLevel;
		this.miningSpeed = miningSpeed;
		this.attackDamages = attackDamages;
		return this;
	}
	
	/** In Kelvins */
	public ResourceMaterial setMeltingPoint(int meltingPoint) {
		this.meltingPoint = meltingPoint;
		return this;
	}
	
	public ResourceMaterial setOreLevel(int level) {
		this.oreLevel = level;
		return this;
	}
	
	public ResourceMaterial setCustomRender(String thing, String resourceLocation) {
		this.customRenders.put(thing, resourceLocation);
		return this;
	}
	
	public ResourceMaterial setEnergyCableCapacity(int cap) {
		this.energyCableCapacity = cap;
		this.setHas("wire");
		return this;
	}
	
	public ResourceMaterial setBurningEnergy(int ticks) {
		this.burningEnergy = ticks;
		return this;
	}
	
	public ResourceMaterial setBurningEnergy(Item reference, int mult) {
		this.burningEnergy = TileEntityFurnace.getItemBurnTime(new ItemStack(reference))*mult;
		return this;
	}
	
	public ResourceMaterial setBurningEnergy(Item reference, float mult) {
		this.burningEnergy = (int) (TileEntityFurnace.getItemBurnTime(new ItemStack(reference))*mult);
		return this;
	}
}
