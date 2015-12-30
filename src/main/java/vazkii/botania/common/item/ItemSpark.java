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

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.entity.EntitySpark;
import vazkii.botania.common.lib.LibItemNames;

public class ItemSpark extends ItemMod implements ICraftAchievement, IManaGivingItem {

	public ItemSpark() {
		setUnlocalizedName(LibItemNames.SPARK);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float xv, float yv, float zv) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof ISparkAttachable) {
			ISparkAttachable attach = (ISparkAttachable) tile;
			if(attach.canAttachSpark(stack) && attach.getAttachedSpark() == null) {
				stack.stackSize--;
				if(!world.isRemote) {
					EntitySpark spark = new EntitySpark(world);
					spark.setPosition(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
					world.spawnEntityInWorld(spark);
					attach.attachSpark(spark);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.sparkCraft;
	}

}
