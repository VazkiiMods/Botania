/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.wand;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * A block that implements this can provide a custom AABB
 * for rendering the wireframe with ITileBound.
 */
public interface IWireframeAABBProvider {

	/**
	 * Retrieves wireframes to render, in world coordinates
	 */
	List<AABB> getWireframeAABB(Level world, BlockPos pos);

}
