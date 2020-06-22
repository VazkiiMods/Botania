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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import vazkii.botania.api.mana.IManaCollector;
import vazkii.botania.client.render.tile.RenderTileRedString;
import vazkii.botania.common.block.subtile.functional.SubTileVinculotus;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;

import java.lang.reflect.Field;

public final class ClientTickHandler {

	private ClientTickHandler() {}

	public static int ticksWithLexicaOpen = 0;
	public static int pageFlipTicks = 0;
	public static int ticksInGame = 0;
	public static float partialTicks = 0;
	public static float delta = 0;
	public static float total = 0;
	private static final Field RENDER_PARTIAL_TICKS_PAUSED = ObfuscationReflectionHelper.findField(Minecraft.class, "field_193996_ah");

	private static void calcDelta() {
		float oldTotal = total;
		total = ticksInGame + partialTicks;
		delta = total - oldTotal;
	}

	public static void renderTick(TickEvent.RenderTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (event.phase == TickEvent.Phase.START) {
			partialTicks = event.renderTickTime;

			if (mc.isGamePaused()) {
				// If game is paused, need to use the saved value. The event is always fired with the "true" value which
				// keeps updating when paused. See RenderTickEvent fire site for details
				try {
					partialTicks = (float) RENDER_PARTIAL_TICKS_PAUSED.get(mc);
				} catch (IllegalAccessException ignored) {
					// fall back to jittery one
				}
			}
		} else {
			calcDelta();
		}
	}

	public static void clientTickEnd(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			RenderTileRedString.tick();
			ItemsRemainingRenderHandler.tick();

			if (Minecraft.getInstance().world == null) {
				ManaNetworkHandler.instance.clear();
				SubTileVinculotus.existingFlowers.clear();
			}

			if (!Minecraft.getInstance().isGamePaused()) {
				ticksInGame++;
				partialTicks = 0;

				PlayerEntity player = Minecraft.getInstance().player;
				if (player != null) {
					if (PlayerHelper.hasHeldItemClass(player, ModItems.twigWand)) {
						for (TileEntity tile : ImmutableList.copyOf(ManaNetworkHandler.instance.getAllCollectorsInWorld(Minecraft.getInstance().world))) {
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
	}

	public static void notifyPageChange() {
		if (pageFlipTicks == 0) {
			pageFlipTicks = 5;
		}
	}

}
