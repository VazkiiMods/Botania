package vazkii.botania.neoforge.client.integration.sodium;

import net.caffeinemc.mods.sodium.api.texture.SpriteUtil;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

// Would love to have this in Xplat, but sodium-common is a Loom project.
public class SodiumNeoforgeHelper {
	@SuppressWarnings("UnstableApiUsage")
	public static void markSpriteActive(TextureAtlasSprite sprite) {
		SpriteUtil.INSTANCE.markSpriteActive(sprite);
	}
}
