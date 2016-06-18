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

import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;

public final class Quat {

	public static final Quat IDENTITY = new Quat(0, 0, 0, 1);
	public final double x;
	public final double y;
	public final double z;
	public final double s;
	private final int hashCode;

	public Quat(double d, double d1, double d2, double d3) {
		x = d1;
		y = d2;
		z = d3;
		s = d;
		this.hashCode = Arrays.hashCode(new double[] { d, d1, d2, d3 } );
	}

	public static Quat aroundAxis(double ax, double ay, double az, double angle) {
		angle *= 0.5D;
		double d4 = Math.sin(angle);
		return new Quat(Math.cos(angle), ax * d4, ay * d4, az * d4);
	}

	public Quat multiply(Quat quat) {
		double d = s * quat.s - x * quat.x - y * quat.y - z * quat.z;
		double d1 = s * quat.x + x * quat.s - y * quat.z + z * quat.y;
		double d2 = s * quat.y + x * quat.z + y * quat.s - z * quat.x;
		double d3 = s * quat.z - x * quat.y + y * quat.x + z * quat.s;
		return new Quat(d1, d2, d3, d);
	}

	public Quat rightMultiply(Quat quat) {
		double d = s * quat.s - x * quat.x - y * quat.y - z * quat.z;
		double d1 = s * quat.x + x * quat.s + y * quat.z - z * quat.y;
		double d2 = s * quat.y - x * quat.z + y * quat.s + z * quat.x;
		double d3 = s * quat.z + x * quat.y - y * quat.x + z * quat.s;
		return new Quat(d1, d2, d3, d);
	}

	public double mag() {
		return Math.sqrt(x * x + y * y + z * z + s * s);
	}

	public Quat normalize() {
		double d = mag();
		if (d == 0.0D) {
			return this;
		} else {
			d = 1.0D / d;
			return new Quat(x * d, y * d, z * d, s * d);
		}
	}

	public void rotate(Vector3 vec) {
		double d = -x * vec.x - y * vec.y - z * vec.z;
		double d1 = s * vec.x + y * vec.z - z * vec.y;
		double d2 = s * vec.y - x * vec.z + z * vec.x;
		double d3 = s * vec.z + x * vec.y - y * vec.x;
		vec.x = d1 * s - d * x - d2 * z + d3 * y;
		vec.y = d2 * s - d * y + d1 * z - d3 * x;
		vec.z = d3 * s - d * z - d1 * y + d2 * x;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof Quat
				&& ((Quat) o).x == this.x
				&& ((Quat) o).y == this.y
				&& ((Quat) o).z == this.z
				&& ((Quat) o).s == this.s;
	}

	@Override
	public String toString() {
		StringBuilder stringbuilder = new StringBuilder();
		Formatter formatter = new Formatter(stringbuilder, Locale.US);
		formatter.format("Quaternion:%n");
		formatter.format("  < %f %f %f %f >%n", s, x, y, z);
		formatter.close();
		return stringbuilder.toString();
	}

	public static Quat aroundAxis(Vector3 axis, double angle) {
		return aroundAxis(axis.x, axis.y, axis.z, angle);
	}

}