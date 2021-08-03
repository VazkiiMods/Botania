/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.corporea;

import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

/**
 * This interface is responsible for taking a location and exposing it, if possible, to the corporea network.
 * Mods with custom storage solutions can implement this and register it using the API so that corporea supports
 * their storaage.
 * Implementations are already provided in Botania for Vanilla's interfaces.
 */
public interface ICorporeaNodeDetector {
	/**
	 * @return A corporea node that this detector found at the given location, else null
	 */
	@Nullable
	ICorporeaNode getNode(Level world, ICorporeaSpark spark);
}
