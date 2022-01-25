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
public class RadiusDescriptor {

	private final BlockPos subtileCoords;

	public RadiusDescriptor(BlockPos subtileCoords) {
		this.subtileCoords = subtileCoords;
	}

	public BlockPos getSubtileCoords() {
		return subtileCoords;
	}

	public boolean isCircle() {
		return false;
	}

	public double getCircleRadius() {
		return 0;
	}

	public AABB getAABB() {
		return null;
	}

	public static class Circle extends RadiusDescriptor {

		final double radius;

		public Circle(BlockPos subtileCoords, double radius) {
			super(subtileCoords);
			this.radius = radius;
		}

		@Override
		public boolean isCircle() {
			return true;
		}

		@Override
		public double getCircleRadius() {
			return radius;
		}

	}

	public static class Rectangle extends RadiusDescriptor {

		final AABB aabb;

		public Rectangle(BlockPos subtileCoords, AABB aabb) {
			super(subtileCoords);
			this.aabb = aabb;
		}

		@Override
		public AABB getAABB() {
			return aabb;
		}

	}

	public static class Square extends Rectangle {

		public Square(BlockPos subtileCoords, int expand) {
			super(subtileCoords, new AABB(subtileCoords.offset(-expand, 0, -expand), subtileCoords.offset(expand + 1, 0, expand + 1)));
		}

	}

}
