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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import vazkii.botania.client.integration.shared.LocaleHelper;

public record RepeaterSettingHud(BlockState state) implements SimpleTextAndIconMonocleHud {

	@Override
	public Component getDisplayString() {
		MutableComponent delayText = Component
				.translatable("botaniamisc.monocle.repeater.delay",
						LocaleHelper.formatAsDecimalFraction(0.1f * state.getValue(RepeaterBlock.DELAY), 1))
				.withStyle(state.getValue(BlockStateProperties.POWERED)
						? ChatFormatting.RED
						: ChatFormatting.WHITE);
		return state.getValue(BlockStateProperties.LOCKED)
				? Component.translatable("botaniamisc.monocle.repeater.locked", delayText)
						.withStyle(ChatFormatting.GRAY)
				: delayText;
	}

	@Override
	public ItemStack getDisplayStack() {
		return new ItemStack(state.getBlock());
	}
}
