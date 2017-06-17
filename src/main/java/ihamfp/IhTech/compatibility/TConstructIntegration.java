package ihamfp.IhTech.compatibility;

import java.util.ArrayList;

import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.fluid.FluidMolten;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.shared.FluidsClientProxy;
import slimeknights.tconstruct.shared.FluidsClientProxy.FluidStateMapper;
import slimeknights.tconstruct.shared.TinkerFluids;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.BlockMolten;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerTools;
import ihamfp.IhTech.Materials;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.common.Config;
import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.common.ResourceMaterial.ResourceType;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TConstructIntegration {
	
	private static ArrayList<FluidMolten> addedMoltenFluids = new ArrayList<FluidMolten>();
	
	private static class CustomFluidStateMapper extends StateMapperBase implements ItemMeshDefinition {
		private final ModelResourceLocation location;
		
		public CustomFluidStateMapper(Fluid fluid) {
			this.location = new ModelResourceLocation(ModIhTech.MODID + ":blockMoltenFluids", fluid.getName());
		}

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			return this.location;
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			return this.location;
		}
	}
	
	@Optional.Method(modid = "tconstruct")
	public static void moltenIntegration() {
		ModIhTech.logger.info("Loading TConstruct pre-integration");
		for (ResourceMaterial mat : Materials.materials) {
			if (mat.resourceType == ResourceType.METAL && !FluidRegistry.isFluidRegistered(mat.name.toLowerCase())) { // add melted metals
				if (Materials.oreDictAliasses.containsKey(mat.name)) {
					boolean willRegister = true;
					for (String alt : Materials.oreDictAliasses.get(mat.name)) {
						if (FluidRegistry.isFluidRegistered(alt.toLowerCase())) willRegister = false;
					}
					if (!willRegister) continue;
				}
				ModIhTech.logger.info("Integrated fluid for " + mat.name);
				FluidMolten fluid = new FluidMolten(stripName(mat.name), mat.color);
			    fluid.setUnlocalizedName(Util.prefix(fluid.getName()));
				FluidRegistry.registerFluid(fluid);
				fluid.setTemperature(mat.meltingPoint);
				
				TinkerFluids.registerMoltenBlock(fluid);
				
				addedMoltenFluids.add(fluid);
				
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("fluid", fluid.getName());
				tag.setString("ore", mat.name);
				
				FMLInterModComms.sendMessage("tconstruct", "integrateSmeltery", tag);
			}
		}
	}
	
	@Optional.Method(modid = "tconstruct")
	@SideOnly(Side.CLIENT)
	public static void moltenModels() {
		for (Fluid fluid : addedMoltenFluids) {
			Block moltenBlock = fluid.getBlock();
			Item item = Item.getItemFromBlock(moltenBlock);
			CustomFluidStateMapper mapper = new CustomFluidStateMapper(fluid);
			ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, mapper);
			ModelLoader.setCustomStateMapper(moltenBlock, mapper);
		}
	}
	
	@Optional.Method(modid = "tconstruct")
	public static void materialsIntegration() {
		ModIhTech.logger.info("Loading TConstruct integration");
		for (ResourceMaterial mat : Materials.materials) {
			if (mat.name.equals("Undefined")) continue;
			//if (mat.toolDurability == 0) continue;
			String strippedName = stripName(mat.name);
			if (isMaterialAlreadyRegistered(strippedName)) {
				ModIhTech.logger.info(mat.name + " is already registered in TCon.");
				continue;
			}
			
			Material matTC = new Material(strippedName, mat.color & 0xffffff);
			
			if (mat.toolDurability > 0) TinkerRegistry.addMaterialStats(matTC,
					new HeadMaterialStats(mat.toolDurability, mat.miningSpeed, mat.attackDamages, mat.miningLevel),
					new HandleMaterialStats(mat.handleModifier, mat.toolDurability/10),
					new ExtraMaterialStats(mat.toolDurability/15),
					new BowMaterialStats(0.0f, 0.0f, 0.0f));
			
			Fluid moltenFluid = FluidRegistry.getFluid(stripName(mat.name));
			matTC.setFluid(moltenFluid);
			
			MaterialIntegration matInt = new MaterialIntegration(matTC, moltenFluid, mat.name);
			matInt.toolforge();
			matInt.integrate();
			//matInt.integrateRecipes();
		}
	}
	
	@Optional.Method(modid = "tconstruct")
	private static boolean isMaterialAlreadyRegistered(String name) {
		for (Material matTC : TinkerRegistry.getAllMaterials()) {
			if (matTC.getIdentifier().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	private static String stripName(String input) {
		return input.replaceAll("\\s+", "").toLowerCase();
	}
}
