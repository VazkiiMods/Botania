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
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.internal.IManaBurst;

public class LensPiston extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrownEntity entity, HitResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		BlockPos coords = burst.getBurstSourceBlockPos();
		if (!entity.world.isClient
				&& pos.getType() == HitResult.Type.BLOCK
				&& !burst.isFake()
				&& !isManaBlock) {
			BlockHitResult rtr = (BlockHitResult) pos;
			if (!coords.equals(rtr.getBlockPos())) {
				BlockPos pos_ = rtr.getBlockPos().offset(rtr.getSide().getOpposite());

				if (entity.world.isAir(pos_) || entity.world.getBlockState(pos_).getMaterial().isReplaceable()) {
					BlockState state = entity.world.getBlockState(rtr.getBlockPos());
					BlockEntity tile = entity.world.getBlockEntity(rtr.getBlockPos());

					if (state.getPistonBehavior() == PistonBehavior.NORMAL && state.getBlock() != Blocks.OBSIDIAN
							&& state.getHardness(entity.world, pos_) >= 0 && tile == null) {
						entity.world.syncWorldEvent(2001, rtr.getBlockPos(), Block.getRawIdFromState(state));
						entity.world.setBlockState(rtr.getBlockPos(), Blocks.AIR.getDefaultState());
						entity.world.setBlockState(pos_, unWaterlog(state));
					}
				}
			}
		}

		return dead;
	}

	public static BlockState unWaterlog(BlockState state) {
		if (state.contains(Properties.WATERLOGGED)) {
			return state.with(Properties.WATERLOGGED, false);
		} else {
			return state;
		}
	}

}
