/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 1, 2015, 7:17:45 PM (GMT)]
 */
package vazkii.botania.common.block.decor.slabs;

import net.minecraft.block.BlockSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockPavementSlab extends BlockLivingSlab {

	final int index;

	public BlockPavementSlab(boolean full, EnumDyeColor color, int index) {
		super(full, ModFluffBlocks.pavement.getDefaultState().withProperty(BotaniaStateProps.PAVEMENT_COLOR, color));
		this.index = index;
		setHardness(2F);
		setResistance(10F);
	}

	@Override
	public BlockSlab getFullBlock() {
		return (BlockSlab) ModFluffBlocks.pavementFullSlabs[index];
	}

	@Override
	public BlockSlab getSingleBlock() {
		return (BlockSlab) ModFluffBlocks.pavementSlabs[index];
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.pavement;
	}


}
