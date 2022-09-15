/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;

import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.common.lib.ModTags;

public class SparkAugmentItem extends Item implements ItemWithBannerPattern {
	public final SparkUpgradeType type;

	public SparkAugmentItem(Properties builder, SparkUpgradeType type) {
		super(builder);
		this.type = type;
	}

	@Override
	public TagKey<BannerPattern> getBannerPattern() {
		return switch (this.type) {
			case DOMINANT -> ModTags.BannerPatterns.PATTERN_ITEM_SPARK_DOMINANT;
			case RECESSIVE -> ModTags.BannerPatterns.PATTERN_ITEM_SPARK_RECESSIVE;
			case DISPERSIVE -> ModTags.BannerPatterns.PATTERN_ITEM_SPARK_DISPERSIVE;
			case ISOLATED -> ModTags.BannerPatterns.PATTERN_ITEM_SPARK_ISOLATED;
			case NONE -> throw new IllegalArgumentException("SparkAugmentItem with none type");
		};
	}

	public static ItemStack getByType(SparkUpgradeType type) {
		return switch (type) {
			case DOMINANT -> new ItemStack(BotaniaItems.sparkUpgradeDominant);
			case RECESSIVE -> new ItemStack(BotaniaItems.sparkUpgradeRecessive);
			case DISPERSIVE -> new ItemStack(BotaniaItems.sparkUpgradeDispersive);
			case ISOLATED -> new ItemStack(BotaniaItems.sparkUpgradeIsolated);
			default -> ItemStack.EMPTY;
		};
	}

}
