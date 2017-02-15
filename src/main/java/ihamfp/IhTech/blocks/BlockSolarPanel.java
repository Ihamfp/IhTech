package ihamfp.IhTech.blocks;

import java.util.List;

import ihamfp.IhTech.TileEntities.TileEntityEnergyStorage;
import ihamfp.IhTech.TileEntities.TileEntitySolarEnergyGenerator;
import ihamfp.IhTech.common.Config;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.energy.EnergyStorage;

public class BlockSolarPanel extends BlockEnergyStorage {

	public BlockSolarPanel(String name, Material material) {
		super(name, material);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntitySolarEnergyGenerator te = new TileEntitySolarEnergyGenerator();
		te.energyPerTickPerLight = 10;
		return te;
	}
	
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		currenttip = super.getWailaBody(itemStack, currenttip, accessor, config);
		
		TileEntity te = accessor.getTileEntity();
		if (!(te instanceof TileEntitySolarEnergyGenerator))
			return currenttip;
		
		TileEntitySolarEnergyGenerator te2 = ((TileEntitySolarEnergyGenerator)accessor.getTileEntity());
		currenttip.add("Energy generation: " + te2.getEnergyProducedPerTick() + " " + Config.energyUnitName + "/t");
		return currenttip;
	}
	
}
