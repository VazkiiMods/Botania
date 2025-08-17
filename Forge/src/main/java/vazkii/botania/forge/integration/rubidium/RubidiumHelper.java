package vazkii.botania.forge.integration.rubidium;

import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class RubidiumHelper {

	public static void markSpriteActive(TextureAtlasSprite sprite) {
		// this should work with all Sodium ports
		SpriteUtil.markSpriteActive(sprite);
	}
}
