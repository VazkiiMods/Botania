/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.world;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.world.SkyblockChunkGenerator;

public final class SkyblockRenderEvents {

	public static void onRender(RenderWorldLastEvent event) {
		World world = Minecraft.getInstance().world;
		if (ConfigHandler.CLIENT.enableFancySkybox.get()
				&& world.func_234923_W_() == World.field_234918_g_
				&& (ConfigHandler.CLIENT.enableFancySkyboxInNormalWorlds.get()
						|| SkyblockChunkGenerator.isWorldSkyblock(world))) {
			/* todo 1.16
			if (!(world.getDimension().getSkyRenderer() instanceof SkyblockSkyRenderer)) {
				world.getDimension().setSkyRenderer(new SkyblockSkyRenderer());
			}
			*/
		}
	}

}
