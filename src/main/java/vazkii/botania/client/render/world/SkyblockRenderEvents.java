/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [18/12/2015, 02:19:53 (GMT)]
 */
package vazkii.botania.client.render.world;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.world.WorldTypeSkyblock;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public final class SkyblockRenderEvents {

	@SubscribeEvent
	public static void onRender(RenderWorldLastEvent event) {
		World world = Minecraft.getMinecraft().world;
		if(ConfigHandler.enableFancySkybox && world.provider.getDimension() == 0 && (ConfigHandler.enableFancySkyboxInNormalWorlds || WorldTypeSkyblock.isWorldSkyblock(Minecraft.getMinecraft().world))) {
			if(!(world.provider.getSkyRenderer() instanceof SkyblockSkyRenderer))
				world.provider.setSkyRenderer(new SkyblockSkyRenderer());
		}
	}

}
