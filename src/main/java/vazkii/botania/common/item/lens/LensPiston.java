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

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.internal.IManaBurst;

public class LensPiston extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		BlockPos coords = burst.getBurstSourceBlockPos();
		if(!entity.worldObj.isRemote && pos.getBlockPos() != null && !coords.equals(pos.getBlockPos()) && !burst.isFake() && !isManaBlock) {
			BlockPos pos_ = pos.getBlockPos().offset(pos.sideHit.getOpposite());

			if(entity.worldObj.isAirBlock(pos_) || entity.worldObj.getBlockState(pos_).getBlock().isReplaceable(entity.worldObj, pos_)) {
				IBlockState state = entity.worldObj.getBlockState(pos.getBlockPos());
				TileEntity tile = entity.worldObj.getTileEntity(pos.getBlockPos());

				if(state.getMobilityFlag() == EnumPushReaction.NORMAL && state.getBlock() != Blocks.OBSIDIAN && state.getBlockHardness(entity.worldObj, pos_) >= 0 && tile == null) {
					entity.worldObj.destroyBlock(pos.getBlockPos(), false);
					entity.worldObj.setBlockState(pos_, state, 1 | 2);
				}
			}
		}

		return dead;
	}

}
