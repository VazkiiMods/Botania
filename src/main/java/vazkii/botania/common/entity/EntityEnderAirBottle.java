/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 16, 2015, 5:15:31 PM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntityEnderAirBottle extends EntityThrowable {

	public EntityEnderAirBottle(World world) {
		super(world);
	}

	public EntityEnderAirBottle(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	@Override
	protected void onImpact(@Nonnull RayTraceResult pos) {
		if(pos.getBlockPos() != null && !world.isRemote) {
			List<BlockPos> coordsList = getCoordsToPut(pos.getBlockPos());
			world.playEvent(2002, new BlockPos(this), 8);

			for(BlockPos coords : coordsList) {
				world.setBlockState(coords, Blocks.END_STONE.getDefaultState());
				if(Math.random() < 0.1)
					world.playEvent(2001, coords, Block.getStateId(Blocks.END_STONE.getDefaultState()));
			}
			setDead();
		}
	}

	private List<BlockPos> getCoordsToPut(BlockPos pos) {
		List<BlockPos> possibleCoords = new ArrayList<>();
		int range = 4;
		int rangeY = 4;

		for (BlockPos bPos : BlockPos.getAllInBox(pos.add(-range, -rangeY, -range), pos.add(range, rangeY, range))) {
			IBlockState state = world.getBlockState(bPos);
			Block block = state.getBlock();
			if(block.isReplaceableOreGen(state, world, bPos, BlockStateMatcher.forBlock(Blocks.STONE)))
				possibleCoords.add(bPos);
		}

		Collections.shuffle(possibleCoords, rand);

		return possibleCoords.stream().limit(64).collect(Collectors.toList());
	}

}
