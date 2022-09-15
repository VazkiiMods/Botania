/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.ManaBurst;

/**
 * Any {@link IManaReceiver} that also implements this is considered a mana collector, by
 * which nearby generating flowers will pump mana into it.<br>
 * <br>
 * <b>Implementation Instructions:</b><br>
 * - When joining the world (e.g. on first tick), call
 * {@link vazkii.botania.api.internal.ManaNetwork#fireManaNetworkEvent}
 * with this object, type {@link ManaBlockType#COLLECTOR}, and action {@link ManaNetworkAction#ADD}.
 * - When leaving the world (e.g. in setRemoved), call
 * {@link vazkii.botania.api.internal.ManaNetwork#fireManaNetworkEvent}
 * with this object, type {@link ManaBlockType#COLLECTOR}, and action {@link ManaNetworkAction#REMOVE}.
 *
 * Get the mana network using {@link BotaniaAPI#getManaNetworkInstance()}.
 */
public interface IManaCollector extends IManaReceiver {

	/**
	 * Called every tick on the client case the player is holding a Wand of the Forest.
	 */
	void onClientDisplayTick();

	/**
	 * Get the multiplier of mana to input into the block, 1.0 is the original amount of mana
	 * in the burst. 0.9, for example, is 90%, so 10% of the mana in the burst will get
	 * dissipated.
	 */
	float getManaYieldMultiplier(ManaBurst burst);

	/**
	 * Gets the maximum amount of mana this collector can have.
	 */
	int getMaxMana();

}
