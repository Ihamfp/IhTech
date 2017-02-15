package ihamfp.IhTech.TileEntities.machines;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ihamfp.IhTech.TileEntities.TileEntityEnergyStorage;
import ihamfp.IhTech.blocks.machines.BlockMachineElectricBase;

public abstract class TileEntityElectricMachine extends TileEntityEnergyStorage {
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return (oldState.getBlock() != newState.getBlock());
	}
	
	protected void setBlockStateActive(boolean active) {
		IBlockState blockState = this.worldObj.getBlockState(this.pos);
		this.worldObj.setBlockState(this.pos, blockState.withProperty(BlockMachineElectricBase.active, active));
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void simpleUpdate(int value);
	
	@SideOnly(Side.SERVER)
	public abstract void sendSimpleUpdate(int value);
}
