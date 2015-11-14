/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 14, 2014, 5:10:16 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import vazkii.botania.api.corporea.CorporeaHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class CommonTickHandler {

	@SubscribeEvent
	public void onTick(WorldTickEvent event) {
		if(event.phase == Phase.END) {
			/*List<Entity> entities = new ArrayList(event.world.loadedEntityList);
			for(Entity entity : entities)
				if(entity instanceof EntityItem)
					TerrasteelCraftingHandler.onEntityUpdate((EntityItem) entity);*/

			CorporeaHelper.clearCache();
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			/*World world = Minecraft.getMinecraft().theWorld;
			if(world != null) {
				List<Entity> entities = new ArrayList(world.loadedEntityList);
				for(Entity entity : entities)
					if(entity instanceof EntityItem)
						TerrasteelCraftingHandler.onEntityUpdate((EntityItem) entity);
			}*/

			CorporeaHelper.clearCache();
		}
	}

}
