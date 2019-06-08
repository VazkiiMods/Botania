/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 20, 2014, 12:12:58 AM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.init.Particles;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemOpenBucket extends ItemMod {

	public ItemOpenBucket(Properties props) {
		super(props);
	}

	// [VanillaCopy] ItemBucket, only the empty cases
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
		if (ret != null) return ret;

		if (raytraceresult == null) {
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		} else if (raytraceresult.type == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = raytraceresult.getBlockPos();
			if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemstack)) {
				BlockState iblockstate1 = worldIn.getBlockState(blockpos);
				if (iblockstate1.getBlock() instanceof IBucketPickupHandler) {
					Fluid fluid = ((IBucketPickupHandler)iblockstate1.getBlock()).pickupFluid(worldIn, blockpos, iblockstate1);
					if (fluid != Fluids.EMPTY) {
						playerIn.addStat(Stats.ITEM_USED.get(this));
						playerIn.playSound(fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);

						// Botania: some particles
						for(int x = 0; x < 5; x++)
							worldIn.addParticle(Particles.POOF, blockpos.getX() + Math.random(), blockpos.getY() + Math.random(), blockpos.getZ() + Math.random(), 0, 0, 0);

						return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
					}
				}

				return new ActionResult<>(ActionResultType.FAIL, itemstack);
			} else {
				return new ActionResult<>(ActionResultType.FAIL, itemstack);
			}
		} else {
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		}
	}



}
