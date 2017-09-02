package ihamfp.IhTech.blocks.machines;

import java.util.List;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.blocks.BlockBase;
import ihamfp.IhTech.common.GuiHandler.EnumGUIs;
import ihamfp.IhTech.creativeTabs.ModCreativeTabs;
import ihamfp.IhTech.interfaces.ITOPInfoProvider;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;
import ihamfp.IhTech.interfaces.IWailaInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMachineBase<T extends TileEntity> extends BlockBase implements ITileEntityProvider, IWailaInfoProvider, ITOPInfoProvider {
	private final T teInstance;
	private final Class<T> teClass;
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	
	public BlockMachineBase(String name, Material mat, T te) {
		super(name, mat);
		this.setCreativeTab(ModCreativeTabs.MACHINES);
		this.teInstance = te;
		this.teClass = (Class<T>)te.getClass();
		setDefaultState(blockState.getBaseState().withProperty(FACING,  EnumFacing.NORTH).withProperty(ACTIVE, false));
	}
	
	public BlockMachineBase(String name, T te) {
		this(name, Material.IRON, te);
	}
	
	// Texture
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		try {
			return this.teClass.newInstance();
		} catch (Exception e) {
			ModIhTech.logger.error("Could not create an instance of " + teClass.getCanonicalName());
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos, state.withProperty(FACING, EnumFacing.getFacingFromVector(
			(float) (placer.posX - pos.getX()),
			(float) (placer.posY - pos.getY()),
			(float) (placer.posZ - pos.getZ())
		)));
	}
	
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ACTIVE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)); // 7 = 0b111
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}
	
	protected boolean tryOpenGUI(World world, BlockPos pos, EntityPlayer player, int id) {
		if (world.isRemote || player.isSneaking()) return true;
		
		if (this.teInstance instanceof ITileEntityInteractable && id != EnumGUIs.GUI_NONE.ordinal()) {
			player.openGui(ModIhTech.instance, id, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		
		return currenttip;
	}
}
