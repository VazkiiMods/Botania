/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.core.helper;

import net.minecraft.resources.ResourceLocation;

public interface GuiGraphicsAddon {
	void botania_blitWithoutShader(ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset,
			int width, int height, int textureWidth, int textureHeight);
}
