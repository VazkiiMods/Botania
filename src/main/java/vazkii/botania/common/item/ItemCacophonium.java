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

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileCacophonium;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibObfuscation;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ItemCacophonium extends ItemMod implements ICraftAchievement {

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
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int s, float xs, float ys, float zs) {
		boolean can = isDOIT(stack);
		if(!can) {
			String sound = ItemNBTHelper.getString(stack, TAG_SOUND, "");
			isDOIT(stack);
			if(sound != null && !sound.isEmpty())
				can = true;
		}

		if(can) {
			Block block = world.getBlock(x, y, z);
			if(block == Blocks.noteblock) {
				world.setBlock(x, y, z, ModBlocks.cacophonium);
				((TileCacophonium) world.getTileEntity(x, y, z)).stack = stack.copy();
				stack.stackSize--;
				return true;
			}
		}

		return false;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		if(isDOIT(stack))
			list.add(StatCollector.translateToLocal("botaniamisc.justDoIt"));
		else if(ItemNBTHelper.getBoolean(stack, TAG_HAS_SOUND, false))
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
		if(ItemNBTHelper.getBoolean(par1ItemStack, TAG_HAS_SOUND, false) || isDOIT(par1ItemStack))
			par3EntityPlayer.setItemInUse(par1ItemStack, 72000);
		return par1ItemStack;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		if(count % (isDOIT(stack) ? 20 : 6) == 0)
			playSound(player.worldObj, stack, player.posX, player.posY, player.posZ, 0.9F);
	}

	public static void playSound(World world, ItemStack stack, double x, double y, double z, float volume) {
		if(stack == null)
			return;

		String sound = ItemNBTHelper.getString(stack, TAG_SOUND, "");
		boolean doit = isDOIT(stack);
		if(doit)
			sound = "botania:doit";

		if(sound != null && !sound.isEmpty())
			world.playSoundEffect(x, y, z, sound, volume, doit ? 1F : (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
	}

	private static boolean isDOIT(ItemStack stack) {
		return stack != null && stack.getDisplayName().equalsIgnoreCase("shia labeouf");
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.cacophoniumCraft;
	}
}
