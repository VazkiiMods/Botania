/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.functional;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import java.util.*;

public class BergamuteBlockEntity extends SpecialFlowerBlockEntity {
	private static final int RANGE = 4;
	private static final Set<BergamuteBlockEntity> clientFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private static final Set<BergamuteBlockEntity> serverFlowers = Collections.newSetFromMap(new WeakHashMap<>());
	private boolean disabled = false;

	public BergamuteBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.BERGAMUTE, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		disabled = getLevel().hasNeighborSignal(getBlockPos());
		if (getLevel().isClientSide) {
			clientFlowers.add(this);
		} else {
			serverFlowers.add(this);
		}
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if (getLevel().isClientSide) {
			clientFlowers.remove(this);
		} else {
			serverFlowers.remove(this);
		}
	}

	public static Pair<Integer, BergamuteBlockEntity> getBergamutesNearby(Level level, double x, double y, double z, int maxCount) {
		int count = 0;
		BergamuteBlockEntity tile = null;

		for (BergamuteBlockEntity f : level.isClientSide ? clientFlowers : serverFlowers) {
			if (!f.disabled
					&& level == f.level
					&& f.getEffectivePos().distToCenterSqr(x, y, z) <= RANGE * RANGE) {
				count++;
				if (count == 1) {
					tile = f;
				}
				if (count >= maxCount) {
					break;
				}
			}
		}
		return Pair.of(count, tile);
	}

	public static boolean isBergamuteNearby(Level level, double x, double y, double z) {
		return getBergamutesNearby(level, x, y, z, 1).getFirst() > 0;
	}

	public static boolean isBergamuteOccludingVibration(Level level, Vec3 sourcePos, Vec3 destPos) {
		BlockPos sourceBlockPos = BlockPos.containing(sourcePos);
		BlockPos destBlockPos = BlockPos.containing(destPos);

		// vibration occlusions assume block centers as source and target positions, so do that here as well
		Vec3 sourceCenterPos = sourceBlockPos.getCenter();
		if (sourceBlockPos.equals(destBlockPos)) {
			// trivial case: source and dest are in the same block, check Bergamute proximity to that block's center
			return isBergamuteNearby(level, sourceCenterPos.x, sourceCenterPos.y, sourceCenterPos.z);
		}

		// find the point on the line between source and destination that is closest to each Bergamute,
		// and check whether it's actually in range of that Bergamute
		// (based on https://stackoverflow.com/questions/51905268/how-to-find-closest-point-on-line)
		Vec3 destCenterPos = destBlockPos.getCenter();
		Vec3 vibrationTravelVector = sourceCenterPos.vectorTo(destCenterPos);
		double vibrationTravelDist = vibrationTravelVector.length();
		Vec3 vibrationTravelDir = vibrationTravelVector.normalize();

		for (BergamuteBlockEntity f : level.isClientSide ? clientFlowers : serverFlowers) {
			if (f.disabled || f.level != level) {
				continue;
			}

			Vec3 flowerPos = f.getEffectivePos().getCenter();
			Vec3 vecSourceToFlower = sourceCenterPos.vectorTo(flowerPos);
			double travelPosition = Mth.clamp(vibrationTravelDir.dot(vecSourceToFlower), 0, vibrationTravelDist);
			Vec3 closestPos = sourceCenterPos.add(vibrationTravelDir.scale(travelPosition));
			if (flowerPos.distanceToSqr(closestPos) <= RANGE * RANGE) {
				return true;
			}
		}

		return false;
	}

	public static void particle(BergamuteBlockEntity berg) {
		int color = ManaPoolBlockEntity.PARTICLE_COLOR;
		float red = (color >> 16 & 0xFF) / 255F;
		float green = (color >> 8 & 0xFF) / 255F;
		float blue = (color & 0xFF) / 255F;
		SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), red, green, blue, 5);
		berg.emitParticle(data, 0.3 + Math.random() * 0.5, 0.5 + Math.random() * 0.5, 0.3 + Math.random() * 0.5, 0, 0, 0);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Circle(getEffectivePos(), RANGE);
	}

}
