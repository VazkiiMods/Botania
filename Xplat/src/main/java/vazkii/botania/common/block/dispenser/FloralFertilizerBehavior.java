package vazkii.botania.common.block.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.item.FloralFertilizerItem;
import vazkii.botania.common.lib.BotaniaTags;

public class FloralFertilizerBehavior extends OptionalDispenseItemBehavior {
	@Override
	protected ItemStack execute(BlockSource source, ItemStack item) {
		BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		ServerLevel level = source.level();
		BlockState state = level.getBlockState(pos);
		setSuccess(state.is(BotaniaTags.Blocks.FERTILIZER_SPREADABLE_PLANTS)
				&& !state.is(BotaniaTags.Blocks.FERTILIZER_EXCLUDED_PLANTS));

		if (isSuccess()) {
			FloralFertilizerItem.spreadPlant(pos, level, state);
			item.shrink(1);
			if (!level.isClientSide) {
				level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 0);
				level.levelEvent(LevelEvent.PARTICLES_BEE_GROWTH, pos, 10);
			}
		}
		return item;
	}
}
