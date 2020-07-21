/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.fx;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.helper.ShaderHelper;

import javax.annotation.Nonnull;

public class FXSparkle extends SpriteBillboardParticle {
	private final boolean corrupt;
	public final boolean fake;
	public final int particle = 16;
	private final boolean slowdown = true;
	private final SpriteProvider sprite;

	public FXSparkle(ClientWorld world, double x, double y, double z, float size,
			float red, float green, float blue, int m,
			boolean fake, boolean noClip, boolean corrupt, SpriteProvider sprite) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		colorRed = red;
		colorGreen = green;
		colorBlue = blue;
		colorAlpha = 0.75F;
		gravityStrength = 0;
		velocityX = velocityY = velocityZ = 0;
		scale = (this.random.nextFloat() * 0.5F + 0.5F) * 0.2F * size;
		maxAge = 3 * m;
		setBoundingBoxSpacing(0.01F, 0.01F);
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		this.fake = fake;
		this.corrupt = corrupt;
		this.collidesWithWorld = !fake && !noClip;
		this.sprite = sprite;
		setSpriteForAge(sprite);
	}

	@Override
	public float getSize(float partialTicks) {
		return scale * (maxAge - age + 1) / (float) maxAge;
	}

	@Override
	public void tick() {
		setSpriteForAge(sprite);
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;

		if (age++ >= maxAge) {
			markDead();
		}

		velocityY -= 0.04D * gravityStrength;

		if (collidesWithWorld && !fake) {
			wiggleAround(x, (getBoundingBox().minY + getBoundingBox().maxY) / 2.0D, z);
		}

		this.move(velocityX, velocityY, velocityZ);

		if (slowdown) {
			velocityX *= 0.908000001907348633D;
			velocityY *= 0.908000001907348633D;
			velocityZ *= 0.908000001907348633D;

			if (onGround) {
				velocityX *= 0.69999998807907104D;
				velocityZ *= 0.69999998807907104D;
			}
		}

		if (fake && age > 1) {
			markDead();
		}
	}

	@Nonnull
	@Override
	public ParticleTextureSheet getType() {
		return corrupt ? CORRUPT_RENDER : NORMAL_RENDER;
	}

	public void setGravity(float value) {
		gravityStrength = value;
	}

	// [VanillaCopy] Entity.pushOutOfBlocks with tweaks
	private void wiggleAround(double x, double y, double z) {
		BlockPos blockpos = new BlockPos(x, y, z);
		Vec3d Vector3d = new Vec3d(x - (double) blockpos.getX(), y - (double) blockpos.getY(), z - (double) blockpos.getZ());
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		Direction direction = Direction.UP;
		double d0 = Double.MAX_VALUE;

		for (Direction direction1 : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP }) {
			blockpos$mutable.set(blockpos).move(direction1);
			if (!this.world.getBlockState(blockpos$mutable).isFullCube(this.world, blockpos$mutable)) {
				double d1 = Vector3d.getComponentAlongAxis(direction1.getAxis());
				double d2 = direction1.getDirection() == Direction.AxisDirection.POSITIVE ? 1.0D - d1 : d1;
				if (d2 < d0) {
					d0 = d2;
					direction = direction1;
				}
			}
		}

		// Botania - made multiplier and add both smaller
		float f = this.random.nextFloat() * 0.05F + 0.025F;
		float f1 = (float) direction.getDirection().offset();
		// Botania - Randomness in other axes as well
		float secondary = (random.nextFloat() - random.nextFloat()) * 0.1F;
		float secondary2 = (random.nextFloat() - random.nextFloat()) * 0.1F;
		if (direction.getAxis() == Direction.Axis.X) {
			velocityX = (double) (f1 * f);
			velocityY = secondary;
			velocityZ = secondary2;
		} else if (direction.getAxis() == Direction.Axis.Y) {
			velocityX = secondary;
			velocityY = (double) (f1 * f);
			velocityZ = secondary2;
		} else if (direction.getAxis() == Direction.Axis.Z) {
			velocityX = secondary;
			velocityY = secondary2;
			velocityZ = (double) (f1 * f);
		}
	}

	private static void beginRenderCommon(BufferBuilder buffer, TextureManager textureManager) {
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		RenderSystem.disableLighting();
		textureManager.bindTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEX);
		AbstractTexture tex = textureManager.getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEX);
		tex.setBlurMipmap(true, false);
		buffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
	}

	private static void endRenderCommon() {
		MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEX).restoreLastBlurMipmap();
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
	}

	private static final ParticleTextureSheet NORMAL_RENDER = new ParticleTextureSheet() {
		@Override
		public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
			beginRenderCommon(bufferBuilder, textureManager);
		}

		@Override
		public void draw(Tessellator tessellator) {
			tessellator.draw();
			endRenderCommon();
		}

		@Override
		public String toString() {
			return "botania:sparkle";
		}
	};

	private static final ParticleTextureSheet CORRUPT_RENDER = new ParticleTextureSheet() {
		@Override
		public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
			beginRenderCommon(bufferBuilder, textureManager);
			ShaderHelper.useShader(ShaderHelper.BotaniaShader.FILM_GRAIN);
		}

		@Override
		public void draw(Tessellator tessellator) {
			tessellator.draw();
			ShaderHelper.releaseShader();
			endRenderCommon();
		}

		@Override
		public String toString() {
			return "botania:corrupt_sparkle";
		}
	};
}
