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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.helper.ForcePushHelper;
import vazkii.botania.mixin.PistonBaseBlockAccessor;

public class ForceLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		if (pos.getType() == HitResult.Type.BLOCK
				&& !burst.isFake()
				&& !isManaBlock) {
			BlockHitResult rtr = (BlockHitResult) pos;
			moveBlocks(entity.level, rtr.getBlockPos().relative(rtr.getDirection()), rtr.getDirection().getOpposite());
		}

		return shouldKill;
	}

	@SuppressWarnings("try")
	public static boolean moveBlocks(Level level, BlockPos pistonPos, Direction direction) {
		try (var ignored = new ForcePushHelper()) {
			return ((PistonBaseBlockAccessor) Blocks.PISTON).botania_moveBlocks(level, pistonPos, direction, true);
		}
	}

}
