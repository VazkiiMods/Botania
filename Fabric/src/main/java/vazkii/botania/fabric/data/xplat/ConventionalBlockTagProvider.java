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

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.ConventionalBotaniaTags;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.data.BlockTagProvider;
import vazkii.botania.data.util.DummyTagLookup;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ConventionalBlockTagProvider extends BlockTagProvider {
	private static final Set<TagKey<Block>> REQUIRED_TAGS = Set.of(
			BlockTags.BASE_STONE_OVERWORLD,
			BlockTags.DIRT,
			BlockTags.ICE,
			BlockTags.SAND,
			BlockTags.MINEABLE_WITH_PICKAXE,
			ConventionalBlockTags.GRAVELS,
			ConventionalBlockTags.ORES
	);

	public ConventionalBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider, DummyTagLookup.completedFuture(REQUIRED_TAGS));
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

		// Cobblestones
		// TODO: switch over biome block/item IDs to match their in-game names?
		tag(ConventionalBotaniaTags.Blocks.METAMORPHIC_FOREST_COBBLESTONES).add(BotaniaBlocks.COBBLED_FUCHSITE);
		tag(ConventionalBotaniaTags.Blocks.METAMORPHIC_PLAINS_COBBLESTONES).add(BotaniaBlocks.COBBLED_TALC);
		tag(ConventionalBotaniaTags.Blocks.METAMORPHIC_MOUNTAIN_COBBLESTONES).add(BotaniaBlocks.COBBLED_GNEISS);
		tag(ConventionalBotaniaTags.Blocks.METAMORPHIC_FUNGAL_COBBLESTONES).add(BotaniaBlocks.COBBLED_MYCELITE);
		tag(ConventionalBotaniaTags.Blocks.METAMORPHIC_SWAMP_COBBLESTONES).add(BotaniaBlocks.COBBLED_CATACLASITE);
		tag(ConventionalBotaniaTags.Blocks.METAMORPHIC_DESERT_COBBLESTONES).add(BotaniaBlocks.COBBLED_SOLITE);
		tag(ConventionalBotaniaTags.Blocks.METAMORPHIC_TAIGA_COBBLESTONES).add(BotaniaBlocks.COBBLED_LUNITE);
		tag(ConventionalBotaniaTags.Blocks.METAMORPHIC_MESA_COBBLESTONES).add(BotaniaBlocks.COBBLED_ROSY_TALC);

		tag(ConventionalBotaniaTags.Blocks.METAMORPHIC_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Blocks.METAMORPHIC_FOREST_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Blocks.METAMORPHIC_PLAINS_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Blocks.METAMORPHIC_MOUNTAIN_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Blocks.METAMORPHIC_FUNGAL_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Blocks.METAMORPHIC_SWAMP_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Blocks.METAMORPHIC_DESERT_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Blocks.METAMORPHIC_TAIGA_COBBLESTONES)
				.addTag(ConventionalBotaniaTags.Blocks.METAMORPHIC_MESA_COBBLESTONES);
		tag(ConventionalBlockTags.COBBLESTONES).addTag(ConventionalBotaniaTags.Blocks.METAMORPHIC_COBBLESTONES);
		tag(ConventionalBlockTags.STONES).add(
				BotaniaBlocks.FUCHSITE, BotaniaBlocks.TALC,
				BotaniaBlocks.GNEISS, BotaniaBlocks.MYCELITE,
				BotaniaBlocks.CATACLASITE, BotaniaBlocks.SOLITE,
				BotaniaBlocks.LUNITE, BotaniaBlocks.ROSY_TALC
		);

		// Dyed blocks
		ColorHelper.supportedColors().forEach(color -> {
			tag(TagKey.create(ConventionalBlockTags.DYED.registry(),
					ConventionalBlockTags.DYED.location().withSuffix("/" + color.getSerializedName())))
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.MANA_POOL, color))
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.CREATIVE_MANA_POOL, color))
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.DILUTED_MANA_POOL, color))
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.FABULOUS_MANA_POOL, color))
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.MANA_SPREADER, color, LibBlockNames.COVERED_INFIX))
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.PULSE_MANA_SPREADER, color, LibBlockNames.COVERED_INFIX))
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.ELVEN_MANA_SPREADER, color, LibBlockNames.COVERED_INFIX))
					.add(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.GAIA_MANA_SPREADER, color, LibBlockNames.COVERED_INFIX));
		});

		// Glass blocks and panes
		tag(ConventionalBotaniaTags.Blocks.MANA_GLASS_BLOCKS).add(BotaniaBlocks.MANAGLASS, BotaniaBlocks.ALFGLASS, BotaniaBlocks.BIFROST_BLOCK);
		tag(ConventionalBlockTags.GLASS_BLOCKS).addTag(ConventionalBotaniaTags.Blocks.MANA_GLASS_BLOCKS);

		tag(ConventionalBotaniaTags.Blocks.MANA_GLASS_PANES).add(BotaniaBlocks.MANAGLASS_PANE, BotaniaBlocks.ALFGLASS_PANE, BotaniaBlocks.BIFROST_PANE);
		tag(ConventionalBlockTags.GLASS_PANES).addTag(ConventionalBotaniaTags.Blocks.MANA_GLASS_PANES);

		// Storage blocks
		tag(ConventionalBotaniaTags.Blocks.MANASTEEL_STORAGE_BLOCKS).add(BotaniaBlocks.MANASTEEL_BLOCK);
		tag(ConventionalBotaniaTags.Blocks.TERRASTEEL_STORAGE_BLOCKS).add(BotaniaBlocks.TERRASTEEL_BLOCK);
		tag(ConventionalBotaniaTags.Blocks.ELEMENTIUM_STORAGE_BLOCKS).add(BotaniaBlocks.ELEMENTIUM_BLOCK);
		tag(ConventionalBotaniaTags.Blocks.MANA_DIAMOND_STORAGE_BLOCKS).add(BotaniaBlocks.MANA_DIAMOND_BLOCK);
		tag(ConventionalBotaniaTags.Blocks.DRAGONSTONE_STORAGE_BLOCKS).add(BotaniaBlocks.DRAGONSTONE_BLOCK);
		tag(ConventionalBotaniaTags.Blocks.BLAZE_STORAGE_BLOCKS).add(BotaniaBlocks.BLAZE_MESH);

		ColorHelper.supportedColors().forEach(dyeColor -> {
			var tag = ConventionalBotaniaTags.Blocks.PETAL_STORAGE_BLOCKS_BY_COLOR.get(dyeColor);
			tag(tag).add(BotaniaBlocks.getPetalBlock(dyeColor));
			tag(ConventionalBotaniaTags.Blocks.PETAL_STORAGE_BLOCKS).addTag(tag);
		});
		tag(ConventionalBlockTags.STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Blocks.MANASTEEL_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Blocks.TERRASTEEL_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Blocks.ELEMENTIUM_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Blocks.MANA_DIAMOND_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Blocks.DRAGONSTONE_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Blocks.BLAZE_STORAGE_BLOCKS)
				.addTag(ConventionalBotaniaTags.Blocks.PETAL_STORAGE_BLOCKS);

		// Miscellaneous
		tag(ConventionalBlockTags.SKULLS).add(BotaniaBlocks.GAIA_HEAD, BotaniaBlocks.GAIA_WALL_HEAD_BLOCK);
		tag(ConventionalBlockTags.STRIPPED_LOGS).add(
				BotaniaBlocks.STRIPPED_LIVINGWOOD_LOG, BotaniaBlocks.STRIPPED_GLIMMERING_LIVINGWOOD_LOG,
				BotaniaBlocks.STRIPPED_DREAMWOOD_LOG, BotaniaBlocks.STRIPPED_GLIMMERING_DREAMWOOD_LOG
		);
		tag(ConventionalBlockTags.STRIPPED_WOODS).add(
				BotaniaBlocks.STRIPPED_LIVINGWOOD, BotaniaBlocks.STRIPPED_GLIMMERING_LIVINGWOOD,
				BotaniaBlocks.STRIPPED_DREAMWOOD, BotaniaBlocks.STRIPPED_GLIMMERING_DREAMWOOD
		);

		tag(BotaniaTags.Blocks.TERRAFORMABLE)
				.addTag(BlockTags.BASE_STONE_OVERWORLD)
				.add(Blocks.INFESTED_STONE)
				.addTag(BlockTags.DIRT)
				.addTag(ConventionalBlockTags.GRAVELS)
				.add(Blocks.SNOW)
				.addTag(BlockTags.SAND);

		tag(BotaniaTags.Blocks.MINEABLE_WITH_VITREOUS_PICKAXE)
				.addTag(BlockTags.MINEABLE_WITH_PICKAXE)
				.addTag(ConventionalBlockTags.GLASS_BLOCKS)
				.addTag(ConventionalBlockTags.GLASS_PANES)
				// ice is a pickaxe block by default
				.add(Blocks.GLOWSTONE, Blocks.SEA_LANTERN, Blocks.REDSTONE_LAMP, Blocks.BEACON);

		tag(BotaniaTags.Blocks.VITREOUS_PICKAXE_SILKTOUCHED)
				.addTag(ConventionalBlockTags.GLASS_BLOCKS)
				.addTag(ConventionalBlockTags.GLASS_PANES)
				.addTag(BlockTags.ICE)
				// redstone lamp and beacon do not need silktouch by default
				.add(Blocks.GLOWSTONE, Blocks.SEA_LANTERN)
				// copper bulbs have higher mining level by default
				.add(
						Blocks.COPPER_BULB, Blocks.EXPOSED_COPPER_BULB,
						Blocks.WEATHERED_COPPER_BULB, Blocks.OXIDIZED_COPPER_BULB,
						Blocks.WAXED_COPPER_BULB, Blocks.WAXED_EXPOSED_COPPER_BULB,
						Blocks.WAXED_WEATHERED_COPPER_BULB, Blocks.WAXED_OXIDIZED_COPPER_BULB
				);

		tag(BotaniaTags.Blocks.MANTLE_RING_AFFECTED).addTag(BlockTags.MINEABLE_WITH_PICKAXE);
		tag(BotaniaTags.Blocks.MANTLE_RING_HARD).addTag(ConventionalBlockTags.ORES);
		tag(BotaniaTags.Blocks.ROD_OF_THE_PLENTIFUL_MANTLE_HIGHLIGHTED).addTag(ConventionalBlockTags.ORES);
	}

	@Override
	public String getName() {
		return "Conventional " + super.getName();
	}
}
