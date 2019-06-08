/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 24, 2015, 4:47:20 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileManaFlame;

public class LensLight extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, ThrowableEntity entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		BlockPos coords = burst.getBurstSourceBlockPos();
		if(!entity.world.isRemote && pos.type == RayTraceResult.Type.BLOCK && !coords.equals(pos.getBlockPos()) && !burst.isFake() && !isManaBlock) {
			BlockPos neighborPos = pos.getBlockPos().offset(pos.sideHit);

			Block blockAt = entity.world.getBlockState(pos.getBlockPos()).getBlock();
			BlockState neighbor = entity.world.getBlockState(neighborPos);

			if(blockAt == ModBlocks.manaFlame)
				entity.world.setBlockState(pos.getBlockPos(), Blocks.AIR.getDefaultState());
			else if(neighbor.isAir(entity.world, neighborPos) || neighbor.getMaterial().isReplaceable()) {
				entity.world.setBlockState(neighborPos, ModBlocks.manaFlame.getDefaultState());
				TileEntity tile = entity.world.getTileEntity(neighborPos);

				if(tile instanceof TileManaFlame)
					((TileManaFlame) tile).setColor(burst.getColor());
			}
		}

		return dead;
	}

}
