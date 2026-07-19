/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.fabric.data.xplat;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.data.util.DummyTagLookup;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ConventionalBiomeTagProvider extends TagsProvider<Biome> {
	private static final Set<TagKey<Biome>> RELEVANT_TAGS = Set.of(
			ConventionalBiomeTags.IS_OVERWORLD,
			ConventionalBiomeTags.IS_NETHER,
			ConventionalBiomeTags.IS_BADLANDS,
			ConventionalBiomeTags.IS_BEACH,
			ConventionalBiomeTags.IS_COLD,
			ConventionalBiomeTags.IS_CONIFEROUS_TREE,
			ConventionalBiomeTags.IS_DESERT,
			ConventionalBiomeTags.IS_FOREST,
			ConventionalBiomeTags.IS_JUNGLE,
			ConventionalBiomeTags.IS_MOUNTAIN,
			ConventionalBiomeTags.IS_MUSHROOM,
			ConventionalBiomeTags.IS_PLAINS,
			ConventionalBiomeTags.IS_SAVANNA,
			ConventionalBiomeTags.IS_SNOWY,
			ConventionalBiomeTags.IS_SWAMP,
			ConventionalBiomeTags.IS_UNDERGROUND
	);

	public ConventionalBiomeTagProvider(FabricDataOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Registries.BIOME, lookupProvider, DummyTagLookup.completedFuture(RELEVANT_TAGS));
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		tag(BotaniaTags.Biomes.MYSTICAL_FLOWER_SPAWNLIST)
				.addTag(ConventionalBiomeTags.IS_OVERWORLD);
		tag(BotaniaTags.Biomes.MYSTICAL_FLOWER_BLOCKLIST)
				.addTag(ConventionalBiomeTags.IS_MUSHROOM);

		tag(BotaniaTags.Biomes.SHIMMERING_MUSHROOM_SPAWNLIST)
				.addTag(ConventionalBiomeTags.IS_OVERWORLD)
				.addTag(ConventionalBiomeTags.IS_NETHER);
		tag(BotaniaTags.Biomes.SHIMMERING_MUSHROOM_BLOCKLIST)
				.add(Biomes.DEEP_DARK);

		tag(BotaniaTags.Biomes.MARIMORPHOSIS_SOLITE_BONUS)
				.addTag(ConventionalBiomeTags.IS_DESERT)
				.addTag(ConventionalBiomeTags.IS_SAVANNA);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_FUCHSITE_BONUS)
				.addTag(ConventionalBiomeTags.IS_FOREST);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_MYCELITE_BONUS)
				.addTag(ConventionalBiomeTags.IS_MUSHROOM)
				.addTag(ConventionalBiomeTags.IS_UNDERGROUND);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_ROSY_TALC_BONUS)
				.addTag(ConventionalBiomeTags.IS_BADLANDS)
				.addTag(ConventionalBiomeTags.IS_SAVANNA);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_GNEISS_BONUS)
				.addTag(ConventionalBiomeTags.IS_MOUNTAIN);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_TALC_BONUS)
				.addTag(ConventionalBiomeTags.IS_PLAINS)
				.addTag(ConventionalBiomeTags.IS_BEACH);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_CATACLASITE_BONUS)
				.addTag(ConventionalBiomeTags.IS_SWAMP)
				.addTag(ConventionalBiomeTags.IS_JUNGLE);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_LUNITE_BONUS)
				.addTag(ConventionalBiomeTags.IS_CONIFEROUS_TREE)
				.addTag(ConventionalBiomeTags.IS_COLD)
				.addTag(ConventionalBiomeTags.IS_SNOWY);

		tag(BotaniaTags.Biomes.ORECHID_STONE_COPPER_BONUS)
				.add(Biomes.DRIPSTONE_CAVES);
		tag(BotaniaTags.Biomes.ORECHID_STONE_EMERALD_BONUS)
				.addTag(ConventionalBiomeTags.IS_MOUNTAIN);
		tag(BotaniaTags.Biomes.ORECHID_STONE_GOLD_BONUS)
				.addTag(ConventionalBiomeTags.IS_BADLANDS);
		tag(BotaniaTags.Biomes.ORECHID_DEEPSLATE_COPPER_BONUS)
				.add(Biomes.DRIPSTONE_CAVES);
		tag(BotaniaTags.Biomes.ORECHID_DEEPSLATE_EMERALD_BONUS)
				.addTag(ConventionalBiomeTags.IS_MOUNTAIN);
	}

	@Override
	public String getName() {
		return "Conventional " + super.getName();
	}

}
