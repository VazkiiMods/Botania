/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.data.gog;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.data.util.DummyTagLookup;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.gogRL;

public class GogBlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {
	private static final Set<TagKey<Block>> REQUIRED_TAGS = Set.of(
			BlockTags.DIRT
	);

	public GogBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Registries.BLOCK, provider, DummyTagLookup.completedFuture(REQUIRED_TAGS),
				block -> ResourceKey.create(Registries.BLOCK, BuiltInRegistries.BLOCK.getKey(block)),
				BotaniaAPI.MODID, null);
	}

	@NotNull
	@Override
	public String getName() {
		return "GoG block tags";
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(TagKey.create(Registries.BLOCK, gogRL("pebble_sources"))).addTag(BlockTags.DIRT);
	}
}
