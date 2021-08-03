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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;

import javax.annotation.Nonnull;

public class ItemWaterRod extends Item implements IManaUsingItem {

	public static final int COST = 75;

	public ItemWaterRod(Properties props) {
		super(props);
	}

	// [VanillaCopy] From BucketItem
	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		HitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.NONE);
		if (raytraceresult.getType() == HitResult.Type.MISS) {
			return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
		} else if (raytraceresult.getType() != HitResult.Type.BLOCK) {
			return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
		} else {
			BlockHitResult blockraytraceresult = (BlockHitResult) raytraceresult;
			BlockPos blockpos = blockraytraceresult.getBlockPos();
			if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos, blockraytraceresult.getDirection(), itemstack)) {
				BlockState blockstate = worldIn.getBlockState(blockpos);
				BlockPos blockpos1 = blockstate.getBlock() instanceof LiquidBlockContainer ? blockpos : blockraytraceresult.getBlockPos().relative(blockraytraceresult.getDirection());
				if (ManaItemHandler.instance().requestManaExactForTool(itemstack, playerIn, COST, true)
						&& ((BucketItem) Items.WATER_BUCKET).emptyBucket(playerIn, worldIn, blockpos1, blockraytraceresult)) {
					if (playerIn instanceof ServerPlayer) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) playerIn, blockpos1, itemstack);
					}

					playerIn.awardStat(Stats.ITEM_USED.get(this));
					SparkleParticleData data = SparkleParticleData.sparkle(1F, 0.2F, 0.2F, 1F, 5);
					for (int i = 0; i < 6; i++) {
						playerIn.level.addParticle(data, blockpos1.getX() + Math.random(), blockpos1.getY() + Math.random(), blockpos1.getZ() + Math.random(), 0, 0, 0);
					}
					return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
				} else {
					return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
				}
			} else {
				return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
			}
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
