/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.GaiaHeadBlockEntity;

public class GaiaHeadBlock extends SkullBlock {
	public static final String GAIA_SKULL_TYPE = "gaia";
	public static final SkullBlock.Type GAIA_TYPE = () -> GAIA_SKULL_TYPE;

	static {
		Type.TYPES.put(GAIA_SKULL_TYPE, GAIA_TYPE);
	}

	public GaiaHeadBlock(Properties builder) {
		super(GAIA_TYPE, builder);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GaiaHeadBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
			Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		// we always create the ticker, since the head might be viewed through a piglin or dragon's eyes
		return level.isClientSide
				? createTickerHelper(blockEntityType, BotaniaBlockEntities.GAIA_HEAD, GaiaHeadBlockEntity::animation)
				: null;
	}
}
