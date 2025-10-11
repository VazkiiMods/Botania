package vazkii.botania.common.block.flower;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.component.BotaniaDataComponents;

import java.util.function.Supplier;

public class FloatingGeneratingFlowerWithCooldownBlock extends FloatingSpecialFlowerBlock {

	public FloatingGeneratingFlowerWithCooldownBlock(Properties props,
			Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> blockEntityType) {
		super(props, blockEntityType);
		registerDefaultState(defaultBlockState()
				.setValue(BotaniaStateProperties.GENERATING, false)
				.setValue(BotaniaStateProperties.ON_COOLDOWN, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BotaniaStateProperties.GENERATING, BotaniaStateProperties.ON_COOLDOWN);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context)
				.setValue(BotaniaStateProperties.ON_COOLDOWN,
						context.getItemInHand().has(BotaniaDataComponents.COOLDOWN));
	}

}
