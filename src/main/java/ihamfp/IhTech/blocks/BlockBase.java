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
	private boolean isFaced = false;
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public BlockBase(String name, Material material, MapColor mapColor) {
		super(material, mapColor);
		this.name = name;
		setRegistryName(ModIhTech.MODID, name);
		setUnlocalizedName(getRegistryName().toString());
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	public BlockBase(String name, Material material) {
		this(name, material, material.getMaterialMapColor());
	}
	
	public void register() {
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	/**
	 * Warning: the 3 lowest bits of the metadata will be used for storing the facing !
	 */
	public void setFaced() {
		isFaced = true;
	}
	
	@Override
	public BlockBase setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (this.isFaced) {
			world.setBlockState(pos, state.withProperty(FACING, EnumFacing.getFacingFromVector(
				(float) (placer.posX - pos.getX()),
				(float) (placer.posY - pos.getY()),
				(float) (placer.posZ - pos.getZ())
			)));
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (this.isFaced) {
			return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)); // 7 = 0b111
		}
		return getDefaultState();
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		if (this.isFaced) {
			return state.getValue(FACING).getIndex();
		}
		return 0;
	}
}
