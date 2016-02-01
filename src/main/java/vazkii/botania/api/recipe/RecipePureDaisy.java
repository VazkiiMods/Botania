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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.subtile.SubTileEntity;

public class RecipePureDaisy {

	private static final Map<String, List<ItemStack>> oreMap = new HashMap<>();

	Object input;
	IBlockState outputState;

	public RecipePureDaisy(Object input, IBlockState state) {
		this.input = input;
		this.outputState = state;

		if(input != null && !(input instanceof String || input instanceof Block))
			throw new IllegalArgumentException("input must be an oredict String or a Block.");
	}

	/**
	 * This gets called every tick, please be careful with your checks.
	 */
	public boolean matches(World world, BlockPos pos, SubTileEntity pureDaisy, IBlockState state) {
		if(input instanceof Block)
			return state.getBlock() == input;

		ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
		String oredict = (String) input;
		return isOreDict(stack, oredict);
	}

	public boolean isOreDict(ItemStack stack, String entry) {
		if(stack == null || stack.getItem() == null)
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
			world.setBlockState(pos, outputState, 1 | 2);
		return true;
	}

	public Object getInput() {
		return input;
	}

	public IBlockState getOutputState() {
		return outputState;
	}

}
