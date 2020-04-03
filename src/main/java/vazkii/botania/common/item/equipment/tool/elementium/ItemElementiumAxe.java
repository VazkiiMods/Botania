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
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelAxe;

import java.util.Random;

public class ItemElementiumAxe extends ItemManasteelAxe {

	public ItemElementiumAxe(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props);
		MinecraftForge.EVENT_BUS.addListener(this::onEntityDrops);
	}

	// Thanks to SpitefulFox for the drop rates
	// https://github.com/SpitefulFox/ForbiddenMagic/blob/master/src/com/spiteful/forbidden/FMEventHandler.java

	private void onEntityDrops(LivingDropsEvent event) {
		if (event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemStack weapon = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (!weapon.isEmpty() && weapon.getItem() == this) {
				Random rand = event.getEntityLiving().world.rand;
				int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, weapon);

				if (event.getEntityLiving() instanceof AbstractSkeletonEntity && rand.nextInt(26) <= 3 + looting) {
					addDrop(event, new ItemStack(event.getEntity() instanceof WitherSkeletonEntity ? Items.WITHER_SKELETON_SKULL : Items.SKELETON_SKULL));
				} else if (event.getEntityLiving() instanceof ZombieEntity && !(event.getEntityLiving() instanceof ZombiePigmanEntity) && rand.nextInt(26) <= 2 + 2 * looting) {
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
