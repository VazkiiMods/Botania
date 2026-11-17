/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.FelPumpkinBlock;

// [VanillaCopy] DispenseItemBehavior for Blocks.CARVED_PUMPKIN as registered in its bootstrap method
public class FelPumpkinBehavior extends OptionalDispenseItemBehavior {
	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level level = source.level();
		BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		FelPumpkinBlock felPumpkinBlock = (FelPumpkinBlock) BotaniaBlocks.FEL_PUMPKIN;
		this.setSuccess(false);
		if (level.isEmptyBlock(blockpos) && felPumpkinBlock.canSpawnBlaze(level, source.pos())) {
			if (!level.isClientSide()) {
				level.setBlock(blockpos, felPumpkinBlock.defaultBlockState(), 3);
				level.gameEvent(null, GameEvent.BLOCK_PLACE, blockpos);
			}

			stack.shrink(1);
			this.setSuccess(true);
		}
		// no armor equip attempt

		return stack;
	}
}
