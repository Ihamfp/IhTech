package ihamfp.IhTech.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.blocks.BlockEnergyStorage;
import ihamfp.IhTech.common.GuiHandler.EnumGUIs;
import ihamfp.IhTech.creativeTabs.ModCreativeTabs;
import ihamfp.IhTech.interfaces.ITileEntityInteractable;

public class BlockMachineElectricBase<T extends TileEntity> extends BlockEnergyStorage {
	private final T teInstance;
	private final Class<T> teClass;
	
	static final PropertyBool active = PropertyBool.create("active");
	
	public BlockMachineElectricBase(String name, Material mat, int capacity, T te) {
		super(name, mat, capacity);
		this.setFaced();
		this.setCreativeTab(ModCreativeTabs.MACHINES);
		this.teInstance = te;
		this.teClass = (Class<T>)te.getClass();
		setDefaultState(blockState.getBaseState().withProperty(FACING,  EnumFacing.NORTH).withProperty(active, false));
	}
	
	public BlockMachineElectricBase(String name, int capacity, T te) {
		this(name, Material.IRON, capacity, te);
	}
	
	@Override
	public void register() {
		super.register();
		GameRegistry.registerTileEntity(this.teClass, ModIhTech.MODID + "_TE_" + this.getRegistryName());
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
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, active);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote || player.isSneaking()) return true;
		
		if (this.teInstance instanceof ITileEntityInteractable && this.GUI_ID != EnumGUIs.GUI_NONE.ordinal()) {
			player.openGui(ModIhTech.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return false;
	}
}
