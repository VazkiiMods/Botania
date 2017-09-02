/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 14, 2014, 11:40:59 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.List;

public class SubTileTangleberrie extends SubTileFunctional {

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(mana > 0) {
			double x1 = supertile.getPos().getX() + 0.5;
			double y1 = supertile.getPos().getY() + 0.5;
			double z1 = supertile.getPos().getZ() + 0.5;

			double maxDist = getMaxDistance();
			double range = getRange();

			AxisAlignedBB boundingBox = new AxisAlignedBB(x1 - range, y1 - range, z1 - range, x1 + range + 1, y1 + range + 1, z1 + range + 1);
			List<EntityLivingBase> entities = supertile.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, boundingBox);

			for(EntityLivingBase entity : entities) {
				if(entity instanceof EntityPlayer || !entity.isNonBoss())
					continue;

				double x2 = entity.posX;
				double y2 = entity.posY;
				double z2 = entity.posZ;

				float distance = MathHelper.pointDistanceSpace(x1, y1, z1, x2, y2, z2);

				if(distance > maxDist && distance < range) {
					MathHelper.setEntityMotionFromVector(entity, new Vector3(x1, y1, z1), getMotionVelocity());
					if(supertile.getWorld().rand.nextInt(3) == 0)
						Botania.proxy.sparkleFX(x2 + Math.random() * entity.width, y2 + Math.random() * entity.height, z2 + Math.random() * entity.width, 0.5F, 0.5F, 0.5F, 1F, 3);
				}
			}

			if(ticksExisted % 4 == 0) {
				mana--;
				sync();
			}
		}
	}

	double getMaxDistance() {
		return 6;
	}

	double getRange() {
		return 7;
	}

	float getMotionVelocity() {
		return 0.05F;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(toBlockPos(), getRange());
	}

	@Override
	public int getColor() {
		return 0x4B797C;
	}

	@Override
	public int getMaxMana() {
		return 20;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.tangleberrie;
	}

}
