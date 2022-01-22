/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

import vazkii.botania.common.brew.ModPotions;

public class PotionBloodthirst extends MobEffect {

	private static final int RANGE = 64;

	public PotionBloodthirst() {
		super(MobEffectCategory.BENEFICIAL, 0xC30000);
	}

	public static boolean overrideSpawn(ServerLevelAccessor world, BlockPos pos, MobCategory entityClass) {
		if (entityClass == MobCategory.MONSTER) {
			AABB aabb = new AABB(pos).inflate(RANGE);
			for (Player player : world.players()) {
				if (player.hasEffect(ModPotions.bloodthrst)
						&& !player.hasEffect(ModPotions.emptiness)
						&& player.getBoundingBox().intersects(aabb)) {
					return true;
				}
			}
		}
		return false;
	}

}
