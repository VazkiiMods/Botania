/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A block that implements this can provide a sprite
 * to be used as an overlay for the mana pool, similarly to the mana void
 * and catalysts.
 */
public interface IPoolOverlayProvider {
	/**
	 * @return A sprite to render. Must be stitched to the main block/item atlas.
	 */
	@OnlyIn(Dist.CLIENT)
	TextureAtlasSprite getIcon(World world, BlockPos pos);

}
