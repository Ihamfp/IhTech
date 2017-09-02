package ihamfp.IhTech.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModFluids {
	// Fluids
	public static FluidSteam steam = new FluidSteam();
	
	// Fluids blocks
	public static BlockFluidClassic steamBlock;
	
	public static void preInit() {
		FluidRegistry.registerFluid(steam);
		steamBlock = new BlockFluidClassic(steam, Material.LAVA);
		steamBlock.setRegistryName("fluidSteam");
		steamBlock.setUnlocalizedName("fluid.steam");
		FluidRegistry.addBucketForFluid(steam);
		MinecraftForge.EVENT_BUS.register(new ModFluids());
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(steamBlock);
	}
}
