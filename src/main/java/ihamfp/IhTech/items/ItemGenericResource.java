package ihamfp.IhTech.items;

import java.awt.Color;
import java.util.List;

import ihamfp.IhTech.Materials;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.common.Config;
import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.common.ResourceMaterial.ResourceType;
import ihamfp.IhTech.creativeTabs.ModCreativeTabs;
import ihamfp.IhTech.interfaces.IItemColored;
import net.minecraft.block.BlockFurnace;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemGenericResource extends ItemBase implements IItemColored {
	
	private String type;
	ItemDye d;
	
	public ItemGenericResource(String type) {
		super(type);
		this.type = type;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(ModCreativeTabs.RESOURCES);
	}
	
	@Override
	public void register() {
		GameRegistry.register(this);
		//this.setCreativeTab(ModCreativeTabs.RESOURCES);
		for (int i=0; i<Materials.materials.size(); ++i) {
			ResourceMaterial material = Materials.materials.get(i);
			if (material.has(this.type) && material.getItemFor(this.type) == null) { // only add if not present
				if (OreDictionary.getOres("" + this.type + material.name).size() == 0 || Config.alwaysAddResources) { // Item doesn't exist in the game
					ItemStack stack = new ItemStack(this, 1, i);
					material.setItemFor(this.type, stack);
					OreDictionary.registerOre("" + this.type + material.name, stack);
					if (Materials.oreDictAliasses.containsKey(material.name)) {
						for (String alias : Materials.oreDictAliasses.get(material.name)) {
							OreDictionary.registerOre("" + this.type + alias, stack);
						}
					}
				} else { // Item already exists (another mod added it)
					material.setItemFor(this.type, OreDictionary.getOres("" + this.type + material.name).get(0));
				}
			}
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getMetadata();
		if (meta >= Materials.materials.size()) {
			meta = 0;
		}
		return super.getUnlocalizedName(stack) + "." + Materials.materials.get(meta).name;
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation resourceLocation = new ModelResourceLocation("ihtech:" + this.type + "_template", "inventory");
		ModelResourceLocation metalIngot = null;
		if (this.type == "ingot") {
			metalIngot = new ModelResourceLocation("ihtech:metalingot_template", "inventory");
		}
		
		for (int i=0; i<Materials.materials.size(); ++i) {
			if (Materials.materials.get(i).customRenders.containsKey(this.type)) {
				ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(Materials.materials.get(i).customRenders.get(this.type)));
			} else if (this.type == "ingot" && Materials.materials.get(i).resourceType == ResourceType.METAL) {
				ModelLoader.setCustomModelResourceLocation(this, i, metalIngot);
			} else {
				ModelLoader.setCustomModelResourceLocation(this, i, resourceLocation);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack) {
		int meta = stack.getMetadata();
		if (meta < Materials.materials.size() && !Materials.materials.get(meta).customRenders.containsKey(this.type)) {
			return Materials.materials.get(meta).color;
		}
		return Color.WHITE.getRGB();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int i=1; i<Materials.materials.size(); ++i) {
			if (Materials.materials.get(i).has(this.type) && Materials.materials.get(i).getItemFor(this.type) != null && Materials.materials.get(i).getItemFor(this.type).getItem() instanceof ItemGenericResource) {
				ItemStack stack = new ItemStack(itemIn, 1, i);
				subItems.add(stack);
			}
		}
	}

}
