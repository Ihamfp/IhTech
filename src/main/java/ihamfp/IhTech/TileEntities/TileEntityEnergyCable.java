package ihamfp.IhTech.TileEntities;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import ihamfp.IhTech.blocks.BlockEnergyCable;
import ihamfp.IhTech.interfaces.ITileEntityEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

// Anarchy cables ! No master, no leading tile entity ... Let's see if it can actually work !
// Spoiler: it does :D
public class TileEntityEnergyCable extends TileEntity implements ITileEntityEnergyStorage, ITickable {
	
	public enum EnumCableSideRenderType {
		NONE, // not connected
		CABLE, // connected to a cable
		BLOCK, // connected to an energy-compatible block
	}
	
	public int energyCapacity = 0;
	
	public EnergyStorage energyStorage; // common energy storage to all the cables in the network
	public Set<BlockPos> network; // list of connected cables
	public Set<BlockPos> nodes; // list of connected nodes
	public boolean updateFlag = false;
	public boolean nodesUpdateFlag = false;
	
	private long lastNetworkUpdate = 0;
	private AtomicLong lastNetworkEnergyUpdate;
	
	public TileEntityEnergyCable() {}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (this.energyStorage != null);
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (T)this.getEnergyStorage();
		}
		return super.getCapability(capability, facing);
	}
	
	public EnumCableSideRenderType getSideRenderType(EnumFacing face) {
		TileEntity otherTile = this.world.getTileEntity(this.getPos().offset(face));
		if (otherTile != null && otherTile instanceof TileEntityEnergyCable) {
			return EnumCableSideRenderType.CABLE;
		} else if (otherTile != null && otherTile.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())) {
			return EnumCableSideRenderType.BLOCK;
		} else {
			return EnumCableSideRenderType.NONE;
		}
	}
	
	protected void checkForCablesAround(Set<BlockPos> network, Set<BlockPos> testedNetwork) {
		for (EnumFacing side : EnumFacing.values()) {
			BlockPos nextpos = this.pos.offset(side);
			if (!this.world.isBlockLoaded(nextpos)) continue; // Do not try to go through unloaded chunks.
			if (network.contains(nextpos) || testedNetwork.contains(nextpos)) continue;
			if (this.world.getTileEntity(nextpos) instanceof TileEntityEnergyCable && ((TileEntityEnergyCable)this.world.getTileEntity(nextpos)).energyCapacity == this.energyCapacity) {
				network.add(nextpos);
			}
		}
	}
	
	protected void checkForNodesAround(Set<BlockPos> nodes) {
		if (this.nodes == null || this.network == null) return;
		for (EnumFacing side : EnumFacing.values()) {
			BlockPos nextpos = this.pos.offset(side);
			if (nodes.contains(nextpos) || this.network.contains(nextpos)) continue;
			if (this.world.getTileEntity(nextpos) == null) continue;
			if (this.world.getTileEntity(nextpos).hasCapability(CapabilityEnergy.ENERGY, side.getOpposite())) { // energy receiver found
				if (this.world.getTileEntity(nextpos) instanceof ITileEntityEnergyStorage && ((ITileEntityEnergyStorage)this.world.getTileEntity(nextpos)).getEnergySideType(side.getOpposite()) == EnumEnergySideTypes.RECEIVE) {
					nodes.add(nextpos);
				} else if (!(this.world.getTileEntity(nextpos) instanceof ITileEntityEnergyStorage)) {
					nodes.add(nextpos);
				}
			}
		}
	}
	
	public void checkForNodesAround() {
		if (this.nodes == null) return;
		checkForNodesAround(this.nodes);
	}
	
	protected void updateNetwork() {
		if (this.network != null && (this.network.size() == 0 || this.lastNetworkUpdate == this.world.getTotalWorldTime())) return; // Something is already doing this
		if (this.network != null) this.network.clear(); // clearing the network also locks it, as nothing else can operate on it while size==0
		this.lastNetworkUpdate = this.world.getTotalWorldTime();
		
		Set<BlockPos> tempNetwork = Collections.newSetFromMap(new ConcurrentHashMap<BlockPos,Boolean>());
		Set<BlockPos> testingNetwork = Collections.newSetFromMap(new ConcurrentHashMap<BlockPos,Boolean>());
		
		testingNetwork.add(this.pos);
		
		// Now we try to list *EVERY* cable on the network
		while (testingNetwork.size() > 0) { // while there are tiles to test ...
			for (BlockPos pos : testingNetwork) {
				((TileEntityEnergyCable)this.world.getTileEntity(pos)).checkForCablesAround(testingNetwork, tempNetwork);
				tempNetwork.add(pos); // this pos is tested, it doesn't have to stay in the testing set
				testingNetwork.remove(pos); // so we only test tiles' neighbors once
			}
		}
		testingNetwork.clear();
		
		if (tempNetwork.size() == 0) {
			return;
		}
		
		// Now we have a complete network. Let's set the network values for all the TE in it.
		EnergyStorage sharedES = new EnergyStorage(this.energyCapacity);
		AtomicLong sharedUpdateTime = new AtomicLong(this.lastNetworkUpdate);
		Set<BlockPos> sharedNodes = Collections.newSetFromMap(new ConcurrentHashMap<BlockPos,Boolean>());
		
		for (BlockPos pos : tempNetwork) {
			TileEntityEnergyCable teCable = ((TileEntityEnergyCable)this.world.getTileEntity(pos));
			teCable.network = tempNetwork;
			teCable.lastNetworkUpdate = this.lastNetworkUpdate;
			teCable.energyStorage = sharedES;
			teCable.lastNetworkEnergyUpdate = sharedUpdateTime;
			teCable.nodes = sharedNodes;
			teCable.nodesUpdateFlag = true;
		}
	}
	
	protected void updateNodes() {
		if (this.nodes != null && this.nodes.size() == 0) return;
		if (this.nodes != null) this.nodes.clear();
		
		for (BlockPos pos : this.network) {
			((TileEntityEnergyCable)this.world.getTileEntity(pos)).checkForNodesAround(this.nodes);
		}
	}

	@Override
	public void update() {
		if (this.world.isRemote) return;
		
		if (this.energyCapacity == 0) {
			this.energyCapacity = ((BlockEnergyCable)this.getBlockType()).energyCapacity*((int)Math.pow(2, this.getBlockMetadata()));
		}
		
		if (this.energyStorage == null || this.network == null || this.network.size() == 0) {
			this.updateFlag = true;
		}
		
		if (this.updateFlag) {
			updateNetwork();
			this.updateFlag = false;
			return;
		}
		
		if (this.nodesUpdateFlag) {
			checkForNodesAround(this.nodes);
			this.nodesUpdateFlag = false;
		}
		
		if (this.energyStorage.getEnergyStored() <= 0 || this.nodes.size() == 0) return; // No energy or no receivers ? No problem.
		if (this.lastNetworkEnergyUpdate.longValue() == this.world.getTotalWorldTime()) return; // something already updated the network.
		this.lastNetworkEnergyUpdate.set(this.world.getTotalWorldTime());
		
		// Count how many nodes can receive energy
		int receivingCount = 0;
		int maxSending = 0;
		for (BlockPos pos : nodes) {
			if (this.world.getTileEntity(pos) == null) {
				this.nodes.remove(pos); // we don't need this anymore
				continue;
			}
			if (!this.world.getTileEntity(pos).hasCapability(CapabilityEnergy.ENERGY, null)) continue;
			if (!(this.world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null) instanceof IEnergyStorage)) continue;
			IEnergyStorage nodeEnergy = (IEnergyStorage)this.world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null);
			if (nodeEnergy == null) {
				this.nodes.remove(pos);
				continue;
			}
			
			int receiving = nodeEnergy.receiveEnergy(this.energyCapacity, true);
			
			if (receiving > 0) {
				receivingCount++;
				maxSending += receiving;
			}
		}
		
		if (receivingCount == 0) return;
		
		// Distribute energy equally to the nodes
		for (BlockPos pos : nodes) {
			if (!this.world.getTileEntity(pos).hasCapability(CapabilityEnergy.ENERGY, null)) continue;
			if (!(this.world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null) instanceof IEnergyStorage)) continue;
			IEnergyStorage nodeEnergy = (IEnergyStorage)this.world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null);
			if (nodeEnergy == null) continue;
			
			int given = nodeEnergy.receiveEnergy(maxSending/receivingCount, false);
			this.energyStorage.extractEnergy(given, false);
		}
	}
	
	public void separateFromNetworks() {
		for (EnumFacing face : EnumFacing.values()) {
			TileEntity te = this.world.getTileEntity(this.pos.offset(face));
			if (te != null && te instanceof TileEntityEnergyCable) {
				((TileEntityEnergyCable)te).network = null;
			}
		}
	}

	@Override
	public IEnergyStorage getEnergyStorage() {
		return this.energyStorage;
	}

	@Override
	public void updateToClient() {} // Not needed

	@Override
	public EnumEnergySideTypes getEnergySideType(EnumFacing face) {
		return EnumEnergySideTypes.RECEIVE; // Just give me energy !
	}

	@Override
	public void updateGlobalEnergySharing() {} // Managed in update()

}
