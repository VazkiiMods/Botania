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
import vazkii.botania.common.entity.ManaSparkEntity;

public class ManaSparkRenderer extends BaseSparkRenderer<ManaSparkEntity> {

	public ManaSparkRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public TextureAtlasSprite getSpinningIcon(ManaSparkEntity entity) {
		int upgrade = entity.getUpgrade().ordinal() - 1;
		return upgrade >= 0 && upgrade < MiscellaneousModels.INSTANCE.sparkUpgradeIcons.length
				? MiscellaneousModels.INSTANCE.sparkUpgradeIcons[upgrade].sprite()
				: null;
	}

}
