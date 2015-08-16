/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 16, 2015, 2:54:35 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityBabylonWeapon;
import vazkii.botania.common.lib.LibItemNames;

public class ItemKingKey extends ItemRelic {

	private static final String TAG_WEAPONS_SPAWNED = "weaponsSpawned";
	private static final String TAG_CHARGING = "charging";
	
	public ItemKingKey() {
		super(LibItemNames.KING_KEY);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		setCharging(par1ItemStack, true);
		return par1ItemStack;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int time) {
		setCharging(stack, false);
		setWeaponsSpawned(stack, 0); // TODO probably move this to a ticker
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		int spawned = getWeaponsSpawned(stack);
		if(count != getMaxItemUseDuration(stack) && spawned < 20 && !player.worldObj.isRemote) {
			Vector3 look = new Vector3(player.getLookVec());
			look.y = 0;
			look.normalize().negate().multiply(2);
			Vector3 pl = look.copy().add(Vector3.fromEntityCenter(player)); 
			
			Random rand = player.worldObj.rand;
			Vector3 axis = pl.copy().crossProduct(new Vector3(-1, 0, -1)).normalize();
			Vector3 axis1 = axis.copy();
			axis1.multiply(rand.nextDouble() * 22 + 3).rotate((rand.nextDouble() - 0.5) * Math.PI / 2.0, look);
			Vector3 end = pl.copy().add(axis1);
			
			EntityBabylonWeapon weapon = new EntityBabylonWeapon(player.worldObj, player);
			weapon.posX = end.x;
			weapon.posY = end.y;
			weapon.posZ = end.z;
			weapon.setVariety(rand.nextInt(12));
			weapon.setDelay(spawned);
			
			player.worldObj.spawnEntityInWorld(weapon);
			setWeaponsSpawned(stack, spawned + 1);
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	
	public static boolean isCharging(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_CHARGING, false);
	}
	
	public static int getWeaponsSpawned(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_WEAPONS_SPAWNED, 0);
	}
	
	public static void setCharging(ItemStack stack, boolean charging) {
		ItemNBTHelper.setBoolean(stack, TAG_CHARGING, charging);
	}
	
	public static void setWeaponsSpawned(ItemStack stack, int count) {
		ItemNBTHelper.setInt(stack, TAG_WEAPONS_SPAWNED, count);
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
}
