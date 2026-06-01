/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.client.integration.embeddium;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.embeddedt.embeddium.api.render.texture.SpriteUtil;

public class EmbeddiumHelper {

	public static void markSpriteActive(TextureAtlasSprite sprite) {
		SpriteUtil.markSpriteActive(sprite);
	}
}
