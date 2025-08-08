package vazkii.botania.neoforge.integration.embeddium;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.embeddedt.embeddium.api.render.texture.SpriteUtil;

public class EmbeddiumHelper {

	public static void markSpriteActive(TextureAtlasSprite sprite) {
		SpriteUtil.markSpriteActive(sprite);
	}
}
