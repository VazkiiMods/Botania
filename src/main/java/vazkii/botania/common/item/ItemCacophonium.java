/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 20, 2015, 9:53:57 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.util.List;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ItemCacophonium extends ItemMod {

	private static final String TAG_SOUND = "sound";
	private static final String TAG_SOUND_NAME = "soundName";
	private static final String TAG_HAS_SOUND = "hasSound";

	public ItemCacophonium() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.CACOPHONIUM);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
		if(entity instanceof EntityLiving) {
			EntityLiving living = (EntityLiving) entity;
			String sound = null;
			try {
				if(living instanceof EntityCreeper)
					sound = "creeper.primed";
				else if(living instanceof EntitySlime)
					sound = "mob.slime." + (((EntitySlime) living).getSlimeSize() > 1 ? "big" : "small");
				else sound = (String) ReflectionHelper.findMethod(EntityLiving.class, living, LibObfuscation.GET_LIVING_SOUND).invoke(living);

				if(sound != null) {
					String s = EntityList.getEntityString(entity);
					if(s == null)
						s = "generic";

					ItemNBTHelper.setString(stack, TAG_SOUND, sound);
					ItemNBTHelper.setString(stack, TAG_SOUND_NAME, "entity." + s + ".name");
					ItemNBTHelper.setBoolean(stack, TAG_HAS_SOUND, true);
					player.inventory.setInventorySlotContents(player.inventory.currentItem, stack.copy());

					if(player.worldObj.isRemote)
						player.swingItem();

					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		if(ItemNBTHelper.getBoolean(stack, TAG_HAS_SOUND, false))
			list.add(StatCollector.translateToLocal(ItemNBTHelper.getString(stack, TAG_SOUND_NAME, "")));
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.block;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if(ItemNBTHelper.getBoolean(par1ItemStack, TAG_HAS_SOUND, false))
			par3EntityPlayer.setItemInUse(par1ItemStack, 72000);
		return par1ItemStack;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		String sound = ItemNBTHelper.getString(stack, TAG_SOUND, "");
		if(count % 6 == 0 && sound != null && !sound.isEmpty())
			player.worldObj.playSoundAtEntity(player, sound, 0.9F, (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
	}
}
