package ihamfp.IhTech.fluids;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModFluids {
	// Fluids
	public static FluidSteam steam = new FluidSteam();
	
	// Fluids blocks
	
	public static void preInit() {
		FluidRegistry.registerFluid(steam);
		BlockFluidClassic steamBlock = new BlockFluidClassic(steam, Material.LAVA);
		steamBlock.setRegistryName("fluidSteam");
		steamBlock.setUnlocalizedName("fluid.steam");
		//GameRegistry.register(steamBlock); // TODO register fluid block
		FluidRegistry.addBucketForFluid(steam);
	}
}
