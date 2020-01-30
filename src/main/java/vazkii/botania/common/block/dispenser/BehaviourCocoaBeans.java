/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [21/09/2016, 21:08:41 (GMT)]
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BehaviourCocoaBeans extends DefaultDispenseItemBehavior {
	@Nonnull
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		Block block = Blocks.COCOA;
		Direction facing = source.getBlockState().get(DispenserBlock.FACING);
		BlockPos pos = source.getBlockPos().offset(facing);
		World world = source.getWorld();
		BlockItemUseContext ctx = new DirectionalPlaceContext(source.getWorld(), source.getBlockPos().offset(facing), facing, new ItemStack(block), facing.getOpposite());
		BlockState cocoa = block.getStateForPlacement(ctx);
		if(cocoa != null && world.isAirBlock(pos)) {
			world.setBlockState(pos, cocoa);
			stack.shrink(1);
			return stack;
		}

		return stack;
	}

}
