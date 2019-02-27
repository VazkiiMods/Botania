/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 11, 2014, 1:12:50 AM (GMT)]
 */
package vazkii.botania.common.block.decor.quartz;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.stairs.BlockModStairs;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockElfQuartzStairs extends BlockModStairs {

	public BlockElfQuartzStairs(IBlockState state, Properties props) {
		super(state, props);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.elvenResources;
	}

}