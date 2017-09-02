package ihamfp.IhTech.blocks;

import java.util.List;

import ihamfp.IhTech.TileEntities.TileEntityEnergyStorage;
import ihamfp.IhTech.common.Config;
import ihamfp.IhTech.common.GuiHandler.EnumGUIs;
import ihamfp.IhTech.interfaces.ITOPInfoProvider;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import ihamfp.IhTech.interfaces.IWailaInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
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
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnergyStorage extends BlockBase implements ITileEntityProvider, IWailaInfoProvider, ITOPInfoProvider {
	public static int GUI_ID = EnumGUIs.GUI_NONE.ordinal();
	
	public BlockEnergyStorage(String name, Material material, MapColor mapColor) {
		super(name, material, mapColor);
		
		this.isBlockContainer = true;
	}
	
	public BlockEnergyStorage(String name, Material material) {
		super(name, material);
	}
	
	@Override
	public BlockEnergyStorage setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityEnergyStorage te = new TileEntityEnergyStorage();
		return te;
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
		
		IEnergyStorage es = ((ITileEntityEnergyStorage)accessor.getTileEntity()).getEnergyStorage();
		if (es == null) return currenttip;
		currenttip.add("Energy stored: " + es.getEnergyStored() + "/" + es.getMaxEnergyStored() + " " + Config.energyUnitName);
		return currenttip;
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		TileEntity te = world.getTileEntity(data.getPos());
		if (!(te instanceof ITileEntityEnergyStorage)) return;
		IEnergyStorage energy = ((ITileEntityEnergyStorage)te).getEnergyStorage();
		if (energy == null) return;
		probeInfo.horizontal()
			.text("Energy stored:")
			.progress(energy.getEnergyStored(), energy.getMaxEnergyStored(), probeInfo.defaultProgressStyle().suffix(Config.energyUnitName));
	}
}
