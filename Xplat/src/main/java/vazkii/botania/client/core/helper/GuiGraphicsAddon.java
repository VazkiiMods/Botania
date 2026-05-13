package vazkii.botania.client.core.helper;

import net.minecraft.resources.ResourceLocation;

public interface GuiGraphicsAddon {
	void botania_blitWithoutShader(ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset,
			int width, int height, int textureWidth, int textureHeight);
}
