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

import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.MatchTool;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;
import net.minecraft.world.storage.loot.functions.CopyName;
import net.minecraft.world.storage.loot.functions.CopyNbt;
import net.minecraft.world.storage.loot.functions.ExplosionDecay;
import net.minecraft.world.storage.loot.functions.SetCount;

import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.BlockAltGrass;
import vazkii.botania.common.block.BlockModDoubleFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.subtile.generating.*;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BlockLootProvider implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final DataGenerator generator;
	private final Map<Block, Function<Block, LootTable.Builder>> functionTable = new HashMap<>();

	public BlockLootProvider(DataGenerator generator) {
		this.generator = generator;

		for (Block b : Registry.BLOCK) {
			ResourceLocation id = Registry.BLOCK.getKey(b);
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
	public void act(DirectoryCache cache) throws IOException {
		Map<ResourceLocation, LootTable.Builder> tables = new HashMap<>();

		for (Block b : Registry.BLOCK) {
			ResourceLocation id = Registry.BLOCK.getKey(b);
			if (!LibMisc.MOD_ID.equals(id.getNamespace())) {
				continue;
			}
			Function<Block, LootTable.Builder> func = functionTable.getOrDefault(b, BlockLootProvider::genRegular);
			tables.put(id, func.apply(b));
		}

		for (Map.Entry<ResourceLocation, LootTable.Builder> e : tables.entrySet()) {
			Path path = getPath(generator.getOutputFolder(), e.getKey());
			IDataProvider.save(GSON, cache, LootTableManager.toJson(e.getValue().setParameterSet(LootParameterSets.BLOCK).build()), path);
		}
	}

	private static Path getPath(Path root, ResourceLocation id) {
		return root.resolve("data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
	}

	private static LootTable.Builder empty(Block b) {
		return LootTable.builder();
	}

	private static LootTable.Builder genCopyNbt(Block b, String... tags) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(b);
		CopyNbt.Builder func = CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY);
		for (String tag : tags) {
			func = func.replaceOperation(tag, "BlockEntityTag." + tag);
		}
		LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry)
				.acceptCondition(SurvivesExplosion.builder())
				.acceptFunction(func);
		return LootTable.builder().addLootPool(pool);
	}

	private static LootTable.Builder genCellBlock(Block b) {
		ItemPredicate.Builder silkPred = ItemPredicate.Builder.create()
				.enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)));
		LootEntry.Builder<?> silk = ItemLootEntry.builder(b)
				.acceptCondition(MatchTool.builder(silkPred));
		return LootTable.builder().addLootPool(LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(silk));
	}

	private static LootTable.Builder genTinyPotato(Block b) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(b)
				.acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY));
		LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry)
				.acceptCondition(SurvivesExplosion.builder());
		return LootTable.builder().addLootPool(pool);
	}

	private static LootTable.Builder genMetamorphicStone(Block b) {
		String cobbleName = Registry.BLOCK.getKey(b).getPath().replaceAll("_stone", "_cobblestone");
		Block cobble = Registry.BLOCK.getValue(new ResourceLocation(LibMisc.MOD_ID, cobbleName)).get();
		return genRegular(cobble);
	}

	private static LootTable.Builder genSolidVine(Block b) {
		LootEntry.Builder<?> entry = TableLootEntry.builder(new ResourceLocation("blocks/vine"));
		return LootTable.builder().addLootPool(LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry));
	}

	private static LootTable.Builder genRoot(Block b) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(ModItems.livingroot)
				.acceptFunction(SetCount.builder(RandomValueRange.of(2, 4)))
				.acceptFunction(ExplosionDecay.builder());
		return LootTable.builder().addLootPool(LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry));
	}

	private static LootTable.Builder genSlab(Block b) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(b)
				.acceptFunction(SetCount.builder(ConstantRange.of(2))
						.acceptCondition(BlockStateProperty.builder(b).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(SlabBlock.TYPE, SlabType.DOUBLE))))
				.acceptFunction(ExplosionDecay.builder());
		return LootTable.builder().addLootPool(LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry));
	}

	private static LootTable.Builder genDoubleFlower(Block b) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(b)
				.acceptCondition(BlockStateProperty.builder(b).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
				.acceptCondition(MatchTool.builder(ItemPredicate.Builder.create().tag(ModTags.Items.SHEARS)));
		LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry)
				.acceptCondition(SurvivesExplosion.builder());
		return LootTable.builder().addLootPool(pool);
	}

	private static LootTable.Builder genAltGrass(Block b) {
		ItemPredicate.Builder silkPred = ItemPredicate.Builder.create()
				.enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)));
		LootEntry.Builder<?> silk = ItemLootEntry.builder(b)
				.acceptCondition(MatchTool.builder(silkPred));
		LootEntry.Builder<?> dirt = ItemLootEntry.builder(Blocks.DIRT)
				.acceptCondition(SurvivesExplosion.builder());
		LootEntry.Builder<?> entry = AlternativesLootEntry.builder(silk, dirt);
		LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry);
		return LootTable.builder().addLootPool(pool);
	}

	private static LootTable.Builder genRegular(Block b) {
		LootEntry.Builder<?> entry = ItemLootEntry.builder(b);
		LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry)
				.acceptCondition(SurvivesExplosion.builder());
		return LootTable.builder().addLootPool(pool);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania block loot tables";
	}
}
