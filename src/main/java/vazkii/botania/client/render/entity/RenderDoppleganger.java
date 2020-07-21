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
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import vazkii.botania.client.core.helper.ShaderCallback;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderDoppleganger extends BipedEntityRenderer<EntityDoppleganger, BipedEntityModel<EntityDoppleganger>> {

	private static final float DEFAULT_GRAIN_INTENSITY = 0.05F;
	private static final float DEFAULT_DISFIGURATION = 0.025F;

	private static float grainIntensity = DEFAULT_GRAIN_INTENSITY;
	private static float disfiguration = DEFAULT_DISFIGURATION;

	private static final ShaderCallback CALLBACK = shader -> {
		// Frag Uniforms
		int disfigurationUniform = GlStateManager.getUniformLocation(shader, "disfiguration");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, disfiguration);
		RenderSystem.glUniform1(disfigurationUniform, ShaderHelper.FLOAT_BUF);

		// Vert Uniforms
		int grainIntensityUniform = GlStateManager.getUniformLocation(shader, "grainIntensity");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, grainIntensity);
		RenderSystem.glUniform1(grainIntensityUniform, ShaderHelper.FLOAT_BUF);
	};

	public static final ShaderCallback defaultCallback = shader -> {
		// Frag Uniforms
		int disfigurationUniform = GlStateManager.getUniformLocation(shader, "disfiguration");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, DEFAULT_DISFIGURATION);
		RenderSystem.glUniform1(disfigurationUniform, ShaderHelper.FLOAT_BUF);

		// Vert Uniforms
		int grainIntensityUniform = GlStateManager.getUniformLocation(shader, "grainIntensity");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, DEFAULT_GRAIN_INTENSITY);
		RenderSystem.glUniform1(grainIntensityUniform, ShaderHelper.FLOAT_BUF);
	};

	public RenderDoppleganger(EntityRenderDispatcher renderManager, EntityRendererRegistry.Context ctx) {
		super(renderManager, new Model(), 0F);
	}

	@Override
	public void render(@Nonnull EntityDoppleganger dopple, float yaw, float partialTicks, MatrixStack ms, VertexConsumerProvider buffers, int light) {
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
	public Identifier getTexture(@Nonnull EntityDoppleganger entity) {
		MinecraftClient mc = MinecraftClient.getInstance();

		if (!(mc.getCameraEntity() instanceof AbstractClientPlayerEntity)) {
			return DefaultSkinHelper.getTexture(entity.getUuid());
		}

		return ((AbstractClientPlayerEntity) mc.getCameraEntity()).getSkinTexture();
	}

	@Override
	protected boolean isVisible(EntityDoppleganger dopple) {
		return true;
	}

	private static class Model extends BipedEntityModel<EntityDoppleganger> {
		private static RenderLayer makeRenderType(Identifier texture) {
			RenderLayer normal = RenderLayer.getEntityTranslucent(texture);
			return ShaderHelper.useShaders()
					? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.DOPPLEGANGER, CALLBACK, normal)
					: normal;
		}

		Model() {
			super(Model::makeRenderType, 0, 0, 64, 64);
		}
	}

}
