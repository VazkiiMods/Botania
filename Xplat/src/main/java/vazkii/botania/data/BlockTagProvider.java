/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.OptionallyColored;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.flower.FloatingFlowerBaseBlock;
import vazkii.botania.common.block.flower.SpecialFlowerBlock;
import vazkii.botania.common.block.mana.DrumBlock;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.block.mana.ManaSpreaderBlock;
import vazkii.botania.common.block.red_string.RedStringBlock;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.data.util.DummyTagLookup;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class BlockTagProvider extends IntrinsicHolderTagsProvider<Block> {
	public static final Predicate<Block> BOTANIA_BLOCK =
			block -> BotaniaAPI.MODID.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace());
	private static final Set<TagKey<Block>> REQUIRED_TAGS = Set.of(
			BlockTags.LEAVES,
			BlockTags.LOGS,
			BlockTags.FIRE
	);

	public BlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		this(packOutput, lookupProvider, DummyTagLookup.completedFuture(REQUIRED_TAGS));
	}

	protected BlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider,
			CompletableFuture<TagLookup<Block>> parentProvider) {
		super(packOutput, Registries.BLOCK, lookupProvider, parentProvider,
				block -> BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow());
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(BlockTags.RAILS).add(BotaniaBlocks.SPECTRAL_RAIL);
		tag(BlockTags.SLABS).add(getModBlocks(block -> block instanceof SlabBlock));
		tag(BlockTags.WOODEN_SLABS).add(
				BotaniaBlocks.LIVINGWOOD_SLAB, BotaniaBlocks.STRIPPED_LIVINGWOOD_SLAB,
				BotaniaBlocks.LIVINGWOOD_PLANK_SLAB, BotaniaBlocks.DREAMWOOD_SLAB,
				BotaniaBlocks.STRIPPED_DREAMWOOD_SLAB, BotaniaBlocks.DREAMWOOD_PLANK_SLAB,
				BotaniaBlocks.SHIMMERWOOD_PLANK_SLAB
		);
		tag(BlockTags.STAIRS).add(getModBlocks(block -> block instanceof StairBlock));
		tag(BlockTags.WOODEN_STAIRS).add(
				BotaniaBlocks.LIVINGWOOD_STAIRS, BotaniaBlocks.STRIPPED_LIVINGWOOD_STAIRS,
				BotaniaBlocks.LIVINGWOOD_PLANK_STAIRS, BotaniaBlocks.DREAMWOOD_STAIRS,
				BotaniaBlocks.STRIPPED_DREAMWOOD_STAIRS, BotaniaBlocks.DREAMWOOD_PLANK_STAIRS,
				BotaniaBlocks.SHIMMERWOOD_PLANK_STAIRS
		);
		tag(BlockTags.WALLS).add(getModBlocks(block -> block instanceof WallBlock));
		tag(BlockTags.WOODEN_FENCES).add(
				BotaniaBlocks.LIVINGWOOD_FENCE, BotaniaBlocks.DREAMWOOD_FENCE, BotaniaBlocks.SHIMMERWOOD_FENCE);
		tag(BlockTags.FENCE_GATES).add(getModBlocks(b -> b instanceof FenceGateBlock));
		tag(BlockTags.STANDING_SIGNS).add(getModBlocks(b -> b instanceof StandingSignBlock));
		tag(BlockTags.WALL_SIGNS).add(getModBlocks(b -> b instanceof WallSignBlock));
		tag(BlockTags.CEILING_HANGING_SIGNS).add(getModBlocks(b -> b instanceof CeilingHangingSignBlock));
		tag(BlockTags.WALL_HANGING_SIGNS).add(getModBlocks(b -> b instanceof WallHangingSignBlock));
		tag(BlockTags.WOODEN_BUTTONS).add(
				BotaniaBlocks.LIVINGWOOD_BUTTON, BotaniaBlocks.DREAMWOOD_BUTTON, BotaniaBlocks.SHIMMERWOOD_BUTTON);
		tag(BlockTags.WOODEN_PRESSURE_PLATES).add(
				BotaniaBlocks.LIVINGWOOD_PRESSURE_PLATE, BotaniaBlocks.DREAMWOOD_PRESSURE_PLATE,
				BotaniaBlocks.SHIMMERWOOD_PRESSURE_PLATE);
		tag(BlockTags.WOODEN_DOORS).add(BotaniaBlocks.LIVINGWOOD_DOOR, BotaniaBlocks.DREAMWOOD_DOOR);
		tag(BlockTags.WOODEN_TRAPDOORS).add(BotaniaBlocks.LIVINGWOOD_TRAPDOOR, BotaniaBlocks.DREAMWOOD_TRAPDOOR);
		tag(BlockTags.STONE_BUTTONS).add(
				BotaniaBlocks.LIVINGROCK_BUTTON, BotaniaBlocks.SHIMMERROCK_BUTTON, BotaniaBlocks.CORPOREA_BUTTON,
				BotaniaBlocks.FUCHSITE_BUTTON, BotaniaBlocks.TALC_BUTTON, BotaniaBlocks.GNEISS_BUTTON,
				BotaniaBlocks.MYCELITE_BUTTON, BotaniaBlocks.CATACLASITE_BUTTON, BotaniaBlocks.SOLITE_BUTTON,
				BotaniaBlocks.LUNITE_BUTTON, BotaniaBlocks.ROSY_TALC_BUTTON
		);
		tag(BlockTags.STONE_PRESSURE_PLATES).add(
				BotaniaBlocks.LIVINGROCK_PRESSURE_PLATE, BotaniaBlocks.SHIMMERROCK_PRESSURE_PLATE,
				BotaniaBlocks.CORPOREA_PRESSURE_PLATE, BotaniaBlocks.FUCHSITE_PRESSURE_PLATE,
				BotaniaBlocks.TALC_PRESSURE_PLATE, BotaniaBlocks.GNEISS_PRESSURE_PLATE,
				BotaniaBlocks.MYCELITE_PRESSURE_PLATE, BotaniaBlocks.CATACLASITE_PRESSURE_PLATE,
				BotaniaBlocks.SOLITE_PRESSURE_PLATE, BotaniaBlocks.LUNITE_PRESSURE_PLATE,
				BotaniaBlocks.ROSY_TALC_PRESSURE_PLATE
		);
		tag(BlockTags.DRAGON_IMMUNE).add(BotaniaBlocks.INFRANGIBLE_PLATFORM);
		tag(BlockTags.WITHER_IMMUNE).add(BotaniaBlocks.INFRANGIBLE_PLATFORM);

		tag(BotaniaTags.Blocks.SHEEP_EDIBLE_GRASSES).add(
				BotaniaBlocks.DRY_GRASS_BLOCK, BotaniaBlocks.GOLDEN_GRASS_BLOCK, BotaniaBlocks.INFUSED_GRASS_BLOCK,
				BotaniaBlocks.MUTATED_GRASS_BLOCK, BotaniaBlocks.SCORCHED_GRASS_BLOCK, BotaniaBlocks.VIVID_GRASS_BLOCK
		);

		tag(BotaniaTags.Blocks.MUNDANE_FLOATING_FLOWERS).add(
				ColorHelper.supportedColors()
						.map(BotaniaBlocks::getFloatingFlower)
						.sorted(Comparator.comparing(BuiltInRegistries.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(BotaniaTags.Blocks.MISC_SPECIAL_FLOATING_FLOWERS).add(
				BotaniaBlocks.FLOATING_MANASTAR, BotaniaBlocks.FLOATING_PURE_DAISY, BotaniaBlocks.FLOATING_BERGAMUTE,
				BotaniaBlocks.FLOATING_SOLEGNOLIA, BotaniaBlocks.FLOATING_SOLEGNOLIA_PETITE
		);
		tag(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOATING_FLOWERS).add(
				BotaniaBlocks.FLOATING_DANDELIFEON, BotaniaBlocks.FLOATING_ENDOFLAME,
				BotaniaBlocks.FLOATING_ENTROPINNYUM, BotaniaBlocks.FLOATING_GOURMARYLLIS,
				BotaniaBlocks.FLOATING_HYDROANGEAS, BotaniaBlocks.FLOATING_KEKIMURUS, BotaniaBlocks.FLOATING_MUNCHDEW,
				BotaniaBlocks.FLOATING_NARSLIMMUS, BotaniaBlocks.FLOATING_RAFFLOWSIA,
				BotaniaBlocks.FLOATING_ROSA_ARCANA, BotaniaBlocks.FLOATING_SHULK_ME_NOT,
				BotaniaBlocks.FLOATING_SPECTROLUS, BotaniaBlocks.FLOATING_THERMALILY
		);
		tag(BotaniaTags.Blocks.FUNCTIONAL_SPECIAL_FLOATING_FLOWERS).add(
				BotaniaBlocks.FLOATING_AGRICARNATION, BotaniaBlocks.FLOATING_AGRICARNATION_PETITE,
				BotaniaBlocks.FLOATING_BELLETHORNE, BotaniaBlocks.FLOATING_BELLETHORNE_PETITE,
				BotaniaBlocks.FLOATING_BUBBELL, BotaniaBlocks.FLOATING_BUBBELL_PETITE, BotaniaBlocks.FLOATING_CLAYCONIA,
				BotaniaBlocks.FLOATING_CLAYCONIA_PETITE, BotaniaBlocks.FLOATING_DAFFOMILL,
				BotaniaBlocks.FLOATING_DREADTHORNE, BotaniaBlocks.FLOATING_EXOFLAME,
				BotaniaBlocks.FLOATING_FALLEN_KANADE, BotaniaBlocks.FLOATING_HEISEI_DREAM,
				BotaniaBlocks.FLOATING_HOPPERHOCK, BotaniaBlocks.FLOATING_HOPPERHOCK_PETITE,
				BotaniaBlocks.FLOATING_HYACIDUS, BotaniaBlocks.FLOATING_JADED_AMARANTHUS,
				BotaniaBlocks.FLOATING_JIYUULIA, BotaniaBlocks.FLOATING_JIYUULIA_PETITE,
				BotaniaBlocks.FLOATING_LABELLIA, BotaniaBlocks.FLOATING_LOONIUM, BotaniaBlocks.FLOATING_MARIMORPHOSIS,
				BotaniaBlocks.FLOATING_MARIMORPHOSIS_PETITE, BotaniaBlocks.FLOATING_MEDUMONE,
				BotaniaBlocks.FLOATING_ORECHID, BotaniaBlocks.FLOATING_ORECHID_IGNEM,
				BotaniaBlocks.FLOATING_POLLIDISIAC, BotaniaBlocks.FLOATING_RANNUNCARPUS,
				BotaniaBlocks.FLOATING_RANNUNCARPUS_PETITE, BotaniaBlocks.FLOATING_SPECTRANTHEMUM,
				BotaniaBlocks.FLOATING_TANGLEBERRIE, BotaniaBlocks.FLOATING_TANGLEBERRIE_PETITE,
				BotaniaBlocks.FLOATING_TIGERSEYE, BotaniaBlocks.FLOATING_VINCULOTUS
		);
		tag(BotaniaTags.Blocks.SPECIAL_FLOATING_FLOWERS).addTag(BotaniaTags.Blocks.MISC_SPECIAL_FLOATING_FLOWERS)
				.addTag(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOATING_FLOWERS)
				.addTag(BotaniaTags.Blocks.FUNCTIONAL_SPECIAL_FLOATING_FLOWERS);

		tag(BotaniaTags.Blocks.FLOATING_FLOWERS).addTag(BotaniaTags.Blocks.MUNDANE_FLOATING_FLOWERS)
				.addTag(BotaniaTags.Blocks.SPECIAL_FLOATING_FLOWERS);

		tag(BotaniaTags.Blocks.SMALL_MYSTICAL_FLOWERS).add(
				ColorHelper.supportedColors()
						.map(BotaniaBlocks::getMysticalFlower)
						.sorted(Comparator.comparing(BuiltInRegistries.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(BotaniaTags.Blocks.SHIMMERING_MUSHROOMS).add(
				ColorHelper.supportedColors()
						.map(BotaniaBlocks::getShimmeringMushroom)
						.sorted(Comparator.comparing(BuiltInRegistries.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(BotaniaTags.Blocks.GLIMMERING_FLOWERS).add(
				ColorHelper.supportedColors()
						.map(BotaniaBlocks::getGlimmeringFlower)
						.sorted(Comparator.comparing(BuiltInRegistries.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(BotaniaTags.Blocks.TALL_MYSTICAL_FLOWERS).add(
				ColorHelper.supportedColors()
						.map(BotaniaBlocks::getTallMysticalFlower)
						.sorted(Comparator.comparing(BuiltInRegistries.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(BotaniaTags.Blocks.MISC_SPECIAL_FLOWERS).add(
				BotaniaBlocks.MANASTAR, BotaniaBlocks.PURE_DAISY, BotaniaBlocks.BERGAMUTE, BotaniaBlocks.SOLEGNOLIA,
				BotaniaBlocks.SOLEGNOLIA_PETITE);
		tag(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOWERS).add(
				BotaniaBlocks.DANDELIFEON, BotaniaBlocks.ENDOFLAME, BotaniaBlocks.ENTROPINNYUM,
				BotaniaBlocks.GOURMARYLLIS, BotaniaBlocks.HYDROANGEAS, BotaniaBlocks.KEKIMURUS,
				BotaniaBlocks.MUNCHDEW, BotaniaBlocks.NARSLIMMUS, BotaniaBlocks.RAFFLOWSIA, BotaniaBlocks.ROSA_ARCANA,
				BotaniaBlocks.SHULK_ME_NOT, BotaniaBlocks.SPECTROLUS, BotaniaBlocks.THERMALILY
		);
		tag(BotaniaTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS).add(
				BotaniaBlocks.AGRICARNATION, BotaniaBlocks.AGRICARNATION_PETITE, BotaniaBlocks.BELLETHORNE,
				BotaniaBlocks.BELLETHORNE_PETITE, BotaniaBlocks.BUBBELL, BotaniaBlocks.BUBBELL_PETITE,
				BotaniaBlocks.CLAYCONIA, BotaniaBlocks.CLAYCONIA_PETITE, BotaniaBlocks.DAFFOMILL,
				BotaniaBlocks.DREADTHORNE, BotaniaBlocks.EXOFLAME, BotaniaBlocks.FALLEN_KANADE,
				BotaniaBlocks.HEISEI_DREAM, BotaniaBlocks.HOPPERHOCK, BotaniaBlocks.HOPPERHOCK_PETITE,
				BotaniaBlocks.HYACIDUS, BotaniaBlocks.JADED_AMARANTHUS, BotaniaBlocks.JIYUULIA,
				BotaniaBlocks.JIYUULIA_PETITE, BotaniaBlocks.LABELLIA, BotaniaBlocks.LOONIUM,
				BotaniaBlocks.MARIMORPHOSIS, BotaniaBlocks.MARIMORPHOSIS_PETITE, BotaniaBlocks.MEDUMONE,
				BotaniaBlocks.ORECHID, BotaniaBlocks.ORECHID_IGNEM, BotaniaBlocks.POLLIDISIAC,
				BotaniaBlocks.RANNUNCARPUS, BotaniaBlocks.RANNUNCARPUS_PETITE, BotaniaBlocks.SPECTRANTHEMUM,
				BotaniaBlocks.TANGLEBERRIE, BotaniaBlocks.TANGLEBERRIE_PETITE, BotaniaBlocks.TIGERSEYE,
				BotaniaBlocks.VINCULOTUS
		);
		tag(BotaniaTags.Blocks.SPECIAL_FLOWERS).addTag(BotaniaTags.Blocks.MISC_SPECIAL_FLOWERS)
				.addTag(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOWERS)
				.addTag(BotaniaTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS);

		tag(BotaniaTags.Blocks.MINI_FLOWERS).add(
				getModBlocks(block -> block instanceof SpecialFlowerBlock
						&& BuiltInRegistries.BLOCK.getKey(block).getPath().endsWith("_petite"))
		);

		tag(BotaniaTags.Blocks.ENCHANTER_FLOWERS).addTag(BotaniaTags.Blocks.SMALL_MYSTICAL_FLOWERS)
				.addTag(BotaniaTags.Blocks.GLIMMERING_FLOWERS)
				.addTag(BotaniaTags.Blocks.MUNDANE_FLOATING_FLOWERS);

		// Special flowers intentionally excluded due to unwanted behaviors with tree growth and mod compat.
		tag(BlockTags.TALL_FLOWERS).addTag(BotaniaTags.Blocks.TALL_MYSTICAL_FLOWERS);
		tag(BlockTags.SMALL_FLOWERS).addTag(BotaniaTags.Blocks.SMALL_MYSTICAL_FLOWERS);
		// intentionally not added to small flowers so Endermen don't grab them
		tag(BlockTags.FLOWERS).addTag(BotaniaTags.Blocks.GLIMMERING_FLOWERS);

		tag(BlockTags.IMPERMEABLE).add(
				BotaniaBlocks.ALFGLASS, BotaniaBlocks.MANAGLASS, BotaniaBlocks.TEMPORARY_BIFROST_BLOCK,
				BotaniaBlocks.BIFROST_BLOCK);
		tag(BlockTags.BEACON_BASE_BLOCKS).add(
				BotaniaBlocks.MANASTEEL_BLOCK, BotaniaBlocks.TERRASTEEL_BLOCK, BotaniaBlocks.ELEMENTIUM_BLOCK,
				BotaniaBlocks.MANA_DIAMOND_BLOCK, BotaniaBlocks.DRAGONSTONE_BLOCK
		);

		Block[] grassBlockVariants = {
				BotaniaBlocks.DRY_GRASS_BLOCK, BotaniaBlocks.GOLDEN_GRASS_BLOCK, BotaniaBlocks.INFUSED_GRASS_BLOCK,
				BotaniaBlocks.MUTATED_GRASS_BLOCK, BotaniaBlocks.SCORCHED_GRASS_BLOCK, BotaniaBlocks.VIVID_GRASS_BLOCK
		};
		tag(BlockTags.DIRT).add(grassBlockVariants);
		tag(BlockTags.SNIFFER_DIGGABLE_BLOCK).add(grassBlockVariants);
		tag(BotaniaTags.Blocks.BLOCKS_QUARTZ).add(
				Blocks.QUARTZ_BLOCK, BotaniaBlocks.BLAZE_QUARTZ_BLOCK, BotaniaBlocks.DARK_QUARTZ_BLOCK, BotaniaBlocks.ELVEN_QUARTZ_BLOCK,
				BotaniaBlocks.LAVENDER_QUARTZ_BLOCK, BotaniaBlocks.MANA_QUARTZ_BLOCK, BotaniaBlocks.RED_QUARTZ_BLOCK, BotaniaBlocks.SUNNY_QUART_BLOCK
		);

		tag(BotaniaTags.Blocks.CORPOREA_SPARK_OVERRIDE).add(
				BotaniaBlocks.CORPOREA_BLOCK, BotaniaBlocks.CORPOREA_BRICKS, BotaniaBlocks.CORPOREA_BRICK_SLAB,
				BotaniaBlocks.CORPOREA_BRICK_STAIRS, BotaniaBlocks.CORPOREA_BRICK_WALL,
				BotaniaBlocks.CORPOREA_CRYSTAL_CUBE, BotaniaBlocks.CORPOREA_FUNNEL, BotaniaBlocks.CORPOREA_INDEX,
				BotaniaBlocks.CORPOREA_INTERCEPTOR, BotaniaBlocks.CORPOREA_RETAINER, BotaniaBlocks.CORPOREA_SLAB,
				BotaniaBlocks.CORPOREA_STAIRS, BotaniaBlocks.CORPOREA_WALL, BotaniaBlocks.CORPOREA_BUTTON,
				BotaniaBlocks.CORPOREA_PRESSURE_PLATE
		);

		tag(BotaniaTags.Blocks.GAIA_GUARDIAN_IMMUNE).add(
				Blocks.BEACON, BotaniaBlocks.MANA_PYLON, BotaniaBlocks.NATURA_PYLON, BotaniaBlocks.GAIA_PYLON
		);
		tag(BotaniaTags.Blocks.SHIELDS_FROM_MAGNET_RING).add(
				BotaniaBlocks.MANA_POOL, BotaniaBlocks.CREATIVE_MANA_POOL, BotaniaBlocks.DILUTED_MANA_POOL,
				BotaniaBlocks.FABULOUS_MANA_POOL, BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE,
				BotaniaBlocks.RUNIC_ALTAR
		);
		tag(BotaniaTags.Blocks.LAPUTA_IMMOBILE);
		tag(BotaniaTags.Blocks.LAPUTA_NO_DOUBLE_BLOCK);

		tag(BotaniaTags.Blocks.COVERED_MANA_SPREADERS)
				.add(getColoredBlocks(BotaniaBlocks.MANA_SPREADER, LibBlockNames.COVERED_INFIX));
		tag(BotaniaTags.Blocks.COVERED_PULSE_SPREADERS)
				.add(getColoredBlocks(BotaniaBlocks.PULSE_MANA_SPREADER, LibBlockNames.COVERED_INFIX));
		tag(BotaniaTags.Blocks.COVERED_ELVEN_SPREADERS)
				.add(getColoredBlocks(BotaniaBlocks.ELVEN_MANA_SPREADER, LibBlockNames.COVERED_INFIX));
		tag(BotaniaTags.Blocks.COVERED_GAIA_SPREADERS)
				.add(getColoredBlocks(BotaniaBlocks.GAIA_MANA_SPREADER, LibBlockNames.COVERED_INFIX));
		tag(BotaniaTags.Blocks.COVERED_SPREADERS)
				.addTag(BotaniaTags.Blocks.COVERED_MANA_SPREADERS)
				.addTag(BotaniaTags.Blocks.COVERED_PULSE_SPREADERS)
				.addTag(BotaniaTags.Blocks.COVERED_ELVEN_SPREADERS)
				.addTag(BotaniaTags.Blocks.COVERED_GAIA_SPREADERS);
		tag(BlockTags.OCCLUDES_VIBRATION_SIGNALS).addTag(BotaniaTags.Blocks.COVERED_SPREADERS);
		tag(BlockTags.DAMPENS_VIBRATIONS).addTag(BotaniaTags.Blocks.COVERED_SPREADERS);

		tag(BotaniaTags.Blocks.TERRA_PLATE_BASE).add(BotaniaBlocks.LIVINGROCK, BotaniaBlocks.SHIMMERROCK);

		tag(BlockTags.CLIMBABLE).add(BotaniaBlocks.SOLID_VINE);

		tag(BlockTags.PLANKS).add(
				BotaniaBlocks.LIVINGWOOD_PLANKS, BotaniaBlocks.MOSSY_LIVINGWOOD_PLANKS,
				BotaniaBlocks.FRAMED_LIVINGWOOD, BotaniaBlocks.PATTERN_FRAMED_LIVINGWOOD,
				BotaniaBlocks.DREAMWOOD_PLANKS, BotaniaBlocks.MOSSY_DREAMWOOD_PLANKS,
				BotaniaBlocks.FRAMED_DREAMWOOD, BotaniaBlocks.PATTERN_FRAMED_DREAMWOOD,
				BotaniaBlocks.SHIMMERWOOD_PLANKS
		);

		tag(BotaniaTags.Blocks.LIVINGWOOD_LOGS_GLIMMERING).add(
				BotaniaBlocks.GLIMMERING_LIVINGWOOD, BotaniaBlocks.GLIMMERING_LIVINGWOOD_LOG,
				BotaniaBlocks.STRIPPED_GLIMMERING_LIVINGWOOD, BotaniaBlocks.STRIPPED_GLIMMERING_LIVINGWOOD_LOG
		);
		tag(BotaniaTags.Blocks.DREAMWOOD_LOGS_GLIMMERING).add(
				BotaniaBlocks.GLIMMERING_DREAMWOOD, BotaniaBlocks.GLIMMERING_DREAMWOOD_LOG,
				BotaniaBlocks.STRIPPED_GLIMMERING_DREAMWOOD, BotaniaBlocks.STRIPPED_GLIMMERING_DREAMWOOD_LOG
		);

		tag(BotaniaTags.Blocks.LIVINGWOOD_LOGS)
				.add(BotaniaBlocks.LIVINGWOOD_LOG, BotaniaBlocks.LIVINGWOOD,
						BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG, BotaniaBlocks.STRIPPED_LIVINGWOOD
				)
				.addTag(BotaniaTags.Blocks.LIVINGWOOD_LOGS_GLIMMERING);
		tag(BotaniaTags.Blocks.DREAMWOOD_LOGS)
				.add(BotaniaBlocks.DREAMWOOD_LOG, BotaniaBlocks.DREAMWOOD,
						BotaniaBlocks.STRIPPED_DREAMWOOD_LOG, BotaniaBlocks.STRIPPED_DREAMWOOD
				)
				.addTag(BotaniaTags.Blocks.DREAMWOOD_LOGS_GLIMMERING);
		tag(BlockTags.LOGS_THAT_BURN)
				.addTag(BotaniaTags.Blocks.LIVINGWOOD_LOGS)
				.addTag(BotaniaTags.Blocks.DREAMWOOD_LOGS);

		tag(BotaniaTags.Blocks.GHOST_RAIL_BARRIER).addTag(BotaniaTags.Blocks.DREAMWOOD_LOGS);

		tag(BotaniaTags.Blocks.ENDER_AIR_CONVERTABLE).add(
				Blocks.STONE, Blocks.DEEPSLATE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE
		);
		tag(BotaniaTags.Blocks.MARIMORPHOSIS_CONVERTABLE).add(
				Blocks.STONE, Blocks.DEEPSLATE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.TUFF
		);

		tag(BotaniaTags.Blocks.WEIGHT_LENS_AFFECTED);

		tag(BlockTags.MUSHROOM_GROW_BLOCK).add(
				BotaniaBlocks.MYCELITE, BotaniaBlocks.MYCELITE_SLAB, BotaniaBlocks.MYCELITE_STAIRS,
				BotaniaBlocks.MYCELITE_WALL, BotaniaBlocks.MYCELITE_BRICKS, BotaniaBlocks.MYCELITE_BRICK_SLAB,
				BotaniaBlocks.MYCELITE_BRICK_STAIRS, BotaniaBlocks.MYCELITE_BRICK_WALL, BotaniaBlocks.COBBLED_MYCELITE,
				BotaniaBlocks.COBBLED_MYCELITE_SLAB, BotaniaBlocks.COBBLED_MYCELITE_STAIRS,
				BotaniaBlocks.COBBLED_MYCELITE_WALL, BotaniaBlocks.CHISELED_MYCELITE_BRICKS,
				BotaniaBlocks.PETAL_APOTHECARY_MYCELITE, BotaniaBlocks.INFUSED_GRASS_BLOCK,
				BotaniaBlocks.MUTATED_GRASS_BLOCK
		);

		tag(BotaniaTags.Blocks.HORN_OF_THE_WILD_BREAKABLE)
				.add(Blocks.MOSS_CARPET)
				.addOptional(ResourceLocation.fromNamespaceAndPath("biomesoplenty", "high_grass"))
				.addOptional(ResourceLocation.fromNamespaceAndPath("biomesoplenty", "high_grass_plant"));
		tag(BotaniaTags.Blocks.HORN_OF_THE_WILD_IMMUNE)
				.addTag(BotaniaTags.Blocks.GLIMMERING_FLOWERS)
				.addTag(BotaniaTags.Blocks.SHIMMERING_MUSHROOMS);
		tag(BotaniaTags.Blocks.HORN_OF_THE_CANOPY_BREAKABLE)
				.addTag(BlockTags.LEAVES)
				.add(
						Blocks.VINE, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, Blocks.TWISTING_VINES,
						Blocks.TWISTING_VINES_PLANT, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT
				);
		tag(BotaniaTags.Blocks.HORN_OF_THE_COVERING_BREAKABLE).add(Blocks.SNOW);

		tag(BotaniaTags.Blocks.UNWANDABLE)
				.addTag(BlockTags.FIRE)
				.add(
						Blocks.CHORUS_PLANT, Blocks.SCULK_VEIN, Blocks.VINE, Blocks.REDSTONE_WIRE, Blocks.NETHER_PORTAL,
						BotaniaBlocks.SOLID_VINE
				);

		tag(BotaniaTags.Blocks.PASTURE_SEED_REPLACEABLE)
				.add(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.MYCELIUM, Blocks.PODZOL, Blocks.ROOTED_DIRT)
				.add(grassBlockVariants);

		tag(BotaniaTags.Blocks.UNETHICAL_TNT_CHECK).addOptional(ResourceLocation.fromNamespaceAndPath("ae2", "tiny_tnt"));

		tag(BotaniaTags.Blocks.SINGLE_ITEM_INSERT).add(Blocks.CRAFTER);

		tag(BotaniaTags.Blocks.UNSUPPORTED_PLATFORM_DISGUISE);

		tag(BlockTags.FLOWER_POTS)
				.add(ColorHelper.supportedColors()
						.map(BotaniaBlocks::getPottedFlower)
						.sorted(Comparator.comparing(BuiltInRegistries.BLOCK::getKey))
						.toArray(Block[]::new))
				.add(ColorHelper.supportedColors()
						.map(BotaniaBlocks::getPottedShinyFlower)
						.sorted(Comparator.comparing(BuiltInRegistries.BLOCK::getKey))
						.toArray(Block[]::new))
				.add(ColorHelper.supportedColors()
						.map(BotaniaBlocks::getPottedMushroom)
						.sorted(Comparator.comparing(BuiltInRegistries.BLOCK::getKey))
						.toArray(Block[]::new))
				.add(
						// misc
						BotaniaBlocks.POTTED_MANASTAR, BotaniaBlocks.POTTED_PURE_DAISY, BotaniaBlocks.POTTED_BERGAMUTE,
						// generating
						BotaniaBlocks.POTTED_DANDELIFEON, BotaniaBlocks.POTTED_ENDOFLAME,
						BotaniaBlocks.POTTED_ENTROPINNYUM, BotaniaBlocks.POTTED_GOURMARYLLIS,
						BotaniaBlocks.POTTED_HYDROANGEAS, BotaniaBlocks.POTTED_KEKIMURUS, BotaniaBlocks.POTTED_MUNCHDEW,
						BotaniaBlocks.POTTED_NARSLIMMUS, BotaniaBlocks.POTTED_RAFFLOWSIA,
						BotaniaBlocks.POTTED_ROSA_ARCANA, BotaniaBlocks.POTTED_SHULK_ME_NOT,
						BotaniaBlocks.POTTED_SPECTROLUS, BotaniaBlocks.POTTED_THERMALILY,
						// functional
						BotaniaBlocks.POTTED_AGRICARNATION, BotaniaBlocks.POTTED_AGRICARNATION_PETITE,
						BotaniaBlocks.POTTED_BELLETHORNE, BotaniaBlocks.POTTED_BELLETHORNE_PETITE,
						BotaniaBlocks.POTTED_BUBBELL, BotaniaBlocks.POTTED_BUBBELL_PETITE,
						BotaniaBlocks.POTTED_CLAYCONIA, BotaniaBlocks.POTTED_CLAYCONIA_PETITE,
						BotaniaBlocks.POTTED_DAFFOMILL, BotaniaBlocks.POTTED_DREADTHORNE, BotaniaBlocks.POTTED_EXOFLAME,
						BotaniaBlocks.POTTED_FALLEN_KANADE, BotaniaBlocks.POTTED_HEISEI_DREAM,
						BotaniaBlocks.POTTED_HOPPERHOCK, BotaniaBlocks.POTTED_HOPPERHOCK_PETITE,
						BotaniaBlocks.POTTED_HYACIDUS, BotaniaBlocks.POTTED_JADED_AMARANTHUS,
						BotaniaBlocks.POTTED_JIYULLIA, BotaniaBlocks.POTTED_JIYUULIA_PETITE,
						BotaniaBlocks.POTTED_LABELLIA, BotaniaBlocks.POTTED_LOONIUM, BotaniaBlocks.POTTED_MARIMORPHOSIS,
						BotaniaBlocks.POTTED_MARIMORPHOSIS_PETITE, BotaniaBlocks.POTTED_MEDUMONE,
						BotaniaBlocks.POTTED_ORECHID, BotaniaBlocks.POTTED_ORECHID_IGNEM,
						BotaniaBlocks.POTTED_POLLIDISIAC, BotaniaBlocks.POTTED_RANNUNCARPUS,
						BotaniaBlocks.POTTED_RANNUNCARPUS_PETITE, BotaniaBlocks.POTTED_SOLEGNOLIA,
						BotaniaBlocks.POTTED_SOLEGNOLIA_PETITE, BotaniaBlocks.POTTED_SPECTRANTHEMUM,
						BotaniaBlocks.POTTED_TANGLEBERRIE, BotaniaBlocks.POTTED_TANGLEBERRIE_PETITE,
						BotaniaBlocks.POTTED_TIGERSEYE, BotaniaBlocks.POTTED_VINCULOTUS
				);

		tag(BotaniaTags.Blocks.AGRICARNATION_APPLY_BONEMEAL)
				.add(Blocks.AZALEA, Blocks.FLOWERING_AZALEA);
		tag(BotaniaTags.Blocks.AGRICARNATION_GROWTH_CANDIDATE)
				.addTag(BotaniaTags.Blocks.AGRICARNATION_APPLY_BONEMEAL);
		tag(BotaniaTags.Blocks.AGRICARNATION_GROWTH_EXCLUDED)
				.add(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.MANGROVE_LEAVES);

		tag(BotaniaTags.Blocks.MUNCHDEW_CONSUMABLE).addTag(BlockTags.LEAVES);

		tag(BotaniaTags.Blocks.TERRA_TRUNCATOR_TRUNK_BLOCKS)
				.addTag(BlockTags.LOGS)
				.add(Blocks.MANGROVE_ROOTS, Blocks.MUSHROOM_STEM)
				.addOptional(ResourceLocation.fromNamespaceAndPath("quark", "glow_shroom_stem"));
		tag(BotaniaTags.Blocks.TERRA_TRUNCATOR_CROWN_BLOCKS)
				.addTag(BlockTags.LEAVES)
				.add(
						Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK, Blocks.SHROOMLIGHT,
						Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK
				)
				.addOptional(ResourceLocation.fromNamespaceAndPath("quark", "glow_shroom_block"))
				.addOptional(ResourceLocation.fromNamespaceAndPath("quark", "glow_shroom_ring"));

		registerMiningTags();
	}

	private void registerMiningTags() {
		tag(BlockTags.MINEABLE_WITH_HOE).add(
				getModBlocks(b -> b == BotaniaBlocks.CELLULAR_BLOCK
						|| BuiltInRegistries.BLOCK.getKey(b).getPath().contains(LibBlockNames.PETAL_BLOCK_SUFFIX)
				)
		);
		tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
				getModBlocks(b -> b instanceof FloatingFlowerBaseBlock || b instanceof BotaniaGrassBlock)
		);
		var pickaxe = Set.of(
				BotaniaBlocks.ALCHEMY_CATALYST, BotaniaBlocks.CONJURATION_CATALYST, BotaniaBlocks.MANASTEEL_BLOCK,
				BotaniaBlocks.ELEMENTIUM_BLOCK, BotaniaBlocks.TERRASTEEL_BLOCK, BotaniaBlocks.MANA_DIAMOND_BLOCK,
				BotaniaBlocks.DRAGONSTONE_BLOCK, BotaniaBlocks.MANAGLASS, BotaniaBlocks.ALFGLASS,
				BotaniaBlocks.BIFROST_BLOCK, BotaniaBlocks.MANAGLASS_PANE, BotaniaBlocks.ALFGLASS_PANE,
				BotaniaBlocks.BIFROST_PANE, BotaniaBlocks.RUNIC_ALTAR, BotaniaBlocks.BOTANICAL_BREWERY,
				BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE, BotaniaBlocks.MANA_SPLITTER, BotaniaBlocks.MANA_VOID,
				BotaniaBlocks.MANA_DETECTOR, BotaniaBlocks.FORCE_RELAY, BotaniaBlocks.TINY_PLANET,
				BotaniaBlocks.LIFE_IMBUER, BotaniaBlocks.MANA_FLUXFIELD, BotaniaBlocks.MANA_PRISM,
				BotaniaBlocks.MANA_PUMP, BotaniaBlocks.SPARK_TINKERER, BotaniaBlocks.EYE_OF_THE_ANCIENTS,
				BotaniaBlocks.ENDER_OVERSEER, BotaniaBlocks.HOVERING_HOURGLASS, BotaniaBlocks.STARFIELD_CREATOR,
				BotaniaBlocks.BLAZE_MESH
		);
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
				getModBlocks(b -> pickaxe.contains(b)
						|| b instanceof PetalApothecaryBlock
						|| b instanceof PylonBlock
						|| b instanceof ManaPoolBlock
						|| b instanceof RedStringBlock
						|| BuiltInRegistries.BLOCK.getKey(b).getPath().contains(LibBlockNames.AZULEJO_PREFIX)
						|| BuiltInRegistries.BLOCK.getKey(b).getPath().contains("corporea")
						|| BuiltInRegistries.BLOCK.getKey(b).getPath().contains(LibBlockNames.PAVEMENT_SUFFIX)
						|| BuiltInRegistries.BLOCK.getKey(b).getPath().contains("_quartz")
						|| (BuiltInRegistries.BLOCK.getKey(b).getPath().contains(LibBlockNames.METAMORPHIC_PREFIX)
								&& !(b instanceof WallBlock)) // vanilla includes #wall already
						|| (BuiltInRegistries.BLOCK.getKey(b).getPath().contains(LibBlockNames.LIVING_ROCK)
								&& !(b instanceof WallBlock)) // vanilla includes #wall already
						|| BuiltInRegistries.BLOCK.getKey(b).getPath().contains(LibBlockNames.SHIMMERROCK)
				)
		);
		var axe = Set.of(
				BotaniaBlocks.ELVEN_GATEWAY_CORE, BotaniaBlocks.SPREADER_TURNTABLE, BotaniaBlocks.MANASTORM_CHARGE,
				BotaniaBlocks.MANATIDE_BELLOWS, BotaniaBlocks.INCENSE_PLATE, BotaniaBlocks.CACOPHONIUM_BLOCK,
				BotaniaBlocks.LIVINGWOOD_AVATAR, BotaniaBlocks.LIVING_ROOT, BotaniaBlocks.FEL_PUMPKIN
		);
		tag(BlockTags.MINEABLE_WITH_AXE).add(
				getModBlocks(b -> axe.contains(b)
						|| b instanceof DrumBlock
						|| b instanceof OpenCrateBlock
						|| b instanceof PlatformBlock
						|| b instanceof ManaSpreaderBlock
						|| BuiltInRegistries.BLOCK.getKey(b).getPath().contains(LibBlockNames.LIVING_WOOD)
						|| BuiltInRegistries.BLOCK.getKey(b).getPath().contains(LibBlockNames.DREAM_WOOD)
						|| BuiltInRegistries.BLOCK.getKey(b).getPath().contains(LibBlockNames.SHIMMERWOOD_PLANKS)
				)
		);
	}

	private static <B extends Block & OptionallyColored> Block[] getColoredBlocks(B baseBlock) {
		return ColorHelper.supportedColors().map(color -> BotaniaBlocks.findOptionallyDyedBlock(baseBlock, color))
				.toArray(Block[]::new);
	}

	private static <B extends Block & OptionallyColored> Block[] getColoredBlocks(B baseBlock, String coloredBlockInfix) {
		return ColorHelper.supportedColors().map(color -> BotaniaBlocks.findOptionallyDyedBlock(baseBlock, color, coloredBlockInfix))
				.toArray(Block[]::new);
	}

	private Block[] getModBlocks(Predicate<Block> predicate) {
		return BuiltInRegistries.BLOCK.stream().filter(BOTANIA_BLOCK.and(predicate))
				.sorted(Comparator.comparing(BuiltInRegistries.BLOCK::getKey))
				.toArray(Block[]::new);
	}
}
