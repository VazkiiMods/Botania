package vazkii.botania.forge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.LibMisc;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ForgeBlockTagProvider extends IntrinsicHolderTagsProvider<Block> {
	public static final TagKey<Block> MUSHROOMS = forge("mushrooms");
	public static final TagKey<Block> ELEMENTIUM = forge("storage_blocks/elementium");
	public static final TagKey<Block> MANASTEEL = forge("storage_blocks/manasteel");
	public static final TagKey<Block> TERRASTEEL = forge("storage_blocks/terrasteel");
	public static final TagKey<Block> MANA_DIAMOND = forge("storage_blocks/mana_diamond");
	public static final TagKey<Block> DRAGONSTONE = forge("storage_blocks/dragonstone");
	public static final TagKey<Block> BLAZE_MESH = forge("storage_blocks/blaze_mesh");
	public static final Map<DyeColor, TagKey<Block>> PETAL_BLOCKS = ColorHelper.supportedColors().collect(
			Collectors.toMap(Function.identity(), color -> forge("storage_blocks/"
					+ ForgeRegistries.BLOCKS.getKey(BotaniaBlocks.getPetalBlock(color)).getPath())));

	public ForgeBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider,
			ExistingFileHelper existingFileHelper) {
		super(output, Registries.BLOCK, provider, (block) -> block.builtInRegistryHolder().key(), LibMisc.MOD_ID, existingFileHelper);
	}

	@Override
	public String getName() {
		return "Botania block tags (Forge-specific)";
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(Tags.Blocks.STORAGE_BLOCKS_QUARTZ).add(
				BotaniaBlocks.darkQuartz, BotaniaBlocks.manaQuartz, BotaniaBlocks.blazeQuartz,
				BotaniaBlocks.lavenderQuartz, BotaniaBlocks.redQuartz, BotaniaBlocks.elfQuartz, BotaniaBlocks.sunnyQuartz
		);

		IntrinsicTagAppender<Block> storageBlocks = tag(Tags.Blocks.STORAGE_BLOCKS);
		ColorHelper.supportedColors().forEach(color -> {
			tag(MUSHROOMS).add(BotaniaBlocks.getMushroom(color));
			TagKey<Block> petalStorageBlockTag = PETAL_BLOCKS.get(color);
			tag(petalStorageBlockTag).add(BotaniaBlocks.getPetalBlock(color));
			storageBlocks.addTag(petalStorageBlockTag);
		});

		tag(TagKey.create(Registries.BLOCK, new ResourceLocation("buzzier_bees", "flower_blacklist")))
				.addTag(BotaniaTags.Blocks.MYSTICAL_FLOWERS)
				.addTag(BotaniaTags.Blocks.SPECIAL_FLOWERS);

		tag(ELEMENTIUM).addTag(BotaniaTags.Blocks.BLOCKS_ELEMENTIUM);
		tag(MANASTEEL).addTag(BotaniaTags.Blocks.BLOCKS_MANASTEEL);
		tag(TERRASTEEL).addTag(BotaniaTags.Blocks.BLOCKS_TERRASTEEL);
		tag(MANA_DIAMOND).add(BotaniaBlocks.manaDiamondBlock);
		tag(DRAGONSTONE).add(BotaniaBlocks.dragonstoneBlock);
		tag(BLAZE_MESH).add(BotaniaBlocks.blazeBlock);
		storageBlocks
				.addTag(ELEMENTIUM)
				.addTag(MANASTEEL)
				.addTag(TERRASTEEL)
				.addTag(MANA_DIAMOND)
				.addTag(DRAGONSTONE)
				.addTag(BLAZE_MESH);
		tag(Tags.Blocks.GLASS).add(BotaniaBlocks.manaGlass, BotaniaBlocks.elfGlass, BotaniaBlocks.bifrostPerm);
		tag(Tags.Blocks.GLASS_PANES).add(BotaniaBlocks.managlassPane, BotaniaBlocks.alfglassPane, BotaniaBlocks.bifrostPane);
		tag(Tags.Blocks.FENCES_WOODEN).add(BotaniaBlocks.livingwoodFence, BotaniaBlocks.dreamwoodFence);
		tag(Tags.Blocks.FENCE_GATES_WOODEN).add(BotaniaBlocks.livingwoodFenceGate, BotaniaBlocks.dreamwoodFenceGate);
	}

	private static TagKey<Block> forge(String name) {
		return TagKey.create(Registries.BLOCK, new ResourceLocation("forge", name));
	}
}
