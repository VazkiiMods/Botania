/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 17, 2015, 5:07:25 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;

public class RecipePureDaisy {

	public static final int DEFAULT_TIME = 150;

	private final Object input;
	private final BlockState outputState;
	private final int time;

	public RecipePureDaisy(Object input, BlockState state) {
		this(input, state, DEFAULT_TIME);
	}

	public RecipePureDaisy(Object input, BlockState state, int time) {
		Preconditions.checkArgument(time >= 0, "Time must be nonnegative");
		this.input = input;
		outputState = state;
		this.time = time;
		if(input != null && !(input instanceof Tag || input instanceof Block || input instanceof BlockState))
			throw new IllegalArgumentException("input must be a Tag<Block>, Block, or IBlockState");
		// todo 1.13 find way to throw or enforce Tag<Block>
	}

	/**
	 * This gets called every tick, please be careful with your checks.
	 */
	public boolean matches(World world, BlockPos pos, TileEntitySpecialFlower pureDaisy, BlockState state) {
		if(input instanceof Block)
			return state.getBlock() == input;

		if(input instanceof BlockState)
			return state == input;

		return ((Tag<Block>) input).contains(state.getBlock());
	}

	/**
	 * Returns true if the block was placed (and if the Pure Daisy should do particles and stuffs).
	 * Should only place the block if !world.isRemote, but should return true if it would've placed
	 * it otherwise. You may return false to cancel the normal particles and do your own.
	 */
	public boolean set(World world, BlockPos pos, TileEntitySpecialFlower pureDaisy) {
		if(!world.isRemote)
			world.setBlockState(pos, outputState);
		return true;
	}

	public Object getInput() {
		return input;
	}

	public BlockState getOutputState() {
		return outputState;
	}

	public int getTime() {
		return time;
	}

}
