/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.block.entity.BlockEntity;

public class ManaNetworkEvent extends Event {

	public final BlockEntity tile;
	public final ManaBlockType type;
	public final Action action;

	public ManaNetworkEvent(BlockEntity tile, ManaBlockType type, Action action) {
		this.tile = tile;
		this.type = type;
		this.action = action;
	}

	public static void addCollector(BlockEntity tile) {
		ManaNetworkEvent event = new ManaNetworkEvent(tile, ManaBlockType.COLLECTOR, Action.ADD);
		MinecraftForge.EVENT_BUS.post(event);
	}

	public static void removeCollector(BlockEntity tile) {
		ManaNetworkEvent event = new ManaNetworkEvent(tile, ManaBlockType.COLLECTOR, Action.REMOVE);
		MinecraftForge.EVENT_BUS.post(event);
	}

	public static void addPool(BlockEntity tile) {
		ManaNetworkEvent event = new ManaNetworkEvent(tile, ManaBlockType.POOL, Action.ADD);
		MinecraftForge.EVENT_BUS.post(event);
	}

	public static void removePool(BlockEntity tile) {
		ManaNetworkEvent event = new ManaNetworkEvent(tile, ManaBlockType.POOL, Action.REMOVE);
		MinecraftForge.EVENT_BUS.post(event);
	}

	public enum ManaBlockType {
		POOL, COLLECTOR
	}

	public enum Action {
		REMOVE, ADD
	}
}
