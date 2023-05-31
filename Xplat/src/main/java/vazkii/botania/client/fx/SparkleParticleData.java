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
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class SparkleParticleData implements ParticleOptions {
	public static final Codec<SparkleParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.FLOAT.fieldOf("size").forGetter(d -> d.size),
			Codec.FLOAT.fieldOf("r").forGetter(d -> d.r),
			Codec.FLOAT.fieldOf("g").forGetter(d -> d.g),
			Codec.FLOAT.fieldOf("b").forGetter(d -> d.b),
			Codec.INT.fieldOf("m").forGetter(d -> d.m),
			Codec.BOOL.fieldOf("noClip").forGetter(d -> d.noClip),
			Codec.BOOL.fieldOf("fake").forGetter(d -> d.fake),
			Codec.BOOL.fieldOf("corrupt").forGetter(d -> d.corrupt)
	).apply(instance, SparkleParticleData::new));
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

	@NotNull
	@Override
	public ParticleType<SparkleParticleData> getType() {
		return BotaniaParticles.SPARKLE;
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(size);
		buf.writeFloat(r);
		buf.writeFloat(g);
		buf.writeFloat(b);
		buf.writeInt(m);
		buf.writeBoolean(noClip);
		buf.writeBoolean(fake);
		buf.writeBoolean(corrupt);
	}

	@NotNull
	@Override
	public String writeToString() {
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %d %s %s %s",
				BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.size, this.r, this.g, this.b, this.m, this.noClip, this.fake, this.corrupt);
	}

	public static final Deserializer<SparkleParticleData> DESERIALIZER = new Deserializer<>() {
		@NotNull
		@Override
		public SparkleParticleData fromCommand(@NotNull ParticleType<SparkleParticleData> type, @NotNull StringReader reader) throws CommandSyntaxException {
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
		public SparkleParticleData fromNetwork(@NotNull ParticleType<SparkleParticleData> type, FriendlyByteBuf buf) {
			return new SparkleParticleData(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
		}
	};
}
