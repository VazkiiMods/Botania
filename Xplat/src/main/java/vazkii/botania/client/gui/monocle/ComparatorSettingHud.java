package vazkii.botania.client.gui.monocle;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public record ComparatorSettingHud(BlockState state) implements SimpleTextAndIconMonocleHud {

	@Override
	public Component getDisplayString() {
		return Component
				.translatable("botaniamisc.monocle.comparator."
						+ state.getValue(ComparatorBlock.MODE).getSerializedName())
				.withStyle(state.getValue(BlockStateProperties.POWERED)
						? ChatFormatting.RED
						: ChatFormatting.WHITE);
	}

	@Override
	public ItemStack getDisplayStack() {
		return new ItemStack(state.getBlock());
	}
}
