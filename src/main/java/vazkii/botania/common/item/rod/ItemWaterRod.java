/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;

import javax.annotation.Nonnull;

public class ItemWaterRod extends Item implements IManaUsingItem {

	public static final int COST = 75;

	public ItemWaterRod(Settings props) {
		super(props);
	}

	// [VanillaCopy] From BucketItem
	@Nonnull
	@Override
	public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
		ItemStack itemstack = playerIn.getStackInHand(handIn);
		HitResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidHandling.NONE);
		if (raytraceresult.getType() == HitResult.Type.MISS) {
			return new TypedActionResult<>(ActionResult.PASS, itemstack);
		} else if (raytraceresult.getType() != HitResult.Type.BLOCK) {
			return new TypedActionResult<>(ActionResult.PASS, itemstack);
		} else {
			BlockHitResult blockraytraceresult = (BlockHitResult) raytraceresult;
			BlockPos blockpos = blockraytraceresult.getBlockPos();
			if (worldIn.canPlayerModifyAt(playerIn, blockpos) && playerIn.canPlaceOn(blockpos, blockraytraceresult.getSide(), itemstack)) {
				BlockState blockstate = worldIn.getBlockState(blockpos);
				BlockPos blockpos1 = blockstate.getBlock() instanceof FluidFillable ? blockpos : blockraytraceresult.getBlockPos().offset(blockraytraceresult.getSide());
				if (ManaItemHandler.instance().requestManaExactForTool(itemstack, playerIn, COST, true)
						&& ((BucketItem) Items.WATER_BUCKET).placeFluid(playerIn, worldIn, blockpos1, blockraytraceresult)) {
					if (playerIn instanceof ServerPlayerEntity) {
						Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerIn, blockpos1, itemstack);
					}

					playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
					SparkleParticleData data = SparkleParticleData.sparkle(1F, 0.2F, 0.2F, 1F, 5);
					for (int i = 0; i < 6; i++) {
						playerIn.world.addParticle(data, blockpos1.getX() + Math.random(), blockpos1.getY() + Math.random(), blockpos1.getZ() + Math.random(), 0, 0, 0);
					}
					return new TypedActionResult<>(ActionResult.SUCCESS, itemstack);
				} else {
					return new TypedActionResult<>(ActionResult.FAIL, itemstack);
				}
			} else {
				return new TypedActionResult<>(ActionResult.FAIL, itemstack);
			}
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
