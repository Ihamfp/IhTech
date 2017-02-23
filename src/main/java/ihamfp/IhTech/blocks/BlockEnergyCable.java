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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.TileEntityEnergyCable;
import ihamfp.IhTech.blocks.properties.UnlistedPropertyBoolean;
import ihamfp.IhTech.blocks.properties.UnlistedPropertyCableSide;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import ihamfp.IhTech.models.ExampleBakedModel;

public class BlockEnergyCable extends BlockEnergyStorage {
	public static final PropertyInteger STACKED_CABLES_LEVEL = PropertyInteger.create("stacked_cables_level", 1, 3); // stacked = level^2
	
	public static final UnlistedPropertyCableSide NORTH = new UnlistedPropertyCableSide("north");
	public static final UnlistedPropertyCableSide SOUTH = new UnlistedPropertyCableSide("south");
	public static final UnlistedPropertyCableSide EAST = new UnlistedPropertyCableSide("east");
	public static final UnlistedPropertyCableSide WEST = new UnlistedPropertyCableSide("west");
	public static final UnlistedPropertyCableSide UP = new UnlistedPropertyCableSide("up");
	public static final UnlistedPropertyCableSide DOWN = new UnlistedPropertyCableSide("down");
	
	public BlockEnergyCable(String name, Material material) {
		super(name, material);
		this.setHardness(0.5f);
		this.setResistance(0.5f);
		this.setDefaultState(blockState.getBaseState().withProperty(STACKED_CABLES_LEVEL, 1));
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
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
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
		if (meta == 0) {
			return super.getStateFromMeta(meta).withProperty(STACKED_CABLES_LEVEL, 1);
		}
		return super.getStateFromMeta(meta).withProperty(STACKED_CABLES_LEVEL, (meta&3));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return (super.getMetaFromState(state) | state.getValue(STACKED_CABLES_LEVEL));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel()	{
		//ModIhTech.logger.info("Initialized model for " + this.getRegistryName());
		// Working !
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
		Item itemBlock = Item.REGISTRY.getObject(new ResourceLocation(ModIhTech.MODID, "blockCable"));
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(this.getRegistryName(), "inventory");
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, 0, itemModelResourceLocation);
	}
}
