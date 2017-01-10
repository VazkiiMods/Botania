/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 4:39:58 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.core.helper.Vector3;

public class LensMagnet extends Lens {

	private static final String TAG_MAGNETIZED = "Botania:Magnetized";
	private static final String TAG_MAGNETIZED_X = "Botania:MagnetizedX";
	private static final String TAG_MAGNETIZED_Y = "Botania:MagnetizedY";
	private static final String TAG_MAGNETIZED_Z = "Botania:MagnetizedZ";

	@Override
	public void updateBurst(IManaBurst burst, EntityThrowable entity, ItemStack stack) {
		BlockPos basePos = new BlockPos(entity);
		boolean magnetized = entity.getEntityData().hasKey(TAG_MAGNETIZED);
		int range = 3;

		magnetize : {
			for (BlockPos pos : BlockPos.getAllInBox(basePos.add(-range, -range, -range), basePos.add(range, range, range))) {
				if(entity.world.getTileEntity(pos) instanceof IManaReceiver) {
					TileEntity tile = entity.world.getTileEntity(pos);

					if(magnetized) {
						int magX = entity.getEntityData().getInteger(TAG_MAGNETIZED_X);
						int magY = entity.getEntityData().getInteger(TAG_MAGNETIZED_Y);
						int magZ = entity.getEntityData().getInteger(TAG_MAGNETIZED_Z);
						if(tile.getPos().getX() != magX || tile.getPos().getY() != magY || tile.getPos().getZ() != magZ)
							continue;
					}

					IManaReceiver receiver = (IManaReceiver) tile;

					BlockPos srcCoords = burst.getBurstSourceBlockPos();

					if(tile.getPos().distanceSq(srcCoords) > 9 && receiver.canRecieveManaFromBursts() && !receiver.isFull()) {
						Vector3 burstVec = Vector3.fromEntity(entity);
						Vector3 tileVec = Vector3.fromTileEntityCenter(tile).add(0, -0.1, 0);
						Vector3 motionVec = new Vector3(entity.motionX, entity.motionY, entity.motionZ);

						Vector3 normalMotionVec = motionVec.normalize();
						Vector3 magnetVec = tileVec.subtract(burstVec).normalize();
						Vector3 differenceVec = normalMotionVec.subtract(magnetVec).multiply(motionVec.mag() * 0.1);

						Vector3 finalMotionVec = motionVec.subtract(differenceVec);
						if(!magnetized) {
							finalMotionVec = finalMotionVec.multiply(0.75);
							entity.getEntityData().setBoolean(TAG_MAGNETIZED, true);
							entity.getEntityData().setInteger(TAG_MAGNETIZED_X, tile.getPos().getX());
							entity.getEntityData().setInteger(TAG_MAGNETIZED_Y, tile.getPos().getY());
							entity.getEntityData().setInteger(TAG_MAGNETIZED_Z, tile.getPos().getZ());
						}

						burst.setMotion(finalMotionVec.x, finalMotionVec.y, finalMotionVec.z);
						break magnetize;
					}
				}

			}
		}
	}

}
