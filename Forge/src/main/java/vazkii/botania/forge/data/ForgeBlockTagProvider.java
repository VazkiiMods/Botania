package vazkii.botania.forge.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.xplat.IXplatAbstractions;

public class ForgeBlockTagProvider extends BlockTagsProvider {
	public static final Tag.Named<Block> MUSHROOMS = IXplatAbstractions.INSTANCE.blockTag(new ResourceLocation("forge", "mushrooms"));

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
				ModFluffBlocks.darkQuartz, ModFluffBlocks.manaQuartz, ModFluffBlocks.blazeQuartz,
				ModFluffBlocks.lavenderQuartz, ModFluffBlocks.redQuartz, ModFluffBlocks.elfQuartz, ModFluffBlocks.sunnyQuartz
		);

		for (DyeColor color : DyeColor.values()) {
			this.tag(MUSHROOMS).add(ModBlocks.getMushroom(color));
		}

		tag(BlockTags.createOptional(new ResourceLocation("buzzier_bees", "flower_blacklist")))
				.addTag(ModTags.Blocks.MYSTICAL_FLOWERS)
				.addTag(ModTags.Blocks.SPECIAL_FLOWERS);
	}
}
