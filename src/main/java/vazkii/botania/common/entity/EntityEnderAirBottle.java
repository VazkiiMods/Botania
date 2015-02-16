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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEnderAirBottle extends EntityThrowable {

	public EntityEnderAirBottle(World world) {
		super(world);
	}

	public EntityEnderAirBottle(World world, EntityLivingBase entity) {
		super(world, entity);
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		if(pos.entityHit == null && !worldObj.isRemote) {
			List<ChunkCoordinates> coordsList = getCoordsToPut(pos.blockX, pos.blockY, pos.blockZ);
			worldObj.playAuxSFX(2002, (int)Math.round(posX), (int)Math.round(posY), (int)Math.round(posZ), 8);

			for(ChunkCoordinates coords : coordsList) {
				worldObj.setBlock(coords.posX, coords.posY, coords.posZ, Blocks.end_stone);
				if(Math.random() < 0.1)
					worldObj.playAuxSFX(2001, coords.posX, coords.posY, coords.posZ, Block.getIdFromBlock(Blocks.end_stone));
			}
			setDead();
		}
	}

	public List<ChunkCoordinates> getCoordsToPut(int xCoord, int yCoord, int zCoord) {
		List<ChunkCoordinates> possibleCoords = new ArrayList();
		List<ChunkCoordinates> selectedCoords = new ArrayList();
		int range = 4;
		int rangeY = 4;

		for(int i = -range; i < range + 1; i++)
			for(int j = -rangeY; j < rangeY; j++)
				for(int k = -range; k < range + 1; k++) {
					int x = xCoord + i;
					int y = yCoord + j;
					int z = zCoord + k;
					Block block = worldObj.getBlock(x, y, z);
					if(block != null && block.isReplaceableOreGen(worldObj, x, y, z, Blocks.stone))
						possibleCoords.add(new ChunkCoordinates(x, y, z));
				}

		int count = 64;
		while(!possibleCoords.isEmpty() && count > 0) {
			ChunkCoordinates coords = possibleCoords.get(worldObj.rand.nextInt(possibleCoords.size()));
			possibleCoords.remove(coords);
			selectedCoords.add(coords);
			count--;
		}
		return selectedCoords;
	}

}
