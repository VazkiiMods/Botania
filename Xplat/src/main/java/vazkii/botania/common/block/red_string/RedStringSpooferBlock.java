/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.red_string;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.red_string.RedStringBlockEntity;
import vazkii.botania.common.block.block_entity.red_string.RedStringSpooferBlockEntity;

public class RedStringSpooferBlock extends RedStringBlock {

	public RedStringSpooferBlock(BlockBehaviour.Properties builder) {
		super(builder);
	}

	@Override
	public RedStringBlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RedStringSpooferBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, BotaniaBlockEntities.RED_STRING_RELAY, RedStringSpooferBlockEntity::commonTick);
	}

}
