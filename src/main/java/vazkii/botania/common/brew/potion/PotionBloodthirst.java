/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 3, 2014, 12:13:09 AM (GMT)]
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class PotionBloodthirst extends Effect {

	private static final int RANGE = 64;

	public PotionBloodthirst() {
		super(EffectType.BENEFICIAL, 0xC30000);
	}

	@SubscribeEvent
	public static void onSpawn(LivingSpawnEvent.CheckSpawn event) {
		if(event.getResult() != Event.Result.ALLOW && event.getEntityLiving() instanceof IMob) {
			AxisAlignedBB aabb = new AxisAlignedBB(event.getX() - RANGE, event.getY() - RANGE, event.getZ() - RANGE,
					event.getX() + RANGE, event.getY() + RANGE, event.getZ() + RANGE);
			for(PlayerEntity player : event.getWorld().getPlayers()) {
				if(player.isPotionActive(ModPotions.bloodthrst)
						&& !player.isPotionActive(ModPotions.emptiness)
						&& player.getBoundingBox().intersects(aabb)) {
					event.setResult(Event.Result.ALLOW);
					return;
				}
			}
		}
	}

}
