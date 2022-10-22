package vazkii.botania.fabric.data;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.biome.Biome;

import vazkii.botania.common.lib.BotaniaTags;

public class FabricBiomeTagProvider extends TagsProvider<Biome> {
	public FabricBiomeTagProvider(DataGenerator generator) {
		super(generator, BuiltinRegistries.BIOME);
	}

	@Override
	protected void addTags() {
		// need to do this so we can use them in addTag. It generates a dummy empty file,
		// but whatever.
		tag(ConventionalBiomeTags.DESERT);
		tag(ConventionalBiomeTags.FOREST);
		tag(ConventionalBiomeTags.MUSHROOM);
		tag(ConventionalBiomeTags.MESA);
		tag(ConventionalBiomeTags.MOUNTAIN);
		tag(ConventionalBiomeTags.PLAINS);
		tag(ConventionalBiomeTags.SWAMP);
		tag(ConventionalBiomeTags.TAIGA);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_DESERT_BONUS).addTag(ConventionalBiomeTags.DESERT);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_FOREST_BONUS).addTag(ConventionalBiomeTags.FOREST);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_FUNGAL_BONUS).addTag(ConventionalBiomeTags.MUSHROOM);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_MESA_BONUS).addTag(ConventionalBiomeTags.MESA);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_MOUNTAIN_BONUS).addTag(ConventionalBiomeTags.MOUNTAIN);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_PLAINS_BONUS).addTag(ConventionalBiomeTags.PLAINS);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_SWAMP_BONUS).addTag(ConventionalBiomeTags.SWAMP);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_TAIGA_BONUS).addTag(ConventionalBiomeTags.TAIGA);
	}
}
