/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;

import java.util.Locale;

public class SparkleParticleData implements IParticleData {
	public final float size;
	public final float r, g, b;
	public final int m;
	public final boolean noClip;
	public final boolean fake;
	public final boolean corrupt;

	public static SparkleParticleData noClip(float size, float r, float g, float b, int m) {
		return new SparkleParticleData(size, r, g, b, m, true, false, false);
	}

	public static SparkleParticleData fake(float size, float r, float g, float b, int m) {
		return new SparkleParticleData(size, r, g, b, m, false, true, false);
	}

	public static SparkleParticleData corrupt(float size, float r, float g, float b, int m) {
		return new SparkleParticleData(size, r, g, b, m, false, false, true);
	}

	public static SparkleParticleData sparkle(float size, float r, float g, float b, int m) {
		return new SparkleParticleData(size, r, g, b, m, false, false, false);
	}

	private SparkleParticleData(float size, float r, float g, float b, int m, boolean noClip, boolean fake, boolean corrupt) {
		this.size = size;
		this.r = r;
		this.g = g;
		this.b = b;
		this.m = m;
		this.noClip = noClip;
		this.fake = fake;
		this.corrupt = corrupt;
	}

	@Nonnull
	@Override
	public ParticleType<SparkleParticleData> getType() {
		return ModParticles.SPARKLE;
	}

	@Override
	public void write(PacketBuffer buf) {
		buf.writeFloat(size);
		buf.writeFloat(r);
		buf.writeFloat(g);
		buf.writeFloat(b);
		buf.writeInt(m);
		buf.writeBoolean(noClip);
		buf.writeBoolean(fake);
		buf.writeBoolean(corrupt);
	}

	@Nonnull
	@Override
	public String getParameters() {
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d %s %s %s",
				Registry.PARTICLE_TYPE.getKey(this.getType()), this.size, this.r, this.g, this.b, this.m, this.noClip, this.fake, this.corrupt);
	}

	public static final IDeserializer<SparkleParticleData> DESERIALIZER = new IDeserializer<SparkleParticleData>() {
		@Nonnull
		@Override
		public SparkleParticleData deserialize(@Nonnull ParticleType<SparkleParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			float size = reader.readFloat();
			reader.expect(' ');
			float r = reader.readFloat();
			reader.expect(' ');
			float g = reader.readFloat();
			reader.expect(' ');
			float b = reader.readFloat();
			reader.expect(' ');
			int m = reader.readInt();
			reader.expect(' ');
			boolean noClip = reader.readBoolean();
			reader.expect(' ');
			boolean fake = reader.readBoolean();
			reader.expect(' ');
			boolean corrupt = reader.readBoolean();

			return new SparkleParticleData(size, r, g, b, m, noClip, fake, corrupt);
		}

		@Override
		public SparkleParticleData read(@Nonnull ParticleType<SparkleParticleData> type, PacketBuffer buf) {
			return new SparkleParticleData(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
		}
	};
}
