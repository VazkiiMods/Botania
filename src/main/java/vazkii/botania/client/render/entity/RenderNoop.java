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
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nonnull;

public class RenderNoop<T extends Entity> extends EntityRenderer<T> {
	public RenderNoop(EntityRenderDispatcher manager, EntityRendererRegistry.Context ctx) {
		super(manager);
	}

	@Override
	public boolean shouldRender(T entity, @Nonnull Frustum clipping, double x, double y, double z) {
		return false;
	}

	@Nonnull
	@Override
	public ResourceLocation getTextureLocation(@Nonnull T entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
