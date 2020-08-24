/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.FlowerBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import java.util.List;

public class BlockMotifFlower extends FlowerBlock {
	private final boolean hidden;

	public BlockMotifFlower(Effect effect, int effectDuration, Properties properties, boolean hidden) {
		super(effect, effectDuration, properties);
		this.hidden = hidden;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (hidden) {
			tooltip.add(new TranslationTextComponent("block.botania.daybloom_motif.description").mergeStyle(TextFormatting.GRAY));
		} else {
			tooltip.add(new TranslationTextComponent("block.botania.hydroangeas_motif.description").mergeStyle(TextFormatting.GRAY));
		}
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (!hidden) {
			super.fillItemGroup(group, items);
		}
	}
}
