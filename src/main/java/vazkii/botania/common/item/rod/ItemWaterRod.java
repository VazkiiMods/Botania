package vazkii.botania.common.item.rod;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ItemMod;

import javax.annotation.Nonnull;

public class ItemWaterRod extends ItemMod implements IManaUsingItem {

	public static final int COST = 75;

	public ItemWaterRod(Properties props) {
		super(props);
	}

	// [VanillaCopy] From ItemBucket
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, false);

		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
		if (ret != null) return ret;

		if (raytraceresult == null) {
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		} else if (raytraceresult.type == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = raytraceresult.getBlockPos();
			if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemstack)) {
				BlockState iblockstate = worldIn.getBlockState(blockpos);
				BlockPos blockpos1 = this.getPlacementPosition(iblockstate, blockpos, raytraceresult);

				if (ManaItemHandler.requestManaExactForTool(itemstack, playerIn, COST, true)
						&& ((BucketItem) Items.WATER_BUCKET).tryPlaceContainedLiquid(playerIn, worldIn, blockpos1, raytraceresult)) {
					if (playerIn instanceof ServerPlayerEntity) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerIn, blockpos1, itemstack);
					}

					playerIn.addStat(Stats.ITEM_USED.get(this));
					for(int i = 0; i < 6; i++)
						Botania.proxy.sparkleFX(blockpos1.getX() + Math.random(), blockpos1.getY() + Math.random(), blockpos1.getZ() + Math.random(), 0.2F, 0.2F, 1F, 1F, 5);
					return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
				} else {
					return new ActionResult<>(ActionResultType.FAIL, itemstack);
				}
			} else {
				return new ActionResult<>(ActionResultType.FAIL, itemstack);
			}
		} else {
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		}
	}

	// [VanillaCopy] ItemBucket
	private BlockPos getPlacementPosition(BlockState p_210768_1_, BlockPos p_210768_2_, RayTraceResult p_210768_3_) {
		return p_210768_1_.getBlock() instanceof ILiquidContainer ? p_210768_2_ : p_210768_3_.getBlockPos().offset(p_210768_3_.sideHit);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
