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
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BehaviourCocoaBeans extends OptionalDispenseBehavior {
	@Nonnull
	@Override
	public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
		Block block = Blocks.COCOA;
		Direction facing = source.getBlockState().get(DispenserBlock.FACING);
		BlockPos pos = source.getBlockPos().offset(facing);
		World world = source.getWorld();
		ItemPlacementContext ctx = new AutomaticItemPlacementContext(source.getWorld(), source.getBlockPos().offset(facing), facing, new ItemStack(block), facing.getOpposite());
		BlockState cocoa = block.getPlacementState(ctx);
		if (cocoa != null && world.isAir(pos)) {
			world.setBlockState(pos, cocoa);
			stack.decrement(1);
			return stack;
		}

		return stack;
	}

}
