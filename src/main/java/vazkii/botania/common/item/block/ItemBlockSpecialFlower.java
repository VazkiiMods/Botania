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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

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

	@Environment(EnvType.CLIENT)
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
