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
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.common.entity.EntityVineBall;
import vazkii.botania.common.lib.LibItemNames;

public class ItemVineBall extends ItemMod {

	public ItemVineBall() {
		setUnlocalizedName(LibItemNames.VINE_BALL);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if(!par3EntityPlayer.capabilities.isCreativeMode)
			--par1ItemStack.stackSize;

		par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (par2World.rand.nextFloat() * 0.4F + 0.8F));

		if(!par2World.isRemote)
			par2World.spawnEntityInWorld(new EntityVineBall(par3EntityPlayer, true));

		return par1ItemStack;
	}

}
