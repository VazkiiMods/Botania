package vazkii.botania.fabric.data;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFluffBlocks;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.List;

public class FabricBlockTagProvider extends BlockTagsProvider {
	public static final TagKey<Block> LAPIS_BLOCKS = blockTag(new ResourceLocation("c", "lapis_blocks"));
	public static final TagKey<Block> QUARTZ_BLOCKS = blockTag(new ResourceLocation("c", "quartz_blocks"));
	public static final TagKey<Block> MUSHROOMS = blockTag(new ResourceLocation("c", "mushrooms"));
	public static final TagKey<Block> GLASS = blockTag(new ResourceLocation("c", "glass"));
	public static final TagKey<Block> GLASS_ALT = blockTag(new ResourceLocation("c", "glass_blocks"));
	public static final TagKey<Block> GLASS_PANE = blockTag(new ResourceLocation("c", "glass_pane"));
	public static final TagKey<Block> GLASS_PANE_ALT = blockTag(new ResourceLocation("c", "glass_panes"));

	private static TagKey<Block> blockTag(ResourceLocation location) {
		return TagKey.create(Registry.BLOCK_REGISTRY, location);
	}

	public FabricBlockTagProvider(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void addTags() {
		tag(LAPIS_BLOCKS).add(Blocks.LAPIS_BLOCK);
		tag(QUARTZ_BLOCKS).add(
				BotaniaFluffBlocks.darkQuartz, BotaniaFluffBlocks.manaQuartz, BotaniaFluffBlocks.blazeQuartz,
				BotaniaFluffBlocks.lavenderQuartz, BotaniaFluffBlocks.redQuartz, BotaniaFluffBlocks.elfQuartz, BotaniaFluffBlocks.sunnyQuartz
		);
		List.of(GLASS, GLASS_ALT).forEach(t -> tag(t).add(BotaniaBlocks.manaGlass, BotaniaBlocks.elfGlass, BotaniaBlocks.bifrostPerm));
		List.of(GLASS_PANE, GLASS_PANE_ALT).forEach(t -> tag(t).add(BotaniaFluffBlocks.managlassPane, BotaniaFluffBlocks.alfglassPane, BotaniaFluffBlocks.bifrostPane));

		for (DyeColor color : DyeColor.values()) {
			this.tag(MUSHROOMS).add(BotaniaBlocks.getMushroom(color));
		}

		var vanillaTags = List.of(
				BlockTags.COAL_ORES,
				BlockTags.IRON_ORES,
				BlockTags.GOLD_ORES,
				BlockTags.LAPIS_ORES,
				BlockTags.REDSTONE_ORES,
				BlockTags.DIAMOND_ORES,
				BlockTags.COPPER_ORES,
				BlockTags.EMERALD_ORES
		);
		// We aren't calling vanilla's generation, so need to add dummy calls so that using them below doesn't error out.
		vanillaTags.forEach(this::tag);

		var oreTag = tag(IXplatAbstractions.INSTANCE.getOreTag());
		vanillaTags.forEach(oreTag::addTag);
	}
}
