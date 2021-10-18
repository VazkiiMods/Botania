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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaReceiver;

import java.util.function.Predicate;

public class LensMagnet extends Lens {

	@Override
	public void updateBurst(IManaBurst burst, ItemStack stack) {
		Entity entity = burst.entity();
		BlockPos basePos = entity.blockPosition();
		boolean magnetized = burst.getMagnetizedPos() != null;
		int range = 3;

		BlockPos source = burst.getBurstSourceBlockPos();
		final boolean sourceless = source.equals(IManaBurst.NO_SOURCE);

		Predicate<BlockEntity> predicate = tile -> tile instanceof IManaReceiver
				&& (sourceless || tile.getBlockPos().distSqr(source) > 9)
				&& ((IManaReceiver) tile).canReceiveManaFromBursts()
				&& !((IManaReceiver) tile).isFull();

		BlockEntity tile = null;
		if (magnetized) {
			tile = entity.level.getBlockEntity(burst.getMagnetizedPos());
			if (!predicate.test(tile)) {
				tile = null;
				burst.setMagnetizePos(null);
				magnetized = false;
			}
		}

		if (!magnetized) {
			for (BlockPos pos : BlockPos.betweenClosed(basePos.offset(-range, -range, -range),
					basePos.offset(range, range, range))) {
				tile = entity.level.getBlockEntity(pos);
				if (predicate.test(tile)) {
					break;
				}
				tile = null;
			}
		}

		if (tile == null) {
			return;
		}

		Vec3 burstVec = entity.position();
		Vec3 tileVec = Vec3.atCenterOf(tile.getBlockPos()).add(0, -0.1, 0);
		Vec3 motionVec = entity.getDeltaMovement();

		Vec3 normalMotionVec = motionVec.normalize();
		Vec3 magnetVec = tileVec.subtract(burstVec).normalize();
		Vec3 differenceVec = normalMotionVec.subtract(magnetVec).scale(motionVec.length() * 0.1);

		Vec3 finalMotionVec = motionVec.subtract(differenceVec);
		if (!magnetized) {
			finalMotionVec = finalMotionVec.scale(0.75);
			burst.setMagnetizePos(tile.getBlockPos());
		}

		entity.setDeltaMovement(finalMotionVec);
	}

}
