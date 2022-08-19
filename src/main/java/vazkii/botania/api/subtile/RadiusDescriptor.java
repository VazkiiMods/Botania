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

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;

/**
 * This object describes the Radius of a SubTileEntity. It can either be
 * a circle or rectangle, it's to note that the
 */
public class RadiusDescriptor {

	final ChunkCoordinates subtileCoords;

	public RadiusDescriptor(ChunkCoordinates subtileCoords) {
		this.subtileCoords = subtileCoords;
	}

	public ChunkCoordinates getSubtileCoords() {
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

		public Circle(ChunkCoordinates subtileCoords, double radius) {
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

		public Rectangle(ChunkCoordinates subtileCoords, AxisAlignedBB aabb) {
			super(subtileCoords);
			this.aabb = aabb;
		}

		@Override
		public AxisAlignedBB getAABB() {
			return aabb;
		}

	}

	public static class Square extends Rectangle {

		public Square(ChunkCoordinates subtileCoords, int expand) {
			super(subtileCoords, AxisAlignedBB.getBoundingBox(subtileCoords.posX - expand, subtileCoords.posY, subtileCoords.posZ - expand, subtileCoords.posX + 1 + expand, subtileCoords.posY, subtileCoords.posZ + 1 + expand));
		}

	}

}
