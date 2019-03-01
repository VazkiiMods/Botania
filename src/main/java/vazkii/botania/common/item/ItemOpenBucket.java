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

import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.init.Fluids;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
		if (ret != null) return ret;

		if (raytraceresult == null) {
			return new ActionResult<>(EnumActionResult.PASS, itemstack);
		} else if (raytraceresult.type == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = raytraceresult.getBlockPos();
			if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemstack)) {
				IBlockState iblockstate1 = worldIn.getBlockState(blockpos);
				if (iblockstate1.getBlock() instanceof IBucketPickupHandler) {
					Fluid fluid = ((IBucketPickupHandler)iblockstate1.getBlock()).pickupFluid(worldIn, blockpos, iblockstate1);
					if (fluid != Fluids.EMPTY) {
						playerIn.addStat(StatList.ITEM_USED.get(this));
						playerIn.playSound(fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);

						// Botania: some particles
						for(int x = 0; x < 5; x++)
							worldIn.addParticle(Particles.POOF, blockpos.getX() + Math.random(), blockpos.getY() + Math.random(), blockpos.getZ() + Math.random(), 0, 0, 0);

						return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
					}
				}

				return new ActionResult<>(EnumActionResult.FAIL, itemstack);
			} else {
				return new ActionResult<>(EnumActionResult.FAIL, itemstack);
			}
		} else {
			return new ActionResult<>(EnumActionResult.PASS, itemstack);
		}
	}



}
