/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaReceiver;

import java.util.function.Predicate;

public class LensMagnet extends Lens {

	@Override
	public void updateBurst(IManaBurst burst, ThrownEntity entity, ItemStack stack) {
		BlockPos basePos = entity.getBlockPos();
		boolean magnetized = burst.getMagnetizedPos() != null;
		int range = 3;

		BlockPos source = burst.getBurstSourceBlockPos();
		final boolean sourceless = source.getY() == -1;

		Predicate<BlockEntity> predicate = tile -> tile instanceof IManaReceiver
				&& (sourceless || tile.getPos().getSquaredDistance(source) > 9)
				&& ((IManaReceiver) tile).canReceiveManaFromBursts()
				&& !((IManaReceiver) tile).isFull();

		BlockEntity tile = null;
		if (magnetized) {
			tile = entity.world.getBlockEntity(burst.getMagnetizedPos());
			if (!predicate.test(tile)) {
				tile = null;
				burst.setMagnetizePos(null);
				magnetized = false;
			}
		}

		if (!magnetized) {
			for (BlockPos pos : BlockPos.iterate(basePos.add(-range, -range, -range),
					basePos.add(range, range, range))) {
				tile = entity.world.getBlockEntity(pos);
				if (predicate.test(tile)) {
					break;
				}
				tile = null;
			}
		}

		if (tile == null) {
			return;
		}

		Vec3d burstVec = entity.getPos();
		Vec3d tileVec = Vec3d.ofCenter(tile.getPos()).add(0, -0.1, 0);
		Vec3d motionVec = entity.getVelocity();

		Vec3d normalMotionVec = motionVec.normalize();
		Vec3d magnetVec = tileVec.subtract(burstVec).normalize();
		Vec3d differenceVec = normalMotionVec.subtract(magnetVec).multiply(motionVec.length() * 0.1);

		Vec3d finalMotionVec = motionVec.subtract(differenceVec);
		if (!magnetized) {
			finalMotionVec = finalMotionVec.multiply(0.75);
			burst.setMagnetizePos(tile.getPos());
		}

		burst.setBurstMotion(finalMotionVec.x, finalMotionVec.y, finalMotionVec.z);
	}

}
