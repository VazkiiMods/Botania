/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 12, 2014, 4:07:26 PM (GMT)]
 */
package vazkii.botania.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;

public class RenderDoppleganger extends BipedRenderer<EntityDoppleganger, BipedModel<EntityDoppleganger>> {

	private static final float DEFAULT_GRAIN_INTENSITY = 0.05F;
	private static final float DEFAULT_DISFIGURATION = 0.025F;

	private static float grainIntensity = DEFAULT_GRAIN_INTENSITY;
	private static float disfiguration = DEFAULT_DISFIGURATION;

	private static final ShaderCallback CALLBACK = shader -> {
		// Frag Uniforms
		int disfigurationUniform = GlStateManager.getUniformLocation(shader, "disfiguration");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, disfiguration);
		GlStateManager.uniform1(disfigurationUniform, ShaderHelper.FLOAT_BUF);

		// Vert Uniforms
		int grainIntensityUniform = GlStateManager.getUniformLocation(shader, "grainIntensity");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, grainIntensity);
		GlStateManager.uniform1(grainIntensityUniform, ShaderHelper.FLOAT_BUF);
	};

	public static final ShaderCallback defaultCallback = shader -> {
		// Frag Uniforms
		int disfigurationUniform = GlStateManager.getUniformLocation(shader, "disfiguration");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, DEFAULT_DISFIGURATION);
		GlStateManager.uniform1(disfigurationUniform, ShaderHelper.FLOAT_BUF);

		// Vert Uniforms
		int grainIntensityUniform = GlStateManager.getUniformLocation(shader, "grainIntensity");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, DEFAULT_GRAIN_INTENSITY);
		GlStateManager.uniform1(grainIntensityUniform, ShaderHelper.FLOAT_BUF);
	};

	public RenderDoppleganger(EntityRendererManager renderManager) {
		super(renderManager, new Model(), 0F);
	}

	@Override
	public void render(@Nonnull EntityDoppleganger dopple, float yaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		int invulTime = dopple.getInvulTime();
		if(invulTime > 0) {
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
	public ResourceLocation getEntityTexture(@Nonnull EntityDoppleganger entity) {
		Minecraft mc = Minecraft.getInstance();

		if(!(mc.getRenderViewEntity() instanceof AbstractClientPlayerEntity))
			return DefaultPlayerSkin.getDefaultSkin(entity.getUniqueID());

		return ((AbstractClientPlayerEntity) mc.getRenderViewEntity()).getLocationSkin();
	}

	@Override
	protected boolean func_225622_a_(EntityDoppleganger dopple) {
		return true;
	}

	private static class Model extends BipedModel<EntityDoppleganger> {
		private static RenderType makeRenderType(ResourceLocation texture) {
			RenderType normal = RenderType.getEntityTranslucent(texture);
			return new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.DOPPLEGANGER, CALLBACK, normal);
		}

		Model() {
			super(Model::makeRenderType, 0, 0, 64, 64);
		}
	}

}
