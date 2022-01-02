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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemBlockSpecialFlower extends BlockItem {
	private static final Tag.Named<Item> GENERATING = ModTags.Items.GENERATING_SPECIAL_FLOWERS;
	private static final Tag.Named<Item> FUNCTIONAL = ModTags.Items.FUNCTIONAL_SPECIAL_FLOWERS;
	private static final Tag.Named<Item> MISC = ModTags.Items.MISC_SPECIAL_FLOWERS;

	public ItemBlockSpecialFlower(Block block1, Properties props) {
		super(block1, props);
	}

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, Level world, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		// Prevent crash when tooltips queried before configs load
		if (Botania.configLoaded) {
			if (world != null) {
				if (GENERATING.contains(this)) {
					tooltip.add(new TranslatableComponent("botania.flowerType.generating").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
				} else if (FUNCTIONAL.contains(this)) {
					tooltip.add(new TranslatableComponent("botania.flowerType.functional").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
				} else if (MISC.contains(this)) {
					tooltip.add(new TranslatableComponent("botania.flowerType.misc").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
				}
			}

			if (ConfigHandler.CLIENT.referencesEnabled.getValue()) {
				String key = getDescriptionId() + ".reference";
				MutableComponent lore = new TranslatableComponent(key);
				if (!lore.getString().equals(key)) {
					tooltip.add(lore.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
				}
			}
		}
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		CompoundTag tag = stack.getTagElement("BlockEntityTag");
		return tag != null && tag.contains(TileEntityGeneratingFlower.TAG_PASSIVE_DECAY_TICKS);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		CompoundTag tag = stack.getTagElement("BlockEntityTag");
		if (tag != null) {
			float frac = 1 - tag.getInt(TileEntityGeneratingFlower.TAG_PASSIVE_DECAY_TICKS) / (float) BotaniaAPI.instance().getPassiveFlowerDecay();
			return Math.round(13F * frac);
		}
		return 0;
	}

	@Override
	public int getBarColor(ItemStack stack) {
		CompoundTag tag = stack.getTagElement("BlockEntityTag");
		if (tag != null) {
			float frac = 1 - tag.getInt(TileEntityGeneratingFlower.TAG_PASSIVE_DECAY_TICKS) / (float) BotaniaAPI.instance().getPassiveFlowerDecay();
			return Mth.hsvToRgb(frac / 3.0F, 1.0F, 1.0F);
		}
		return 0;
	}
}
