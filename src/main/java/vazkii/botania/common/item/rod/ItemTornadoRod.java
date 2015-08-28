/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 25, 2014, 4:36:13 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

public class ItemTornadoRod extends ItemMod implements IManaUsingItem {

	private static final int FLY_TIME = 20;
	private static final int FALL_MULTIPLIER = 3;
	private static final int MAX_DAMAGE = FLY_TIME * FALL_MULTIPLIER;
	private static final int COST = 350;

	private static final String TAG_FLYING = "flying";

	IIcon iconIdle, iconFlying;

	public ItemTornadoRod() {
		setMaxDamage(MAX_DAMAGE);
		setUnlocalizedName(LibItemNames.TORNADO_ROD);
		setMaxStackSize(1);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par3Entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) par3Entity;
			ItemStack itemInUse = player.getCurrentEquippedItem();
			boolean damaged = par1ItemStack.getItemDamage() > 0;

			if(damaged && !isFlying(par1ItemStack))
				par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() - 1);

			if(itemInUse != par1ItemStack)
				setFlying(par1ItemStack, false);
			else {
				int max = FALL_MULTIPLIER * FLY_TIME;
				if(par1ItemStack.getItemDamage() >= max) {
					setFlying(par1ItemStack, false);
					player.stopUsingItem();
				} else if(isFlying(par1ItemStack)) {
					player.fallDistance = 0F;
					player.motionY = 1.25;
					par1ItemStack.setItemDamage(Math.min(max, par1ItemStack.getItemDamage() + FALL_MULTIPLIER));
					if(par1ItemStack.getItemDamage() == MAX_DAMAGE)
						setFlying(par1ItemStack, false);
					player.worldObj.playSoundAtEntity(player, "botania:airRod", 0.1F, 0.25F);
					for(int i = 0; i < 5; i++)
						Botania.proxy.wispFX(player.worldObj, player.posX, player.posY, player.posZ, 0.25F, 0.25F, 0.25F, 0.35F + (float) Math.random() * 0.1F, 0.2F * (float) (Math.random() - 0.5), -0.01F * (float) Math.random(), 0.2F * (float) (Math.random() - 0.5));
				}
			}

			if(damaged)
				player.fallDistance = 0;
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		int meta = par1ItemStack.getItemDamage();
		if(meta != 0 || ManaItemHandler.requestManaExactForTool(par1ItemStack, par3EntityPlayer, COST, false)) {
			par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
			if(meta == 0) {
				setFlying(par1ItemStack, true);
				ManaItemHandler.requestManaExactForTool(par1ItemStack, par3EntityPlayer, COST, true);
			}
		}

		return par1ItemStack;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {

	}

	@Override
	public IIcon getIconIndex(ItemStack par1ItemStack) {
		return isFlying(par1ItemStack) ? iconFlying : iconIdle;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 720000;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		iconIdle = IconHelper.forItem(par1IconRegister, this, 0);
		iconFlying = IconHelper.forItem(par1IconRegister, this, 1);
	}

	public boolean isFlying(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_FLYING, false);
	}

	public void setFlying(ItemStack stack, boolean flying) {
		ItemNBTHelper.setBoolean(stack, TAG_FLYING, flying);
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
