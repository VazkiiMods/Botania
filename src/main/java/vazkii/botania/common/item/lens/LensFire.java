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
import net.minecraft.block.Blocks;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileIncensePlate;

public class LensFire extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrownEntity entity, HitResult rtr, boolean isManaBlock, boolean dead, ItemStack stack) {
		BlockPos coords = burst.getBurstSourceBlockPos();

		if (!entity.world.isClient && rtr.getType() == HitResult.Type.BLOCK
				&& !burst.isFake() && !isManaBlock) {
			BlockHitResult brtr = (BlockHitResult) rtr;
			BlockPos pos = brtr.getBlockPos();
			if (!coords.equals(pos)) {
				Direction dir = brtr.getSide();

				BlockPos offPos = pos.offset(dir);

				Block blockAt = entity.world.getBlockState(pos).getBlock();
				Block blockAtOffset = entity.world.getBlockState(offPos).getBlock();

				if (blockAt == Blocks.NETHER_PORTAL) {
					entity.world.setBlockState(pos, Blocks.AIR.getDefaultState());
				}
				if (blockAtOffset == Blocks.NETHER_PORTAL) {
					entity.world.setBlockState(offPos, Blocks.AIR.getDefaultState());
				} else if (blockAt == ModBlocks.incensePlate) {
					TileIncensePlate plate = (TileIncensePlate) entity.world.getBlockEntity(pos);
					plate.ignite();
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(plate);
				} else if (blockAtOffset.isAir(entity.world.getBlockState(offPos), entity.world, offPos)) {
					entity.world.setBlockState(offPos, Blocks.FIRE.getDefaultState());
				}
			}
		}

		return dead;
	}

}
