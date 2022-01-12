/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.Event;

public class ManaNetworkEvent extends Event {
	private final BlockEntity blockEntity;
	private final ManaBlockType type;
	private final ManaNetworkAction action;

	public ManaNetworkEvent(BlockEntity blockEntity, ManaBlockType type, ManaNetworkAction action) {
		this.blockEntity = blockEntity;
		this.type = type;
		this.action = action;
	}

	public BlockEntity getBlockEntity() {
		return blockEntity;
	}

	public ManaBlockType getType() {
		return type;
	}

	public ManaNetworkAction getAction() {
		return action;
	}
}
