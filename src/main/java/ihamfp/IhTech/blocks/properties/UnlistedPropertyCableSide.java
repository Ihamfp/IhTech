package ihamfp.IhTech.blocks.properties;

import ihamfp.IhTech.TileEntities.TileEntityEnergyCable;
import ihamfp.IhTech.TileEntities.TileEntityEnergyCable.EnumCableSideRenderType;
import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyCableSide implements IUnlistedProperty<TileEntityEnergyCable.EnumCableSideRenderType> {
	
	private final String name;
	
	public UnlistedPropertyCableSide(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(EnumCableSideRenderType value) {
		return true;
	}

	@Override
	public Class<EnumCableSideRenderType> getType() {
		return EnumCableSideRenderType.class;
	}

	@Override
	public String valueToString(EnumCableSideRenderType value) {
		switch (value) {
		case NONE:
			return "None";
		
		case CABLE:
			return "Cable";
		
		case BLOCK:
			return "Block";
		}
		
		return "wut";
	}
	
}
