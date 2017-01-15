package ihamfp.IhTech.blocks;

import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ihamfp.IhTech.TileEntities.TileEntityEnergyCable;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;

public class BlockEnergyCable extends BlockEnergyStorage {
	
	// TODO: finish this
	
	public BlockEnergyCable(String name, Material material, int capacity) {
		super(name, material, capacity);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TileEntityEnergyCable te = new TileEntityEnergyCable();
		return te;
	}

}
