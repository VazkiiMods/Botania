/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelAxe;

import java.util.Random;

public class ItemElementiumAxe extends ItemManasteelAxe {

	public ItemElementiumAxe(Settings props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props);
		MinecraftForge.EVENT_BUS.addListener(this::onEntityDrops);
	}

	// Thanks to SpitefulFox for the drop rates
	// https://github.com/SpitefulFox/ForbiddenMagic/blob/master/src/com/spiteful/forbidden/FMEventHandler.java

	private void onEntityDrops(LivingDropsEvent event) {
		if (event.isRecentlyHit() && event.getSource().getAttacker() != null && event.getSource().getAttacker() instanceof PlayerEntity) {
			ItemStack weapon = ((PlayerEntity) event.getSource().getAttacker()).getMainHandStack();
			if (!weapon.isEmpty() && weapon.getItem() == this) {
				Random rand = event.getEntityLiving().world.random;
				int looting = EnchantmentHelper.getLevel(Enchantments.FORTUNE, weapon);

				if (event.getEntityLiving() instanceof AbstractSkeletonEntity && rand.nextInt(26) <= 3 + looting) {
					addDrop(event, new ItemStack(event.getEntity() instanceof WitherSkeletonEntity ? Items.WITHER_SKELETON_SKULL : Items.SKELETON_SKULL));
				} else if (event.getEntityLiving() instanceof ZombieEntity && !(event.getEntityLiving() instanceof ZombifiedPiglinEntity) && rand.nextInt(26) <= 2 + 2 * looting) {
					addDrop(event, new ItemStack(Items.ZOMBIE_HEAD));
				} else if (event.getEntityLiving() instanceof CreeperEntity && rand.nextInt(26) <= 2 + 2 * looting) {
					addDrop(event, new ItemStack(Items.CREEPER_HEAD));
				} else if (event.getEntityLiving() instanceof PlayerEntity && rand.nextInt(11) <= 1 + looting) {
					ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
					ItemNBTHelper.setString(stack, "SkullOwner", ((PlayerEntity) event.getEntityLiving()).getGameProfile().getName());
					addDrop(event, stack);
				} else if (event.getEntityLiving() instanceof EntityDoppleganger && rand.nextInt(13) < 1 + looting) {
					addDrop(event, new ItemStack(ModBlocks.gaiaHead));
				}
			}
		}
	}

	private void addDrop(LivingDropsEvent event, ItemStack drop) {
		ItemEntity entityitem = new ItemEntity(event.getEntityLiving().world, event.getEntityLiving().getX(), event.getEntityLiving().getY(), event.getEntityLiving().getZ(), drop);
		entityitem.setPickupDelay(10);
		event.getDrops().add(entityitem);
	}

}
