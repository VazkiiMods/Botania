package vazkii.botania.common.item.rod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

public class ItemWaterRod extends ItemMod implements IManaUsingItem {

	public static final int COST = 75;

	public ItemWaterRod() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.WATER_ROD);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumFacing side, float par8, float par9, float par10) {
		if(ManaItemHandler.requestManaExactForTool(par1ItemStack, par2EntityPlayer, COST, false) && !par3World.provider.doesWaterVaporize()) {

			// Adapted from bucket code
			MovingObjectPosition mop = getMovingObjectPositionFromPlayer(par3World, par2EntityPlayer, false);

			if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				BlockPos hitPos = mop.getBlockPos();
				if(!par3World.isBlockModifiable(par2EntityPlayer, hitPos))
					return false;
				BlockPos placePos = hitPos.offset(mop.sideHit);
				if(par2EntityPlayer.canPlayerEdit(placePos, mop.sideHit, par1ItemStack)) {
					if (ManaItemHandler.requestManaExactForTool(par1ItemStack, par2EntityPlayer, COST, true)
							&& ((ItemBucket) Items.water_bucket).tryPlaceContainedLiquid(par3World, placePos)) {
						for(int i = 0; i < 6; i++)
							Botania.proxy.sparkleFX(par3World, pos.getX() + side.getFrontOffsetX() + Math.random(), pos.getY() + side.getFrontOffsetY() + Math.random(), pos.getZ() + side.getFrontOffsetZ() + Math.random(), 0.2F, 0.2F, 1F, 1F, 5);
					}
				}

			}
		}
		return true;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
