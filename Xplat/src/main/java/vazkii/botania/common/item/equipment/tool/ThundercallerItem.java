/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.tool;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.item.equipment.tool.manasteel.ManasteelSwordItem;
import vazkii.botania.common.proxy.Proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ThundercallerItem extends ManasteelSwordItem {

	private static final String TAG_LIGHTNING_SEED = "lightningSeed";

	public ThundercallerItem(Properties props) {
		super(BotaniaAPI.instance().getTerrasteelItemTier(), 3, -1.5F, props);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity entity, @NotNull LivingEntity attacker) {
		if (!(entity instanceof Player) && entity != null) {
			double range = 8;
			List<LivingEntity> alreadyTargetedEntities = new ArrayList<>();
			int dmg = 5;
			long lightningSeed = ItemNBTHelper.getLong(stack, TAG_LIGHTNING_SEED, 0);

			Predicate<Entity> selector = e -> e instanceof LivingEntity && e instanceof Enemy && !(e instanceof Player) && !alreadyTargetedEntities.contains(e);

			Random rand = new Random(lightningSeed);
			LivingEntity lightningSource = entity;
			int hops = entity.level.isThundering() ? 10 : 4;
			for (int i = 0; i < hops; i++) {
				List<Entity> entities = entity.level.getEntities(lightningSource, new AABB(lightningSource.getX() - range, lightningSource.getY() - range, lightningSource.getZ() - range, lightningSource.getX() + range, lightningSource.getY() + range, lightningSource.getZ() + range), selector);
				if (entities.isEmpty()) {
					break;
				}

				LivingEntity target = (LivingEntity) entities.get(rand.nextInt(entities.size()));
				if (attacker instanceof Player player) {
					target.hurt(DamageSource.playerAttack(player), dmg);
				} else {
					target.hurt(DamageSource.mobAttack(attacker), dmg);
				}

				Proxy.INSTANCE.lightningFX(entity.level, VecHelper.fromEntityCenter(lightningSource), VecHelper.fromEntityCenter(target), 1, 0x0179C4, 0xAADFFF);

				alreadyTargetedEntities.add(target);
				lightningSource = target;
				dmg--;
			}

			if (!entity.level.isClientSide) {
				ItemNBTHelper.setLong(stack, TAG_LIGHTNING_SEED, entity.level.random.nextLong());
			}
		}

		return super.hurtEnemy(stack, entity, attacker);
	}

}
