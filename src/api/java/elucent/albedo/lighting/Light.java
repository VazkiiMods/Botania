/*
 * The MIT License
 *
 * Copyright (c) 2017 Elucent, William Thompson (unascribed), and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package elucent.albedo.lighting;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Light {
	
	public float x;
	public float y;
	public float z;
	
	public float r;
	public float g;
	public float b;
	public float a;
	
	public float radius;

	@Deprecated
	public Light(float x, float y, float z, float r, float g, float b, float a, float radius) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.radius = radius;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		private float x = Float.NaN;
		private float y = Float.NaN;
		private float z = Float.NaN;
		
		private float r = Float.NaN;
		private float g = Float.NaN;
		private float b = Float.NaN;
		private float a = Float.NaN;
		
		private float radius = Float.NaN;
		
		
		public Builder pos(BlockPos pos) {
			return pos(pos.getX()+0.5f, pos.getY()+0.5f, pos.getZ()+0.5f);
		}
		
		public Builder pos(Vec3d pos) {
			return pos(pos.x, pos.y, pos.z);
		}
		
		public Builder pos(Entity e) {
			return pos(e.posX, e.posY, e.posZ);
		}
		
		public Builder pos(double x, double y, double z) {
			return pos((float)x, (float)y, (float)z);
		}
		
		public Builder pos(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
			return this;
		}
		
		
		public Builder color(int c, boolean hasAlpha) {
			return color(extract(c, 2), extract(c, 1), extract(c, 0), hasAlpha ? extract(c, 3) : 1);
		}
		
		private float extract(int i, int idx) {
			return ((i >> (idx*8)) & 0xFF)/255f;
		}

		public Builder color(float r, float g, float b) {
			return color(r, g, b, 1);
		}
		
		public Builder color(float r, float g, float b, float a) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
			return this;
		}
		
		
		public Builder radius(float radius) {
			this.radius = radius;
			return this;
		}
		
		
		public Light build() {
			if (Float.isFinite(x) && Float.isFinite(y) && Float.isFinite(z) &&
					Float.isFinite(r) && Float.isFinite(g) && Float.isFinite(b) && Float.isFinite(a) &&
					Float.isFinite(radius)) {
				return new Light(x, y, z, r, g, b, a, radius);
			} else {
				throw new IllegalArgumentException("Position, color, and radius must be set, and cannot be infinite");
			}
		}
		
	}
	
}