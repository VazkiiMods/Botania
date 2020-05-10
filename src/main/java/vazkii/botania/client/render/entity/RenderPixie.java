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

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.client.core.helper.ShaderCallback;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelPixie;
import vazkii.botania.common.entity.EntityPixie;

import javax.annotation.Nonnull;

public class RenderPixie extends MobRenderer<EntityPixie, ModelPixie> {

	public static final ShaderCallback SHADER_CALLBACK = shader -> {
		// Frag Uniforms
		int disfigurationUniform = GlStateManager.getUniformLocation(shader, "disfiguration");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, 0.025F);
		GlStateManager.uniform1f(disfigurationUniform, ShaderHelper.FLOAT_BUF);

		// Vert Uniforms
		int grainIntensityUniform = GlStateManager.getUniformLocation(shader, "grainIntensity");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, 0.05F);
		GlStateManager.uniform1f(grainIntensityUniform, ShaderHelper.FLOAT_BUF);
	};

	public RenderPixie(EntityRendererManager renderManager) {
		super(renderManager, new ModelPixie(), 0.0F);
	}

	@Nonnull
	@Override
	public ResourceLocation getEntityTexture(@Nonnull EntityPixie entity) {
		return new ResourceLocation(LibResources.MODEL_PIXIE);
	}
}
