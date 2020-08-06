/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaReceiver;

import java.util.function.Predicate;

public class LensMagnet extends Lens {

	@Override
	public void updateBurst(IManaBurst burst, ThrowableEntity entity, ItemStack stack) {
		BlockPos basePos = entity.func_233580_cy_();
		boolean magnetized = burst.getMagnetizedPos() != null;
		int range = 3;

		BlockPos source = burst.getBurstSourceBlockPos();
		final boolean sourceless = source.getY() == -1;

		Predicate<TileEntity> predicate = tile -> tile instanceof IManaReceiver
				&& (sourceless || tile.getPos().distanceSq(source) > 9)
				&& ((IManaReceiver) tile).canReceiveManaFromBursts()
				&& !((IManaReceiver) tile).isFull();

		TileEntity tile = null;
		if (magnetized) {
			tile = entity.world.getTileEntity(burst.getMagnetizedPos());
			if (!predicate.test(tile)) {
				tile = null;
				burst.setMagnetizePos(null);
				magnetized = false;
			}
		}

		if (!magnetized) {
			for (BlockPos pos : BlockPos.getAllInBoxMutable(basePos.add(-range, -range, -range),
					basePos.add(range, range, range))) {
				tile = entity.world.getTileEntity(pos);
				if (predicate.test(tile)) {
					break;
				}
				tile = null;
			}
		}

		if (tile == null) {
			return;
		}

		Vector3d burstVec = entity.getPositionVec();
		Vector3d tileVec = Vector3d.func_237489_a_(tile.getPos()).add(0, -0.1, 0);
		Vector3d motionVec = entity.getMotion();

		Vector3d normalMotionVec = motionVec.normalize();
		Vector3d magnetVec = tileVec.subtract(burstVec).normalize();
		Vector3d differenceVec = normalMotionVec.subtract(magnetVec).scale(motionVec.length() * 0.1);

		Vector3d finalMotionVec = motionVec.subtract(differenceVec);
		if (!magnetized) {
			finalMotionVec = finalMotionVec.scale(0.75);
			burst.setMagnetizePos(tile.getPos());
		}

		burst.setBurstMotion(finalMotionVec.x, finalMotionVec.y, finalMotionVec.z);
	}

}
