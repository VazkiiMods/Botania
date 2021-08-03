/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;

public class ItemOpenBucket extends Item {

	public ItemOpenBucket(Properties props) {
		super(props);
	}

	// [VanillaCopy] BucketItem, only the empty cases
	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		HitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
		if (raytraceresult.getType() == HitResult.Type.MISS) {
			return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
		} else if (raytraceresult.getType() != HitResult.Type.BLOCK) {
			return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
		} else {
			BlockHitResult blockraytraceresult = (BlockHitResult) raytraceresult;
			BlockPos blockpos = blockraytraceresult.getBlockPos();
			if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos, blockraytraceresult.getDirection(), itemstack)) {
				BlockState blockstate1 = worldIn.getBlockState(blockpos);
				if (blockstate1.getBlock() instanceof BucketPickup) {
					Fluid fluid = ((BucketPickup) blockstate1.getBlock()).takeLiquid(worldIn, blockpos, blockstate1);
					if (fluid != Fluids.EMPTY) {
						playerIn.awardStat(Stats.ITEM_USED.get(this));
						playerIn.playSound(fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL, 1.0F, 1.0F);
						// Botania: some particles
						for (int x = 0; x < 5; x++) {
							worldIn.addParticle(ParticleTypes.POOF, blockpos.getX() + Math.random(), blockpos.getY() + Math.random(), blockpos.getZ() + Math.random(), 0, 0, 0);
						}

						ItemStack itemstack1 = itemstack; // this.fillBucket(itemstack, playerIn, fluid.getFilledBucket());
						if (!worldIn.isClientSide) {
							CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) playerIn, new ItemStack(fluid.getBucket()));
						}

						return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack1);
					}
				}

				return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
			} else {
				return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
			}
		}
	}

}
