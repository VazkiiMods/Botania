/**
 * This class was created by <Phanta>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [August 23, 2015, 9:59:34 PM (CDT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.entity.EntityShadowBeam;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

public class ItemShadowRod extends ItemMod implements IManaUsingItem {

	public static final int COST = 32;
	
	public ItemShadowRod() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.SHADOW_ROD);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		if(!player.worldObj.isRemote && ManaItemHandler.requestManaExact(stack, player, COST, true)) {
			if (count % 3 == 0)
				player.worldObj.playSoundAtEntity(player, "botania:shadowBeam", 0.5F, 1.0F);
			EntityShadowBeam beam = new EntityShadowBeam(player);
			while(!beam.isDead)
				beam.onUpdate();
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
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
