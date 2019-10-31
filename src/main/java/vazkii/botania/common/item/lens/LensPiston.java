/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 4:46:39 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.internal.IManaBurst;

public class LensPiston extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrowableEntity entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		BlockPos coords = burst.getBurstSourceBlockPos();
		if(!entity.world.isRemote
				&& pos.getType() == RayTraceResult.Type.BLOCK
				&& !burst.isFake()
				&& !isManaBlock) {
			BlockRayTraceResult rtr = (BlockRayTraceResult) pos;
			if(!coords.equals(rtr.getPos())) {
				BlockPos pos_ = rtr.getPos().offset(rtr.getFace().getOpposite());

				if(entity.world.isAirBlock(pos_) || entity.world.getBlockState(pos_).getMaterial().isReplaceable()) {
					BlockState state = entity.world.getBlockState(rtr.getPos());
					TileEntity tile = entity.world.getTileEntity(rtr.getPos());

					if(state.getPushReaction() == PushReaction.NORMAL && state.getBlock() != Blocks.OBSIDIAN
							&& state.getBlockHardness(entity.world, pos_) >= 0 && tile == null) {
						entity.world.destroyBlock(rtr.getPos(), false);
						entity.world.setBlockState(pos_, state, 1 | 2);
					}
				}
			}
		}

		return dead;
	}

}
