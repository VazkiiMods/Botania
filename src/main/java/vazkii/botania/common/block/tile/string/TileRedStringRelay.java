/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.common.block.tile.ModTiles;

public class TileRedStringRelay extends TileRedString {
	public TileRedStringRelay() {
		super(ModTiles.RED_STRING_RELAY);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		if (pos.equals(getPos().up())) {
			return false;
		}

		Block block = world.getBlockState(pos).getBlock();
		BlockEntity tile = world.getBlockEntity(pos);
		return (block instanceof FlowerBlock || block instanceof MushroomPlantBlock || block instanceof TallPlantBlock) && (tile == null || !(tile instanceof TileEntitySpecialFlower));
	}

}
