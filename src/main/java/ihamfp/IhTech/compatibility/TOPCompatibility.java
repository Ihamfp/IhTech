package ihamfp.IhTech.compatibility;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.interfaces.ITOPInfoProvider;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TOPCompatibility {
	private static boolean registered;
	
	public static void register() {
		if (registered)
			return;
		registered = true;
		FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "ihamfp.IhTech.compatibility.TOPCompatibility$GetTheOneProbe");
	}
	
	public static class GetTheOneProbe implements com.google.common.base.Function<ITheOneProbe, Void> {
		public static ITheOneProbe probe;
		
		@Nullable
		@Override
		public Void apply(ITheOneProbe theOneProbe) {
			this.probe = theOneProbe;
			ModIhTech.logger.log(Level.INFO, "Loaded TheOneProbe Compatibility");
			probe.registerProvider(new IProbeInfoProvider() {
				@Override
				public String getID() {
					return "ihtech:default";
				}

				@Override
				public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
					if (blockState.getBlock() instanceof ITOPInfoProvider) {
						ITOPInfoProvider provider = (ITOPInfoProvider)blockState.getBlock();
						provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
					}
				}
			});
			
			return null;
		}
	}
}
