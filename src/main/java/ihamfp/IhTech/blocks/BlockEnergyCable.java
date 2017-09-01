package ihamfp.IhTech.blocks;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ihamfp.IhTech.Materials;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.TileEntityEnergyCable;
import ihamfp.IhTech.blocks.properties.UnlistedPropertyBoolean;
import ihamfp.IhTech.blocks.properties.UnlistedPropertyCableSide;
import ihamfp.IhTech.common.Config;
import ihamfp.IhTech.creativeTabs.ModCreativeTabs;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import ihamfp.IhTech.models.ExampleBakedModel;

public class BlockEnergyCable extends BlockEnergyStorage {
	public static final PropertyInteger STACKED_CABLES_LEVEL = PropertyInteger.create("stacked_cables_level", 0, 2); // stacked = level^2
	
	public static final UnlistedPropertyCableSide NORTH = new UnlistedPropertyCableSide("north");
	public static final UnlistedPropertyCableSide SOUTH = new UnlistedPropertyCableSide("south");
	public static final UnlistedPropertyCableSide EAST = new UnlistedPropertyCableSide("east");
	public static final UnlistedPropertyCableSide WEST = new UnlistedPropertyCableSide("west");
	public static final UnlistedPropertyCableSide UP = new UnlistedPropertyCableSide("up");
	public static final UnlistedPropertyCableSide DOWN = new UnlistedPropertyCableSide("down");
	
	public int energyCapacity = 0;
	
	public BlockEnergyCable(String prefix, int matID) {
		super(prefix + Materials.materials.get(matID).name, Material.IRON);
		this.energyCapacity = Materials.materials.get(matID).energyCableCapacity;
		if (this.energyCapacity == 0) {
			ModIhTech.logger.error("Cable of material " + Materials.materials.get(matID).name + " is not conductive !!! This is an error !!!");
		}
		this.setHardness(0.5f);
		this.setResistance(0.5f);
		this.setDefaultState(blockState.getBaseState().withProperty(STACKED_CABLES_LEVEL, 1));
		this.setCreativeTab(ModCreativeTabs.PIPES);
	}
	
	/*
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		int multiplier = (int)Math.pow(2, stack.getMetadata());
		tooltip.add(this.energyCapacity*multiplier + " " + Config.energyUnitName + "/t");
	}*/
	
	@Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i=0;i<3;i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) return false;
		TileEntityEnergyCable te = (TileEntityEnergyCable) worldIn.getTileEntity(pos);
		playerIn.sendMessage(new TextComponentString("Conductivity: " + te.energyCapacity)); // TODO remove this (low priority)
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityEnergyCable te = new TileEntityEnergyCable();
		return te;
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if (!world.isRemote) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileEntityEnergyCable) {
				((TileEntityEnergyCable)te).separateFromNetworks();
			}
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		((TileEntityEnergyCable)worldIn.getTileEntity(pos)).checkForNodesAround();
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState blockState) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState blockState) {
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.markBlockRangeForRenderUpdate(pos.add(-1,-1,-1), pos.add(1,1,1));
		world.setBlockState(pos, state.withProperty(STACKED_CABLES_LEVEL, stack.getMetadata()));
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		BlockStateContainer baseState = super.createBlockState();
		Collection<IProperty<?>> propertiesCollection = baseState.getProperties();
		
		IProperty<?>[] listedProperties = propertiesCollection.toArray(new IProperty<?>[propertiesCollection.size()+1]);
		IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] {
				NORTH, SOUTH, EAST, WEST, UP, DOWN
		};
		
		listedProperties[propertiesCollection.size()] = STACKED_CABLES_LEVEL;
		
		return new ExtendedBlockState(this, listedProperties, unlistedProperties);
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedBlockState = (IExtendedBlockState)state;
		
		if (world.getTileEntity(pos) == null || !(world.getTileEntity(pos) instanceof TileEntityEnergyCable)) {
			return extendedBlockState;
		}
		TileEntityEnergyCable te = (TileEntityEnergyCable)world.getTileEntity(pos);
		
		return extendedBlockState
				.withProperty(NORTH, te.getSideRenderType(EnumFacing.NORTH))
				.withProperty(SOUTH, te.getSideRenderType(EnumFacing.SOUTH))
				.withProperty(EAST, te.getSideRenderType(EnumFacing.EAST))
				.withProperty(WEST, te.getSideRenderType(EnumFacing.WEST))
				.withProperty(UP, te.getSideRenderType(EnumFacing.UP))
				.withProperty(DOWN, te.getSideRenderType(EnumFacing.DOWN));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(STACKED_CABLES_LEVEL, (meta&3));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(STACKED_CABLES_LEVEL);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel()	{
		for (int i=0;i<3;i++) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, new ModelResourceLocation(ModIhTech.MODID + ":blockcable" + (1<<i) + "x", "inventory"));
		}
		StateMapperBase stateMap = new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				switch (state.getValue(STACKED_CABLES_LEVEL)) {
				default:
					return ExampleBakedModel.BAKED_MODEL;
				}
			}
		};
		ModelLoader.setCustomStateMapper(this, stateMap);
	}
	
	@SideOnly(Side.CLIENT)
	public void initItemModel() {
		Item itemBlock = Item.getItemFromBlock(this);
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("blockcable", "inventory");
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, 0, itemModelResourceLocation);
	}
}
