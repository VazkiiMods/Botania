/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

import vazkii.botania.common.block.*;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import static vazkii.botania.common.block.ModSubtiles.*;

public class BlockTagProvider extends BlockTagsProvider {
	private static final Predicate<Block> BOTANIA_BLOCK = b -> LibMisc.MOD_ID.equals(Registry.BLOCK.getKey(b).getNamespace());

	public BlockTagProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void addTags() {
		tag(BlockTags.RAILS).add(ModBlocks.ghostRail);
		tag(BlockTags.SLABS).add(getModBlocks(b -> b instanceof SlabBlock));
		tag(BlockTags.STAIRS).add(getModBlocks(b -> b instanceof StairBlock));
		tag(BlockTags.WALLS).add(getModBlocks(b -> b instanceof WallBlock));
		tag(BlockTags.FENCES).add(getModBlocks(b -> b instanceof FenceBlock));
		tag(BlockTags.FENCE_GATES).add(getModBlocks(b -> b instanceof FenceGateBlock));
		tag(BlockTags.DRAGON_IMMUNE).add(ModBlocks.infrangiblePlatform);
		tag(BlockTags.WITHER_IMMUNE).add(ModBlocks.infrangiblePlatform);

		tag(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFloatingFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS).add(registry.stream().filter(BOTANIA_BLOCK)
				.filter(b -> b instanceof BlockFloatingSpecialFlower)
				.sorted(Comparator.comparing(Registry.BLOCK::getKey))
				.toArray(Block[]::new)
		);

		tag(ModTags.Blocks.FLOATING_FLOWERS).addTag(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS)
				.addTag(ModTags.Blocks.SPECIAL_FLOATING_FLOWERS);

		tag(ModTags.Blocks.MYSTICAL_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(ModTags.Blocks.SHINY_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getShinyFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS).add(
				Arrays.stream(DyeColor.values())
						.map(ModBlocks::getDoubleFlower)
						.sorted(Comparator.comparing(Registry.BLOCK::getKey))
						.toArray(Block[]::new)
		);

		tag(ModTags.Blocks.MISC_SPECIAL_FLOWERS).add(manastar, pureDaisy, bergamute);
		tag(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS).add(
				dandelifeon, endoflame, entropinnyum,
				gourmaryllis, hydroangeas, kekimurus,
				munchdew, narslimmus, rafflowsia, rosaArcana,
				shulkMeNot, spectrolus, thermalily
		);
		tag(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS).add(
				agricarnation, agricarnationChibi, bellethorn, bellethornChibi,
				bubbell, bubbellChibi, clayconia, clayconiaChibi,
				daffomill, dreadthorn, exoflame, fallenKanade, heiseiDream,
				hopperhock, hopperhockChibi, hyacidus, jadedAmaranthus,
				jiyuulia, labellia, loonium, marimorphosis, marimorphosisChibi,
				medumone, orechid, orechidIgnem, pollidisiac, rannuncarpus, rannuncarpusChibi,
				solegnolia, solegnoliaChibi, spectranthemum, tangleberrie, tigerseye, vinculotus
		);
		tag(ModTags.Blocks.SPECIAL_FLOWERS).addTag(ModTags.Blocks.MISC_SPECIAL_FLOWERS)
				.addTag(ModTags.Blocks.GENERATING_SPECIAL_FLOWERS)
				.addTag(ModTags.Blocks.FUNCTIONAL_SPECIAL_FLOWERS);

		tag(ModTags.Blocks.MINI_FLOWERS).add(
				getModBlocks(b -> b instanceof BlockSpecialFlower && registry.getKey(b).getPath().endsWith("_chibi"))
		);

		tag(ModTags.Blocks.ENCHANTER_FLOWERS).addTag(ModTags.Blocks.MYSTICAL_FLOWERS)
				.addTag(ModTags.Blocks.SHINY_FLOWERS)
				.addTag(ModTags.Blocks.MUNDANE_FLOATING_FLOWERS);

		// Special flowers intentionally excluded due to unwanted behaviors with tree growth and mod compat.
		tag(BlockTags.TALL_FLOWERS).addTag(ModTags.Blocks.DOUBLE_MYSTICAL_FLOWERS);
		tag(BlockTags.SMALL_FLOWERS).addTag(ModTags.Blocks.MYSTICAL_FLOWERS);

		tag(BlockTags.IMPERMEABLE).add(ModBlocks.elfGlass, ModBlocks.manaGlass, ModBlocks.bifrost, ModBlocks.bifrostPerm);
		tag(BlockTags.BEACON_BASE_BLOCKS).add(ModBlocks.manasteelBlock, ModBlocks.terrasteelBlock, ModBlocks.elementiumBlock,
				ModBlocks.manaDiamondBlock, ModBlocks.dragonstoneBlock);

		tag(BlockTags.DIRT).add(getModBlocks(b -> b instanceof BlockAltGrass));
		tag(ModTags.Blocks.BLOCKS_ELEMENTIUM).add(ModBlocks.elementiumBlock);
		tag(ModTags.Blocks.BLOCKS_MANASTEEL).add(ModBlocks.manasteelBlock);
		tag(ModTags.Blocks.BLOCKS_QUARTZ).add(
				ModFluffBlocks.darkQuartz, ModFluffBlocks.manaQuartz, ModFluffBlocks.blazeQuartz,
				ModFluffBlocks.lavenderQuartz, ModFluffBlocks.redQuartz, ModFluffBlocks.elfQuartz, ModFluffBlocks.sunnyQuartz
		);
		tag(ModTags.Blocks.BLOCKS_TERRASTEEL).add(ModBlocks.terrasteelBlock);

		tag(ModTags.Blocks.LIVINGROCK).add(ModBlocks.livingrock);
		tag(ModTags.Blocks.LIVINGWOOD).add(ModBlocks.livingwood);

		tag(ModTags.Blocks.CORPOREA_SPARK_OVERRIDE).add(
				ModBlocks.corporeaBlock, ModBlocks.corporeaBrick, ModBlocks.corporeaBrickSlab, ModBlocks.corporeaBrickStairs,
				ModBlocks.corporeaBrickWall, ModBlocks.corporeaCrystalCube, ModBlocks.corporeaFunnel, ModBlocks.corporeaIndex,
				ModBlocks.corporeaInterceptor, ModBlocks.corporeaSlab, ModBlocks.corporeaStairs);

		tag(BlockTags.SAND); // We aren't calling vanilla's generation, so need to add a dummy so that using this below doesn't error out.
		tag(ModTags.Blocks.TERRAFORMABLE)
				.add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.INFESTED_STONE, Blocks.STONE, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_DIORITE, Blocks.POLISHED_GRANITE)
				.add(Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.MYCELIUM)
				.add(Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.SNOW)
				.addTag(BlockTags.SAND);
		tag(ModTags.Blocks.GAIA_BREAK_BLACKLIST).add(Blocks.BEACON, ModBlocks.manaPylon, ModBlocks.naturaPylon, ModBlocks.gaiaPylon);
		tag(ModTags.Blocks.MAGNET_RING_BLACKLIST).add(ModBlocks.manaPool, ModBlocks.creativePool, ModBlocks.dilutedPool,
				ModBlocks.fabulousPool, ModBlocks.terraPlate, ModBlocks.runeAltar);

		tag(ModTags.Blocks.TERRA_PLATE_BASE).add(ModBlocks.livingrock, ModBlocks.shimmerrock);

		tag(BlockTags.BAMBOO_PLANTABLE_ON).add(ModBlocks.dryGrass, ModBlocks.goldenGrass, ModBlocks.vividGrass,
				ModBlocks.scorchedGrass, ModBlocks.infusedGrass, ModBlocks.mutatedGrass);
		tag(BlockTags.CLIMBABLE).add(ModBlocks.solidVines);

		for (DyeColor color : DyeColor.values()) {
			this.tag(ModTags.Blocks.MUSHROOMS).add(ModBlocks.getMushroom(color));
		}

		registerCommonTags();
	}

	private void registerCommonTags() {
		tag(ModTags.Blocks.LAPIS_BLOCKS).add(Blocks.LAPIS_BLOCK);

		tag(ModTags.Blocks.COAL_ORES).add(Blocks.COAL_ORE);
		tag(ModTags.Blocks.IRON_ORES).add(Blocks.IRON_ORE);
		tag(ModTags.Blocks.GOLD_ORES).add(Blocks.GOLD_ORE);
		tag(ModTags.Blocks.LAPIS_ORES).add(Blocks.LAPIS_ORE);
		tag(ModTags.Blocks.REDSTONE_ORES).add(Blocks.REDSTONE_ORE);
		tag(ModTags.Blocks.DIAMOND_ORES).add(Blocks.DIAMOND_ORE);
		tag(ModTags.Blocks.EMERALD_ORES).add(Blocks.EMERALD_ORE);
	}

	@Nonnull
	private Block[] getModBlocks(Predicate<Block> predicate) {
		return registry.stream().filter(BOTANIA_BLOCK)
				.filter(predicate)
				.sorted(Comparator.comparing(Registry.BLOCK::getKey))
				.toArray(Block[]::new);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania block tags";
	}
}
