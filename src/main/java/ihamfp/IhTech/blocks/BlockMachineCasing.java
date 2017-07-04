package ihamfp.IhTech.blocks;

import ihamfp.IhTech.ModIhTech;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMachineCasing extends Block {
	public static enum CasingType implements IStringSerializable {
		PRIMITIVE(0, "primitive", ""),
		BASIC(1, "basic", "Iron"),
		AGRICULTURE(2, "agriculture", "Plastic"),
		PRESSURIZED(3, "pressurized", "Bronze"),
		HIGHLY_PRESSURIZED(4, "highly_pressurized", "Cast iron"),
		ADVANCED(5, "advanced", "Steel"),
		REINFORCED(6, "reinforced", ""),
		LIGHT(7, "light", "Aluminium"),
		HEAVY(8, "heavy", "Tungsten"),
		TOP_TIER(9, "top_tier", "Iridium"),
		QUANTIC(10, "quantic", "Enderium"),
		BLACK(11, "black", "Black matter");
		
		private int id;
		private String name;
		private String material;
		
		// Leave material blank to not add crafting
		private CasingType(int id, String name, String material) {
			this.id = id;
			this.name = name;
			this.material = material;
		}
		
		public static CasingType fromID(int id) {
			for (CasingType c : values()) {
				if (c.id == id) return c;
			}
			return null;
		}
		
		public int getID() {
			return this.id;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getMaterial() {
			return this.material;
		}
		
		@Override
		public String toString() {
			return getName();
		}
	}
	
	public static final PropertyEnum<CasingType> TYPE = PropertyEnum.create("type", CasingType.class);
	
	public BlockMachineCasing() {
		super(Material.IRON);
		this.setRegistryName(ModIhTech.MODID, "blockMachineCasing");
		this.setDefaultState(this.getDefaultState().withProperty(this.TYPE, CasingType.PRIMITIVE));
	}
	
	public void register() {
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this) {
			@Override
			public String getUnlocalizedName(ItemStack stack) {
				return (this.getRegistryName().toString() + "." + CasingType.fromID(stack.getMetadata()) + ".name");
			}

			@Override
			public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
				for (int i=0;i<CasingType.values().length;i++) {
					subItems.add(new ItemStack(itemIn, 1, i));
				}
			}
		}.setRegistryName(this.getRegistryName()).setHasSubtypes(true));
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos, state.withProperty(this.TYPE, CasingType.fromID(stack.getMetadata())));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(this.TYPE, CasingType.fromID(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(this.TYPE).getID();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{ TYPE });
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(this.TYPE).getID();
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for (int i=0;i<CasingType.values().length;i++) {
			list.add(new ItemStack(itemIn, 1, i));
		}
	}
}
