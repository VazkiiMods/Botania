/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [02/10/2016, 18:27:42 (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaSpreader;

import java.util.List;

public class LensTripwire extends Lens {

	private static final String TAG_TRIPPED = "botania:triwireLensTripped";

	@Override
	public boolean allowBurstShooting(ItemStack stack, IManaSpreader spreader, boolean redstone) {
		IManaBurst burst = spreader.runBurstSimulation();
		Entity e = (Entity) burst;
		return e.getEntityData().getBoolean(TAG_TRIPPED);
	}

	@Override
	public void updateBurst(IManaBurst burst, EntityThrowable entity, ItemStack stack) {
		if(burst.isFake()) {
			if(entity.world.isRemote)
				return;

			AxisAlignedBB axis = new AxisAlignedBB(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).grow(0.25);
			List<EntityLivingBase> entities = entity.world.getEntitiesWithinAABB(EntityLivingBase.class, axis);
			if(!entities.isEmpty()) {
				Entity e = (Entity) burst;
				e.getEntityData().setBoolean(TAG_TRIPPED, true);
			}
		}

	}

}
