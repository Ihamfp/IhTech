package ihamfp.IhTech.blocks;

import ihamfp.IhTech.ModIhTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

/***
 * Contains everything to make facing blocks, only 1 metadata bit left if "setFaced" is called.
 * You may want to use the Block class for simpler applications.
 */
public class BlockBase extends Block {
	private String name;
	
	public BlockBase(String name, Material material, MapColor mapColor) {
		super(material, mapColor);
		this.name = name;
		setRegistryName(ModIhTech.MODID, name);
		setUnlocalizedName(getRegistryName().toString());
	}
	
	public BlockBase(String name, Material material) {
		this(name, material, material.getMaterialMapColor());
	}
	
	public void register() {
		
	}
	
	@Override
	public BlockBase setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
}
