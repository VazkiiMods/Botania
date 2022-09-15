/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.function.Predicate;

public class MagnetizingLens extends Lens {

	@Override
	public void updateBurst(ManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		BlockPos basePos = entity.blockPosition();
		boolean magnetized = burst.getMagnetizedPos() != null;
		int range = 3;

		BlockPos source = burst.getBurstSourceBlockPos();
		final boolean sourceless = source.equals(ManaBurst.NO_SOURCE);

		Predicate<BlockPos> predicate = pos -> {
			var state = entity.level.getBlockState(pos);
			var be = entity.level.getBlockEntity(pos);
			var receiver = IXplatAbstractions.INSTANCE.findManaReceiver(entity.level, pos, state, be, null);
			return receiver != null
					&& (sourceless || pos.distSqr(source) > 9)
					&& receiver.canReceiveManaFromBursts()
					&& !receiver.isFull();
		};

		// todo: clean this logic up
		BlockPos target = null;
		if (magnetized) {
			target = burst.getMagnetizedPos();
			if (!predicate.test(target)) {
				target = null;
				burst.setMagnetizePos(null);
				magnetized = false;
			}
		}

		if (!magnetized) {
			for (BlockPos pos : BlockPos.betweenClosed(basePos.offset(-range, -range, -range),
					basePos.offset(range, range, range))) {
				target = pos;
				if (predicate.test(target)) {
					break;
				}
				target = null;
			}
		}

		if (target == null) {
			return;
		}

		Vec3 burstVec = entity.position();
		Vec3 tileVec = Vec3.atCenterOf(target).add(0, -0.1, 0);
		Vec3 motionVec = entity.getDeltaMovement();

		Vec3 normalMotionVec = motionVec.normalize();
		Vec3 magnetVec = tileVec.subtract(burstVec).normalize();
		Vec3 differenceVec = normalMotionVec.subtract(magnetVec).scale(motionVec.length() * 0.1);

		Vec3 finalMotionVec = motionVec.subtract(differenceVec);
		if (!magnetized) {
			finalMotionVec = finalMotionVec.scale(0.75);
			burst.setMagnetizePos(target.immutable());
		}

		entity.setDeltaMovement(finalMotionVec);
	}

}
