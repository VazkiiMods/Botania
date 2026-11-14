/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import vazkii.botania.api.capability.BlockApiNoContext;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * A block with this client capability can provide a sprite
 * to be used as an overlay for the mana pool, similarly to the mana void
 * and catalysts.
 */
public interface PoolOverlayProvider {
	ResourceLocation ID = botaniaRL("pool_overlay");
	BlockApiNoContext<PoolOverlayProvider> LOOKUP = new BlockApiNoContext<>(ID, PoolOverlayProvider.class);

	/**
	 * @return A sprite to render. Must be stitched to the main block/item atlas.
	 */
	ResourceLocation getIcon(Level level, BlockPos pos);

	/**
	 * Defines a PoolOverlayProvider for a static icon.
	 */
	static PoolOverlayProvider simple(ResourceLocation icon) {
		return (level, pos) -> icon;
	}
}
