/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;

import javax.annotation.Nonnull;

public class ItemWaterRod extends Item {

	public static final int COST = 75;

	public ItemWaterRod(Properties props) {
		super(props);
	}

	// [VanillaCopy] BucketItem, placement case
	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand interactionHand) {
		ItemStack itemStack = player.getItemInHand(interactionHand);
		BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
		if (blockHitResult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(itemStack);
		} else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(itemStack);
		} else {
			BlockPos blockPos = blockHitResult.getBlockPos();
			Direction direction = blockHitResult.getDirection();
			BlockPos blockPos2 = blockPos.relative(direction);
			if (level.mayInteract(player, blockPos) && player.mayUseItemAt(blockPos2, direction, itemStack)) {
				BlockState blockState;
				blockState = level.getBlockState(blockPos);
				BlockPos blockPos3 = blockState.getBlock() instanceof LiquidBlockContainer ? blockPos : blockPos2;
				// Botania - consume mana
				boolean success =
						ManaItemHandler.instance().requestManaExactForTool(itemStack, player, COST, true)
								&& ((BucketItem) Items.WATER_BUCKET).emptyContents(player, level, blockPos3, blockHitResult);
				if (success) {
					// No extra content for water buckets - this.checkExtraContent(player, level, itemStack, blockPos3);
					if (player instanceof ServerPlayer) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockPos3, itemStack);
					}

					player.awardStat(Stats.ITEM_USED.get(this));
					// Botania - particles
					SparkleParticleData data = SparkleParticleData.sparkle(1F, 0.2F, 0.2F, 1F, 5);
					for (int i = 0; i < 6; i++) {
						player.level.addParticle(data, blockPos3.getX() + Math.random(), blockPos3.getY() + Math.random(), blockPos3.getZ() + Math.random(), 0, 0, 0);
					}
					return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
				} else {
					return InteractionResultHolder.fail(itemStack);
				}
			} else {
				return InteractionResultHolder.fail(itemStack);
			}
		}
	}

}
