/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 4:42:33 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.item.ModItems;

import java.util.List;

public class LensInfluence extends Lens {

	@Override
	public void updateBurst(IManaBurst burst, EntityThrowable entity, ItemStack stack) {
		if(!burst.isFake()) {
			double range = 3.5;
			AxisAlignedBB bounds = new AxisAlignedBB(entity.posX - range, entity.posY - range, entity.posZ - range, entity.posX + range, entity.posY + range, entity.posZ + range);
			List<Entity> movables = entity.world.getEntitiesWithinAABB(EntityItem.class, bounds);
			movables.addAll(entity.world.getEntitiesWithinAABB(EntityXPOrb.class, bounds));
			movables.addAll(entity.world.getEntitiesWithinAABB(EntityArrow.class, bounds));
			movables.addAll(entity.world.getEntitiesWithinAABB(EntityFallingBlock.class, bounds));
			movables.addAll(entity.world.getEntitiesWithinAABB(EntityThrowable.class, bounds, Predicates.instanceOf(IManaBurst.class)));

			for(Entity movable : movables) {
				if(movable == burst)
					continue;

				if(movable instanceof IManaBurst) {
					IManaBurst otherBurst = (IManaBurst) movable;
					ItemStack lens = otherBurst.getSourceLens();
					if(lens != null && lens.getItem() == ModItems.lens && lens.getItemDamage() == ItemLens.INFLUENCE)
						continue;

					((IManaBurst) movable).setMotion(entity.motionX, entity.motionY, entity.motionZ);
				} else {
					movable.motionX = entity.motionX;
					movable.motionY = entity.motionY;
					movable.motionZ = entity.motionZ;
				}
			}
		}
	}

}
