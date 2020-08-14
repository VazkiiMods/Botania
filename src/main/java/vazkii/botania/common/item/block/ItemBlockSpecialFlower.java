/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tag.Tag;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemBlockSpecialFlower extends BlockItem {
	private static final Tag.Identified<Item> GENERATING = ModTags.Items.GENERATING_SPECIAL_FLOWERS;
	private static final Tag.Identified<Item> FUNCTIONAL = ModTags.Items.FUNCTIONAL_SPECIAL_FLOWERS;
	private static final Tag.Identified<Item> MISC = ModTags.Items.MISC_SPECIAL_FLOWERS;

	public ItemBlockSpecialFlower(Block block1, Settings props) {
		super(block1, props);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(@Nonnull ItemStack stack, World world, @Nonnull List<Text> tooltip, @Nonnull TooltipContext flag) {
		// Prevent crash when tooltips queried before configs load
		if (Botania.finishedLoading) {
			if (world != null) {
				if (GENERATING.contains(this)) {
					tooltip.add(new TranslatableText("botania.flowerType.generating").formatted(Formatting.ITALIC, Formatting.BLUE));
				} else if (FUNCTIONAL.contains(this)) {
					tooltip.add(new TranslatableText("botania.flowerType.functional").formatted(Formatting.ITALIC, Formatting.BLUE));
				} else if (MISC.contains(this)) {
					tooltip.add(new TranslatableText("botania.flowerType.misc").formatted(Formatting.ITALIC, Formatting.BLUE));
				}
			}

			if (ConfigHandler.CLIENT.referencesEnabled.getValue()) {
				String key = getTranslationKey() + ".reference";
				MutableText lore = new TranslatableText(key);
				if (!lore.getString().equals(key)) {
					tooltip.add(lore.formatted(Formatting.ITALIC, Formatting.GRAY));
				}
			}
		}
	}

	/* todo 1.16-fabric
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		CompoundTag tag = stack.getSubTag("BlockEntityTag");
		return tag != null && tag.contains(TileEntityGeneratingFlower.TAG_PASSIVE_DECAY_TICKS);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		CompoundTag tag = stack.getSubTag("BlockEntityTag");
		if (tag != null) {
			return tag.getInt(TileEntityGeneratingFlower.TAG_PASSIVE_DECAY_TICKS) / (double) BotaniaAPI.instance().getPassiveFlowerDecay();
		}
		return 0;
	}
	*/
}
