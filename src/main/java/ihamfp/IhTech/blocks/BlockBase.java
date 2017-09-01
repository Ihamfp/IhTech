package ihamfp.IhTech.blocks;

import ihamfp.IhTech.ModIhTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
