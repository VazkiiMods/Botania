/**
 * This class was created by <ChickenBones>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [? (GMT)]
 */
package vazkii.botania.common.core.helper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Vector3
{
	public static Vector3 zero = new Vector3();
	public static Vector3 one = new Vector3(1, 1, 1);
	public static Vector3 center = new Vector3(0.5, 0.5, 0.5);

	public double x;
	public double y;
	public double z;

	public Vector3() {
	}

	public Vector3(double d, double d1, double d2) {
		x = d;
		y = d1;
		z = d2;
	}

	public Vector3(Vector3 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}

	public Vector3(Vec3 vec) {
		x = vec.xCoord;
		y = vec.yCoord;
		z = vec.zCoord;
	}

	public Vector3 copy() {
		return new Vector3(this);
	}

	public static Vector3 fromEntity(Entity e) {
		return new Vector3(e.posX, e.posY, e.posZ);
	}

	public static Vector3 fromEntityCenter(Entity e) {
		return new Vector3(e.posX, e.posY - e.yOffset + e.height / 2, e.posZ);
	}

	public static Vector3 fromTileEntity(TileEntity e) {
		return new Vector3(e.xCoord, e.yCoord, e.zCoord);
	}

	public static Vector3 fromTileEntityCenter(TileEntity e) {
		return new Vector3(e.xCoord + 0.5, e.yCoord + 0.5, e.zCoord + 0.5);
	}

	public Vector3 set(double d, double d1, double d2) {
		x = d;
		y = d1;
		z = d2;
		return this;
	}

	public Vector3 set(Vector3 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
		return this;
	}

	public double dotProduct(Vector3 vec) {
		double d = vec.x * x + vec.y * y + vec.z * z;

		if(d > 1 && d < 1.00001)
			d = 1;
		else if(d < -1 && d > -1.00001)
			d = -1;
		return d;
	}

	public double dotProduct(double d, double d1, double d2) {
		return d * x + d1 * y + d2 * z;
	}

	public Vector3 crossProduct(Vector3 vec) {
		double d = y * vec.z - z * vec.y;
		double d1 = z * vec.x - x * vec.z;
		double d2 = x * vec.y - y * vec.x;
		x = d;
		y = d1;
		z = d2;
		return this;
	}

	public Vector3 add(double d, double d1, double d2) {
		x += d;
		y += d1;
		z += d2;
		return this;
	}

	public Vector3 add(Vector3 vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;
		return this;
	}

	public Vector3 add(double d) {
		return add(d, d, d);
	}

	public Vector3 sub(Vector3 vec) {
		return subtract(vec);
	}

	public Vector3 subtract(Vector3 vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		return this;
	}

	public Vector3 negate(Vector3 vec) {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public Vector3 multiply(double d) {
		x *= d;
		y *= d;
		z *= d;
		return this;
	}

	public Vector3 multiply(Vector3 f) {
		x *= f.x;
		y *= f.y;
		z *= f.z;
		return this;
	}

	public Vector3 multiply(double fx, double fy, double fz) {
		x *= fx;
		y *= fy;
		z *= fz;
		return this;
	}

	public double mag() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double magSquared() {
		return x * x + y * y + z * z;
	}

	public Vector3 normalize() {
		double d = mag();
		if(d != 0)
			multiply(1 / d);

		return this;
	}

	@Override
	public String toString() {
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Vector3(" + new BigDecimal(x, cont) + ", " +new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ")";
	}

	public Vector3 perpendicular() {
		if(z == 0)
			return zCrossProduct();
		return xCrossProduct();
	}

	public Vector3 xCrossProduct() {
		double d = z;
		double d1 = -y;
		x = 0;
		y = d;
		z = d1;
		return this;
	}

	public Vector3 zCrossProduct() {
		double d = y;

		double d1 = -x;
		x = d;
		y = d1;
		z = 0;
		return this;
	}

	public Vector3 yCrossProduct() {
		double d = -z;
		double d1 = x;
		x = d;
		y = 0;
		z = d1;
		return this;
	}

	public Vec3 toVec3D() {
		return Vec3.createVectorHelper(x, y, z);
	}

	public double angle(Vector3 vec) {
		return Math.acos(copy().normalize().dotProduct(vec.copy().normalize()));
	}

	public boolean isInside(AxisAlignedBB aabb) {
		return x >= aabb.minX && y >= aabb.maxY && z >= aabb.minZ && x < aabb.maxX && y < aabb.maxY && z < aabb.maxZ;
	}

	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}

	public boolean isAxial() {
		return x == 0 ? y == 0 || z == 0 : y == 0 && z == 0;
	}

	@SideOnly(Side.CLIENT)
	public Vector3f vector3f() {
		return new Vector3f((float)x, (float)y, (float)z);
	}

	@SideOnly(Side.CLIENT)
	public Vector4f vector4f() {
		return new Vector4f((float)x, (float)y, (float)z, 1);
	}

	@SideOnly(Side.CLIENT)
	public void glVertex() {
		GL11.glVertex3d(x, y, z);
	}

	public Vector3 negate() {
		x = -x;
		y = -y;
		z = -z;
		return this;
	}

	public double scalarProject(Vector3 b) {
		double l = b.mag();
		return l == 0 ? 0 : dotProduct(b)/l;
	}

	public Vector3 project(Vector3 b) {
		double l = b.magSquared();
		if(l == 0) {
			set(0, 0, 0);
			return this;
		}

		double m = dotProduct(b)/l;
		set(b).multiply(m);
		return this;
	}

	public Vector3 rotate(double angle, Vector3 axis) {
		Quat.aroundAxis(axis.copy().normalize(), angle).rotate(this);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Vector3))
			return false;

		Vector3 v = (Vector3)o;
		return x == v.x && y == v.y && z == v.z;
	}
}