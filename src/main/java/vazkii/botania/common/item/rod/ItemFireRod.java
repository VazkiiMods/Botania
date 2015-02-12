/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 26, 2014, 12:08:06 AM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.entity.EntityFlameRing;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

public class ItemFireRod extends ItemMod implements IManaUsingItem {

	private static final int COST = 900;
	private static final int COOLDOWN = 1200;

	public ItemFireRod() {
		setUnlocalizedName(LibItemNames.FIRE_ROD);
		setMaxStackSize(1);
		setMaxDamage(COOLDOWN);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World par3World, int x, int y, int z, int par7, float par8, float par9, float par10) {
		if(!par3World.isRemote && par1ItemStack.getItemDamage() == 0 && ManaItemHandler.requestManaExact(par1ItemStack, player, COST, false)) {
			EntityFlameRing entity = new EntityFlameRing(player.worldObj);
			entity.setPosition(x + 0.5, y + 1, z + 0.5);
			player.worldObj.spawnEntityInWorld(entity);

			par1ItemStack.setItemDamage(COOLDOWN);
			ManaItemHandler.requestManaExact(par1ItemStack, player, COST, true);
			par3World.playSoundAtEntity(player, "mob.blaze.breathe", 1F, 1F);
		}

		return true;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par1ItemStack.isItemDamaged())
			par1ItemStack.setItemDamage(par1ItemStack.getItemDamage() - 1);
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
