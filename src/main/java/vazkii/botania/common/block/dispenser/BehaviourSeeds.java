/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BehaviourSeeds extends ItemDispenserBehavior {
	private Block block;

	public BehaviourSeeds(Block block) {
		this.block = block;
	}

	@Nonnull
	@Override
	public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
		Direction facing = source.getBlockState().get(DispenserBlock.FACING);
		BlockPos pos = source.getBlockPos().offset(facing);
		World world = source.getWorld();

		setSuccessful(false);
		if (world.isAir(pos) && block.getDefaultState().canPlaceAt(world, pos)) {
			world.setBlockState(pos, block.getDefaultState());
			stack.decrement(1);
			setSuccessful(true);
		}

        return stack;
	}

}
