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

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.stairs.BlockModStairs;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockSpecialQuartzStairs extends BlockModStairs {

	public BlockSpecialQuartzStairs(Block source) {
		super(source.getDefaultState(), "quartzStairs" + ((BlockSpecialQuartz) source).type);
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return this == ModFluffBlocks.elfQuartzStairs ? LexiconData.elvenResources : super.getEntry(world, pos, player, lexicon);
	}

}