/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package org.violetmoon.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.item.HornItem;

public class DrumOfTheWildBlock extends DrumBlock {
	public DrumOfTheWildBlock(Properties builder) {
		super(builder);
	}

	@Override
	public void activate(Level level, BlockPos pos) {
		HornItem.breakBlocks(level, BotaniaItems.HORN_OF_THE_WILD, pos);
	}
}
