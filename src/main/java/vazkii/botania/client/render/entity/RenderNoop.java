/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderNoop<T extends Entity> extends EntityRenderer<T> {
	public RenderNoop(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public boolean shouldRender(T entity, @Nonnull ClippingHelper clipping, double x, double y, double z) {
		return false;
	}

	@Nonnull
	@Override
	public ResourceLocation getEntityTexture(@Nonnull T entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}
}
