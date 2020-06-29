/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityFunctionalFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.helper.MathHelper;
import vazkii.botania.common.core.helper.Vector3;

import java.util.List;

public class SubTileTangleberrie extends TileEntityFunctionalFlower {
	public SubTileTangleberrie(TileEntityType<?> type) {
		super(type);
	}

	public SubTileTangleberrie() {
		this(ModSubtiles.TANGLEBERRIE);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getMana() > 0) {
			double x1 = getEffectivePos().getX() + 0.5;
			double y1 = getEffectivePos().getY() + 0.5;
			double z1 = getEffectivePos().getZ() + 0.5;

			double maxDist = getMaxDistance();
			double range = getRange();

			AxisAlignedBB boundingBox = new AxisAlignedBB(x1 - range, y1 - range, z1 - range, x1 + range + 1, y1 + range + 1, z1 + range + 1);
			List<LivingEntity> entities = getWorld().getEntitiesWithinAABB(LivingEntity.class, boundingBox);

			SparkleParticleData data = SparkleParticleData.sparkle(1F, 0.5F, 0.5F, 0.5F, 3);
			for (LivingEntity entity : entities) {
				if (entity instanceof PlayerEntity || !entity.isNonBoss()) {
					continue;
				}

				double x2 = entity.getPosX();
				double y2 = entity.getPosY();
				double z2 = entity.getPosZ();

				float distance = MathHelper.pointDistanceSpace(x1, y1, z1, x2, y2, z2);

				if (distance > maxDist && distance < range) {
					MathHelper.setEntityMotionFromVector(entity, new Vector3(x1, y1, z1), getMotionVelocity(entity));
					if (getWorld().rand.nextInt(3) == 0) {
						world.addParticle(data, x2 + Math.random() * entity.getWidth(), y2 + Math.random() * entity.getHeight(), z2 + Math.random() * entity.getWidth(), 0, 0, 0);
					}
				}
			}

			if (ticksExisted % 4 == 0) {
				addMana(-1);
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

	float getMotionVelocity(LivingEntity entity) {
		return Math.max(entity.getAIMoveSpeed() / 2F, 0.05F);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), getRange());
	}

	@Override
	public int getColor() {
		return 0x4B797C;
	}

	@Override
	public int getMaxMana() {
		return 20;
	}

}
