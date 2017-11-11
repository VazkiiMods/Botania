/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 25, 2015, 5:55:59 PM (GMT)]
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.lib.LibResources;

import javax.annotation.Nonnull;

public class RenderPinkWither extends RenderWither {

	private static final ResourceLocation resource = new ResourceLocation(LibResources.MODEL_PINK_WITHER);

	public RenderPinkWither(RenderManager manager) {
		super(manager);
	}

	@Nonnull
	@Override
	protected ResourceLocation getEntityTexture(EntityWither entity) {
		return resource;
	}

}
