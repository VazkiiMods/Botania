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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.IManaBurst;

public class LensPiston extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.level.isClientSide
				&& pos.getType() == HitResult.Type.BLOCK
				&& !burst.isFake()
				&& !isManaBlock) {
			BlockHitResult rtr = (BlockHitResult) pos;
			BlockPos pos_ = rtr.getBlockPos().relative(rtr.getDirection().getOpposite());

			if (entity.level.isEmptyBlock(pos_) || entity.level.getBlockState(pos_).getMaterial().isReplaceable()) {
				BlockState state = entity.level.getBlockState(rtr.getBlockPos());
				BlockEntity tile = entity.level.getBlockEntity(rtr.getBlockPos());

				if (state.getPistonPushReaction() == PushReaction.NORMAL && !state.is(Blocks.OBSIDIAN)
						&& state.getDestroySpeed(entity.level, pos_) >= 0 && tile == null) {
					entity.level.levelEvent(2001, rtr.getBlockPos(), Block.getId(state));
					entity.level.setBlockAndUpdate(rtr.getBlockPos(), Blocks.AIR.defaultBlockState());
					entity.level.setBlockAndUpdate(pos_, unWaterlog(state));
				}
			}
		}

		return shouldKill;
	}

	public static BlockState unWaterlog(BlockState state) {
		if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
			return state.setValue(BlockStateProperties.WATERLOGGED, false);
		} else {
			return state;
		}
	}

}
