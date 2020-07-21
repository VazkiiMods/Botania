/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import vazkii.botania.common.brew.ModPotions;

public class PotionBloodthirst extends StatusEffect {

	private static final int RANGE = 64;

	public PotionBloodthirst() {
		super(StatusEffectType.BENEFICIAL, 0xC30000);
	}

	public static void onSpawn(LivingSpawnEvent.CheckSpawn event) {
		if (event.getResult() != Event.Result.ALLOW && event.getEntityLiving() instanceof Monster) {
			Box aabb = new Box(event.getX() - RANGE, event.getY() - RANGE, event.getZ() - RANGE,
					event.getX() + RANGE, event.getY() + RANGE, event.getZ() + RANGE);
			for (PlayerEntity player : event.getWorld().getPlayers()) {
				if (player.hasStatusEffect(ModPotions.bloodthrst)
						&& !player.hasStatusEffect(ModPotions.emptiness)
						&& player.getBoundingBox().intersects(aabb)) {
					event.setResult(Event.Result.ALLOW);
					return;
				}
			}
		}
	}

}
