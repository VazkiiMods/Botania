/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 4:43:16 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.core.handler.ConfigHandler;

public class LensWeight extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if(!entity.worldObj.isRemote && !burst.isFake() && pos.getBlockPos() != null) {
			int harvestLevel = ConfigHandler.harvestLevelWeight;
			
			Block block = entity.worldObj.getBlockState(pos.getBlockPos()).getBlock();
			Block blockBelow = entity.worldObj.getBlockState(pos.getBlockPos().down()).getBlock();
			IBlockState state = entity.worldObj.getBlockState(pos.getBlockPos());
			int neededHarvestLevel = block.getHarvestLevel(state);
			
			if(blockBelow.isAir(entity.worldObj, pos.getBlockPos().down()) && block.getBlockHardness(entity.worldObj, pos.getBlockPos()) != -1 && neededHarvestLevel <= harvestLevel && entity.worldObj.getTileEntity(pos.getBlockPos()) == null && block.canSilkHarvest(entity.worldObj, pos.getBlockPos(), entity.worldObj.getBlockState(pos.getBlockPos()), null)) {
				EntityFallingBlock falling = new EntityFallingBlock(entity.worldObj, pos.getBlockPos().getX() + 0.5, pos.getBlockPos().getY() + 0.5, pos.getBlockPos().getZ() + 0.5, state);
				entity.worldObj.spawnEntityInWorld(falling);
			}
		}

		return dead;
	}

}
