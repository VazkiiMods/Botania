/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.neoforge.mana;

import net.neoforged.bus.api.Event;

import vazkii.botania.api.mana.ManaBlockType;
import vazkii.botania.api.mana.ManaCollector;
import vazkii.botania.api.mana.ManaNetworkAction;
import vazkii.botania.api.mana.ManaReceiver;

public class ManaNetworkEvent extends Event {
	private final ManaReceiver thing;
	private final ManaBlockType type;
	private final ManaNetworkAction action;

	public ManaNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action) {
		this.thing = thing;
		this.type = type;
		this.action = action;
	}

	/**
	 * @return If {@link #getType()} is {@link ManaBlockType#COLLECTOR}, a {@link ManaCollector}.
	 */
	public ManaReceiver getReceiver() {
		return thing;
	}

	public ManaBlockType getType() {
		return type;
	}

	public ManaNetworkAction getAction() {
		return action;
	}
}
