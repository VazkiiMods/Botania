/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 30, 2015, 3:27:20 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.lexicon.LexiconData;

import java.util.Map;

public class SubTileOrechidIgnem extends SubTileOrechid {

	private static final int COST = 20000;

	@Override
	public boolean canOperate() {
		return supertile.getWorld().provider.isNether();
	}

	@Override
	public Map<String, Integer> getOreMap() {
		return BotaniaAPI.oreWeightsNether;
	}

	@Override
	public Predicate<IBlockState> getReplaceMatcher() {
		return state -> state.getBlock() == Blocks.NETHERRACK;
	}

	@Override
	public int getCost() {
		return COST;
	}

	@Override
	public int getColor() {
		return 0xAE3030;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.orechidIgnem;
	}

}
