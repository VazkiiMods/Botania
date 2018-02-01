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
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.subtile.SubTileEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipePureDaisy {

	public static final int DEFAULT_TIME = 150;
	private static final Map<String, List<ItemStack>> oreMap = new HashMap<>();

	private final Object input;
	private final IBlockState outputState;
	private final int time;

	public RecipePureDaisy(Object input, IBlockState state) {
		this(input, state, DEFAULT_TIME);
	}

	public RecipePureDaisy(Object input, IBlockState state, int time) {
		Preconditions.checkArgument(time >= 0, "Time must be nonnegative");
		this.input = input;
		outputState = state;
		this.time = time;
		if(input != null && !(input instanceof String || input instanceof Block || input instanceof IBlockState))
			throw new IllegalArgumentException("input must be an oredict String, Block, or IBlockState");
	}

	/**
	 * This gets called every tick, please be careful with your checks.
	 */
	public boolean matches(World world, BlockPos pos, SubTileEntity pureDaisy, IBlockState state) {
		if(input instanceof Block)
			return state.getBlock() == input;

		if(input instanceof IBlockState)
			return state == input;

		ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
		String oredict = (String) input;
		return isOreDict(stack, oredict);
	}

	private boolean isOreDict(ItemStack stack, String entry) {
		if(stack.isEmpty())
			return false;

		List<ItemStack> ores;
		if(oreMap.containsKey(entry))
			ores = oreMap.get(entry);
		else {
			ores = OreDictionary.getOres(entry);
			oreMap.put(entry, ores);
		}

		for(ItemStack ostack : ores) {
			ItemStack cstack = ostack.copy();
			if(cstack.getItemDamage() == Short.MAX_VALUE)
				cstack.setItemDamage(stack.getItemDamage());

			if(stack.isItemEqual(cstack))
				return true;
		}

		return false;
	}

	/**
	 * Returns true if the block was placed (and if the Pure Daisy should do particles and stuffs).
	 * Should only place the block if !world.isRemote, but should return true if it would've placed
	 * it otherwise. You may return false to cancel the normal particles and do your own.
	 */
	public boolean set(World world, BlockPos pos, SubTileEntity pureDaisy) {
		if(!world.isRemote)
			world.setBlockState(pos, outputState);
		return true;
	}

	public Object getInput() {
		return input;
	}

	public IBlockState getOutputState() {
		return outputState;
	}

	public int getTime() {
		return time;
	}

}
