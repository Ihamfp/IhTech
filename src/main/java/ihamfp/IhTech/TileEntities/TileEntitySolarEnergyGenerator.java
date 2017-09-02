package ihamfp.IhTech.TileEntities;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.EnumSkyBlock;

public class TileEntitySolarEnergyGenerator extends TileEntityEnergyStorage implements ITickable {
	public TileEntitySolarEnergyGenerator() {}
	
	public int energyPerTickPerLight = 10;
	
	@Override
	public void update() {
		super.update();
		int energyGenerated = this.getEnergyProducedPerTick();
		this.energyStorage.receiveEnergy(energyGenerated, false);
		this.markDirty();
	}
	
	public int getEnergyProducedPerTick() {
		int lightLevel = this.world.getLightFor(EnumSkyBlock.SKY, this.getPos().up()) - this.world.calculateSkylightSubtracted(1.0f);
		int gen = this.energyPerTickPerLight * lightLevel;
		if (gen < 0) return 0;
		return gen;
	}
	
	@Override
	public EnumEnergySideTypes getEnergySideType(EnumFacing face) {
		if (face == EnumFacing.UP)
			return EnumEnergySideTypes.BLOCKED;
		return EnumEnergySideTypes.SEND;
	}
}
