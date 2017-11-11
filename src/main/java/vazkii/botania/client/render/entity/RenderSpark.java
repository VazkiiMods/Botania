/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 14, 2015, 1:03:11 AM (GMT)]
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.entity.EntitySpark;

public class RenderSpark extends RenderSparkBase<EntitySpark> {

	public RenderSpark(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public TextureAtlasSprite getSpinningIcon(EntitySpark entity) {
		int upgrade = entity.getUpgrade().ordinal() - 1;
		return upgrade >= 0 && upgrade < MiscellaneousIcons.INSTANCE.sparkUpgradeIcons.length ? MiscellaneousIcons.INSTANCE.sparkUpgradeIcons[upgrade] : null;
	}

}
