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

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vector3d;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.helper.ShaderHelper;

import javax.annotation.Nonnull;

public class FXSparkle extends SpriteTexturedParticle {
	private final boolean corrupt;
	public final boolean fake;
	public final int particle = 16;
	private final boolean slowdown = true;
	private final IAnimatedSprite sprite;

	public FXSparkle(World world, double x, double y, double z, float size,
			float red, float green, float blue, int m,
			boolean fake, boolean noClip, boolean corrupt, IAnimatedSprite sprite) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		particleRed = red;
		particleGreen = green;
		particleBlue = blue;
		particleAlpha = 0.75F;
		particleGravity = 0;
		motionX = motionY = motionZ = 0;
		particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 0.2F * size;
		maxAge = 3 * m;
		setSize(0.01F, 0.01F);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		this.fake = fake;
		this.corrupt = corrupt;
		this.canCollide = !fake && !noClip;
		this.sprite = sprite;
		selectSpriteWithAge(sprite);
	}

	@Override
	public float getScale(float partialTicks) {
		return particleScale * (maxAge - age + 1) / (float) maxAge;
	}

	@Override
	public void tick() {
		selectSpriteWithAge(sprite);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (age++ >= maxAge) {
			setExpired();
		}

		motionY -= 0.04D * particleGravity;

		if (canCollide && !fake) {
			wiggleAround(posX, (getBoundingBox().minY + getBoundingBox().maxY) / 2.0D, posZ);
		}

		this.move(motionX, motionY, motionZ);

		if (slowdown) {
			motionX *= 0.908000001907348633D;
			motionY *= 0.908000001907348633D;
			motionZ *= 0.908000001907348633D;

			if (onGround) {
				motionX *= 0.69999998807907104D;
				motionZ *= 0.69999998807907104D;
			}
		}

		if (fake && age > 1) {
			setExpired();
		}
	}

	@Nonnull
	@Override
	public IParticleRenderType getRenderType() {
		return corrupt ? CORRUPT_RENDER : NORMAL_RENDER;
	}

	public void setGravity(float value) {
		particleGravity = value;
	}

	// [VanillaCopy] Entity.pushOutOfBlocks with tweaks
	private void wiggleAround(double x, double y, double z) {
		BlockPos blockpos = new BlockPos(x, y, z);
		Vector3d Vector3d = new Vector3d(x - (double) blockpos.getX(), y - (double) blockpos.getY(), z - (double) blockpos.getZ());
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		Direction direction = Direction.UP;
		double d0 = Double.MAX_VALUE;

		for (Direction direction1 : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP }) {
			blockpos$mutable.setPos(blockpos).move(direction1);
			if (!this.world.getBlockState(blockpos$mutable).isCollisionShapeOpaque(this.world, blockpos$mutable)) {
				double d1 = Vector3d.getCoordinate(direction1.getAxis());
				double d2 = direction1.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0D - d1 : d1;
				if (d2 < d0) {
					d0 = d2;
					direction = direction1;
				}
			}
		}

		// Botania - made multiplier and add both smaller
		float f = this.rand.nextFloat() * 0.05F + 0.025F;
		float f1 = (float) direction.getAxisDirection().getOffset();
		// Botania - Randomness in other axes as well
		float secondary = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
		float secondary2 = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
		if (direction.getAxis() == Direction.Axis.X) {
			motionX = (double) (f1 * f);
			motionY = secondary;
			motionZ = secondary2;
		} else if (direction.getAxis() == Direction.Axis.Y) {
			motionX = secondary;
			motionY = (double) (f1 * f);
			motionZ = secondary2;
		} else if (direction.getAxis() == Direction.Axis.Z) {
			motionX = secondary;
			motionY = secondary2;
			motionZ = (double) (f1 * f);
		}
	}

	private static void beginRenderCommon(BufferBuilder buffer, TextureManager textureManager) {
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		RenderSystem.disableLighting();
		textureManager.bindTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE);
		Texture tex = textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE);
		tex.setBlurMipmap(true, false);
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
	}

	private static void endRenderCommon() {
		Minecraft.getInstance().textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE).restoreLastBlurMipmap();
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
	}

	private static final IParticleRenderType NORMAL_RENDER = new IParticleRenderType() {
		@Override
		public void beginRender(BufferBuilder bufferBuilder, TextureManager textureManager) {
			beginRenderCommon(bufferBuilder, textureManager);
		}

		@Override
		public void finishRender(Tessellator tessellator) {
			tessellator.draw();
			endRenderCommon();
		}

		@Override
		public String toString() {
			return "botania:sparkle";
		}
	};

	private static final IParticleRenderType CORRUPT_RENDER = new IParticleRenderType() {
		@Override
		public void beginRender(BufferBuilder bufferBuilder, TextureManager textureManager) {
			beginRenderCommon(bufferBuilder, textureManager);
			ShaderHelper.useShader(ShaderHelper.BotaniaShader.FILM_GRAIN);
		}

		@Override
		public void finishRender(Tessellator tessellator) {
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
