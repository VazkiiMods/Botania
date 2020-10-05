/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.FlowerBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.BlockView;

import javax.annotation.Nullable;

import java.util.List;

public class BlockMotifFlower extends FlowerBlock {
	private final boolean hidden;

	public BlockMotifFlower(StatusEffect effect, int effectDuration, Settings properties, boolean hidden) {
		super(effect, effectDuration, properties);
		this.hidden = hidden;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, @Nullable BlockView worldIn, List<Text> tooltip, TooltipContext flags) {
		if (hidden) {
			tooltip.add(new TranslatableText("block.botania.daybloom_motif.description").formatted(Formatting.GRAY));
		} else {
			tooltip.add(new TranslatableText("block.botania.hydroangeas_motif.description").formatted(Formatting.GRAY));
		}
	}

	@Override
	public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> items) {
		if (!hidden) {
			super.addStacksForDisplay(group, items);
		}
	}
}
