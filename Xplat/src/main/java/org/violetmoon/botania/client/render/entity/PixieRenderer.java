/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import org.violetmoon.botania.client.core.proxy.ClientProxy;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.client.model.BotaniaModelLayers;
import org.violetmoon.botania.client.model.PixieModel;
import org.violetmoon.botania.common.entity.PixieEntity;

public class PixieRenderer extends MobRenderer<PixieEntity, PixieModel> {

	public PixieRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new PixieModel(ctx.bakeLayer(BotaniaModelLayers.PIXIE)), 0.0F);
	}

	@Override
	public ResourceLocation getTextureLocation(PixieEntity entity) {
		return ClientProxy.dootDoot
				? ResourceLocation.parse(ResourcesLib.MODEL_PIXIE_HALLOWEEN)
				: ResourceLocation.parse(ResourcesLib.MODEL_PIXIE);
	}
}
