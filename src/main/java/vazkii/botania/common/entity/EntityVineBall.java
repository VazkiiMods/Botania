/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 26, 2014, 7:32:16 PM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.common.block.ModBlocks;

public class EntityVineBall extends EntityThrowable {

	public EntityVineBall(World par1World) {
		super(par1World);
		dataWatcher.addObject(30, 0F);
		dataWatcher.setObjectWatched(30);
	}

	public EntityVineBall(EntityPlayer player, boolean gravity) {
		super(player.worldObj, player);
		dataWatcher.addObject(30, gravity ? 0.03F : 0F);
		dataWatcher.setObjectWatched(30);
	}

	@Override
	protected void onImpact(MovingObjectPosition var1) {
		if(var1 != null) {
			int meta = var1.sideHit;
			int[] metaPlace = new int[] {
					1, 4, 8, 2
			};

			if(meta > 1 && meta < 6) {
				ForgeDirection dir = ForgeDirection.getOrientation(meta);
				int x = var1.blockX + dir.offsetX;
				int y = var1.blockY + dir.offsetY;
				int z = var1.blockZ + dir.offsetZ;
				while(y > 0) {
					Block block = worldObj.getBlock(x, y, z);
					if(block.isAir(worldObj, x, y, z)) {
						worldObj.setBlock(x, y, z, ModBlocks.solidVines, metaPlace[meta - 2], 1 | 2);
						worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(ModBlocks.solidVines) + (metaPlace[meta - 2] << 12));
						y--;
					} else break;
				}
			}

		}

		setDead();
	}

	@Override
	protected float getGravityVelocity() {
		return dataWatcher.getWatchableObjectFloat(30);
	}

}
