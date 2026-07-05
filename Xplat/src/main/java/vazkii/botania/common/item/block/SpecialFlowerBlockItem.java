/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.List;

public class SpecialFlowerBlockItem extends BlockItem {

	public SpecialFlowerBlockItem(Block block, Properties props) {
		super(block, props);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		// Prevent crash when tooltips queried before configs load
		// TODO: This should be moved to being builtin tooltip text components on the relevant items
		if (BotaniaConfig.client() != null) {
			if (stack.is(BotaniaTags.Items.GENERATING_SPECIAL_FLOWERS)
					|| stack.is(BotaniaTags.Items.GENERATING_FLOATING_FLOWERS)) {
				tooltip.add(Component.translatable("botania.flowerType.generating").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
			} else if (stack.is(BotaniaTags.Items.FUNCTIONAL_SPECIAL_FLOWERS)
					|| stack.is(BotaniaTags.Items.FUNCTIONAL_FLOATING_FLOWERS)) {
				tooltip.add(Component.translatable("botania.flowerType.functional").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
			} else if (stack.is(BotaniaTags.Items.MISC_SPECIAL_FLOWERS)
					|| stack.is(BotaniaTags.Items.MISC_FLOATING_FLOWERS)) {
				tooltip.add(Component.translatable("botania.flowerType.misc").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
			}

			if (BotaniaConfig.client().referencesEnabled()) {
				String key = getDescriptionId() + ".reference";
				MutableComponent lore = Component.translatable(key);
				if (!lore.getString().equals(key)) {
					tooltip.add(lore.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
				}
			}
		}
	}
}
