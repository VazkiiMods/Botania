/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * A block that implements this can provide a sprite
 * to be used as an overlay for the mana pool, similarly to the mana void
 * and catalysts.
 */
public interface IPoolOverlayProvider {
	/**
	 * @return A sprite to render. Must be stitched to the main block/item atlas.
	 */
	@Environment(EnvType.CLIENT)
	TextureAtlasSprite getIcon(Level world, BlockPos pos);

}
