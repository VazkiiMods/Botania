/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import vazkii.botania.common.brew.ModPotions;

public class PotionEmptiness extends StatusEffect {

	private static final int RANGE = 128;

	public PotionEmptiness() {
		super(StatusEffectType.BENEFICIAL, 0xFACFFF);
	}

	public static boolean shouldCancel(LivingEntity entity) {
		if (entity instanceof Monster) {
			Box aabb = new Box(entity.getX() - RANGE, entity.getY() - RANGE, entity.getZ() - RANGE,
					entity.getX() + RANGE, entity.getY() + RANGE, entity.getZ() + RANGE);
			for (PlayerEntity player : entity.world.getPlayers()) {
				if (player.hasStatusEffect(ModPotions.emptiness) && player.getBoundingBox().intersects(aabb)) {
					return true;
				}
			}
		}
		return false;
	}

}
