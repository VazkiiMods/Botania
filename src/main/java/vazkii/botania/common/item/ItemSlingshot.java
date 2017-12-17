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
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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

		if(!world.isRemote && (!(living instanceof EntityPlayer) || ((EntityPlayer) living).capabilities.isCreativeMode || PlayerHelper.hasAmmo((EntityPlayer) living, AMMO_FUNC))) {
			float f = j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if(f < 1F)
				return;

			if(living instanceof EntityPlayer && !((EntityPlayer) living).capabilities.isCreativeMode)
				PlayerHelper.consumeAmmo((EntityPlayer) living, AMMO_FUNC);

			EntityVineBall ball = new EntityVineBall(living, false);
			ball.shoot(living, living.rotationPitch, living.rotationYaw, 0F, 1.5F, 1F);
			ball.motionX *= 1.6;
			ball.motionY *= 1.6;
			ball.motionZ *= 1.6;
			world.spawnEntity(ball);
			world.playSound(null, living.posX, living.posY, living.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		}
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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(player.capabilities.isCreativeMode || PlayerHelper.hasAmmo(player, AMMO_FUNC)) {
			player.setActiveHand(hand);
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}

		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

}
