package ihamfp.IhTech.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
	// Blocks
	public static BlockEnergyStorage blockCell = new BlockEnergyStorage("blockCell", Material.ROCK, 400000).setCreativeTab(CreativeTabs.MISC);
	public static BlockSolidFuelEnergyGenerator blockGen = new BlockSolidFuelEnergyGenerator("blockGen", Material.IRON);
	public static BlockSolarPanel blockPanel = new BlockSolarPanel("blockPanel", Material.GLASS, 10000);
	
	public static void preInit() {
		blockCell.register();
		blockGen.register();
		blockPanel.register();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels() {
		blockCell.initModel();
		blockGen.initModel();
		blockPanel.initModel();
	}
}
