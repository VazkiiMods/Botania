/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.item;

import net.minecraft.world.IWorld;

/**
 * A Biome that implements this will not have Botania flowers generated.
 */
public interface IFlowerlessBiome {
	/**
	 * @return Should this world be allowed to generate flowers?
	 */
	public boolean canGenerateFlowers(IWorld world, int x, int z);
}
