package vazkii.botania.client.gui.monocle;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import org.jetbrains.annotations.Nullable;

public record RedstonePowerHud(BlockState state) implements SimpleTextAndIconMonocleHud {

	@Deprecated(forRemoval = true)
	@Nullable
	public static RedstonePowerHud fallbackFactory(Level level, BlockPos blockPos, BlockState state,
			@Nullable BlockEntity blockEntity, Unit unit) {
		return isApplicable(state) ? new RedstonePowerHud(state) : null;
	}

	public static boolean isApplicable(BlockState state) {
		return state.hasProperty(BlockStateProperties.POWER);
	}

	@Override
	public ItemStack getDisplayStack() {
		return new ItemStack(state.getBlock());
	}

	@Override
	public Component getDisplayString() {
		Integer power = state.getValue(BlockStateProperties.POWER);
		return Component.literal(power.toString())
				.withStyle(power.equals(0) ? ChatFormatting.GRAY : ChatFormatting.RED);
	}
}
