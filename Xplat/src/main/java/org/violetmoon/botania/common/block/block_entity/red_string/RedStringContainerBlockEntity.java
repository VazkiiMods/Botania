/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.red_string;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Objects;

public class RedStringContainerBlockEntity extends RedStringBlockEntity {
	public RedStringContainerBlockEntity(BlockPos pos, BlockState state) {
		this(BotaniaBlockEntities.RED_STRINGED_CONTAINER, pos, state);
	}

	protected RedStringContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public boolean acceptBlock(BlockPos pos) {
		return XplatAbstractions.INSTANCE.isRedStringContainerTarget(Objects.requireNonNull(level), pos);
	}
}
