package vazkii.botania.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.List;

public class FabricBlockTagProvider extends BlockTagsProvider {
	public static final Tag.Named<Block> QUARTZ_BLOCKS = IXplatAbstractions.INSTANCE.blockTag(new ResourceLocation("c", "quartz_blocks"));
	public static final Tag.Named<Block> MUSHROOMS = IXplatAbstractions.INSTANCE.blockTag(new ResourceLocation("c", "mushrooms"));

	public FabricBlockTagProvider(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	public String getName() {
		return "Botania block tags (Fabric-specific)";
	}

	@Override
	protected void addTags() {
		tag(QUARTZ_BLOCKS).add(
				ModFluffBlocks.darkQuartz, ModFluffBlocks.manaQuartz, ModFluffBlocks.blazeQuartz,
				ModFluffBlocks.lavenderQuartz, ModFluffBlocks.redQuartz, ModFluffBlocks.elfQuartz, ModFluffBlocks.sunnyQuartz
		);

		for (DyeColor color : DyeColor.values()) {
			this.tag(MUSHROOMS).add(ModBlocks.getMushroom(color));
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
