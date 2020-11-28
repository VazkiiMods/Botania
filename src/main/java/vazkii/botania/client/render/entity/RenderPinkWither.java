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
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.WitherEntityRenderer;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.util.Identifier;

import vazkii.botania.client.lib.LibResources;

import javax.annotation.Nonnull;

public class RenderPinkWither extends WitherEntityRenderer {

	private static final Identifier resource = new Identifier(LibResources.MODEL_PINK_WITHER);

	public RenderPinkWither(EntityRenderDispatcher manager, EntityRendererRegistry.Context ctx) {
		super(manager);
	}

	@Nonnull
	@Override
	public Identifier getTexture(WitherEntity entity) {
		return resource;
	}

}
