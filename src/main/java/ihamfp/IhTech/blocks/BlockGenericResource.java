package ihamfp.IhTech.blocks;

import ihamfp.IhTech.Materials;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.common.ResourceMaterial.ResourceType;
import ihamfp.IhTech.creativeTabs.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGenericResource extends Block {
	
	public int material = 0;
	protected String name;
	protected String prefix;
	
	public BlockGenericResource(String prefix, int material, Material blockMat) {
		super(blockMat);
		this.name = prefix + Materials.materials.get(material).name;
		this.material = material;
		this.prefix = prefix;
		this.setRegistryName(ModIhTech.MODID, this.name);
		this.setUnlocalizedName(getRegistryName().toString());
		//this.setHarvestLevel("pickaxe", Materials.materials.get(material).oreLevel);
		this.setCreativeTab(ModCreativeTabs.RESOURCES);
	}
	
	public void register() {
		/*GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));*/
	}
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	private String getModel() {
		if (prefix == "blockStorage" && Materials.materials.get(material).resourceType == ResourceType.CRYSTAL) {
			return "blockstoragegem";
		} else if (prefix == "blockStorage" && Materials.materials.get(material).resourceType == ResourceType.COAL) {
			return "blockstoragecoal";
		} else {
			return prefix.toLowerCase();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		//ModelBakery.registerItemVariants(Item.getItemFromBlock(this), new ResourceLocation(ModIhTech.MODID + ":blockOre"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(ModIhTech.MODID + ":" + getModel(), "inventory"));
		ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState ibs) {
				return new ModelResourceLocation(ModIhTech.MODID + ":" + getModel());
			}
		});
	}
}
