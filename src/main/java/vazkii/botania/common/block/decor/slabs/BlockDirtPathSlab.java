/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 26, 2015, 10:15:05 PM (GMT)]
 */
package vazkii.botania.common.block.decor.slabs;

import net.minecraft.block.BlockSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockDirtPathSlab extends BlockLivingSlab {

	public BlockDirtPathSlab(boolean full) {
		super(full, ModBlocks.dirtPath.getDefaultState());
		setHardness(0.6F);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.dirtPathSlabFull;
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.dirtPathSlab;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.dirtPath;
	}

}
