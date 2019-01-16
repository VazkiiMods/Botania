package vazkii.botania.common.item.rod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
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

public class ItemWaterRod extends ItemMod implements IManaUsingItem {

	public static final int COST = 75;

	public ItemWaterRod() {
		super(LibItemNames.WATER_ROD);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		ItemStack stack = player.getHeldItem(hand);
		if(ManaItemHandler.requestManaExactForTool(stack, player, COST, false) && !world.provider.doesWaterVaporize()) {
			// Adapted from bucket code
			RayTraceResult mop = rayTrace(world, player, false);

			if (mop != null && mop.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos hitPos = mop.getBlockPos();
				if(!world.isBlockModifiable(player, hitPos))
					return EnumActionResult.FAIL;
				BlockPos placePos = hitPos.offset(mop.sideHit);
				if(player.canPlayerEdit(placePos, mop.sideHit, stack)) {
					if (ManaItemHandler.requestManaExactForTool(stack, player, COST, true)
							&& ((ItemBucket) Items.WATER_BUCKET).tryPlaceContainedLiquid(player, world, placePos)) {
						for(int i = 0; i < 6; i++)
							Botania.proxy.sparkleFX(pos.getX() + side.getXOffset() + Math.random(), pos.getY() + side.getYOffset() + Math.random(), pos.getZ() + side.getZOffset() + Math.random(), 0.2F, 0.2F, 1F, 1F, 5);
						return EnumActionResult.SUCCESS;
					}
				}

			}
			return EnumActionResult.FAIL;
		}

		return EnumActionResult.PASS;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
