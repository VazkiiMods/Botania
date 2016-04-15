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

import java.util.ArrayList;
import java.util.List;

public class EntityEnderAirBottle extends EntityThrowable {

	public EntityEnderAirBottle(World world) {
		super(world);
	}

	public EntityEnderAirBottle(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	@Override
	protected void onImpact(RayTraceResult pos) {
		if(pos.entityHit == null && !worldObj.isRemote) {
			List<BlockPos> coordsList = getCoordsToPut(pos.getBlockPos());
			worldObj.playAuxSFX(2002, new BlockPos((int)Math.round(posX), (int)Math.round(posY), (int)Math.round(posZ)), 8);

			for(BlockPos coords : coordsList) {
				worldObj.setBlockState(coords, Blocks.end_stone.getDefaultState());
				if(Math.random() < 0.1)
					worldObj.playAuxSFX(2001, coords, Block.getIdFromBlock(Blocks.end_stone));
			}
			setDead();
		}
	}

	public List<BlockPos> getCoordsToPut(BlockPos pos) {
		List<BlockPos> possibleCoords = new ArrayList<>();
		List<BlockPos> selectedCoords = new ArrayList<>();
		int range = 4;
		int rangeY = 4;

		for (BlockPos bPos : BlockPos.getAllInBox(pos.add(-range, -rangeY, -range), pos.add(range, rangeY, range))) {
			IBlockState state = worldObj.getBlockState(pos);
			Block block = state.getBlock();
			if(block != null && block.isReplaceableOreGen(state, worldObj, pos, BlockStateMatcher.forBlock(Blocks.stone)))
				possibleCoords.add(pos);
		}

		int count = 64;
		while(!possibleCoords.isEmpty() && count > 0) {
			BlockPos coords = possibleCoords.get(worldObj.rand.nextInt(possibleCoords.size()));
			possibleCoords.remove(coords);
			selectedCoords.add(coords);
			count--;
		}
		return selectedCoords;
	}

}
