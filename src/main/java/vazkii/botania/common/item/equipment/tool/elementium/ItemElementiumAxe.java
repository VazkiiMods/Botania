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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
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
	}

	// Thanks to SpitefulFox for the drop rates
	// https://github.com/SpitefulFox/ForbiddenMagic/blob/master/src/com/spiteful/forbidden/FMEventHandler.java

	public static void onEntityDrops(int playerHitTimer, DamageSource source, LivingEntity target) {
		if (playerHitTimer > 0 && source.getAttacker() != null && source.getAttacker() instanceof PlayerEntity) {
			ItemStack weapon = ((PlayerEntity) source.getAttacker()).getMainHandStack();
			if (!weapon.isEmpty() && weapon.getItem() instanceof ItemElementiumAxe) {
				Random rand = target.world.random;
				int looting = EnchantmentHelper.getLevel(Enchantments.FORTUNE, weapon);

				if (target instanceof AbstractSkeletonEntity && rand.nextInt(26) <= 3 + looting) {
					target.dropStack(new ItemStack(target instanceof WitherSkeletonEntity ? Items.WITHER_SKELETON_SKULL : Items.SKELETON_SKULL));
				} else if (target instanceof ZombieEntity && !(target instanceof ZombifiedPiglinEntity) && rand.nextInt(26) <= 2 + 2 * looting) {
					target.dropStack(new ItemStack(Items.ZOMBIE_HEAD));
				} else if (target instanceof CreeperEntity && rand.nextInt(26) <= 2 + 2 * looting) {
					target.dropStack(new ItemStack(Items.CREEPER_HEAD));
				} else if (target instanceof PlayerEntity && rand.nextInt(11) <= 1 + looting) {
					ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
					ItemNBTHelper.setString(stack, "SkullOwner", ((PlayerEntity) target).getGameProfile().getName());
					target.dropStack(stack);
				} else if (target instanceof EntityDoppleganger && rand.nextInt(13) < 1 + looting) {
					target.dropStack(new ItemStack(ModBlocks.gaiaHead));
				}
			}
		}
	}

}
