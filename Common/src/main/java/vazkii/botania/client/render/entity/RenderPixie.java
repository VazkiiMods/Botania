/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import vazkii.botania.client.core.helper.CoreShaders;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModModelLayers;
import vazkii.botania.client.model.ModelPixie;
import vazkii.botania.common.entity.EntityPixie;

import javax.annotation.Nonnull;

public class RenderPixie extends MobRenderer<EntityPixie, ModelPixie> {

	public RenderPixie(EntityRendererProvider.Context ctx) {
		super(ctx, new ModelPixie(ctx.bakeLayer(ModModelLayers.PIXIE)), 0.0F);
	}

	@Override
	public void render(EntityPixie mob, float yaw, float partialTicks, PoseStack pos, MultiBufferSource buffers, int light) {
		ShaderInstance shader = CoreShaders.doppleganger();
		if (shader != null) {
			shader.safeGetUniform("BotaniaDisfiguration").set(RenderDoppleganger.DEFAULT_DISFIGURATION);
			shader.safeGetUniform("BotaniaGrainIntensity").set(RenderDoppleganger.DEFAULT_GRAIN_INTENSITY);
		}
		super.render(mob, yaw, partialTicks, pos, buffers, light);
	}

	@Nonnull
	@Override
	public ResourceLocation getTextureLocation(@Nonnull EntityPixie entity) {
		return ClientProxy.dootDoot
				? new ResourceLocation(LibResources.MODEL_PIXIE_HALLOWEEN)
				: new ResourceLocation(LibResources.MODEL_PIXIE);
	}
}
