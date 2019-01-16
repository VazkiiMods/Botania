package vazkii.botania.common.integration.buildcraft;

import buildcraft.api.core.render.ISprite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;

/**
 * Adapts Minecraft TAS to buildcraft ISprite
 */
public class TASprite implements ISprite {
	private final TextureAtlasSprite sprite;

	public TASprite(TextureAtlasSprite sprite) {
		this.sprite = sprite;
	}

	@Override
	public void bindTexture() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	@Override
	public double getInterpU(double u) {
		return sprite.getInterpolatedU(u * 16);
	}

	@Override
	public double getInterpV(double v) {
		return sprite.getInterpolatedV(v * 16);
	}
}
