package ihamfp.IhTech.common;

import ihamfp.IhTech.ModIhTech;
import ihamfp.IhTech.common.packets.PacketEnergyChange;
import ihamfp.IhTech.common.packets.PacketMachineSimpleUpdate;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModIhTech.MODID);
	private static int packetId = 0;
	
	public PacketHandler() {
	}
	
	public static int nextID() {
		return packetId++;
	}
	
	public static void registerMessages() {
		
	}
	
	public static void registerClientMessages() {
		INSTANCE.registerMessage(PacketEnergyChange.Handler.class, PacketEnergyChange.class, nextID(), Side.CLIENT);
		INSTANCE.registerMessage(PacketMachineSimpleUpdate.Handler.class, PacketMachineSimpleUpdate.class, nextID(), Side.CLIENT);
	}
}
