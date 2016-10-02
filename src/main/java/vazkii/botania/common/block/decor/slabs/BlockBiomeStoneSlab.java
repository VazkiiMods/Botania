/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 29, 2015, 7:34:16 PM (GMT)]
 */
package vazkii.botania.common.block.decor.slabs;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockBiomeStoneSlab extends BlockLivingSlab {

	final int index;

	public BlockBiomeStoneSlab(boolean full, IBlockState state, int index) {
		super(full, state);
		this.index = index;
		setHardness(1.5F);
		setResistance(10F);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.biomeStoneFullSlabs[index];
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.biomeStoneSlabs[index];
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.marimorphosis;
	}

}
