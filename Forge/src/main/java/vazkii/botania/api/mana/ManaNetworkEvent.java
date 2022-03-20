/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

public class ManaNetworkEvent extends Event {
	private final Level level;
	private final IManaReceiver thing;
	private final ManaBlockType type;
	private final ManaNetworkAction action;

	public ManaNetworkEvent(Level level, IManaReceiver thing, ManaBlockType type, ManaNetworkAction action) {
		this.level = level;
		this.thing = thing;
		this.type = type;
		this.action = action;
	}

	public Level getLevel() {
		return level;
	}

	/**
	 * @return If {@link #getType()} is {@link ManaBlockType#COLLECTOR}, an {@link IManaCollector},
	 *         otherwise if {@link #getType()} is {@link ManaBlockType#POOL}, an {@link IManaPool}
	 */
	public IManaReceiver getReceiver() {
		return thing;
	}

	public ManaBlockType getType() {
		return type;
	}

	public ManaNetworkAction getAction() {
		return action;
	}
}
