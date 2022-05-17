/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.subtile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

/**
 * This object describes the Radius of a SubTileEntity. It can either be
 * a circle or rectangle.
 */
public sealed interface RadiusDescriptor permits RadiusDescriptor.Circle,RadiusDescriptor.Rectangle {
	record Circle(BlockPos subtileCoords, double radius) implements RadiusDescriptor {
	}

	record Rectangle(BlockPos subtileCoords, AABB aabb) implements RadiusDescriptor {
		public static Rectangle square(BlockPos subtileCoords, int expand) {
			return new Rectangle(subtileCoords, new AABB(subtileCoords.offset(-expand, 0, -expand), subtileCoords.offset(expand + 1, 0, expand + 1)));
		}
	}
}
