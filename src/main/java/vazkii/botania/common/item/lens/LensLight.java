/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileManaFlame;

public class LensLight extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		Entity entity = burst.entity();
		BlockPos coords = burst.getBurstSourceBlockPos();
		if (!entity.world.isRemote && pos.getType() == RayTraceResult.Type.BLOCK && !burst.isFake() && !isManaBlock) {
			BlockRayTraceResult rtr = (BlockRayTraceResult) pos;
			if (!coords.equals(rtr.getPos())) {
				BlockPos neighborPos = rtr.getPos().offset(rtr.getFace());

				Block blockAt = entity.world.getBlockState(rtr.getPos()).getBlock();
				BlockState neighbor = entity.world.getBlockState(neighborPos);

				if (blockAt == ModBlocks.manaFlame) {
					entity.world.removeBlock(rtr.getPos(), false);
				} else if (neighbor.isAir(entity.world, neighborPos) || neighbor.getMaterial().isReplaceable()) {
					entity.world.setBlockState(neighborPos, ModBlocks.manaFlame.getDefaultState());
					TileEntity tile = entity.world.getTileEntity(neighborPos);

					if (tile instanceof TileManaFlame) {
						((TileManaFlame) tile).setColor(burst.getColor());
					}
				}
			}
		}

		return dead;
	}

}
