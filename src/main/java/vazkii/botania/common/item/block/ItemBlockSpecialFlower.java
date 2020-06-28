/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemBlockSpecialFlower extends BlockItem {
	private static final ITag.INamedTag<Item> GENERATING = ModTags.Items.GENERATING_SPECIAL_FLOWERS;
	private static final ITag.INamedTag<Item> FUNCTIONAL = ModTags.Items.FUNCTIONAL_SPECIAL_FLOWERS;
	private static final ITag.INamedTag<Item> MISC = ModTags.Items.MISC_SPECIAL_FLOWERS;

	public ItemBlockSpecialFlower(Block block1, Properties props) {
		super(block1, props);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
		// Prevent crash when tooltips queried before configs load
		if (Botania.finishedLoading) {
			if (GENERATING.func_230235_a_(this)) {
				tooltip.add(new TranslationTextComponent("botania.flowerType.generating").func_240701_a_(TextFormatting.ITALIC, TextFormatting.BLUE));
			} else if (FUNCTIONAL.func_230235_a_(this)) {
				tooltip.add(new TranslationTextComponent("botania.flowerType.functional").func_240701_a_(TextFormatting.ITALIC, TextFormatting.BLUE));
			} else if (MISC.func_230235_a_(this)) {
				tooltip.add(new TranslationTextComponent("botania.flowerType.misc").func_240701_a_(TextFormatting.ITALIC, TextFormatting.BLUE));
			}

			if (ConfigHandler.CLIENT.referencesEnabled.get()) {
				String key = getTranslationKey() + ".reference";
				IFormattableTextComponent lore = new TranslationTextComponent(key);
				if (!lore.getString().equals(key)) {
					tooltip.add(lore.func_240701_a_(TextFormatting.ITALIC, TextFormatting.GRAY));
				}
			}
		}
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		CompoundNBT tag = stack.getChildTag("BlockEntityTag");
		return tag != null && tag.contains(TileEntityGeneratingFlower.TAG_PASSIVE_DECAY_TICKS);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		CompoundNBT tag = stack.getChildTag("BlockEntityTag");
		if (tag != null) {
			return tag.getInt(TileEntityGeneratingFlower.TAG_PASSIVE_DECAY_TICKS) / (double) BotaniaAPI.instance().internalHandler().getPassiveFlowerDecay();
		}
		return 0;
	}
}
