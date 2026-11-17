/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.gui.monocle;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public record DaylightDetectorHud(BlockState state) implements SimpleTextAndIconMonocleHud {
	@Override
	public ItemStack getDisplayStack() {
		return new ItemStack(state.getBlock());
	}

	@Override
	public Component getDisplayString() {
		Integer power = state.getValue(BlockStateProperties.POWER);
		var powerComponent = Component.literal(power.toString())
				.withStyle(power.equals(0) ? ChatFormatting.GRAY : ChatFormatting.RED);
		Boolean inverted = state.getValue(BlockStateProperties.INVERTED);
		return Component
				.translatable("botaniamisc.monocle.daylight_detector." + (inverted ? "night" : "day"), powerComponent)
				.withColor(inverted ? DyeColor.LIGHT_BLUE.getTextColor() : DyeColor.YELLOW.getTextColor());
	}
}
