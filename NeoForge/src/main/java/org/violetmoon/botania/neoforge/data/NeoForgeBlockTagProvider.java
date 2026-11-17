/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.data.util.DummyTagLookup;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class NeoForgeBlockTagProvider extends IntrinsicHolderTagsProvider<Block> {
	private static final Set<TagKey<Block>> REQUIRED_TAGS = Set.of(
			BotaniaTags.Blocks.SMALL_MYSTICAL_FLOWERS,
			BotaniaTags.Blocks.SPECIAL_FLOWERS
	);

	public NeoForgeBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.BLOCK, provider, DummyTagLookup.completedFuture(REQUIRED_TAGS),
				block -> ResourceKey.create(Registries.BLOCK, BuiltInRegistries.BLOCK.getKey(block)),
				BotaniaAPI.MODID, null);
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania block tags (NeoForge-specific)";
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("buzzier_bees", "flower_blacklist")))
				.addTag(BotaniaTags.Blocks.SMALL_MYSTICAL_FLOWERS)
				.addTag(BotaniaTags.Blocks.SPECIAL_FLOWERS);
	}
}
