/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 14, 2014, 5:10:16 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public final class CommonTickHandler {

	@SubscribeEvent
	public void onTick(WorldTickEvent event) {
		if(event.phase == Phase.END) {
			List<Entity> entities = new ArrayList(event.world.loadedEntityList);
			for(Entity entity : entities)
				if(entity instanceof EntityItem)
					TerrasteelCraftingHandler.onEntityUpdate((EntityItem) entity);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			World world = Minecraft.getMinecraft().theWorld;
			if(world != null) {
				List<Entity> entities = new ArrayList(world.loadedEntityList);
				for(Entity entity : entities)
					if(entity instanceof EntityItem)
						TerrasteelCraftingHandler.onEntityUpdate((EntityItem) entity);
			}

		}
	}

}
