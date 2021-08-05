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
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

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
	private static final LootItemCondition.Builder SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item()
			.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

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
	public void run(HashCache cache) throws IOException {
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
			DataProvider.save(GSON, cache, LootTables.serialize(e.getValue().setParamSet(LootContextParamSets.BLOCK).build()), path);
		}
	}

	private static Path getPath(Path root, ResourceLocation id) {
		return root.resolve("data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
	}

	private static LootTable.Builder empty(Block b) {
		return LootTable.lootTable();
	}

	private static LootTable.Builder genCopyNbt(Block b, String... tags) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b);
		CopyNbtFunction.Builder func = CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY);
		for (String tag : tags) {
			func = func.copy(tag, "BlockEntityTag." + tag);
		}
		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
				.when(ExplosionCondition.survivesExplosion())
				.apply(func);
		return LootTable.lootTable().withPool(pool);
	}

	private static LootTable.Builder genCellBlock(Block b) {
		ItemPredicate.Builder silkPred = ItemPredicate.Builder.item()
				.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)));
		LootPoolEntryContainer.Builder<?> silk = LootItem.lootTableItem(b)
				.when(MatchTool.toolMatches(silkPred));
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(silk));
	}

	private static LootTable.Builder genTinyPotato(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b)
				.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
				.when(ExplosionCondition.survivesExplosion());
		return LootTable.lootTable().withPool(pool);
	}

	private static LootTable.Builder genMetamorphicStone(Block b) {
		String cobbleName = Registry.BLOCK.getKey(b).getPath().replaceAll("_stone", "_cobblestone");
		Block cobble = Registry.BLOCK.getOptional(prefix(cobbleName)).get();
		return genSilkDrop(b, cobble);
	}

	private static LootTable.Builder genSilkDrop(ItemLike silkDrop, ItemLike normalDrop) {
		LootPoolEntryContainer.Builder<?> cobbleDrop = LootItem.lootTableItem(normalDrop).when(ExplosionCondition.survivesExplosion());
		LootPoolEntryContainer.Builder<?> stoneDrop = LootItem.lootTableItem(silkDrop).when(SILK_TOUCH);

		return LootTable.lootTable().withPool(
				LootPool.lootPool().setRolls(ConstantValue.exactly(1))
						.add(stoneDrop.otherwise(cobbleDrop)));
	}

	private static LootTable.Builder genSolidVine(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootTableReference.lootTableReference(new ResourceLocation("blocks/vine"));
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry));
	}

	private static LootTable.Builder genRoot(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(ModItems.livingroot)
				.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
				.apply(ApplyExplosionDecay.explosionDecay());
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry));
	}

	private static LootTable.Builder genSlab(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b)
				.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE))))
				.apply(ApplyExplosionDecay.explosionDecay());
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry));
	}

	private static LootTable.Builder genDoubleFlower(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b)
				.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
				.when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(FabricToolTags.SHEARS)));
		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
				.when(ExplosionCondition.survivesExplosion());
		return LootTable.lootTable().withPool(pool);
	}

	private static LootTable.Builder genAltGrass(Block b) {
		LootPoolEntryContainer.Builder<?> silk = LootItem.lootTableItem(b)
				.when(SILK_TOUCH);
		LootPoolEntryContainer.Builder<?> dirt = LootItem.lootTableItem(Blocks.DIRT)
				.when(ExplosionCondition.survivesExplosion());
		LootPoolEntryContainer.Builder<?> entry = AlternativesEntry.alternatives(silk, dirt);
		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry);
		return LootTable.lootTable().withPool(pool);
	}

	private static LootTable.Builder genRegular(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b);
		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
				.when(ExplosionCondition.survivesExplosion());
		return LootTable.lootTable().withPool(pool);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania block loot tables";
	}
}
