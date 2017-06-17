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
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemGenericResource extends ItemBase implements IItemColored {
	
	public String type;
	private String hasMatch;
	
	public ItemGenericResource(String type) {
		this(type, type);
	}
	
	/***
	 * @param type type of the resource (as in registry and in oreDict)
	 * @param hasMatch resource "has" match type, see @ref:ModItems.densePlate
	 */
	public ItemGenericResource(String type, String hasMatch) {
		super(type);
		this.type = type;
		this.hasMatch = hasMatch;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(ModCreativeTabs.RESOURCES);
	}
	
	@Override
	public void register() {
		GameRegistry.register(this);
		for (int i=0; i<Materials.materials.size(); ++i) {
			ResourceMaterial material = Materials.materials.get(i);
			if (material.has(this.hasMatch) && material.getItemFor(this.type) == null) { // only add if not present
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getItemStackDisplayName(ItemStack stack) {
		int meta = stack.getMetadata();
		if (meta >= Materials.materials.size()) {
			meta = 0;
		}
		
		String key = "item." + ModIhTech.MODID + ":" + this.type.toLowerCase() + ".format";
		if (I18n.hasKey(getUnlocalizedName(stack).toLowerCase() + ".name")) {
			return I18n.format(getUnlocalizedName(stack).toLowerCase() + ".name");
		} else if (I18n.hasKey(key)) {
			return I18n.format(key, Materials.materials.get(meta).name);
		} else {
			return this.getUnlocalizedName(stack);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {		
		for (int i=0; i<Materials.materials.size(); ++i) {
			if (Materials.materials.get(i).customRenders.containsKey(this.type)) {
				ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(Materials.materials.get(i).customRenders.get(this.type)));
			} else if (this.type == "ingot" && Materials.materials.get(i).resourceType == ResourceType.METAL) {
				ModelResourceLocation metalIngot = new ModelResourceLocation(ModIhTech.MODID + ":metalingot_template", "inventory");
				ModelLoader.setCustomModelResourceLocation(this, i, metalIngot);
			} else if (this.type == "plate" && (Materials.materials.get(i).resourceType == ResourceType.DUST || Materials.materials.get(i).resourceType == ResourceType.CRYSTAL)) {
				ModelResourceLocation dustPlate = new ModelResourceLocation(ModIhTech.MODID + ":dustplate_template", "inventory");
				ModelLoader.setCustomModelResourceLocation(this, i, dustPlate);
			} else if (this.type == "gem" && Materials.materials.get(i).resourceType == ResourceType.COAL) {
				ModelResourceLocation coalGem = new ModelResourceLocation(ModIhTech.MODID + ":coal_template", "inventory");
				ModelLoader.setCustomModelResourceLocation(this, i, coalGem);
			} else if (this.type == "gem") {
				ModelResourceLocation gem = new ModelResourceLocation(ModIhTech.MODID + ":gem_template", "inventory");
				ModelLoader.setCustomModelResourceLocation(this, i, gem);
			} else {
				ModelResourceLocation resourceLocation = new ModelResourceLocation(ModIhTech.MODID + ":" + this.type.toLowerCase() + "_template", "inventory");
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
