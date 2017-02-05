package ihamfp.IhTech.fluids;

import ihamfp.IhTech.ModIhTech;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidSteam extends Fluid {

	public FluidSteam() {
		super("steam", new ResourceLocation(ModIhTech.MODID, "fluids/steam_still"), new ResourceLocation(ModIhTech.MODID, "fluids/steam_flow"));
		this.setGaseous(true);
		this.setTemperature(374);
		this.setViscosity(1);
		this.setDensity(-100);
		this.setUnlocalizedName("steam");
	}

}
