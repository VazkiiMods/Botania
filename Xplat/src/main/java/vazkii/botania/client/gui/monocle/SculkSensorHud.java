package vazkii.botania.client.gui.monocle;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;

public record SculkSensorHud(BlockState state) implements SimpleTextAndIconMonocleHud {
	@Override
	public ItemStack getDisplayStack() {
		return new ItemStack(state.getBlock());
	}

	@Override
	public Component getDisplayString() {
		Integer power = state.getValue(BlockStateProperties.POWER);
		var powerComponent = Component.literal(power.toString())
				.withStyle(power.equals(0) ? ChatFormatting.GRAY : ChatFormatting.RED);
		SculkSensorPhase phase = state.getValue(BlockStateProperties.SCULK_SENSOR_PHASE);
		return Component
				.translatable("botaniamisc.monocle.sculk_sensor." + phase.getSerializedName(), powerComponent)
				.withStyle(ChatFormatting.WHITE);
	}
}
