/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.*;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.CopyNameLootFunction;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.BlockAltGrass;
import vazkii.botania.common.block.BlockModDoubleFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.*;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BlockLootProvider implements DataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;
	private final Map<Block, Function<Block, LootTable.Builder>> functionTable = new HashMap<>();

	public BlockLootProvider(DataGenerator generator) {
		this.generator = generator;

		for (Block b : Registry.BLOCK) {
			Identifier id = Registry.BLOCK.getId(b);
			if (!LibMisc.MOD_ID.equals(id.getNamespace())) {
				continue;
			}
			if (b instanceof SlabBlock) {
				functionTable.put(b, BlockLootProvider::genSlab);
			} else if (b instanceof BlockModDoubleFlower) {
				functionTable.put(b, BlockLootProvider::genDoubleFlower);
			} else if (b instanceof BlockAltGrass) {
				functionTable.put(b, BlockLootProvider::genAltGrass);
			} else if (id.getPath().matches(LibBlockNames.METAMORPHIC_PREFIX + "\\w+" + "_stone")) {
				functionTable.put(b, BlockLootProvider::genMetamorphicStone);
			}
		}

		// Empty
		functionTable.put(ModBlocks.bifrost, BlockLootProvider::empty);
		functionTable.put(ModBlocks.cocoon, BlockLootProvider::empty);
		functionTable.put(ModBlocks.fakeAir, BlockLootProvider::empty);
		functionTable.put(ModBlocks.manaFlame, BlockLootProvider::empty);
		functionTable.put(ModBlocks.pistonRelay, BlockLootProvider::empty);

		// Redirects
		functionTable.put(ModBlocks.cacophonium, b -> genRegular(Blocks.NOTE_BLOCK));
		functionTable.put(ModBlocks.enchantedSoil, b -> genRegular(Blocks.DIRT));
		functionTable.put(ModBlocks.enchanter, b -> genRegular(Blocks.LAPIS_BLOCK));
		functionTable.put(ModBlocks.mossyAltar, b -> genRegular(ModBlocks.defaultAltar));

		// Special
		functionTable.put(ModBlocks.cellBlock, BlockLootProvider::genCellBlock);
		functionTable.put(ModBlocks.root, BlockLootProvider::genRoot);
		functionTable.put(ModBlocks.solidVines, BlockLootProvider::genSolidVine);
		functionTable.put(ModBlocks.tinyPotato, BlockLootProvider::genTinyPotato);

		// Flower NBT saving
		functionTable.put(ModSubtiles.gourmaryllis, b -> genCopyNbt(b, SubTileGourmaryllis.TAG_LAST_FOODS, SubTileGourmaryllis.TAG_LAST_FOOD_COUNT, SubTileGourmaryllis.TAG_STREAK_LENGTH));
		functionTable.put(ModSubtiles.gourmaryllisFloating, b -> genCopyNbt(b, SubTileGourmaryllis.TAG_LAST_FOODS, SubTileGourmaryllis.TAG_LAST_FOOD_COUNT, SubTileGourmaryllis.TAG_STREAK_LENGTH));
		functionTable.put(ModSubtiles.hydroangeas, b -> genCopyNbt(b, SubTileHydroangeas.TAG_COOLDOWN, TileEntityGeneratingFlower.TAG_PASSIVE_DECAY_TICKS));
		functionTable.put(ModSubtiles.hydroangeasFloating, b -> genCopyNbt(b, SubTileHydroangeas.TAG_COOLDOWN, TileEntityGeneratingFlower.TAG_PASSIVE_DECAY_TICKS));
		functionTable.put(ModSubtiles.munchdew, b -> genCopyNbt(b, SubTileMunchdew.TAG_COOLDOWN));
		functionTable.put(ModSubtiles.munchdewFloating, b -> genCopyNbt(b, SubTileMunchdew.TAG_COOLDOWN));
		functionTable.put(ModSubtiles.rafflowsia, b -> genCopyNbt(b, SubTileRafflowsia.TAG_LAST_FLOWERS, SubTileRafflowsia.TAG_LAST_FLOWER_TIMES));
		functionTable.put(ModSubtiles.rafflowsiaFloating, b -> genCopyNbt(b, SubTileRafflowsia.TAG_LAST_FLOWERS, SubTileRafflowsia.TAG_LAST_FLOWER_TIMES));
		functionTable.put(ModSubtiles.spectrolus, b -> genCopyNbt(b, SubTileSpectrolus.TAG_NEXT_COLOR));
		functionTable.put(ModSubtiles.spectrolusFloating, b -> genCopyNbt(b, SubTileSpectrolus.TAG_NEXT_COLOR));
		functionTable.put(ModSubtiles.thermalily, b -> genCopyNbt(b, SubTileHydroangeas.TAG_COOLDOWN));
		functionTable.put(ModSubtiles.thermalilyFloating, b -> genCopyNbt(b, SubTileHydroangeas.TAG_COOLDOWN));
	}

	@Override
	public void run(DataCache cache) throws IOException {
		Map<Identifier, LootTable.Builder> tables = new HashMap<>();

		for (Block b : Registry.BLOCK) {
			Identifier id = Registry.BLOCK.getId(b);
			if (!LibMisc.MOD_ID.equals(id.getNamespace())) {
				continue;
			}
			Function<Block, LootTable.Builder> func = functionTable.getOrDefault(b, BlockLootProvider::genRegular);
			tables.put(id, func.apply(b));
		}

		for (Map.Entry<Identifier, LootTable.Builder> e : tables.entrySet()) {
			Path path = getPath(generator.getOutput(), e.getKey());
			DataProvider.writeToPath(GSON, cache, LootManager.toJson(e.getValue().type(LootContextTypes.BLOCK).build()), path);
		}
	}

	private static Path getPath(Path root, Identifier id) {
		return root.resolve("data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
	}

	private static LootTable.Builder empty(Block b) {
		return LootTable.builder();
	}

	private static LootTable.Builder genCopyNbt(Block b, String... tags) {
		LootPoolEntry.Builder<?> entry = ItemEntry.builder(b);
		CopyNbtLootFunction.Builder func = CopyNbtLootFunction.builder(CopyNbtLootFunction.Source.BLOCK_ENTITY);
		for (String tag : tags) {
			func = func.withOperation(tag, "BlockEntityTag." + tag);
		}
		LootPool.Builder pool = LootPool.builder().rolls(ConstantLootTableRange.create(1)).with(entry)
				.conditionally(SurvivesExplosionLootCondition.builder())
				.apply(func);
		return LootTable.builder().pool(pool);
	}

	private static LootTable.Builder genCellBlock(Block b) {
		ItemPredicate.Builder silkPred = ItemPredicate.Builder.create()
				.enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.atLeast(1)));
		LootPoolEntry.Builder<?> silk = ItemEntry.builder(b)
				.conditionally(MatchToolLootCondition.builder(silkPred));
		return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootTableRange.create(1)).with(silk));
	}

	private static LootTable.Builder genTinyPotato(Block b) {
		LootPoolEntry.Builder<?> entry = ItemEntry.builder(b)
				.apply(CopyNameLootFunction.builder(CopyNameLootFunction.Source.BLOCK_ENTITY));
		LootPool.Builder pool = LootPool.builder().rolls(ConstantLootTableRange.create(1)).with(entry)
				.conditionally(SurvivesExplosionLootCondition.builder());
		return LootTable.builder().pool(pool);
	}

	private static LootTable.Builder genMetamorphicStone(Block b) {
		String cobbleName = Registry.BLOCK.getId(b).getPath().replaceAll("_stone", "_cobblestone");
		Block cobble = Registry.BLOCK.getOrEmpty(prefix(cobbleName)).get();
		return genRegular(cobble);
	}

	private static LootTable.Builder genSolidVine(Block b) {
		LootPoolEntry.Builder<?> entry = LootTableEntry.builder(new Identifier("blocks/vine"));
		return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootTableRange.create(1)).with(entry));
	}

	private static LootTable.Builder genRoot(Block b) {
		LootPoolEntry.Builder<?> entry = ItemEntry.builder(ModItems.livingroot)
				.apply(SetCountLootFunction.builder(UniformLootTableRange.between(2, 4)))
				.apply(ExplosionDecayLootFunction.builder());
		return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootTableRange.create(1)).with(entry));
	}

	private static LootTable.Builder genSlab(Block b) {
		LootPoolEntry.Builder<?> entry = ItemEntry.builder(b)
				.apply(SetCountLootFunction.builder(ConstantLootTableRange.create(2))
						.conditionally(BlockStatePropertyLootCondition.builder(b).properties(StatePredicate.Builder.create().exactMatch(SlabBlock.TYPE, SlabType.DOUBLE))))
				.apply(ExplosionDecayLootFunction.builder());
		return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootTableRange.create(1)).with(entry));
	}

	private static LootTable.Builder genDoubleFlower(Block b) {
		LootPoolEntry.Builder<?> entry = ItemEntry.builder(b)
				.conditionally(BlockStatePropertyLootCondition.builder(b).properties(StatePredicate.Builder.create().exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER)))
				.conditionally(MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(FabricToolTags.SHEARS)));
		LootPool.Builder pool = LootPool.builder().rolls(ConstantLootTableRange.create(1)).with(entry)
				.conditionally(SurvivesExplosionLootCondition.builder());
		return LootTable.builder().pool(pool);
	}

	private static LootTable.Builder genAltGrass(Block b) {
		ItemPredicate.Builder silkPred = ItemPredicate.Builder.create()
				.enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.atLeast(1)));
		LootPoolEntry.Builder<?> silk = ItemEntry.builder(b)
				.conditionally(MatchToolLootCondition.builder(silkPred));
		LootPoolEntry.Builder<?> dirt = ItemEntry.builder(Blocks.DIRT)
				.conditionally(SurvivesExplosionLootCondition.builder());
		LootPoolEntry.Builder<?> entry = AlternativeEntry.builder(silk, dirt);
		LootPool.Builder pool = LootPool.builder().rolls(ConstantLootTableRange.create(1)).with(entry);
		return LootTable.builder().pool(pool);
	}

	private static LootTable.Builder genRegular(Block b) {
		LootPoolEntry.Builder<?> entry = ItemEntry.builder(b);
		LootPool.Builder pool = LootPool.builder().rolls(ConstantLootTableRange.create(1)).with(entry)
				.conditionally(SurvivesExplosionLootCondition.builder());
		return LootTable.builder().pool(pool);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania block loot tables";
	}
}
