package ihamfp.IhTech;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;

@SideOnly(Side.CLIENT)
public class TweakCape {
	private static Minecraft mc = FMLClientHandler.instance().getClient();
	
	private static HashMap<String, Boolean> addedList = new HashMap<String, Boolean>();
	private static HashMap<String, ResourceLocation> capesList = new HashMap<String, ResourceLocation>() {{
		put("_Firew0lf", new ResourceLocation("ihtech:textures/capes/ihamfp.png"));
		put("Nolifertu", new ResourceLocation("ihtech:textures/capes/noli.png"));
	}};
	
	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if (event.phase == Phase.START && mc.world != null) {
			for (EntityPlayer player : mc.world.playerEntities) {
				if (addedList.containsKey(player.getName())) continue;
				if (capesList.containsKey(StringUtils.stripControlCodes(player.getName()))) {
					if (addCape(player, capesList.get(StringUtils.stripControlCodes(player.getName())))) addedList.put(player.getName(), true);
				} else {
					addedList.put(player.getName(), false);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void worldUnloaded(WorldEvent.Unload event) {
		if (addedList.containsValue(true)) {
			ModIhTech.logger.info("Cleared capes list");
			addedList.clear();
		}
	}
	
	/*private static boolean checkForCape(EntityPlayer player) {
		if (player == null) return false;
		if (StringUtils.stripControlCodes(player.getName()).equals("_Firew0lf")) {
			return addCape(player, ihamfp);
		} else if (StringUtils.stripControlCodes(player.getName()).equals("Nolifertu")) {
			return addCape(player, noli);
		}
		return false;
	}*/
	
	private static boolean addCape(EntityPlayer player, ResourceLocation cape) {
		if (!(player instanceof AbstractClientPlayer)) {
			ModIhTech.logger.error("Player was not abstract enough.");
			return false;
		}
		
		AbstractClientPlayer aplayer = (AbstractClientPlayer)player;
		
		try {
			Method met;
			try {
				met = AbstractClientPlayer.class.getDeclaredMethod("func_175155_b");
			} catch (Exception ignore) {
				ModIhTech.logger.warn("Could not get normal meth");
			} finally {
				met = AbstractClientPlayer.class.getDeclaredMethod("getPlayerInfo");
			}
			if (met == null) {
				ModIhTech.logger.error("Could not get any meth :(");
				return false;
			}
			met.setAccessible(true);
			
			Object obj = met.invoke(aplayer);
			if (!(obj instanceof NetworkPlayerInfo)) {
				if (obj == null) {
					ModIhTech.logger.error("Not Pointing Exception");
				} else {
					ModIhTech.logger.error("Not Pointed Exception");
				}
				return false;
			}
		
			NetworkPlayerInfo npi = (NetworkPlayerInfo)obj;
			
			Field playerTextures;
			try {
				playerTextures = NetworkPlayerInfo.class.getDeclaredField("field_187107_a");
			} catch (Exception ignore) {
				
			} finally {
				playerTextures = NetworkPlayerInfo.class.getDeclaredField("playerTextures");
			}
			playerTextures.setAccessible(true);
			Map<MinecraftProfileTexture.Type, ResourceLocation> map = (Map<MinecraftProfileTexture.Type, ResourceLocation>)playerTextures.get(npi);
			
			map.put(MinecraftProfileTexture.Type.CAPE, cape);
			ModIhTech.logger.info("My cape goes here.");
			
		} catch (Exception e) {
			ModIhTech.logger.error("I failed. " + e.getMessage());
			return false;
		}
		
		return true;
	}
}
