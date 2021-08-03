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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.entity.EntityCorporeaSpark;

public class RenderCorporeaSpark extends RenderSparkBase<EntityCorporeaSpark> {

	public RenderCorporeaSpark(EntityRenderDispatcher manager, EntityRendererRegistry.Context ctx) {
		super(manager);
	}

	@Override
	public TextureAtlasSprite getBaseIcon(EntityCorporeaSpark entity) {
		return entity.isMaster() ? MiscellaneousIcons.INSTANCE.corporeaWorldIconMaster.sprite() : MiscellaneousIcons.INSTANCE.corporeaWorldIcon.sprite();
	}

}
