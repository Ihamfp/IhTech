package ihamfp.IhTech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ihamfp.IhTech.interfaces.IProxy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModIhTech.MODID, version = "@VERSION@",
	dependencies = "required-after:forge@[14.22.0.2463,);"
	+ "after:opencomputers@[1.7.0.4,);"
//	+ "after:tconstruct@[2.7.2,);"
)
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
		ModIhTech.logger.info("Finished loading everything. Materials: " + Materials.materials.size());
	}
	
}
