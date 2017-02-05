package ihamfp.IhTech.compatibility;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.blocks.BlockEnergyStorage;
import ihamfp.IhTech.interfaces.IWailaInfoProvider;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaCompatibility implements IWailaDataProvider {
	
	public static final WailaCompatibility instance = new WailaCompatibility();
	
	private static boolean registered;
	private static boolean loaded;
	
	public static void load(IWailaRegistrar registrar) {
		if (!registered) {
			throw new RuntimeException("The WailaCompatibility has not been registered.");
		}
		if (!loaded) {
			registrar.registerBodyProvider(instance, BlockEnergyStorage.class);
			
			ModIhTech.logger.log(Level.INFO, "Loaded Waila Compatibility");
			loaded = true;
		}
	}
	
	public static void register() {
		if (registered) {
			return;
		}
		FMLInterModComms.sendMessage("Waila", "register", "ihamfp.IhTech.compatibility.WailaCompatibility.load");
		registered = true;
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		Block block = accessor.getBlock();
		if (block instanceof IWailaInfoProvider) {
			return ((IWailaInfoProvider) block).getWailaBody(itemStack, currenttip, accessor, config);
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
		return tag;
	}
	
}
