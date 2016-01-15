/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 4:46:03 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileIncensePlate;

public class LensFire extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		BlockPos coords = burst.getBurstSourceBlockPos();
		if(pos.getBlockPos() != null && !coords.equals(pos.getBlockPos()) && !burst.isFake() && !isManaBlock) {
			EnumFacing dir = pos.sideHit;

			BlockPos pos_ = pos.getBlockPos().offset(dir);

			Block blockAt = entity.worldObj.getBlockState(pos.getBlockPos()).getBlock();
			Block blockAt_ = entity.worldObj.getBlockState(pos_).getBlock();

			if(blockAt == Blocks.portal)
				entity.worldObj.setBlockState(pos.getBlockPos(), Blocks.air.getDefaultState());
			else if(blockAt == ModBlocks.incensePlate) {
				TileIncensePlate plate = (TileIncensePlate) entity.worldObj.getTileEntity(pos.getBlockPos());
				plate.ignite();
			} else if(blockAt_.isAir(entity.worldObj, pos_))
				entity.worldObj.setBlockState(pos_, Blocks.fire.getDefaultState());
		}

		return dead;
	}

}
