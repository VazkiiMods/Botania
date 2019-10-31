/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 14, 2014, 8:37:26 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import vazkii.botania.api.recipe.IElvenItem;

public class BlockStorage extends BlockMod implements IElvenItem {

	public BlockStorage(Properties builder) {
		super(builder);
	}

	@Override
	public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beaconPos) {
		return true;
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return this == ModBlocks.elementiumBlock;
	}
}
