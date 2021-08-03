/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelAxe;

import java.util.Random;

public class ItemElementiumAxe extends ItemManasteelAxe {

	public ItemElementiumAxe(Properties props) {
		super(BotaniaAPI.instance().getElementiumItemTier(), props);
	}

	// Thanks to SpitefulFox for the drop rates
	// https://github.com/SpitefulFox/ForbiddenMagic/blob/master/src/com/spiteful/forbidden/FMEventHandler.java

	public static void onEntityDrops(int playerHitTimer, DamageSource source, LivingEntity target) {
		if (playerHitTimer > 0 && source.getEntity() != null && source.getEntity() instanceof Player) {
			ItemStack weapon = ((Player) source.getEntity()).getMainHandItem();
			if (!weapon.isEmpty() && weapon.getItem() instanceof ItemElementiumAxe) {
				Random rand = target.level.random;
				int looting = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, weapon);

				if (target instanceof AbstractSkeleton && rand.nextInt(26) <= 3 + looting) {
					target.spawnAtLocation(new ItemStack(target instanceof WitherSkeleton ? Items.WITHER_SKELETON_SKULL : Items.SKELETON_SKULL));
				} else if (target instanceof Zombie && !(target instanceof ZombifiedPiglin) && rand.nextInt(26) <= 2 + 2 * looting) {
					target.spawnAtLocation(new ItemStack(Items.ZOMBIE_HEAD));
				} else if (target instanceof Creeper && rand.nextInt(26) <= 2 + 2 * looting) {
					target.spawnAtLocation(new ItemStack(Items.CREEPER_HEAD));
				} else if (target instanceof Player && rand.nextInt(11) <= 1 + looting) {
					ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
					ItemNBTHelper.setString(stack, "SkullOwner", ((Player) target).getGameProfile().getName());
					target.spawnAtLocation(stack);
				} else if (target instanceof EntityDoppleganger && rand.nextInt(13) < 1 + looting) {
					target.spawnAtLocation(new ItemStack(ModBlocks.gaiaHead));
				}
			}
		}
	}

}
