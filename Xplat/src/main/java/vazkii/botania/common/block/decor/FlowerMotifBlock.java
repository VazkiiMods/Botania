/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FlowerBlock;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlowerMotifBlock extends FlowerBlock {
	private final boolean hidden;

	public FlowerMotifBlock(MobEffect effect, int effectDuration, Properties properties, boolean hidden) {
		super(effect, effectDuration, properties);
		this.hidden = hidden;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flags) {
		if (hidden) {
			tooltip.add(Component.translatable("block.botania.daybloom_motif.description").withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(Component.translatable("block.botania.hydroangeas_motif.description").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (!hidden) {
			super.fillItemCategory(group, items);
		}
	}
}
