/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 31, 2015, 2:59:19 PM (GMT)]
 */
package vazkii.botania.api.subtile;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

/**
 * This object describes the Radius of a SubTileEntity. It can either be
 * a circle or rectangle, it's to note that the
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

	public AxisAlignedBB getAABB() {
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

		final AxisAlignedBB aabb;

		public Rectangle(BlockPos subtileCoords, AxisAlignedBB aabb) {
			super(subtileCoords);
			this.aabb = aabb;
		}

		@Override
		public AxisAlignedBB getAABB() {
			return aabb;
		}

	}

	public static class Square extends Rectangle {

		public Square(BlockPos subtileCoords, int expand) {
			super(subtileCoords, new AxisAlignedBB(subtileCoords.add(-expand, 0, -expand), subtileCoords.add(expand + 1, 0, expand + 1)));
		}

	}

}
