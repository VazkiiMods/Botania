/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SkullBlockEntity;

import javax.annotation.Nonnull;

public class TileGaiaHead extends SkullBlockEntity {
	@Nonnull
	@Override
	public BlockEntityType<TileGaiaHead> getType() {
		return ModTiles.GAIA_HEAD;
	}

}
