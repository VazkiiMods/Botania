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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.ManaFlameBlockEntity;

public class FlashLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Entity entity = burst.entity();
		if (!entity.level().isClientSide() && pos.getType() == HitResult.Type.BLOCK && !burst.isFake() && !isManaBlock) {
			BlockHitResult rtr = (BlockHitResult) pos;
			BlockPos blockPos = rtr.getBlockPos();
			BlockPos neighborPos = blockPos.relative(rtr.getDirection());

			BlockState stateAt = entity.level().getBlockState(blockPos);
			BlockState neighbor = entity.level().getBlockState(neighborPos);

			if (stateAt.is(BotaniaBlocks.MANA_FLAME) && entity.mayInteract(entity.level(), blockPos)) {
				entity.level().destroyBlock(blockPos, false, entity);
			} else if ((neighbor.isAir() || neighbor.canBeReplaced())
					&& entity.mayInteract(entity.level(), neighborPos)) {
				var fluid = entity.level().getFluidState(neighborPos);
				var water = fluid.isSource() && fluid.is(FluidTags.WATER);
				entity.level().setBlockAndUpdate(neighborPos,
						BotaniaBlocks.MANA_FLAME.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, water));

				if (entity.level().getBlockEntity(neighborPos) instanceof ManaFlameBlockEntity manaFlame) {
					manaFlame.setColor(burst.getColor());
				}
			}
		}

		return shouldKill;
	}

}
