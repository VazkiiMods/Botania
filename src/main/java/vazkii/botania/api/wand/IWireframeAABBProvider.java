/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.wand;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

/**
 * A block that implements this can provide a custom AABB
 * for rendering the wireframe with ITileBound.
 */
public interface IWireframeAABBProvider {

	/**
	 * Retrieves wireframes to render, in world coordinates
	 */
	public List<Box> getWireframeAABB(World world, BlockPos pos);

}
