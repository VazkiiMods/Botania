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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AnimalMode;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.EyeOfTheAncientsBlockEntity;

public class EyeOfTheAncientsBlock extends BotaniaWaterloggedEntityBlock {

	private static final VoxelShape SHAPE = box(4, 4, 4, 12, 12, 12);

	public EyeOfTheAncientsBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState()
				.setValue(BotaniaStateProperties.ANIMAL_MODE, AnimalMode.ALL)
				.setValue(BlockStateProperties.POWER, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BotaniaStateProperties.ANIMAL_MODE, BlockStateProperties.POWER);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return state.getOptionalValue(BlockStateProperties.POWER).orElse(0);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EyeOfTheAncientsBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (!level.isClientSide()) {
			return createTickerHelper(type, BotaniaBlockEntities.EYE_OF_THE_ANCIENTS, EyeOfTheAncientsBlockEntity::serverTick);
		}
		return null;
	}
}
