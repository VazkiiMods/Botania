/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.test;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.ConventionalBotaniaTags;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TranslationKeysTest {
	private static final Set<TagKey<Biome>> IGNORED_BIOME_TAGS = Set.of();
	private static final Set<TagKey<Block>> IGNORED_BLOCK_TAGS = Set.of();
	private static final Set<TagKey<EntityType<?>>> IGNORED_ENTITY_TAGS = Set.of();
	private static final Set<TagKey<Fluid>> IGNORED_FLUID_TAGS = Set.of();
	private static final Set<TagKey<Item>> IGNORED_ITEM_TAGS = Set.of();
	private static final Set<Class<?>> ADDITIONAL_BIOME_TAG_SOURCES = Set.of(BotaniaTags.Biomes.class);
	private static final Set<Class<?>> ADDITIONAL_BLOCK_TAG_SOURCES = Set.of(
			BotaniaTags.Blocks.class, ConventionalBotaniaTags.Blocks.class);
	private static final Set<Class<?>> ADDITIONAL_ENTITY_TAG_SOURCES = Set.of(BotaniaTags.Entities.class);
	private static final Set<Class<?>> ADDITIONAL_FLUID_TAG_SOURCES = Set.of(BotaniaTags.Fluids.class);
	private static final Set<Class<?>> ADDITIONAL_ITEM_TAG_SOURCES = Set.of(
			BotaniaTags.Items.class, ConventionalBotaniaTags.Items.class);

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void everyBlockHasValidDescriptionId(GameTestHelper helper) {
		MutableInt missing = new MutableInt();
		BuiltInRegistries.BLOCK.keySet().stream()
				.filter(id -> BotaniaAPI.MODID.equals(id.getNamespace()))
				.forEach(id -> {
					Block block = BuiltInRegistries.BLOCK.get(id);
					validateComponent(id, "name of block", block.getName(), missing);
					String descriptionId = block.getDescriptionId();
					if (!Language.getInstance().has(descriptionId)) {
						BotaniaAPI.LOGGER.error("Missing block translation key {} for block {}", descriptionId, id);
						missing.increment();
					}
				});
		if (missing.getValue() > 0) {
			helper.fail("%d missing block description IDs (see log)".formatted(missing.getValue()));
		} else {
			helper.succeed();
		}
	}

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void everyItemHasValidDescriptionId(GameTestHelper helper) {
		MutableInt missing = new MutableInt();
		BuiltInRegistries.ITEM.keySet().stream()
				.filter(id -> BotaniaAPI.MODID.equals(id.getNamespace()))
				.forEach(id -> {
					Item item = BuiltInRegistries.ITEM.get(id);

					validateComponent(id, "description for item", item.getDescription(), missing);
					validateComponent(id, "name for item", item.getName(item.getDefaultInstance()), missing);
					try {
						List<Component> tooltipLines = new ArrayList<>();
						item.appendHoverText(
								item.getDefaultInstance(),
								// TODO: pretend we are pressing all the modifiers (this is platform-specific)
								new Item.TooltipContext() {
									@Override
									public HolderLookup.Provider registries() {
										return helper.getLevel().registryAccess();
									}

									@Override
									public float tickRate() {
										return 0;
									}

									@Override
									public @Nullable MapItemSavedData mapData(MapId mapId) {
										return null;
									}
								},
								tooltipLines,
								TooltipFlag.ADVANCED
						);
						for (Component tooltipLine : tooltipLines) {
							validateComponent(id, "tooltip for item", tooltipLine, missing);
						}
					} catch (Exception e) {
						BotaniaAPI.LOGGER.error("Failed to analyze tooltip of item {}. (Does it assume client context?)", id, e);
						// close enough...
						missing.increment();
					}

					String descriptionId = item.getDescriptionId();
					if (item instanceof BlockItem blockItem
							&& blockItem.getBlock().getDescriptionId().equals(descriptionId)) {
						// ignore missing block item descriptions if they are also reported by the block test
						return;
					}
					if (!Language.getInstance().has(descriptionId)) {
						BotaniaAPI.LOGGER.error("Missing item translation key {} for item {}", descriptionId, id);
						missing.increment();
					}
				});
		if (missing.getValue() > 0) {
			helper.fail("%d missing item description IDs (see log)".formatted(missing.getValue()));
		} else {
			helper.succeed();
		}
	}

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void everyBlockTagHasATranslationKey(GameTestHelper helper) {
		MutableInt missing = new MutableInt();
		Set<TagKey<Block>> additionalTags = findAdditionalTagKeys(ADDITIONAL_BLOCK_TAG_SOURCES);
		BuiltInRegistries.BLOCK.getTagNames()
				.filter(tag -> (tag.location().getNamespace().equals(BotaniaAPI.MODID)
						|| additionalTags.contains(tag)) && !IGNORED_BLOCK_TAGS.contains(tag))
				.forEach(tag -> {
					ResourceLocation tagId = tag.location();
					String translationKey = "tag.block.%s.%s".formatted(
							tagId.getNamespace(),
							tagId.getPath().replace('/', '.'));
					if (!Language.getInstance().has(translationKey)) {
						BotaniaAPI.LOGGER.error("Missing block tag translation key {} for {}", translationKey, tagId);
						missing.increment();
					}
				});
		if (missing.getValue() > 0) {
			helper.fail("%d missing block tag translations (see log)".formatted(missing.getValue()));
		} else {
			helper.succeed();
		}
	}

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void everyEntityTagHasATranslationKey(GameTestHelper helper) {
		MutableInt missing = new MutableInt();
		Set<TagKey<EntityType<?>>> additionalTags = findAdditionalTagKeys(ADDITIONAL_ENTITY_TAG_SOURCES);
		BuiltInRegistries.ENTITY_TYPE.getTagNames()
				.filter(tag -> (tag.location().getNamespace().equals(BotaniaAPI.MODID)
						|| additionalTags.contains(tag)) && !IGNORED_ENTITY_TAGS.contains(tag))
				.forEach(tag -> {
					ResourceLocation tagId = tag.location();
					String translationKey = "tag.entity_type.%s.%s".formatted(
							tagId.getNamespace(),
							tagId.getPath().replace('/', '.'));
					if (!Language.getInstance().has(translationKey)) {
						BotaniaAPI.LOGGER.error("Missing entity tag translation key {} for {}", translationKey, tagId);
						missing.increment();
					}
				});
		if (missing.getValue() > 0) {
			helper.fail("%d missing entity tag translations (see log)".formatted(missing.getValue()));
		} else {
			helper.succeed();
		}
	}

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void everyFluidTagHasATranslationKey(GameTestHelper helper) {
		MutableInt missing = new MutableInt();
		Set<TagKey<Fluid>> additionalTags = findAdditionalTagKeys(ADDITIONAL_FLUID_TAG_SOURCES);
		BuiltInRegistries.FLUID.getTagNames()
				.filter(tag -> (tag.location().getNamespace().equals(BotaniaAPI.MODID)
						|| additionalTags.contains(tag)) && !IGNORED_FLUID_TAGS.contains(tag))
				.forEach(tag -> {
					ResourceLocation tagId = tag.location();
					String translationKey = "tag.fluid.%s.%s".formatted(
							tagId.getNamespace(),
							tagId.getPath().replace('/', '.'));
					if (!Language.getInstance().has(translationKey)) {
						BotaniaAPI.LOGGER.error("Missing fluid tag translation key {} for {}", translationKey, tagId);
						missing.increment();
					}
				});
		if (missing.getValue() > 0) {
			helper.fail("%d missing fluid tag translations (see log)".formatted(missing.getValue()));
		} else {
			helper.succeed();
		}
	}

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void everyItemTagHasATranslationKey(GameTestHelper helper) {
		MutableInt missing = new MutableInt();
		Set<TagKey<Item>> additionalTags = findAdditionalTagKeys(ADDITIONAL_ITEM_TAG_SOURCES);
		BuiltInRegistries.ITEM.getTagNames()
				.filter(tag -> (tag.location().getNamespace().equals(BotaniaAPI.MODID)
						|| additionalTags.contains(tag)) && !IGNORED_ITEM_TAGS.contains(tag))
				.forEach(tag -> {
					ResourceLocation tagId = tag.location();
					String translationKey = "tag.item.%s.%s".formatted(
							tagId.getNamespace(),
							tagId.getPath().replace('/', '.'));
					if (!Language.getInstance().has(translationKey)) {
						BotaniaAPI.LOGGER.error("Missing item tag translation key {} for {}", translationKey, tagId);
						missing.increment();
					}
				});
		if (missing.getValue() > 0) {
			helper.fail("%d missing item tag translations (see log)".formatted(missing.getValue()));
		} else {
			helper.succeed();
		}
	}

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void everyBiomeTagHasATranslationKey(GameTestHelper helper) {
		MutableInt missing = new MutableInt();
		Set<TagKey<Item>> additionalTags = findAdditionalTagKeys(ADDITIONAL_BIOME_TAG_SOURCES);
		Registry<Biome> biomeRegistry = helper.getLevel().registryAccess().registryOrThrow(Registries.BIOME);
		biomeRegistry.getTagNames()
				.filter(tag -> (tag.location().getNamespace().equals(BotaniaAPI.MODID)
						|| additionalTags.contains(tag)) && !IGNORED_BIOME_TAGS.contains(tag))
				.forEach(tag -> {
					ResourceLocation tagId = tag.location();
					String translationKey = "tag.worldgen.%s.biome.%s".formatted(
							tagId.getNamespace(),
							tagId.getPath().replace('/', '.'));
					if (!Language.getInstance().has(translationKey)) {
						BotaniaAPI.LOGGER.error("Missing biome tag translation key {} for {}", translationKey, tagId);
						missing.increment();
					}
				});
		if (missing.getValue() > 0) {
			helper.fail("%d missing biome tag translations (see log)".formatted(missing.getValue()));
		} else {
			helper.succeed();
		}
	}

	@GameTest(template = TestingUtil.EMPTY_STRUCTURE)
	public void everyStructureHasATranslationKey(GameTestHelper helper) {
		MutableInt missing = new MutableInt();
		Registry<Structure> structureRegistry = helper.getLevel().registryAccess().registryOrThrow(Registries.STRUCTURE);
		structureRegistry.stream()
				.forEach(structure -> {
					ResourceLocation structureId = structureRegistry.getKey(structure);
					String translationKey = "structure.%s.%s".formatted(
							structureId.getNamespace(),
							structureId.getPath().replace('/', '.'));
					if (!Language.getInstance().has(translationKey)) {
						BotaniaAPI.LOGGER.error("Missing vanilla structure translation key {} for {}",
								translationKey, structureId);
						missing.increment();
					}
				});
		if (missing.getValue() > 0) {
			helper.fail("%d missing vanilla structure translations (see log)".formatted(missing.getValue()));
		} else {
			helper.succeed();
		}
	}

	private void validateComponent(ResourceLocation id, String type, Component component, MutableInt missing) {
		ComponentContents contents = component.getContents();
		if (contents instanceof TranslatableContents translatableContents
				&& !Language.getInstance().has(translatableContents.getKey())) {
			BotaniaAPI.LOGGER.error("Missing translation key {} in {} {}",
					translatableContents.getKey(), type, id);
			missing.increment();
		}
		for (Component sibling : component.getSiblings()) {
			validateComponent(id, type, sibling, missing);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> Set<TagKey<T>> findAdditionalTagKeys(Set<Class<?>> sourceClasses) {
		Set<TagKey<T>> tags = new TreeSet<>(Comparator.comparing(TagKey::location));
		for (Class<?> sourceClass : sourceClasses) {
			for (Field field : sourceClass.getDeclaredFields()) {
				if (field.getType() == TagKey.class && (field.getModifiers() & Modifier.STATIC) != 0
						&& field.canAccess(null)) {
					try {
						tags.add((TagKey<T>) field.get(sourceClass));
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		BotaniaAPI.LOGGER.info("Including additional tags in check: {}", tags.stream().map(TagKey::location).toList());
		return tags;
	}
}
