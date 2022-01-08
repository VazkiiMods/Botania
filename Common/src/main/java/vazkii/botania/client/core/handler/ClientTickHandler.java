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

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.client.render.tile.RenderTileRedString;
import vazkii.botania.common.block.subtile.functional.SubTileVinculotus;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;

public final class ClientTickHandler {

	private ClientTickHandler() {}

	public static int ticksWithLexicaOpen = 0;
	public static int pageFlipTicks = 0;
	public static int ticksInGame = 0;
	public static float partialTicks = 0;
	public static float delta = 0;
	public static float total = 0;

	public static void calcDelta() {
		float oldTotal = total;
		total = ticksInGame + partialTicks;
		delta = total - oldTotal;
	}

	public static void renderTick(float renderTickTime) {
		partialTicks = renderTickTime;
	}

	public static void clientTickEnd(Minecraft mc) {
		RenderTileRedString.tick();
		ItemsRemainingRenderHandler.tick();

		if (Minecraft.getInstance().level == null) {
			ManaNetworkHandler.instance.clear();
			SubTileVinculotus.existingFlowers.clear();
		}

		if (!mc.isPaused()) {
			ticksInGame++;
			partialTicks = 0;

			Player player = mc.player;
			if (player != null) {
				if (PlayerHelper.hasHeldItemClass(player, ModItems.twigWand)) {
					for (BlockEntity tile : ImmutableList.copyOf(ManaNetworkHandler.instance.getAllCollectorsInWorld(Minecraft.getInstance().level))) {
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
