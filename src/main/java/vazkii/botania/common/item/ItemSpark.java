/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 21, 2014, 5:24:55 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.entity.EntitySpark;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSpark extends ItemMod implements ICraftAchievement, IManaGivingItem {

	public static IIcon invIcon, worldIcon;

	public ItemSpark() {
		setUnlocalizedName(LibItemNames.SPARK);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xv, float yv, float zv) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof ISparkAttachable) {
			ISparkAttachable attach = (ISparkAttachable) tile;
			if(attach.canAttachSpark(stack) && attach.getAttachedSpark() == null) {
				stack.stackSize--;
				if(!world.isRemote) {
					EntitySpark spark = new EntitySpark(world);
					spark.setPosition(x + 0.5, y + 1.5, z + 0.5);
					world.spawnEntityInWorld(spark);
					attach.attachSpark(spark);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, x, y, z);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		invIcon = IconHelper.forItem(par1IconRegister, this, 0);
		worldIcon = IconHelper.forItem(par1IconRegister, this, 1);
	}

	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		return invIcon;
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.sparkCraft;
	}

}
