/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 4:44:01 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.Botania;

public class LensPaint extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		int storedColor = ItemLens.getStoredColor(stack);
		if(!burst.isFake() && storedColor > -1 && storedColor < 17) {
			if(pos.entityHit != null && pos.entityHit instanceof EntitySheep) {
				int r = 20;
				int sheepColor = ((EntitySheep) pos.entityHit).getFleeceColor();
				List<EntitySheep> sheepList = entity.worldObj.getEntitiesWithinAABB(EntitySheep.class, AxisAlignedBB.getBoundingBox(pos.entityHit.posX - r, pos.entityHit.posY - r, pos.entityHit.posZ - r, pos.entityHit.posX + r, pos.entityHit.posY + r, pos.entityHit.posZ + r));
				for(EntitySheep sheep : sheepList) {
					if(sheep.getFleeceColor() == sheepColor)
						sheep.setFleeceColor(storedColor == 16 ? sheep.worldObj.rand.nextInt(16) : storedColor);
				}
				dead = true;
			} else {
				Block block = entity.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);
				if(BotaniaAPI.paintableBlocks.contains(block)) {
					int meta = entity.worldObj.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ);
					List<ChunkCoordinates> coordsToPaint = new ArrayList();
					List<ChunkCoordinates> coordsFound = new ArrayList();

					ChunkCoordinates theseCoords = new ChunkCoordinates(pos.blockX, pos.blockY, pos.blockZ);
					coordsFound.add(theseCoords);

					do {
						List<ChunkCoordinates> iterCoords = new ArrayList(coordsFound);
						for(ChunkCoordinates coords : iterCoords) {
							coordsFound.remove(coords);
							coordsToPaint.add(coords);

							for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
								Block block_ = entity.worldObj.getBlock(coords.posX + dir.offsetX, coords.posY + dir.offsetY, coords.posZ + dir.offsetZ);
								int meta_ = entity.worldObj.getBlockMetadata(coords.posX + dir.offsetX, coords.posY + dir.offsetY, coords.posZ + dir.offsetZ);
								ChunkCoordinates coords_ = new ChunkCoordinates(coords.posX + dir.offsetX, coords.posY + dir.offsetY, coords.posZ + dir.offsetZ);
								if(block_ == block && meta_ == meta && !coordsFound.contains(coords_) && !coordsToPaint.contains(coords_))
									coordsFound.add(coords_);
							}
						}
					} while(!coordsFound.isEmpty() && coordsToPaint.size() < 1000);

					for(ChunkCoordinates coords : coordsToPaint) {
						int placeColor = storedColor == 16 ? entity.worldObj.rand.nextInt(16) : storedColor;
						int metaThere = entity.worldObj.getBlockMetadata(coords.posX, coords.posY, coords.posZ);

						if(metaThere != placeColor) {
							if(!entity.worldObj.isRemote)
								entity.worldObj.setBlockMetadataWithNotify(coords.posX, coords.posY, coords.posZ, placeColor, 2);
							float[] color = EntitySheep.fleeceColorTable[placeColor];
							float r = color[0];
							float g = color[1];
							float b = color[2];
							for(int i = 0; i < 4; i++)
								Botania.proxy.sparkleFX(entity.worldObj, coords.posX + (float) Math.random(), coords.posY + (float) Math.random(), coords.posZ + (float) Math.random(), r, g, b, 0.6F + (float) Math.random() * 0.3F, 5);

						}
					}
				}
			}
		}

		return dead;
	}

}
