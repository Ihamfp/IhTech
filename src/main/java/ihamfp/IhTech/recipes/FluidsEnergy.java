package ihamfp.IhTech.recipes;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;

public class FluidsEnergy {	
	public static Map<Fluid, Integer> fluidEnergies = new HashMap<Fluid, Integer>();
	public static Map<Fluid, Integer> fluidPowers = new HashMap<Fluid, Integer>();
	
	/***
	 * Registers an entry in the FluidEnergy register
	 * @param fluid fluid to registers
	 * @param energy Energy Units per mB
	 * @param power base energy per tick
	 */
	public static void registerFluidEnergy(Fluid fluid, int energy, int power) {
		fluidEnergies.put(fluid, energy);
		fluidPowers.put(fluid, power);
	}
}
