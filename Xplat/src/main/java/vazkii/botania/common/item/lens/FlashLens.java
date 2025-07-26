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
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.ManaFlameBlockEntity;
import vazkii.botania.xplat.XplatAbstractions;

public class FlashLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.level().isClientSide && pos.getType() == HitResult.Type.BLOCK && !burst.isFake() && !isManaBlock) {
			BlockHitResult rtr = (BlockHitResult) pos;
			BlockPos neighborPos = rtr.getBlockPos().relative(rtr.getDirection());
			Level level = entity.level();

			BlockState stateAt = level.getBlockState(rtr.getBlockPos());
			BlockState neighbor = level.getBlockState(neighborPos);

			// Get player for event firing
			var player = XplatAbstractions.INSTANCE.getPlayer(level, burst.getShooterUUID(), getClass().getName());

			if (stateAt.is(BotaniaBlocks.manaFlame)) {
				// Fire event before mana flame removal
				if (XplatAbstractions.INSTANCE.flashLensManaFlameRemoveEvent(player, rtr.getBlockPos())) {
					return shouldKill; // Event cancelled, don't remove flame
				}
				level.removeBlock(rtr.getBlockPos(), false);
			} else if (neighbor.isAir() || neighbor.canBeReplaced()) {
				// Fire event before mana flame creation
				int color = burst.getColor();
				if (XplatAbstractions.INSTANCE.flashLensManaFlameCreateEvent(player, neighborPos, color)) {
					return shouldKill; // Event cancelled, don't create flame
				}

				var fluid = level.getFluidState(neighborPos);
				var water = fluid.isSource() && fluid.is(FluidTags.WATER);
				level.setBlockAndUpdate(neighborPos,
						BotaniaBlocks.manaFlame.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, water));

				if (level.getBlockEntity(neighborPos) instanceof ManaFlameBlockEntity manaFlame) {
					manaFlame.setColor(color);
				}
			}
		}

		return shouldKill;
	}

}
