package ihamfp.IhTech.compatibility;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.fluid.FluidMolten;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerMaterials;
import ihamfp.IhTech.Materials;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.common.Config;
import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.common.ResourceMaterial.ResourceType;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TConstructIntegration {
	
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
				FluidMolten fluid = new FluidMolten(mat.name.toLowerCase(), mat.color);
				fluid.setTemperature(mat.meltingPoint);
				FluidRegistry.registerFluid(fluid);
				FluidRegistry.addBucketForFluid(fluid);
				
				BlockFluidClassic fluidBlock = new BlockFluidClassic(fluid, MaterialLiquid.LAVA);//new MaterialLiquid(MapColor.ADOBE));
				fluidBlock.setRegistryName(mat.name.toLowerCase());
				fluidBlock.setUnlocalizedName("fluid.molten" + mat.name);
				GameRegistry.register(fluidBlock);
				mat.setItemFor("molten", new ItemStack(fluidBlock));
				
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("fluid", fluid.getName());
				tag.setString("ore", mat.name);
				tag.setBoolean("toolforge", mat.has("block"));
				
				FMLInterModComms.sendMessage("tconstruct", "integrateSmeltery", tag);
			}
		}
	}
	
	@Optional.Method(modid = "tconstruct")
	public static void materialsIntegration() {
		ModIhTech.logger.info("Loading TConstruct integration");
		for (ResourceMaterial mat : Materials.materials) {
			if (mat.name.equals("Undefined")) continue;
			if (mat.toolDurability == 0) continue;
			String strippedName = mat.name.replaceAll("\\s+", "").toLowerCase();
			if (isMaterialAlreadyRegistered(strippedName)) continue;
			
			Material matTC = new Material(strippedName, mat.color & 0xffffff);
			
			if (mat.has("ingot")) {
				matTC.addItemIngot("ingot" + mat.name);
				matTC.setRepresentativeItem(mat.getItemFor("ingot"));
			}
			if (mat.has("molten")) {
				Fluid molten = FluidRegistry.getFluid(mat.name.toLowerCase());
				if (molten == null) {
					ModIhTech.logger.error("Molten fluid for " + mat.name + " is null !");
				} else {
					matTC.setFluid(molten);
					matTC.setCastable(true);
					TinkerSmeltery.registerOredictMeltingCasting(molten, mat.name);
					TinkerSmeltery.registerToolpartMeltingCasting(matTC);
				}
			} else {
				matTC.setCraftable(true);
			}
			
			TinkerRegistry.addMaterialStats(matTC,
					new HeadMaterialStats(mat.toolDurability, mat.miningSpeed, mat.attackDamages, mat.miningLevel),
					new HandleMaterialStats(mat.handleModifier, mat.toolDurability/10),
					new ExtraMaterialStats(mat.toolDurability/15),
					new BowMaterialStats(0.0f, 0.0f, 0.0f));
			
			TinkerRegistry.addMaterial(matTC);
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
}
