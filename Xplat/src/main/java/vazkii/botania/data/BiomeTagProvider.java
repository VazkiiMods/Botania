package vazkii.botania.data;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;

import vazkii.botania.common.lib.BotaniaTags;

public class BiomeTagProvider extends TagsProvider<Biome> {
	public BiomeTagProvider(DataGenerator generator) {
		super(generator, BuiltinRegistries.BIOME);
	}

	@Override
	protected void addTags() {
		// todo 1.19 generate marimorphosis tags
		// todo 1.19 do we still want to block icy biomes from flowers and mushrooms? seems arbitrary

		// need to do this so we can use them in addTag. It generates a dummy empty file,
		// but whatever.
		tag(BiomeTags.IS_OVERWORLD);
		tag(BiomeTags.IS_NETHER);

		tag(BotaniaTags.Biomes.MYSTICAL_FLOWER_SPAWNLIST).addTag(BiomeTags.IS_OVERWORLD);
		tag(BotaniaTags.Biomes.MYSTICAL_FLOWER_BLOCKLIST)
				.addOptionalTag(new ResourceLocation("forge", "is_mushroom"))
				.addOptionalTag(new ResourceLocation("c", "mushroom"));

		tag(BotaniaTags.Biomes.MYSTICAL_MUSHROOM_SPAWNLIST).addTag(BiomeTags.IS_OVERWORLD)
				.addTag(BiomeTags.IS_NETHER);
		tag(BotaniaTags.Biomes.MYSTICAL_MUSHROOM_BLOCKLIST);
	}
}
