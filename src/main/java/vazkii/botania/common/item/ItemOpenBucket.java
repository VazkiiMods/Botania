/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemOpenBucket extends Item {

	public ItemOpenBucket(Settings props) {
		super(props);
	}

	// [VanillaCopy] BucketItem, only the empty cases
	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
		ItemStack itemstack = playerIn.getStackInHand(handIn);
		HitResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidHandling.SOURCE_ONLY);
		if (raytraceresult.getType() == HitResult.Type.MISS) {
			return new TypedActionResult<>(ActionResult.PASS, itemstack);
		} else if (raytraceresult.getType() != HitResult.Type.BLOCK) {
			return new TypedActionResult<>(ActionResult.PASS, itemstack);
		} else {
			BlockHitResult blockraytraceresult = (BlockHitResult) raytraceresult;
			BlockPos blockpos = blockraytraceresult.getBlockPos();
			if (worldIn.canPlayerModifyAt(playerIn, blockpos) && playerIn.canPlaceOn(blockpos, blockraytraceresult.getSide(), itemstack)) {
				BlockState blockstate1 = worldIn.getBlockState(blockpos);
				if (blockstate1.getBlock() instanceof FluidDrainable) {
					Fluid fluid = ((FluidDrainable) blockstate1.getBlock()).tryDrainFluid(worldIn, blockpos, blockstate1);
					if (fluid != Fluids.EMPTY) {
						playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
						playerIn.playSound(fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
						// Botania: some particles
						for (int x = 0; x < 5; x++) {
							worldIn.addParticle(ParticleTypes.POOF, blockpos.getX() + Math.random(), blockpos.getY() + Math.random(), blockpos.getZ() + Math.random(), 0, 0, 0);
						}

						ItemStack itemstack1 = itemstack; // this.fillBucket(itemstack, playerIn, fluid.getFilledBucket());
						if (!worldIn.isClient) {
							Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity) playerIn, new ItemStack(fluid.getBucketItem()));
						}

						return new TypedActionResult<>(ActionResult.SUCCESS, itemstack1);
					}
				}

				return new TypedActionResult<>(ActionResult.FAIL, itemstack);
			} else {
				return new TypedActionResult<>(ActionResult.FAIL, itemstack);
			}
		}
	}

}
