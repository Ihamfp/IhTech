package ihamfp.IhTech;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TweakPolarBear {
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		
		if (entity instanceof EntityPolarBear) {
			event.setCanceled(true);
		}
	}
}
