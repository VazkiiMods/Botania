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
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.handler.ContributorList;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemWithBannerPattern;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.Locale;
import java.util.regex.Pattern;

public class TinyPotatoBlockItem extends BlockItem implements ItemWithBannerPattern {

	private static final Pattern TYPOS = Pattern.compile(
			"(?!^vazkii$)" // Do not match the properly spelled version 
					+ "^v[ao]{1,2}[sz]{0,2}[ak]{1,2}(i){1,2}l{0,2}$",
			Pattern.CASE_INSENSITIVE
	);

	private static final String[] NOT_MY_NAME = {
			"Six letter word just to get me along",
			"It's a intricacy and I'm coding on my mod and I,",
			"I keep fixin', and keepin' it together",
			"People around gotta find something to play now",
			"Holding back, every mod's the same",
			"Don't wanna be a loser",
			"Listen to me, oh no, I don't break anything at all",
			"But with nothing to consider they forget my name",
			"'ame, 'ame, 'ame",
			"They call me Vaskii",
			"They call me Vazki",
			"They call me Voskii",
			"They call me Vazkki",
			"That's not my name",
			"That's not my name",
			"That's not my name",
			"That's not my name"
	};

	private static final String TAG_TICKS = "notMyNameTicks";

	public TinyPotatoBlockItem(Block block, Properties props) {
		super(block, props);
	}

	@Override
	public TagKey<BannerPattern> getBannerPattern() {
		return BotaniaTags.BannerPatterns.PATTERN_ITEM_TINY_POTATO;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity e, int t, boolean idunno) {
		if (!world.isClientSide && e instanceof Player player && e.tickCount % 30 == 0
				&& TYPOS.matcher(stack.getHoverName().getString()).matches()) {
			int ticks = ItemNBTHelper.getInt(stack, TAG_TICKS, 0);
			if (ticks < NOT_MY_NAME.length) {
				player.sendSystemMessage(Component.literal(NOT_MY_NAME[ticks]).withStyle(ChatFormatting.RED));
				ItemNBTHelper.setInt(stack, TAG_TICKS, ticks + 1);
			}
		}
	}

	public static boolean isEnchantedName(@NotNull Component name, @Nullable StringBuilder nameBuilder) {
		String trimmed = name.getString().trim();
		var nameString = trimmed.toLowerCase(Locale.ROOT);
		for (var prefix : ENCHANTMENT_NAMES) {
			if (nameString.equals(prefix) || nameString.startsWith(prefix + " ")) {
				if (nameBuilder != null) {
					if (trimmed.length() > prefix.length()) {
						nameBuilder.append(trimmed, prefix.length() + 1, trimmed.length());
					} else {
						nameBuilder.append(trimmed);
					}
				}
				return true;
			}
		}
		if (nameBuilder != null) {
			nameBuilder.append(trimmed);
		}
		return false;
	}

	public static String removeFromFront(String name, String match) {
		return name.substring(match.length()).trim();
	}

	@Override
	public boolean isFoil(@NotNull ItemStack stack) {
		return super.isFoil(stack) || isEnchantedName(stack.getHoverName(), null);
	}

	@SoftImplement("IForgeItem")
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@SoftImplement("IForgeItem") // TODO implement on fabric
	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
		return armorType == getEquipmentSlot(stack) && entity instanceof Player player
				&& ContributorList.hasFlower(player.getGameProfile().getName().toLowerCase(Locale.ROOT));
	}
}
