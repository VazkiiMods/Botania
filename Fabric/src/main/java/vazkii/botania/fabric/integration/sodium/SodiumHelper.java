package vazkii.botania.fabric.integration.sodium;

import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class SodiumHelper {

	public static void markSpriteActive(TextureAtlasSprite sprite) {
		// this should work with both the actual Sodium and the Fabric backport of its Forge port Embeddium
		SpriteUtil.markSpriteActive(sprite);
	}
}
