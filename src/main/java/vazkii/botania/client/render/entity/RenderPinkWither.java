/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.WitherBossRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.wither.WitherBoss;

import vazkii.botania.client.lib.LibResources;

import javax.annotation.Nonnull;

public class RenderPinkWither extends WitherBossRenderer {

	private static final ResourceLocation resource = new ResourceLocation(LibResources.MODEL_PINK_WITHER);

	public RenderPinkWither(EntityRenderDispatcher manager, EntityRendererRegistry.Context ctx) {
		super(manager);
	}

	@Nonnull
	@Override
	public ResourceLocation getTextureLocation(WitherBoss entity) {
		return resource;
	}

}
