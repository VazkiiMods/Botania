package vazkii.botania.common.item.rod;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.item.ItemMod;

import javax.annotation.Nonnull;

public class ItemWaterRod extends ItemMod implements IManaUsingItem {

	public static final int COST = 75;

	public ItemWaterRod(Properties props) {
		super(props);
	}

	// [VanillaCopy] From BucketItem
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.NONE);
		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
		if (ret != null) return ret;
		if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		} else if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		} else {
			BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
			BlockPos blockpos = blockraytraceresult.getPos();
			if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, blockraytraceresult.getFace(), itemstack)) {
				BlockState blockstate = worldIn.getBlockState(blockpos);
				BlockPos blockpos1 = blockstate.getBlock() instanceof ILiquidContainer ? blockpos : blockraytraceresult.getPos().offset(blockraytraceresult.getFace());
				if (ManaItemHandler.requestManaExactForTool(itemstack, playerIn, COST, true)
						&& ((BucketItem) Items.WATER_BUCKET).tryPlaceContainedLiquid(playerIn, worldIn, blockpos1, blockraytraceresult)) {
					if (playerIn instanceof ServerPlayerEntity) {
						CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerIn, blockpos1, itemstack);
					}

					playerIn.addStat(Stats.ITEM_USED.get(this));
					SparkleParticleData data = SparkleParticleData.sparkle(1F, 0.2F, 0.2F, 1F, 5);
					for(int i = 0; i < 6; i++)
                        playerIn.world.addParticle(data, blockpos1.getX() + Math.random(), blockpos1.getY() + Math.random(), blockpos1.getZ() + Math.random(), 0, 0, 0);
					return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
				} else {
					return new ActionResult<>(ActionResultType.FAIL, itemstack);
				}
			} else {
				return new ActionResult<>(ActionResultType.FAIL, itemstack);
			}
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
