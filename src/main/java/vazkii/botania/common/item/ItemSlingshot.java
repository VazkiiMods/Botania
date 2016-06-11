/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 27, 2014, 12:38:58 AM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityVineBall;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ItemSlingshot extends ItemMod {

	private static final Predicate<ItemStack> AMMO_FUNC = s -> s != null && s.getItem() == ModItems.vineBall;

	public ItemSlingshot() {
		super(LibItemNames.SLINGSHOT);
		setMaxStackSize(1);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World world, EntityLivingBase living, int par4) {
		int j = getMaxItemUseDuration(par1ItemStack) - par4;

		if(!(living instanceof EntityPlayer) || ((EntityPlayer) living).capabilities.isCreativeMode || PlayerHelper.hasAmmo(((EntityPlayer) living), AMMO_FUNC)) {
			float f = j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if(f < 1F)
				return;

			if(living instanceof EntityPlayer && !((EntityPlayer) living).capabilities.isCreativeMode)
				PlayerHelper.consumeAmmo(((EntityPlayer) living), AMMO_FUNC);

			if(!world.isRemote) {
				EntityVineBall ball = new EntityVineBall(living, false);
				ball.motionX *= 1.6;
				ball.motionY *= 1.6;
				ball.motionZ *= 1.6;
				world.spawnEntityInWorld(ball);
			}
		}
	}

	@Override
	public ItemStack onItemUseFinish(@Nonnull ItemStack par1ItemStack, World world, EntityLivingBase living) {
		return par1ItemStack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack par1ItemStack, World world, EntityPlayer player, EnumHand hand) {
		if(player.capabilities.isCreativeMode || PlayerHelper.hasAmmo(player, AMMO_FUNC)) {
			player.setActiveHand(hand);
			return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, par1ItemStack);
	}

}
