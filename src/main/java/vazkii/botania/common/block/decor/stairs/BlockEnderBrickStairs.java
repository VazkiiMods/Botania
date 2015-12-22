/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 7, 2015, 10:38:15 PM (GMT)]
 */
package vazkii.botania.common.block.decor.stairs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.EndBrickVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockEnderBrickStairs extends BlockLivingStairs {

	public BlockEnderBrickStairs() {
		super(ModBlocks.endStoneBrick.getDefaultState().withProperty(BotaniaStateProps.ENDBRICK_VARIANT, EndBrickVariant.ENDER_BRICKS));
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.endStoneDecor;
	}

}
