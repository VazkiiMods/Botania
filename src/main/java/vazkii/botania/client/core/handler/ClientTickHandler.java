/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.client.render.tile.RenderTileRedString;
import vazkii.botania.common.block.subtile.functional.SubTileVinculotus;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.mixin.AccessorMinecraft;

public final class ClientTickHandler {

	private ClientTickHandler() {}

	public static int ticksWithLexicaOpen = 0;
	public static int pageFlipTicks = 0;
	public static int ticksInGame = 0;
	public static float partialTicks = 0;
	public static float delta = 0;
	public static float total = 0;

	private static void calcDelta() {
		float oldTotal = total;
		total = ticksInGame + partialTicks;
		delta = total - oldTotal;
	}

	public static void renderTick(TickEvent.RenderTickEvent event) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (event.phase == TickEvent.Phase.START) {
			partialTicks = event.renderTickTime;

			if (mc.isPaused()) {
				// If game is paused, need to use the saved value. The event is always fired with the "true" value which
				// keeps updating when paused. See RenderTickEvent fire site for details, remove when MinecraftForge#6991 is resolved
				partialTicks = ((AccessorMinecraft) mc).getPausedTickDelta();
			}
		} else {
			calcDelta();
		}
	}

	public static void clientTickEnd(MinecraftClient mc) {
		RenderTileRedString.tick();
		ItemsRemainingRenderHandler.tick();

		if (MinecraftClient.getInstance().world == null) {
			ManaNetworkHandler.instance.clear();
			SubTileVinculotus.existingFlowers.clear();
		}

		if (!mc.isPaused()) {
			ticksInGame++;
			partialTicks = 0;

			PlayerEntity player = mc.player;
			if (player != null) {
				if (PlayerHelper.hasHeldItemClass(player, ModItems.twigWand)) {
					for (BlockEntity tile : ImmutableList.copyOf(ManaNetworkHandler.instance.getAllCollectorsInWorld(MinecraftClient.getInstance().world))) {
						if (tile instanceof IManaCollector) {
							((IManaCollector) tile).onClientDisplayTick();
						}
					}
				}
			}
		}

		int ticksToOpen = 10;
		if (ItemLexicon.isOpen()) {
			if (ticksWithLexicaOpen < 0) {
				ticksWithLexicaOpen = 0;
			}
			if (ticksWithLexicaOpen < ticksToOpen) {
				ticksWithLexicaOpen++;
			}
			if (pageFlipTicks > 0) {
				pageFlipTicks--;
			}
		} else {
			pageFlipTicks = 0;
			if (ticksWithLexicaOpen > 0) {
				if (ticksWithLexicaOpen > ticksToOpen) {
					ticksWithLexicaOpen = ticksToOpen;
				}
				ticksWithLexicaOpen--;
			}
		}

		calcDelta();
	}

	public static void notifyPageChange() {
		if (pageFlipTicks == 0) {
			pageFlipTicks = 5;
		}
	}

}
