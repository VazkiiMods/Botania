/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 17, 2015, 6:48:29 PM (GMT)]
 */
package vazkii.botania.common.item;

import mods.railcraft.api.core.items.IMinecartItem;
import net.minecraft.block.BlockRailBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.world.World;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.entity.EntityPoolMinecart;
import vazkii.botania.common.lib.LibItemNames;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.Optional;

@Optional.Interface(modid = "Railcraft", iface = "mods.railcraft.api.core.items.IMinecartItem", striprefs = true)
public class ItemPoolMinecart extends ItemMod implements ICraftAchievement, IMinecartItem {

	public ItemPoolMinecart() {
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.POOL_MINECART);
	}

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		if(BlockRailBase.func_150051_a(p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_))) {
			if(!p_77648_3_.isRemote) {
				EntityMinecart entityminecart = new EntityPoolMinecart(p_77648_3_, p_77648_4_ + 0.5, p_77648_5_ + 0.5, p_77648_6_ + 0.5);

				if(p_77648_1_.hasDisplayName())
					entityminecart.setMinecartName(p_77648_1_.getDisplayName());

				p_77648_3_.spawnEntityInWorld(entityminecart);
			}

			--p_77648_1_.stackSize;
			return true;
		}

		return false;
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.manaCartCraft;
	}

	@Override
	public boolean canBePlacedByNonPlayer(ItemStack cart) {
		return true;
	}

	@Override
	public EntityMinecart placeCart(GameProfile owner, ItemStack cart, World world, int i, int j, int k) {
		if(BlockRailBase.func_150051_a(world.getBlock(i, j, k))) {
			if(!world.isRemote) {
				EntityMinecart entityminecart = new EntityPoolMinecart(world, i + 0.5,j + 0.5, k + 0.5);

				if(cart.hasDisplayName())
					entityminecart.setMinecartName(cart.getDisplayName());

				if(world.spawnEntityInWorld(entityminecart))
					return entityminecart;
			}
		}
		return null;
	}

}
