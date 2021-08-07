/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import vazkii.botania.client.core.helper.ShaderCallback;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;

public class RenderDoppleganger extends HumanoidMobRenderer<EntityDoppleganger, HumanoidModel<EntityDoppleganger>> {

	private static final float DEFAULT_GRAIN_INTENSITY = 0.05F;
	private static final float DEFAULT_DISFIGURATION = 0.025F;

	private static float grainIntensity = DEFAULT_GRAIN_INTENSITY;
	private static float disfiguration = DEFAULT_DISFIGURATION;

	private static final ShaderCallback CALLBACK = shader -> {
		// Frag Uniforms
		int disfigurationUniform = GlStateManager._glGetUniformLocation(shader, "disfiguration");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, disfiguration);
		RenderSystem.glUniform1(disfigurationUniform, ShaderHelper.FLOAT_BUF);

		// Vert Uniforms
		int grainIntensityUniform = GlStateManager._glGetUniformLocation(shader, "grainIntensity");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, grainIntensity);
		RenderSystem.glUniform1(grainIntensityUniform, ShaderHelper.FLOAT_BUF);
	};

	public static final ShaderCallback defaultCallback = shader -> {
		// Frag Uniforms
		int disfigurationUniform = GlStateManager._glGetUniformLocation(shader, "disfiguration");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, DEFAULT_DISFIGURATION);
		RenderSystem.glUniform1(disfigurationUniform, ShaderHelper.FLOAT_BUF);

		// Vert Uniforms
		int grainIntensityUniform = GlStateManager._glGetUniformLocation(shader, "grainIntensity");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, DEFAULT_GRAIN_INTENSITY);
		RenderSystem.glUniform1(grainIntensityUniform, ShaderHelper.FLOAT_BUF);
	};

	public RenderDoppleganger(EntityRendererProvider.Context ctx) {
		super(ctx, new Model(), 0F);
	}

	@Override
	public void render(@Nonnull EntityDoppleganger dopple, float yaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light) {
		int invulTime = dopple.getInvulTime();
		if (invulTime > 0) {
			grainIntensity = invulTime > 20 ? 1F : invulTime * 0.05F;
			disfiguration = grainIntensity * 0.3F;
		} else {
			disfiguration = (0.025F + dopple.hurtTime * ((1F - 0.15F) / 20F)) / 2F;
			grainIntensity = 0.05F + dopple.hurtTime * ((1F - 0.15F) / 10F);
		}

		super.render(dopple, yaw, partialTicks, ms, buffers, light);
	}

	@Nonnull
	@Override
	public ResourceLocation getTextureLocation(@Nonnull EntityDoppleganger entity) {
		Minecraft mc = Minecraft.getInstance();

		if (!(mc.getCameraEntity() instanceof AbstractClientPlayer)) {
			return DefaultPlayerSkin.getDefaultSkin(entity.getUUID());
		}

		return ((AbstractClientPlayer) mc.getCameraEntity()).getSkinTextureLocation();
	}

	@Override
	protected boolean isBodyVisible(EntityDoppleganger dopple) {
		return true;
	}

	private static class Model extends HumanoidModel<EntityDoppleganger> {
		private static RenderType makeRenderType(ResourceLocation texture) {
			RenderType normal = RenderType.entityTranslucent(texture);
			return ShaderHelper.useShaders()
					? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.DOPPLEGANGER, CALLBACK, normal)
					: normal;
		}

		Model() {
			super(Model::makeRenderType, 0, 0, 64, 64);
		}
	}

}
