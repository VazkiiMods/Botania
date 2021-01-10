/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;

import vazkii.botania.common.brew.ModPotions;

public class PotionBloodthirst extends Effect {

	private static final int RANGE = 64;

	public PotionBloodthirst() {
		super(EffectType.BENEFICIAL, 0xC30000);
	}

	public static boolean overrideSpawn(IServerWorld world, BlockPos pos, EntityClassification entityClass) {
		if (entityClass == EntityClassification.MONSTER) {
			AxisAlignedBB aabb = new AxisAlignedBB(pos).grow(RANGE);
			for (PlayerEntity player : world.getPlayers()) {
				if (player.isPotionActive(ModPotions.bloodthrst)
						&& !player.isPotionActive(ModPotions.emptiness)
						&& player.getBoundingBox().intersects(aabb)) {
					return true;
				}
			}
		}
		return false;
	}

}
