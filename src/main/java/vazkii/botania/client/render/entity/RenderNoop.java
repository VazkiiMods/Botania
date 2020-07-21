/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import javax.annotation.Nonnull;

public class RenderNoop<T extends Entity> extends EntityRenderer<T> {
	public RenderNoop(EntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public boolean shouldRender(T entity, @Nonnull Frustum clipping, double x, double y, double z) {
		return false;
	}

	@Nonnull
	@Override
	public Identifier getTexture(@Nonnull T entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEX;
	}
}
