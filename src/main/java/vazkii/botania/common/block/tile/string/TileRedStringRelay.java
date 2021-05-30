/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.*;
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
		if (isValidPlant(block)) {
			BlockEntity tile = world.getBlockEntity(pos);
			return !(tile instanceof TileEntitySpecialFlower);
		}
		return false;

	}

	private static boolean isValidPlant(Block block) {
		if (block instanceof FlowerPotBlock) {
			block = ((FlowerPotBlock) block).getContent();
		}
		return block instanceof FlowerBlock || block instanceof MushroomBlock || block instanceof TallPlantBlock;
	}

}
