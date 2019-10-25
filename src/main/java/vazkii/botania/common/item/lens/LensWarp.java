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
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.RayTraceResult;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;

public class LensWarp extends Lens {

	private static final String TAG_WARPED = "botania:warped";
	
	@Override
	public boolean collideBurst(IManaBurst burst, ThrowableEntity entity, RayTraceResult pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		if(burst.isFake() || pos.getType() != RayTraceResult.Type.BLOCK)
			return dead;

		BlockPos hit = ((BlockRayTraceResult) pos).getPos();
		Block block = entity.world.getBlockState(hit).getBlock();
		if(block == ModBlocks.pistonRelay) {
			GlobalPos key = ((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.get(GlobalPos.of(entity.world.getDimension().getType(), hit));
			if(key != null) {
				if(key.getDimension() == entity.world.getDimension().getType()) {
					entity.setPosition(key.getPos().getX() + 0.5, key.getPos().getY() + 0.5, key.getPos().getZ() + 0.5);
					burst.setCollidedAt(key.getPos());
					
					entity.getPersistentData().putBoolean(TAG_WARPED, true);
					
					return false;
				}
			}
		}
		return dead;
	}
	
	@Override
	public int getManaToTransfer(IManaBurst burst, ThrowableEntity entity, ItemStack stack, IManaReceiver receiver) {
		return entity.getPersistentData().getBoolean(TAG_WARPED) ? 0 : burst.getMana();
	}

}
