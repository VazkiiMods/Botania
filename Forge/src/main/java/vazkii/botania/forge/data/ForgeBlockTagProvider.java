package vazkii.botania.forge.data;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFluffBlocks;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

public class ForgeBlockTagProvider extends BlockTagsProvider {
	public static final TagKey<Block> MUSHROOMS = forge("mushrooms");
	public static final TagKey<Block> ELEMENTIUM = forge("storage_blocks/elementium");
	public static final TagKey<Block> MANASTEEL = forge("storage_blocks/manasteel");
	public static final TagKey<Block> TERRASTEEL = forge("storage_blocks/terrasteel");

	public ForgeBlockTagProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, LibMisc.MOD_ID, helper);
	}

	@Override
	public String getName() {
		return "Botania block tags (Forge-specific)";
	}

	@Override
	protected void addTags() {
		tag(Tags.Blocks.STORAGE_BLOCKS_QUARTZ).add(
				BotaniaFluffBlocks.darkQuartz, BotaniaFluffBlocks.manaQuartz, BotaniaFluffBlocks.blazeQuartz,
				BotaniaFluffBlocks.lavenderQuartz, BotaniaFluffBlocks.redQuartz, BotaniaFluffBlocks.elfQuartz, BotaniaFluffBlocks.sunnyQuartz
		);

		for (DyeColor color : DyeColor.values()) {
			this.tag(MUSHROOMS).add(BotaniaBlocks.getMushroom(color));
		}

		tag(TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("buzzier_bees", "flower_blacklist")))
				.addTag(ModTags.Blocks.MYSTICAL_FLOWERS)
				.addTag(ModTags.Blocks.SPECIAL_FLOWERS);

		tag(ELEMENTIUM).addTag(ModTags.Blocks.BLOCKS_ELEMENTIUM);
		tag(MANASTEEL).addTag(ModTags.Blocks.BLOCKS_MANASTEEL);
		tag(TERRASTEEL).addTag(ModTags.Blocks.BLOCKS_TERRASTEEL);
		tag(Tags.Blocks.STORAGE_BLOCKS).addTag(ELEMENTIUM).addTag(MANASTEEL).addTag(TERRASTEEL);
		tag(Tags.Blocks.GLASS).add(BotaniaBlocks.manaGlass, BotaniaBlocks.elfGlass, BotaniaBlocks.bifrostPerm);
		tag(Tags.Blocks.GLASS_PANES).add(BotaniaFluffBlocks.managlassPane, BotaniaFluffBlocks.alfglassPane, BotaniaFluffBlocks.bifrostPane);
	}

	private static TagKey<Block> forge(String name) {
		return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("forge", name));
	}
}
