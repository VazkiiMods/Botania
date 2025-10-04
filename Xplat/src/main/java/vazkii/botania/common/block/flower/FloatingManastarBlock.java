package vazkii.botania.common.block.flower;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.ManastarState;

import java.util.function.Supplier;

public class FloatingManastarBlock extends FloatingSpecialFlowerBlock {

	public FloatingManastarBlock(Properties props,
			Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType) {
		super(props, blockEntityType);
		registerDefaultState(defaultBlockState()
				.setValue(BotaniaStateProperties.MANASTAR_STATE, ManastarState.NEUTRAL));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BotaniaStateProperties.MANASTAR_STATE);
	}

}
