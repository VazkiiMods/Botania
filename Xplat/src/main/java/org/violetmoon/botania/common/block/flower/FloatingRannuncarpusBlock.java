/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.common.block.flower;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import org.violetmoon.botania.api.block_entity.SpecialFlowerBlockEntity;
import org.violetmoon.botania.api.state.BotaniaStateProperties;
import org.violetmoon.botania.api.state.enums.RannuncarpusMode;

import java.util.function.Supplier;

public class FloatingRannuncarpusBlock extends PoweredFloatingSpecialFlowerBlock {

	public FloatingRannuncarpusBlock(Properties props,
			Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType) {
		super(props, blockEntityType);
		registerDefaultState(defaultBlockState()
				.setValue(BotaniaStateProperties.RANNUNCARPUS_MODE, RannuncarpusMode.STATE_INSENSITIVE));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BotaniaStateProperties.RANNUNCARPUS_MODE);
	}
}
