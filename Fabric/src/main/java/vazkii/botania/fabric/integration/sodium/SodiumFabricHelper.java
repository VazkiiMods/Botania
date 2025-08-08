package vazkii.botania.fabric.integration.sodium;

import net.caffeinemc.mods.sodium.api.texture.SpriteUtil;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class SodiumFabricHelper {
	@SuppressWarnings("UnstableApiUsage")
	public static void markSpriteActive(TextureAtlasSprite sprite) {
		SpriteUtil.INSTANCE.markSpriteActive(sprite);
	}
}
