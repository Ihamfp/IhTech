package ihamfp.IhTech.recipes;

import ihamfp.IhTech.Materials;
import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.blocks.BlockGenericResource;
import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.common.ResourceMaterial.ResourceType;
import ihamfp.IhTech.items.ItemGenericResource;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes {
	public static void preInit() {
		MinecraftForge.EVENT_BUS.register(new ModRecipes());
	}
	
	@SubscribeEvent
	public void registerMaterialRecipes(RegistryEvent.Register<IRecipe> event) {
		ResourceLocation ingot2nugget = new ResourceLocation(ModIhTech.MODID, "ingot2nugget");
		ResourceLocation nugget2ingot = new ResourceLocation(ModIhTech.MODID, "nugget2ingot");

		ResourceLocation block2ingot = new ResourceLocation(ModIhTech.MODID, "block2ingot");
		ResourceLocation ingot2block = new ResourceLocation(ModIhTech.MODID, "ingot2block");
		
		ResourceLocation gem2block = new ResourceLocation(ModIhTech.MODID, "gem2block");
		ResourceLocation block2gem = new ResourceLocation(ModIhTech.MODID, "block2gem");
		
		ResourceLocation tiny2dust = new ResourceLocation(ModIhTech.MODID, "tiny2dust");
		ResourceLocation dust2tiny = new ResourceLocation(ModIhTech.MODID, "dust2tiny");
		
		for (int i=1;i<Materials.materials.size();++i) {
			ResourceMaterial mat = Materials.materials.get(i);
			
			// 9 nuggets <=> ingot
			if (mat.has("ingot") && mat.has("nugget") && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || mat.getItemFor("nugget").getItem() instanceof ItemGenericResource)) {
				IRecipe in2nu = new ShapedOreRecipe(nugget2ingot, mat.getItemFor("ingot"), "nnn", "nnn", "nnn", 'n', "nugget"+mat.name).setRegistryName(ModIhTech.MODID, "nugget2ingot"+mat.name);
				
				ItemStack nineNuggets = mat.getItemFor("nugget").copy();
				nineNuggets.setCount(9);
				IRecipe nu2in = new ShapelessOreRecipe(ingot2nugget, nineNuggets, mat.getItemFor("ingot")).setRegistryName(ModIhTech.MODID, "ingot2nugget"+mat.name);
				
				event.getRegistry().registerAll(in2nu, nu2in);
			}
			
			// 9 ingots <=> block
			if (mat.has("ingot") && mat.has("block") && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || ((ItemBlock)(mat.getItemFor("block").getItem())).getBlock() instanceof BlockGenericResource)) {
				if (mat.getItemFor("block").isEmpty()) ModIhTech.logger.error("No block for " + mat.name);
				IRecipe bl2in = new ShapedOreRecipe(ingot2block, mat.getItemFor("block"), "iii", "iii", "iii", 'i', "ingot"+mat.name).setRegistryName(ModIhTech.MODID, "ingot2block"+mat.name);
				
				ItemStack nineIngots = mat.getItemFor("ingot").copy();
				nineIngots.setCount(9);
				IRecipe in2bl = new ShapelessOreRecipe(block2ingot, nineIngots, mat.getItemFor("block")).setRegistryName(ModIhTech.MODID, "block2ingot"+mat.name);
				
				event.getRegistry().registerAll(bl2in, in2bl);
			}
			
			// 9 gems <=> block
			if (mat.has("gem") && mat.has("block") && (mat.getItemFor("gem").getItem() instanceof ItemGenericResource || ((ItemBlock)(mat.getItemFor("block").getItem())).getBlock() instanceof BlockGenericResource)) {
				IRecipe ge2bl = new ShapedOreRecipe(gem2block, mat.getItemFor("block"), "iii", "iii", "iii", 'i', "gem"+mat.name).setRegistryName(ModIhTech.MODID, "gem2block"+mat.name);
				
				ItemStack nineIngots = mat.getItemFor("gem").copy();
				nineIngots.setCount(9);
				IRecipe bl2ge = new ShapelessOreRecipe(block2gem, nineIngots, mat.getItemFor("block")).setRegistryName(ModIhTech.MODID, "block2gem"+mat.name);
				
				event.getRegistry().registerAll(ge2bl, bl2ge);
			}
			
			// 9 tiny dusts <=> dust
			if (mat.has("dust") && (mat.getItemFor("dust").getItem() instanceof ItemGenericResource || mat.getItemFor("dustTiny").getItem() instanceof ItemGenericResource)) {
				IRecipe td2du = new ShapedOreRecipe(tiny2dust, mat.getItemFor("dust"), "ttt", "ttt", "ttt", 't', "dustTiny"+mat.name).setRegistryName(ModIhTech.MODID, "tiny2dust"+mat.name);
				
				ItemStack nineTinyDusts = mat.getItemFor("dustTiny").copy();
				nineTinyDusts.setCount(9);
				IRecipe du2td = new ShapelessOreRecipe(dust2tiny, nineTinyDusts, mat.getItemFor("dust")).setRegistryName(ModIhTech.MODID, "dust2tiny"+mat.name);
				
				event.getRegistry().registerAll(td2du, du2td);
			}
			
			// dust => ingot in furnace
			if (mat.has("dust") && mat.has("ingot") && mat.resourceType == ResourceType.METAL && mat.meltingPoint <= 2000 && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || mat.getItemFor("dust").getItem() instanceof ItemGenericResource)) {
				GameRegistry.addSmelting(mat.getItemFor("dust"), mat.getItemFor("ingot"), 1);
			}
			
			// ore => ingot in furnace
			if (mat.has("ore") && mat.has("ingot") && mat.resourceType == ResourceType.METAL && mat.meltingPoint <= 1600 && (mat.getItemFor("ingot").getItem() instanceof ItemGenericResource || ((ItemBlock)(mat.getItemFor("ore").getItem())).getBlock() instanceof BlockGenericResource)) {
				GameRegistry.addSmelting(mat.getItemFor("ore"), mat.getItemFor("ingot"), 1);
			}
			
			// ingot => dust grinding
			if (mat.has("dust") && mat.has("ingot") && mat.resourceType == ResourceType.METAL) {
				RecipesGrinding.registerGrinding(mat.getItemFor("ingot"), new ItemStack[] {mat.getItemFor("dust")}, 185, true);
			}
			
			// gem => dust grinding
			if (mat.has("dust") && mat.has("gem")) {
				switch (mat.resourceType) {
				case COAL:
					RecipesGrinding.registerGrinding(mat.getItemFor("gem"), new ItemStack[] {mat.getItemFor("dust")}, 185, true);
					break;
				
				case CRYSTAL:
					RecipesGrinding.registerGrinding(mat.getItemFor("gem"), new ItemStack[] {mat.getItemFor("dust")}, 185, true).requiresDiamond();
					break;
				
				default:
					break;
				}
			}
			
			// block => dust grinding
			if (mat.has("dust") && mat.has("block")) {
				ItemStack dusts = mat.getItemFor("dust").copy();
				switch (mat.resourceType) {
				case METAL:
				case COAL:
					dusts.setCount(9);
					RecipesGrinding.registerGrinding(mat.getItemFor("block"), new ItemStack[] {dusts}, 185*8, true);
					break;
				
				case CRYSTAL:
					dusts.setCount(9);
					RecipesGrinding.registerGrinding(mat.getItemFor("block"), new ItemStack[] {dusts}, 185*8, true).requiresDiamond();
					break;
				
				default:
					if (mat.name == "Wood") {
						dusts.setCount(4);
						RecipesGrinding.registerGrinding(mat.getItemFor("block"), new ItemStack[] {dusts}, 185*2);
					}
					break;
				}
			}
		}
	}
}
