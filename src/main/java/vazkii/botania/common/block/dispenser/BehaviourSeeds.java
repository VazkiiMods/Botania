/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 16, 2014, 10:36:05 PM (GMT)]
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BehaviourSeeds extends BehaviorDefaultDispenseItem {

	private final Block block;

	public BehaviourSeeds(Block block) {
		this.block = block;
	}

	@Nonnull
	@Override
	public ItemStack dispenseStack(IBlockSource source, ItemStack par2ItemStack) {
		World world = source.getWorld();
		EnumFacing facing = world.getBlockState(source.getBlockPos()).getValue(BlockDispenser.FACING);
		BlockPos pos = source.getBlockPos().offset(facing);

		if(world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos) && block.canPlaceBlockAt(world, pos)) {
			world.setBlockState(pos, block.getDefaultState());
			par2ItemStack.stackSize--;
			return par2ItemStack;
		}

		return super.dispenseStack(source, par2ItemStack);
	}

}
