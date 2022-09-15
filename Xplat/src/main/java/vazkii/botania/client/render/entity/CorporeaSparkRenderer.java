/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import vazkii.botania.client.core.handler.MiscellaneousModels;
import vazkii.botania.common.entity.CorporeaSparkEntity;

public class CorporeaSparkRenderer extends BaseSparkRenderer<CorporeaSparkEntity> {

	public CorporeaSparkRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public TextureAtlasSprite getBaseIcon(CorporeaSparkEntity entity) {
		return entity.isCreative() ? MiscellaneousModels.INSTANCE.corporeaWorldIconCreative.sprite() : entity.isMaster() ? MiscellaneousModels.INSTANCE.corporeaWorldIconMaster.sprite() : MiscellaneousModels.INSTANCE.corporeaWorldIcon.sprite();
	}

}
