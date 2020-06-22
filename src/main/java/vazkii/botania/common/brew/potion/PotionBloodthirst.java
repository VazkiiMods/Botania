/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;

import vazkii.botania.common.brew.ModPotions;

public class PotionBloodthirst extends Effect {

	private static final int RANGE = 64;

	public PotionBloodthirst() {
		super(EffectType.BENEFICIAL, 0xC30000);
	}

	public static void onSpawn(LivingSpawnEvent.CheckSpawn event) {
		if (event.getResult() != Event.Result.ALLOW && event.getEntityLiving() instanceof IMob) {
			AxisAlignedBB aabb = new AxisAlignedBB(event.getX() - RANGE, event.getY() - RANGE, event.getZ() - RANGE,
					event.getX() + RANGE, event.getY() + RANGE, event.getZ() + RANGE);
			for (PlayerEntity player : event.getWorld().getPlayers()) {
				if (player.isPotionActive(ModPotions.bloodthrst)
						&& !player.isPotionActive(ModPotions.emptiness)
						&& player.getBoundingBox().intersects(aabb)) {
					event.setResult(Event.Result.ALLOW);
					return;
				}
			}
		}
	}

}
