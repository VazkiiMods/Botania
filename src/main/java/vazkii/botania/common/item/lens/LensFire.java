/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileIncensePlate;

public class LensFire extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult rtr, boolean isManaBlock, boolean dead, ItemStack stack) {
		Entity entity = burst.entity();

		if (!entity.level.isClientSide && rtr.getType() == HitResult.Type.BLOCK
				&& !burst.isFake() && !isManaBlock) {
			BlockHitResult brtr = (BlockHitResult) rtr;
			BlockPos pos = brtr.getBlockPos();
			Direction dir = brtr.getDirection();

			BlockPos offPos = pos.relative(dir);

			BlockState stateAt = entity.level.getBlockState(pos);
			BlockState stateAtOffset = entity.level.getBlockState(offPos);

			if (stateAt.is(Blocks.NETHER_PORTAL)) {
				entity.level.removeBlock(pos, false);
			}
			if (stateAtOffset.is(Blocks.NETHER_PORTAL)) {
				entity.level.removeBlock(offPos, false);
			} else if (stateAt.is(ModBlocks.incensePlate)) {
				TileIncensePlate plate = (TileIncensePlate) entity.level.getBlockEntity(pos);
				plate.ignite();
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(plate);
			} else if (stateAtOffset.isAir()) {
				entity.level.setBlockAndUpdate(offPos, Blocks.FIRE.defaultBlockState());
			}
		}

		return dead;
	}

}
