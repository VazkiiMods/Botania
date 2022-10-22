package vazkii.botania.forge.data;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.LibMisc;

public class ForgeBiomeTagProvider extends TagsProvider<Biome> {
	protected ForgeBiomeTagProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, BuiltinRegistries.BIOME, LibMisc.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_DESERT_BONUS).addTag(Tags.Biomes.IS_DESERT);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_FOREST_BONUS).addTag(BiomeTags.IS_FOREST);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_FUNGAL_BONUS).addTag(Tags.Biomes.IS_MUSHROOM);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_MESA_BONUS).addTag(BiomeTags.IS_BADLANDS);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_MOUNTAIN_BONUS).addTag(BiomeTags.IS_MOUNTAIN);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_PLAINS_BONUS).addTag(Tags.Biomes.IS_PLAINS);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_SWAMP_BONUS).addTag(Tags.Biomes.IS_SWAMP);
		tag(BotaniaTags.Biomes.MARIMORPHOSIS_TAIGA_BONUS).addTag(BiomeTags.IS_TAIGA);
	}
}
