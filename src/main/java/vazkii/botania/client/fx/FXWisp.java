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
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;

import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.ExtendedTexture;

import javax.annotation.Nonnull;

public class FXWisp extends SpriteBillboardParticle {
	private final boolean depthTest;
	private final float moteParticleScale;
	private final int moteHalfLife;

	public FXWisp(ClientWorld world, double d, double d1, double d2, double xSpeed, double ySpeed, double zSpeed,
			float size, float red, float green, float blue, boolean depthTest, float maxAgeMul, boolean noClip) {
		super(world, d, d1, d2, 0, 0, 0);
		// super applies wiggle to motion so set it here instead
		velocityX = xSpeed;
		velocityY = ySpeed;
		velocityZ = zSpeed;
		colorRed = red;
		colorGreen = green;
		colorBlue = blue;
		colorAlpha = 0.375F;
		gravityStrength = 0;
		scale = (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F * size;
		moteParticleScale = scale;
		maxAge = (int) (28D / (Math.random() * 0.3D + 0.7D) * maxAgeMul);
		this.depthTest = depthTest;

		moteHalfLife = maxAge / 2;
		setBoundingBoxSpacing(0.01F, 0.01F);

		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		this.collidesWithWorld = !noClip;
	}

	@Override
	public float getSize(float p_217561_1_) {
		float agescale = (float) age / (float) moteHalfLife;
		if (agescale > 1F) {
			agescale = 2 - agescale;
		}

		scale = moteParticleScale * agescale * 0.5F;
		return scale;
	}

	@Override
	protected int getColorMultiplier(float partialTicks) {
		return 0xF000F0;
	}

	@Nonnull
	@Override
	public ParticleTextureSheet getType() {
		return depthTest ? NORMAL_RENDER : DIW_RENDER;
	}

	// [VanillaCopy] of super, without drag when onGround is true
	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.age++ >= this.maxAge) {
			this.markDead();
		}

		this.velocityY -= 0.04D * (double) this.gravityStrength;
		this.move(this.velocityX, this.velocityY, this.velocityZ);
		this.velocityX *= 0.9800000190734863D;
		this.velocityY *= 0.9800000190734863D;
		this.velocityZ *= 0.9800000190734863D;
	}

	public void setGravity(float value) {
		gravityStrength = value;
	}

	private static void beginRenderCommon(BufferBuilder bufferBuilder, TextureManager textureManager) {
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		RenderSystem.disableLighting();

		textureManager.bindTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
		AbstractTexture tex = textureManager.getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
		((ExtendedTexture) tex).setFilterSave(true, false);
		bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
	}

	private static void endRenderCommon() {
		AbstractTexture tex = MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
		((ExtendedTexture) tex).restoreLastFilter();
		RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
	}

	public static final ParticleTextureSheet NORMAL_RENDER = new ParticleTextureSheet() {
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
			return "botania:wisp";
		}
	};

	public static final ParticleTextureSheet DIW_RENDER = new ParticleTextureSheet() {
		@Override
		public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
			beginRenderCommon(bufferBuilder, textureManager);
			RenderSystem.disableDepthTest();
		}

		@Override
		public void draw(Tessellator tessellator) {
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
