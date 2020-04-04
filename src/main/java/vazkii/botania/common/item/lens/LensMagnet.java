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

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.core.helper.Vector3;

public class LensMagnet extends Lens {

	private static final String TAG_MAGNETIZED = "botania:magnetized";
	private static final String TAG_MAGNETIZED_X = "botania:magnetized_x";
	private static final String TAG_MAGNETIZED_Y = "botania:magnetized_y";
	private static final String TAG_MAGNETIZED_Z = "botania:magnetized_z";

	@Override
	public void updateBurst(IManaBurst burst, ThrowableEntity entity, ItemStack stack) {
		BlockPos basePos = new BlockPos(entity);
		boolean magnetized = entity.getPersistentData().contains(TAG_MAGNETIZED);
		int range = 3;

		magnetize: {
			for (BlockPos pos : BlockPos.getAllInBoxMutable(basePos.add(-range, -range, -range),
					basePos.add(range, range, range))) {
				if (entity.world.getTileEntity(pos) instanceof IManaReceiver) {
					TileEntity tile = entity.world.getTileEntity(pos);

					if (magnetized) {
						int magX = entity.getPersistentData().getInt(TAG_MAGNETIZED_X);
						int magY = entity.getPersistentData().getInt(TAG_MAGNETIZED_Y);
						int magZ = entity.getPersistentData().getInt(TAG_MAGNETIZED_Z);
						if (tile.getPos().getX() != magX || tile.getPos().getY() != magY || tile.getPos().getZ() != magZ) {
							continue;
						}
					}

					IManaReceiver receiver = (IManaReceiver) tile;

					BlockPos srcCoords = burst.getBurstSourceBlockPos();

					if (tile.getPos().distanceSq(srcCoords) > 9 && receiver.canReceiveManaFromBursts() && !receiver.isFull()) {
						Vector3 burstVec = Vector3.fromEntity(entity);
						Vector3 tileVec = Vector3.fromTileEntityCenter(tile).add(0, -0.1, 0);
						Vector3 motionVec = new Vector3(entity.getMotion());

						Vector3 normalMotionVec = motionVec.normalize();
						Vector3 magnetVec = tileVec.subtract(burstVec).normalize();
						Vector3 differenceVec = normalMotionVec.subtract(magnetVec).multiply(motionVec.mag() * 0.1);

						Vector3 finalMotionVec = motionVec.subtract(differenceVec);
						if (!magnetized) {
							finalMotionVec = finalMotionVec.multiply(0.75);
							entity.getPersistentData().putBoolean(TAG_MAGNETIZED, true);
							entity.getPersistentData().putInt(TAG_MAGNETIZED_X, tile.getPos().getX());
							entity.getPersistentData().putInt(TAG_MAGNETIZED_Y, tile.getPos().getY());
							entity.getPersistentData().putInt(TAG_MAGNETIZED_Z, tile.getPos().getZ());
						}

						burst.setBurstMotion(finalMotionVec.x, finalMotionVec.y, finalMotionVec.z);
						break magnetize;
					}
				}

			}
		}
	}

}
