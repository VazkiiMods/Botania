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
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.MushroomBlock;
import net.minecraft.tileentity.TileEntity;
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
			TileEntity tile = world.getTileEntity(pos);
			return !(tile instanceof TileEntitySpecialFlower);
		}
		return false;

	}

	private static boolean isValidPlant(Block block) {
		if (block instanceof FlowerPotBlock) {
			block = ((FlowerPotBlock) block).getFlower();
		}
		return block instanceof FlowerBlock || block instanceof MushroomBlock || block instanceof DoublePlantBlock;
	}

}
