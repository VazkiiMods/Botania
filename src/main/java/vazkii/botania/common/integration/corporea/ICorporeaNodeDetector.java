/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.corporea;

import net.minecraft.world.World;

import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaSpark;

import javax.annotation.Nullable;

public interface ICorporeaNodeDetector {
	/**
	 * @return A corporea node that this detector found at the given location, else null
	 */
	@Nullable
	ICorporeaNode getNode(World world, ICorporeaSpark spark);
}
