/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.WitherRenderer;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.client.lib.LibResources;

import javax.annotation.Nonnull;

public class RenderPinkWither extends WitherRenderer {

	private static final ResourceLocation resource = new ResourceLocation(LibResources.MODEL_PINK_WITHER);

	public RenderPinkWither(EntityRendererManager manager) {
		super(manager);
	}

	@Nonnull
	@Override
	public ResourceLocation getEntityTexture(WitherEntity entity) {
		return resource;
	}

}
