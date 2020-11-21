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
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelPixie;
import vazkii.botania.common.entity.EntityPixie;

import javax.annotation.Nonnull;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class RenderPixie extends MobEntityRenderer<EntityPixie, ModelPixie> {

	public static final ShaderCallback SHADER_CALLBACK = shader -> {
		// Frag Uniforms
		int disfigurationUniform = GlStateManager.getUniformLocation(shader, "disfiguration");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, 0.025F);
		RenderSystem.glUniform1(disfigurationUniform, ShaderHelper.FLOAT_BUF);

		// Vert Uniforms
		int grainIntensityUniform = GlStateManager.getUniformLocation(shader, "grainIntensity");
		ShaderHelper.FLOAT_BUF.position(0);
		ShaderHelper.FLOAT_BUF.put(0, 0.05F);
		RenderSystem.glUniform1(grainIntensityUniform, ShaderHelper.FLOAT_BUF);
	};

	public RenderPixie(EntityRenderDispatcher renderManager, EntityRendererRegistry.Context ctx) {
		super(renderManager, new ModelPixie(), 0.0F);
	}

	@Nonnull
	@Override
	public Identifier getTexture(@Nonnull EntityPixie entity) {
		return ClientProxy.dootDoot
			? new Identifier(LibResources.MODEL_PIXIE_HALLOWEEN)
			: new Identifier(LibResources.MODEL_PIXIE);
	}
}
