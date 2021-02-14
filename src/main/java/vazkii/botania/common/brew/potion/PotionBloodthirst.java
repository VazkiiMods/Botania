/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.ServerWorldAccess;
import vazkii.botania.common.brew.ModPotions;

public class PotionBloodthirst extends StatusEffect {

	private static final int RANGE = 64;

	public PotionBloodthirst() {
		super(StatusEffectType.BENEFICIAL, 0xC30000);
	}


	public static boolean overrideSpawn(ServerWorldAccess world, BlockPos pos, SpawnGroup entityClass) {
		if (entityClass == SpawnGroup.MONSTER) {
			Box aabb = new Box(pos).expand(RANGE);
			for (PlayerEntity player : world.getPlayers()) {
				if (player.hasStatusEffect(ModPotions.bloodthrst)
						&& !player.hasStatusEffect(ModPotions.emptiness)
						&& player.getBoundingBox().intersects(aabb)) {
					return true;
				}
			}
		}
		return false;
	}

}
