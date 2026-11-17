/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.data.loot;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaGrassBlock;
import vazkii.botania.common.block.mana.ManaSpreaderBlock;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.common.item.BotaniaItems;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BotaniaBlockLoot extends BlockLootSubProvider {
	// TODO: which other blocks should never be deleted by explosions? (i.e. always drop as item)
	// (vanilla default: heads, shulker boxes, dragon egg, beacon, conduit)
	private static final Set<Item> EXPLOSION_RESISTANT = Stream
			.of(
					BotaniaBlocks.GAIA_HEAD
			)
			.map(Block::asItem)
			.collect(Collectors.toSet());

	private final Set<Block> specialHandling = new HashSet<>();

	public BotaniaBlockLoot(HolderLookup.Provider registries) {
		super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags(), registries);
	}

	@Override
	public void generate() {
		Map<Block, LootTable.Builder> specialCases = new HashMap<>();

		// Empty
		Stream.of(
				BotaniaBlocks.BIFROST_BRIDGE,
				BotaniaBlocks.COCOON_OF_CAPRICE,
				BotaniaBlocks.FAKE_AIR,
				BotaniaBlocks.MANA_FLAME
		).forEach(b -> specialCases.put(b, noDrop()));

		// Redirects
		specialCases.put(BotaniaBlocks.CACOPHONIUM_BLOCK, createSingleItemTable(Blocks.NOTE_BLOCK));
		specialCases.put(BotaniaBlocks.MANA_ENCHANTER, createSingleItemTable(Blocks.LAPIS_BLOCK));

		// Special
		dropWhenSilkTouch(BotaniaBlocks.CELLULAR_BLOCK);
		specialCases.put(BotaniaBlocks.LIVING_ROOT, createSingleItemTable(BotaniaItems.LIVING_ROOT, ConstantValue.exactly(4)));
		specialCases.put(BotaniaBlocks.SOLID_VINE, BotaniaLootTableProvider.copyReferencedLootTable(Blocks.VINE.getLootTable()));
		specialCases.put(BotaniaBlocks.TINY_POTATO, createNameableBlockEntityTable(BotaniaBlocks.TINY_POTATO));

		// Flower component saving
		saveSpecialFlowerState(specialCases, BotaniaBlocks.GOURMARYLLIS, BotaniaBlocks.FLOATING_GOURMARYLLIS,
				BotaniaDataComponents.STREAK_LENGTH, BotaniaDataComponents.LAST_REPEATS, BotaniaDataComponents.LAST_FOODS);
		saveSpecialFlowerState(specialCases, BotaniaBlocks.HYDROANGEAS, BotaniaBlocks.FLOATING_HYDROANGEAS,
				BotaniaDataComponents.COOLDOWN, BotaniaDataComponents.DECAY_TICKS);
		saveSpecialFlowerState(specialCases, BotaniaBlocks.MUNCHDEW, BotaniaBlocks.FLOATING_MUNCHDEW,
				BotaniaDataComponents.COOLDOWN, BotaniaDataComponents.ACTIVE);
		saveSpecialFlowerState(specialCases, BotaniaBlocks.RAFFLOWSIA, BotaniaBlocks.FLOATING_RAFFLOWSIA,
				BotaniaDataComponents.LAST_REPEATS, BotaniaDataComponents.LAST_FLOWERS);
		saveSpecialFlowerState(specialCases, BotaniaBlocks.SPECTROLUS, BotaniaBlocks.FLOATING_SPECTROLUS,
				BotaniaDataComponents.NEXT_COLOR, BotaniaDataComponents.COLOR_SEQUENCE);
		saveSpecialFlowerState(specialCases, BotaniaBlocks.THERMALILY, BotaniaBlocks.FLOATING_THERMALILY,
				BotaniaDataComponents.COOLDOWN);
		saveSpecialFlowerState(specialCases, BotaniaBlocks.ORECHID, BotaniaBlocks.FLOATING_ORECHID,
				BotaniaDataComponents.COOLDOWN);
		saveSpecialFlowerState(specialCases, BotaniaBlocks.ORECHID_IGNEM, BotaniaBlocks.FLOATING_ORECHID_IGNEM,
				BotaniaDataComponents.COOLDOWN);

		Map.of(
				BotaniaBlocks.SOLITE, BotaniaBlocks.COBBLED_SOLITE,
				BotaniaBlocks.FUCHSITE, BotaniaBlocks.COBBLED_FUCHSITE,
				BotaniaBlocks.MYCELITE, BotaniaBlocks.COBBLED_MYCELITE,
				BotaniaBlocks.ROSY_TALC, BotaniaBlocks.COBBLED_ROSY_TALC,
				BotaniaBlocks.GNEISS, BotaniaBlocks.COBBLED_GNEISS,
				BotaniaBlocks.TALC, BotaniaBlocks.COBBLED_TALC,
				BotaniaBlocks.CATACLASITE, BotaniaBlocks.COBBLED_CATACLASITE,
				BotaniaBlocks.LUNITE, BotaniaBlocks.COBBLED_LUNITE
		).forEach((stone, cobble) -> specialCases.put(stone, createSingleItemTableWithSilkTouch(stone, cobble)));

		for (Block block : BuiltInRegistries.BLOCK) {
			ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
			if (!BotaniaAPI.MODID.equals(blockId.getNamespace()) || specialHandling.contains(block)) {
				continue;
			}
			if (specialCases.containsKey(block)) {
				add(block, specialCases.get(block));
			} else if (block instanceof SlabBlock) {
				add(block, createSlabItemTable(block));
			} else if (block instanceof ManaSpreaderBlock spreaderBlock) {
				add(block, createManaSpreaderTable(spreaderBlock));
			} else if (block instanceof TallFlowerBlock) {
				add(block, createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
			} else if (block instanceof DoorBlock) {
				add(block, createSinglePropConditionTable(block, DoorBlock.HALF, DoubleBlockHalf.LOWER));
			} else if (block instanceof BotaniaGrassBlock) {
				add(block, createSingleItemTableWithSilkTouch(block, Blocks.DIRT));
			} else if (block instanceof FlowerPotBlock flowerPot) {
				dropPottedContents(flowerPot);
			} else {
				dropSelf(block);
			}
		}
	}

	private LootTable.Builder createManaSpreaderTable(ManaSpreaderBlock block) {
		LootTable.Builder builder = LootTable.lootTable()
				.withPool(this.applyExplosionCondition(block,
						LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(ManaSpreaderBlock.getBaseBlock(block)))))
				.withPool(this.applyExplosionCondition(Blocks.SCAFFOLDING,
						LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
								.add(LootItem.lootTableItem(Blocks.SCAFFOLDING))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties()
												.hasProperty(ManaSpreaderBlock.HAS_SCAFFOLDING, true)))));
		if (block.getCoverColor() != null) {
			builder.withPool(this.applyExplosionCondition(block,
					LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
							.add(LootItem.lootTableItem(ColorHelper.WOOL_MAP.apply(block.getCoverColor())))));
		}
		return builder;
	}

	private void saveSpecialFlowerState(Map<Block, LootTable.Builder> specialCases, Block flower, Block floatingFlower,
			DataComponentType<?>... components) {
		specialCases.put(flower, createBlockEntityTableWithComponents(flower, components));
		specialCases.put(floatingFlower, createBlockEntityTableWithComponents(floatingFlower, components));
	}

	@Override
	public void otherWhenSilkTouch(Block block, Block other) {
		super.otherWhenSilkTouch(block, other);
		specialHandling.add(block);
	}

	protected LootTable.Builder createBlockEntityTableWithComponents(Block block, DataComponentType<?>... componentsToInclude) {
		CopyComponentsFunction.Builder copyComponents = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY);
		for (DataComponentType<?> component : componentsToInclude) {
			copyComponents.include(component);
		}
		return LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(block).apply(copyComponents))));
	}

	public Map<? extends ResourceKey<LootTable>, ? extends LootTable.Builder> getMap() {
		return Collections.unmodifiableMap(this.map);
	}
}
