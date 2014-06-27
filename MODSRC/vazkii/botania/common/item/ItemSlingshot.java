/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 27, 2014, 12:38:58 AM (GMT)]
 */
package vazkii.botania.common.item;

import vazkii.botania.common.entity.EntityVineBall;
import vazkii.botania.common.lib.LibItemNames;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public class ItemSlingshot extends ItemMod {

	public ItemSlingshot() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.SLINGSHOT);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		int j = getMaxItemUseDuration(par1ItemStack) - par4;

		if(par3EntityPlayer.capabilities.isCreativeMode || par3EntityPlayer.inventory.hasItem(ModItems.vineBall)) {
			float f = (float)j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if(f < 1F)
				return;
				
			if(!par3EntityPlayer.capabilities.isCreativeMode)
				par3EntityPlayer.inventory.consumeInventoryItem(ModItems.vineBall);

			if(!par2World.isRemote) {
				EntityVineBall ball = new EntityVineBall(par3EntityPlayer, false);
				ball.motionX *= 1.6;
				ball.motionY *= 1.6;
				ball.motionZ *= 1.6;
				par2World.spawnEntityInWorld(ball);
			}
		}
	}

	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if(par3EntityPlayer.capabilities.isCreativeMode || par3EntityPlayer.inventory.hasItem(ModItems.vineBall))
			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));

		return par1ItemStack;
	}

}
