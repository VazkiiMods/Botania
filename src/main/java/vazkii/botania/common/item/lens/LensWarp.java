/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 8, 2015, 2:17:01 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import net.minecraft.block.Block;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;

public class LensWarp extends Lens {

	private static final String TAG_WARPED = "botania:warped";
	
	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if(burst.isFake() || pos.getBlockPos() == null)
			return dead;
		
		Block block = entity.world.getBlockState(pos.getBlockPos()).getBlock();
		if(block == ModBlocks.pistonRelay) {
			BlockPistonRelay.DimWithPos key = ((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.get(new BlockPistonRelay.DimWithPos(entity.world.provider.getDimension(), pos.getBlockPos()));
			if(key != null) {
				if(key.dim == entity.world.provider.getDimension()) {
					entity.setPosition(key.blockPos.getX() + 0.5, key.blockPos.getY() + 0.5, key.blockPos.getZ() + 0.5);
					burst.setCollidedAt(key.blockPos);
					
					entity.getEntityData().setBoolean(TAG_WARPED, true);
					
					return false;
				}
			}
		}
		return dead;
	}
	
	@Override
	public int getManaToTransfer(IManaBurst burst, EntityThrowable entity, ItemStack stack, IManaReceiver receiver) {
		return entity.getEntityData().getBoolean(TAG_WARPED) ? 0 : burst.getMana();
	}

}
