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
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class FXWisp extends SpriteTexturedParticle {
	private final boolean depthTest;
	private final float moteParticleScale;
	private final int moteHalfLife;

	public FXWisp(World world, double d, double d1, double d2, double xSpeed, double ySpeed, double zSpeed,
			float size, float red, float green, float blue, boolean depthTest, float maxAgeMul, boolean noClip) {
		super(world, d, d1, d2, 0, 0, 0);
		// super applies wiggle to motion so set it here instead
		motionX = xSpeed;
		motionY = ySpeed;
		motionZ = zSpeed;
		particleRed = red;
		particleGreen = green;
		particleBlue = blue;
		particleAlpha = 0.375F;
		particleGravity = 0;
		particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F * size;
		moteParticleScale = particleScale;
		maxAge = (int) (28D / (Math.random() * 0.3D + 0.7D) * maxAgeMul);
		this.depthTest = depthTest;

		moteHalfLife = maxAge / 2;
		setSize(0.01F, 0.01F);

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		this.canCollide = !noClip;
	}

	@Override
	public float getScale(float p_217561_1_) {
		float agescale = (float) age / (float) moteHalfLife;
		if (agescale > 1F) {
			agescale = 2 - agescale;
		}

		particleScale = moteParticleScale * agescale * 0.5F;
		return particleScale;
	}

	@Override
	protected int getBrightnessForRender(float partialTicks) {
		return 0xF000F0;
	}

	@Nonnull
	@Override
	public IParticleRenderType getRenderType() {
		return depthTest ? NORMAL_RENDER : DIW_RENDER;
	}

	// [VanillaCopy] of super, without drag when onGround is true
	@Override
	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.age++ >= this.maxAge) {
			this.setExpired();
		}

		this.motionY -= 0.04D * (double) this.particleGravity;
		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;
	}

	public void setGravity(float value) {
		particleGravity = value;
	}

	private static void beginRenderCommon(BufferBuilder bufferBuilder, TextureManager textureManager) {
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		RenderSystem.disableLighting();

		textureManager.bindTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE);
		textureManager.getTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE).setBlurMipmap(true, false);
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
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
			return "botania:wisp";
		}
	};

	private static final IParticleRenderType DIW_RENDER = new IParticleRenderType() {
		@Override
		public void beginRender(BufferBuilder bufferBuilder, TextureManager textureManager) {
			beginRenderCommon(bufferBuilder, textureManager);
			RenderSystem.disableDepthTest();
		}

		@Override
		public void finishRender(Tessellator tessellator) {
			tessellator.draw();
			RenderSystem.enableDepthTest();
			endRenderCommon();
		}

		@Override
		public String toString() {
			return "botania:depth_ignoring_wisp";
		}
	};
}
