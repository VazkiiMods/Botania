/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 3, 2014, 12:12:36 AM (GMT)]
 */
package vazkii.botania.common.brew.potion;

import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.botania.common.brew.ModPotions;

public class PotionEmptiness extends PotionMod {

	private static final int RANGE = 128;

	public PotionEmptiness() {
		super(false, 0xFACFFF, 2);
		setBeneficial();
	}

	@SubscribeEvent
	public static void onSpawn(LivingSpawnEvent.CheckSpawn event) {
		if(event.getResult() != Event.Result.ALLOW && event.getEntityLiving() instanceof IMob) {
			AxisAlignedBB aabb = new AxisAlignedBB(event.getX() - RANGE, event.getY() - RANGE, event.getZ() - RANGE, event.getX() + RANGE, event.getY() + RANGE, event.getZ() + RANGE);
			for(EntityPlayer player : event.getWorld().getWorld().playerEntities) {
				if(player.isPotionActive(ModPotions.emptiness) && player.getBoundingBox().intersects(aabb)) {
					event.setResult(Event.Result.DENY);
					return;
				}
			}
		}
	}

}
