package ihamfp.IhTech;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import akka.io.Tcp.Register;
import ihamfp.IhTech.TileEntities.TileEntityBatteryRack;
import ihamfp.IhTech.TileEntities.TileEntityEnergyStorage;
import ihamfp.IhTech.TileEntities.TileEntityItemEnergyGenerator;
import ihamfp.IhTech.TileEntities.TileEntitySolarEnergyGenerator;
import ihamfp.IhTech.blocks.BlockBase;
import ihamfp.IhTech.blocks.BlockEnergyStorage;
import ihamfp.IhTech.blocks.BlockSolidFuelEnergyGenerator;
import ihamfp.IhTech.blocks.BlockSolarPanel;
import ihamfp.IhTech.common.CommonProxy;
import ihamfp.IhTech.common.ResourceMaterial;
import ihamfp.IhTech.interfaces.IProxy;
import ihamfp.IhTech.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = ModIhTech.MODID, version = "@VERSION@",
	dependencies = "required-after:Forge@[12.18.3.2185,);"
	+ "after:OpenComputers@[1.6.1.6,);"
	+ "after:TConstruct@[2.6.2,);"
	+ "after:Waila@[1.7.0,);after:theoneprobe@[1.3.3,);")
public class ModIhTech {
	static {
		FluidRegistry.enableUniversalBucket();
	}
	
	public static final String MODID = "ihtech";
	
	@Mod.Instance(MODID)
	public static ModIhTech instance;
	
	@SidedProxy(serverSide = "ihamfp.IhTech.common.CommonProxy", clientSide = "ihamfp.IhTech.client.ClientProxy")
	public static IProxy proxy;
	
	public static final Logger logger = LogManager.getLogger(MODID);
	
	///////////////////////////////////////////////////////////////////////////
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Test
		Achievement ach = new Achievement("achievement.wow", "wow", 0, 0, ModItems.ingot, null).setSpecial();
		ach.registerStat();
		AchievementPage.registerAchievementPage(new AchievementPage("IhTech achievements", new Achievement[] {
				ach
		}));
		// Proxy
		proxy.preInit(event);
	}
		
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Proxy
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Proxy
		proxy.postInit(event);
	}
	
}
