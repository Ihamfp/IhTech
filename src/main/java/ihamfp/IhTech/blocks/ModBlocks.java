package ihamfp.IhTech.blocks;

import ihamfp.IhTech.blocks.machines.BlockMachineElectricFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
	// Blocks
	public static BlockSolidFuelEnergyGenerator blockGen = new BlockSolidFuelEnergyGenerator("blockGen", Material.IRON);
	public static BlockSolarPanel blockPanel = new BlockSolarPanel("blockPanel", Material.GLASS);
	public static BlockBatteryRack blockBattRack = new BlockBatteryRack();
	
	// machines
	public static BlockMachineElectricFurnace blockElectricFurnace = new BlockMachineElectricFurnace("electricFurnace");
	
	public static void preInit() {		
		blockGen.register();
		blockPanel.register();
		blockBattRack.register();
		
		blockElectricFurnace.register();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		blockGen.initModel();
		blockPanel.initModel();
		blockBattRack.initModel();
	}
}
