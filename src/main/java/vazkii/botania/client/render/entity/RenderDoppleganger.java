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

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.common.entity.EntityDoppleganger;

import javax.annotation.Nonnull;

public class RenderDoppleganger extends BipedRenderer<EntityDoppleganger, BipedModel<EntityDoppleganger>> {

	private static final float DEFAULT_GRAIN_INTENSITY = 0.05F;
	private static final float DEFAULT_DISFIGURATION = 0.025F;

	private static float grainIntensity = DEFAULT_GRAIN_INTENSITY;
	private static float disfiguration = DEFAULT_DISFIGURATION;

	public static final ShaderCallback callback = shader -> {
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
		super(renderManager, new PlayerModel<>(0.0F, false), 0F);
	}

	@Override
	public void doRender(@Nonnull EntityDoppleganger dopple, double par2, double par4, double par6, float par8, float par9) {
		int invulTime = dopple.getInvulTime();
		if(invulTime > 0) {
			grainIntensity = invulTime > 20 ? 1F : invulTime * 0.05F;
			disfiguration = grainIntensity * 0.3F;
		} else {
			disfiguration = (0.025F + dopple.hurtTime * ((1F - 0.15F) / 20F)) / 2F;
			grainIntensity = 0.05F + dopple.hurtTime * ((1F - 0.15F) / 10F);
		}

		ShaderHelper.useShader(ShaderHelper.doppleganger, callback);
		super.doRender(dopple, par2, par4, par6, par8, par9);
		ShaderHelper.releaseShader();
	}

	@Nonnull
	@Override
	public ResourceLocation getEntityTexture(@Nonnull EntityDoppleganger entity) {
		Minecraft mc = Minecraft.getInstance();

		if(!(mc.getRenderViewEntity() instanceof AbstractClientPlayerEntity))
			return DefaultPlayerSkin.getDefaultSkin(entity.getUniqueID());

		return ((AbstractClientPlayerEntity) mc.getRenderViewEntity()).getLocationSkin();
	}

}
