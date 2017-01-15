package ihamfp.IhTech.blocks;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.TileEntities.TileEntityEnergyStorage;
import ihamfp.IhTech.common.CommonProxy;
import ihamfp.IhTech.common.Config;
import ihamfp.IhTech.interfaces.ITOPInfoProvider;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import ihamfp.IhTech.interfaces.IWailaInfoProvider;

import java.util.List;

import javax.annotation.Nullable;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnergyStorage extends BlockBase implements ITileEntityProvider, IWailaInfoProvider, ITOPInfoProvider {
	public static int GUI_ID;
	
	public int capacity;
	
	public BlockEnergyStorage(String name, Material material, MapColor mapColor, int capacity) {
		super(name, material, mapColor);
		this.capacity = capacity;
		
		this.isBlockContainer = true;
	}
	
	public BlockEnergyStorage(String name, Material material, int capacity) {
		super(name, material);
		this.capacity = capacity;
	}
	
	@Override
	public BlockEnergyStorage setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityEnergyStorage te = new TileEntityEnergyStorage();
		te.setEnergyStorage(new EnergyStorage(this.capacity));
		return te;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof ITileEntityEnergyStorage))
			return false;
		
		return true;
	}
	
	// Texture
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntity te = accessor.getTileEntity();
		if (!(te instanceof ITileEntityEnergyStorage))
			return currenttip;
		
		EnergyStorage es = ((ITileEntityEnergyStorage)accessor.getTileEntity()).getEnergyStorage();
		currenttip.add("Energy stored: " + es.getEnergyStored() + "/" + es.getMaxEnergyStored() + " " + Config.energyUnitName);
		return currenttip;
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		TileEntity te = world.getTileEntity(data.getPos());
		if (te instanceof ITileEntityEnergyStorage) {
			EnergyStorage energy = ((ITileEntityEnergyStorage)te).getEnergyStorage();
			probeInfo.horizontal()
				.text("Energy stored:")
				.progress(energy.getEnergyStored(), energy.getMaxEnergyStored(), probeInfo.defaultProgressStyle().suffix(Config.energyUnitName));
		}
	}
}
