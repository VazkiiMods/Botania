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
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
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
			EnumFacing dir = var1.sideHit;
			int[] metaPlace = new int[] {
					1, 4, 8, 2
			};

			if(dir.getAxis() != EnumFacing.Axis.Y) {
				BlockPos pos = var1.getBlockPos().offset(dir);
				while(pos.getY() > 0) {
					Block block = worldObj.getBlockState(pos).getBlock();
					if(block.isAir(worldObj, pos)) {
						IBlockState state = ModBlocks.solidVines.getDefaultState().withProperty(BlockVine.ALL_FACES[dir.getIndex() + 1], true);
						worldObj.setBlockState(pos, state, 1 | 2);
						worldObj.playAuxSFX(2001, pos, Block.getStateId(state)); // todo 1.8 verify
						pos = pos.down();
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
