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
import net.minecraft.client.player.LocalPlayer;

import vazkii.botania.client.gui.ItemsRemainingRenderHandler;
import vazkii.botania.client.render.block_entity.RedStringBlockEntityRenderer;
import vazkii.botania.common.block.block_entity.flower.functional.VinculotusBlockEntity;
import vazkii.botania.common.handler.ManaNetworkHandler;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.item.LexicaBotaniaItem;
import vazkii.botania.common.item.WandOfTheForestItem;

public final class ClientTickHandler {

	public static final int TICKS_TO_OPEN = 10;

	private ClientTickHandler() {}

	private static int ticksWithLexicaOpen = 0;
	private static int pageFlipTicks = 0;

	private static int playerTicksInGame = 0;
	private static int unfrozenTicksInGame = 0;
	private static int uiAnimationTicks = 0;

	/**
	 * Returns a number of game ticks that keeps incrementing while the game not paused, even when frozen.
	 * (Use only for things that should keep animating under {@code /tick freeze} conditions.)
	 */
	public static int getPlayerTicksInGame() {
		return playerTicksInGame;
	}

	/**
	 * Returns the partial tick value, treating the game as unfrozen at all times.
	 * (Use only if no unfrozen partial tick is available already.)
	 */
	public static float getPartialPlayerTick() {
		return Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
	}

	/**
	 * Returns a number of game ticks that keeps increasing while the game is not frozen or paused.
	 * (Use for things that stop during {@code /tick freeze}.)
	 */
	public static int getEntityTicksInGame() {
		return unfrozenTicksInGame;
	}

	/**
	 * Continuously updating, non-negative value that wraps around after a bit over 69 minutes (nice) to stay within a
	 * range that still lets {@link net.minecraft.util.Mth#sin(float)} and {@link net.minecraft.util.Mth#cos(float)}
	 * return reasonably precise values.
	 */
	public static float getUiAnimationTicks() {
		return (uiAnimationTicks & 0x3FFFFF) + Minecraft.getInstance().getTimer().getRealtimeDeltaTicks();
	}

	/**
	 * Returns the partial tick value, respecting the game's frozen state.
	 * (Use only if no partial tick is available that respects the frozen state.)
	 */
	public static float getEntityPartialTick() {
		Minecraft minecraft = Minecraft.getInstance();
		return minecraft.getTimer().getGameTimeDeltaPartialTick(false);
	}

	public static float getTicksWithLexicaOpen() {
		boolean isOpen = LexicaBotaniaItem.isOpen();
		if (isOpen) {
			return ticksWithLexicaOpen == TICKS_TO_OPEN
					? ticksWithLexicaOpen
					: Math.min(ticksWithLexicaOpen + getPartialPlayerTick(), TICKS_TO_OPEN);
		} else {
			return ticksWithLexicaOpen == 0
					? ticksWithLexicaOpen
					: Math.max(ticksWithLexicaOpen - getPartialPlayerTick(), 0);
		}
	}

	public static float getPageFlipTicks() {
		return pageFlipTicks > 0 ? Math.max(pageFlipTicks - getPartialPlayerTick(), 0) : 0;
	}

	public static void clientTickEnd(Minecraft mc) {
		RedStringBlockEntityRenderer.tick();
		ItemsRemainingRenderHandler.tick();

		if (mc.level == null) {
			ManaNetworkHandler.instance.clear();
			VinculotusBlockEntity.existingFlowers.clear();
		}

		uiAnimationTicks++;
		if (!mc.isPaused()) {
			playerTicksInGame++;
			if (mc.level != null && !mc.level.tickRateManager().isFrozen()) {
				unfrozenTicksInGame++;
			}

			LocalPlayer player = mc.player;
			if (player != null && mc.level != null) {
				if (PlayerHelper.hasHeldItemClass(player, WandOfTheForestItem.class)) {
					for (var collector : ImmutableList.copyOf(ManaNetworkHandler.instance.getAllCollectorsInWorld(mc.level))) {
						collector.onClientDisplayTick();
					}
				}
			}
		}

		if (LexicaBotaniaItem.isOpen()) {
			if (ticksWithLexicaOpen < 0) {
				ticksWithLexicaOpen = 0;
			}
			if (ticksWithLexicaOpen < TICKS_TO_OPEN) {
				ticksWithLexicaOpen++;
			}
			if (pageFlipTicks > 0) {
				pageFlipTicks--;
			}
		} else {
			pageFlipTicks = 0;
			if (ticksWithLexicaOpen > 0) {
				if (ticksWithLexicaOpen > TICKS_TO_OPEN) {
					ticksWithLexicaOpen = TICKS_TO_OPEN;
				}
				ticksWithLexicaOpen--;
			}
		}
	}
}
