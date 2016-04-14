/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 26, 2014, 7:50:37 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityVineBall;
import vazkii.botania.common.lib.LibItemNames;

public class ItemVineBall extends ItemMod {

	public ItemVineBall() {
		super(LibItemNames.VINE_BALL);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand) {
		if(!par3EntityPlayer.capabilities.isCreativeMode)
			--par1ItemStack.stackSize;

		par2World.playSound(null, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, SoundEvents.entity_arrow_shoot, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if(!par2World.isRemote) {
			EntityVineBall ball = new EntityVineBall(par3EntityPlayer, true);
			ball.setHeadingFromThrower(par3EntityPlayer, par3EntityPlayer.rotationPitch, par3EntityPlayer.rotationYaw, 0.0F, 1.5F, 1.0F);
			par2World.spawnEntityInWorld(ball);
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
	}

}
