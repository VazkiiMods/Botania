/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.entity.BannerPattern;

import vazkii.botania.common.block.BotaniaBannerPatterns;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class BannerPatternTagsProvider extends TagsProvider<BannerPattern> {
	public BannerPatternTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Registries.BANNER_PATTERN, lookupProvider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(BotaniaTags.BannerPatterns.PATTERN_ITEM_BOTANIA).add(
				BotaniaBannerPatterns.LEXICON, BotaniaBannerPatterns.LOGO, BotaniaBannerPatterns.TINY_POTATO
		);
		this.tag(BotaniaTags.BannerPatterns.PATTERN_ITEM_MATERIALS).add(
				BotaniaBannerPatterns.FISH, BotaniaBannerPatterns.FLOWER, BotaniaBannerPatterns.SAPLING
		);
		this.tag(BotaniaTags.BannerPatterns.PATTERN_ITEM_SPARK_AUGMENTS).add(
				BotaniaBannerPatterns.SPARK_DISPERSIVE, BotaniaBannerPatterns.SPARK_DOMINANT,
				BotaniaBannerPatterns.SPARK_RECESSIVE, BotaniaBannerPatterns.SPARK_ISOLATED
		);
		this.tag(BotaniaTags.BannerPatterns.PATTERN_ITEM_TOOLS).add(
				BotaniaBannerPatterns.AXE, BotaniaBannerPatterns.HOE, BotaniaBannerPatterns.PICKAXE,
				BotaniaBannerPatterns.SHOVEL, BotaniaBannerPatterns.SWORD
		);
	}
}
