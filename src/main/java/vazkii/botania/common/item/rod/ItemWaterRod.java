package vazkii.botania.common.item.rod;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.fluid.Fluid;
import net.minecraft.init.Fluids;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.StatList;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemWaterRod extends ItemMod implements IManaUsingItem {

	public static final int COST = 75;

	public ItemWaterRod(Properties props) {
		super(props);
	}

	// [VanillaCopy] From ItemBucket
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, false);

		ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
		if (ret != null) return ret;

		if (raytraceresult == null) {
			return new ActionResult<>(EnumActionResult.PASS, itemstack);
		} else if (raytraceresult.type == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = raytraceresult.getBlockPos();
			if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemstack)) {
				IBlockState iblockstate = worldIn.getBlockState(blockpos);
				BlockPos blockpos1 = this.getPlacementPosition(iblockstate, blockpos, raytraceresult);

				if (ManaItemHandler.requestManaExactForTool(itemstack, playerIn, COST, true)
						&& ((ItemBucket) Items.WATER_BUCKET).tryPlaceContainedLiquid(playerIn, worldIn, blockpos1, raytraceresult)) {
					if (playerIn instanceof EntityPlayerMP) {
						CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, blockpos1, itemstack);
					}

					playerIn.addStat(StatList.ITEM_USED.get(this));
					for(int i = 0; i < 6; i++)
						Botania.proxy.sparkleFX(blockpos1.getX() + Math.random(), blockpos1.getY() + Math.random(), blockpos1.getZ() + Math.random(), 0.2F, 0.2F, 1F, 1F, 5);
					return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
				} else {
					return new ActionResult<>(EnumActionResult.FAIL, itemstack);
				}
			} else {
				return new ActionResult<>(EnumActionResult.FAIL, itemstack);
			}
		} else {
			return new ActionResult<>(EnumActionResult.PASS, itemstack);
		}
	}

	// [VanillaCopy] ItemBucket
	private BlockPos getPlacementPosition(IBlockState p_210768_1_, BlockPos p_210768_2_, RayTraceResult p_210768_3_) {
		return p_210768_1_.getBlock() instanceof ILiquidContainer ? p_210768_2_ : p_210768_3_.getBlockPos().offset(p_210768_3_.sideHit);
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
