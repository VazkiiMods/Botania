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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.ShimmeringMushroomBlock;
import vazkii.botania.common.block.flower.BotaniaFlowerBlock;
import vazkii.botania.common.block.flower.FloatingFlowerBaseBlock;
import vazkii.botania.common.block.flower.FlowerMotifBlock;
import vazkii.botania.common.block.flower.SpecialFlowerBlock;
import vazkii.botania.common.block.flower.TallMysticalFlowerBlock;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.block.mana.ManaSpreaderBlock;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.BottledManaItem;
import vazkii.botania.common.item.brew.BaseBrewItem;
import vazkii.botania.common.item.lens.LensItem;
import vazkii.botania.common.item.material.MysticalPetalItem;
import vazkii.botania.data.util.ModelWithOverrides;
import vazkii.botania.data.util.OverrideHolder;
import vazkii.botania.data.util.SimpleModelSupplierWithOverrides;
import vazkii.botania.mixin.TextureSlotAccessor;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ItemModelProvider implements DataProvider {
	private static final TextureSlot LAYER1 = TextureSlotAccessor.botania_create("layer1");
	private static final TextureSlot LAYER2 = TextureSlotAccessor.botania_create("layer2");
	private static final TextureSlot LAYER3 = TextureSlotAccessor.botania_create("layer3");
	private static final ModelTemplate GENERATED_1 = new ModelTemplate(
			Optional.of(ResourceLocation.withDefaultNamespace("item/generated")), Optional.empty(),
			TextureSlot.LAYER0, LAYER1);
	private static final ModelTemplate GENERATED_2 = new ModelTemplate(
			Optional.of(ResourceLocation.withDefaultNamespace("item/generated")), Optional.empty(),
			TextureSlot.LAYER0, LAYER1, LAYER2);
	private static final ModelTemplate HANDHELD_1 = new ModelTemplate(
			Optional.of(ResourceLocation.withDefaultNamespace("item/handheld")), Optional.empty(),
			TextureSlot.LAYER0, LAYER1);
	private static final ModelTemplate HANDHELD_3 = new ModelTemplate(
			Optional.of(ResourceLocation.withDefaultNamespace("item/handheld")), Optional.empty(),
			TextureSlot.LAYER0, LAYER1, LAYER2, LAYER3);
	private static final ModelTemplate WALL_INVENTORY = new ModelTemplate(
			Optional.of(botaniaRL("block/shapes/wall_inventory")), Optional.empty(),
			TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.WALL);
	private static final ModelTemplate WALL_INVENTORY_CHECKERED = new ModelTemplate(
			Optional.of(botaniaRL("block/shapes/wall_inventory_checkered")), Optional.empty(),
			TextureSlot.NORTH, TextureSlot.SIDE);
	private static final TextureSlot OUTSIDE = TextureSlotAccessor.botania_create("outside");
	private static final TextureSlot CORE = TextureSlotAccessor.botania_create("core");
	private static final ModelTemplate SPREADER = new ModelTemplate(
			Optional.of(botaniaRL("block/shapes/spreader_item")), Optional.empty(),
			TextureSlot.SIDE, TextureSlot.BACK, TextureSlot.INSIDE, OUTSIDE, CORE);
	private static final ModelWithOverrides GENERATED_OVERRIDES = new ModelWithOverrides(
			ResourceLocation.withDefaultNamespace("item/generated"), TextureSlot.LAYER0);
	private static final ModelWithOverrides GENERATED_OVERRIDES_1 = new ModelWithOverrides(
			ResourceLocation.withDefaultNamespace("item/generated"), TextureSlot.LAYER0, LAYER1);
	private static final ModelWithOverrides HANDHELD_OVERRIDES = new ModelWithOverrides(
			ResourceLocation.withDefaultNamespace("item/handheld"), TextureSlot.LAYER0);
	private static final ModelWithOverrides HANDHELD_OVERRIDES_2 = new ModelWithOverrides(
			ResourceLocation.withDefaultNamespace("item/handheld"), TextureSlot.LAYER0, LAYER1, LAYER2);

	private final PackOutput packOutput;

	public ItemModelProvider(PackOutput packOutput) {
		this.packOutput = packOutput;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		Set<Item> items = BuiltInRegistries.ITEM.stream()
				.filter(item -> BotaniaAPI.MODID.equals(BuiltInRegistries.ITEM.getKey(item).getNamespace()))
				.collect(Collectors.toSet());
		Map<ResourceLocation, Supplier<JsonElement>> map = new HashMap<>();
		registerItemBlocks(takeAll(items, item -> item instanceof BlockItem).stream()
				.map(item -> (BlockItem) item)
				.collect(Collectors.toSet()), map::put);
		registerItemOverrides(items, map::put);
		registerItems(items, map::put);

		PackOutput.PathProvider modelPathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
		List<CompletableFuture<?>> outputList = new ArrayList<>();

		for (Map.Entry<ResourceLocation, Supplier<JsonElement>> e : map.entrySet()) {
			ResourceLocation id = e.getKey();
			outputList.add(DataProvider.saveStable(output, e.getValue().get(), modelPathProvider.json(id)));
		}

		return CompletableFuture.allOf(outputList.toArray(CompletableFuture[]::new));
	}

	private static void registerItems(Set<Item> items, BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer) {
		// Written manually
		items.remove(BotaniaItems.MANA_BLASTER);

		takeAll(items, item -> item instanceof LensItem).forEach(item -> {
			ResourceLocation lens;
			if (item == BotaniaItems.RESISTANCE_LENS || item == BotaniaItems.WARP_LENS
					|| item == BotaniaItems.KINDLE_LENS || item == BotaniaItems.TRIPWIRE_LENS) {
				// To avoid z-fighting
				lens = botaniaRL("item/lens_small");
			} else {
				lens = botaniaRL("item/lens");
			}
			GENERATED_1.create(ModelLocationUtils.getModelLocation(item),
					TextureMapping.layer0(lens).put(LAYER1, TextureMapping.getItemTexture(item)), consumer);
		});

		GENERATED_1.create(ModelLocationUtils.getModelLocation(BotaniaItems.TAINTED_BLOOD_PENDANT),
				TextureMapping.layer0(TextureMapping.getItemTexture(BotaniaItems.TAINTED_BLOOD_PENDANT))
						.put(LAYER1, TextureMapping.getItemTexture(BotaniaItems.TAINTED_BLOOD_PENDANT, "_overlay")),
				consumer);
		items.remove(BotaniaItems.TAINTED_BLOOD_PENDANT);

		HANDHELD_1.create(ModelLocationUtils.getModelLocation(BotaniaItems.SOULSCRIBE),
				TextureMapping.layer0(TextureMapping.getItemTexture(BotaniaItems.SOULSCRIBE))
						.put(LAYER1, TextureMapping.getItemTexture(BotaniaItems.SOULSCRIBE, "_overlay")),
				consumer);
		items.remove(BotaniaItems.SOULSCRIBE);

		GENERATED_1.create(ModelLocationUtils.getModelLocation(BotaniaItems.INCENSE_STICK),
				TextureMapping.layer0(TextureMapping.getItemTexture(BotaniaItems.INCENSE_STICK))
						.put(LAYER1, TextureMapping.getItemTexture(BotaniaItems.INCENSE_STICK, "_overlay")),
				consumer);
		items.remove(BotaniaItems.INCENSE_STICK);

		GENERATED_1.create(ModelLocationUtils.getModelLocation(BotaniaItems.MANA_MIRROR),
				TextureMapping.layer0(TextureMapping.getItemTexture(BotaniaItems.MANA_MIRROR))
						.put(LAYER1, TextureMapping.getItemTexture(BotaniaItems.MANA_MIRROR, "_overlay")),
				consumer);
		items.remove(BotaniaItems.MANA_MIRROR);

		GENERATED_1.create(ModelLocationUtils.getModelLocation(BotaniaItems.MANA_TABLET),
				TextureMapping.layer0(TextureMapping.getItemTexture(BotaniaItems.MANA_TABLET))
						.put(LAYER1, TextureMapping.getItemTexture(BotaniaItems.MANA_TABLET, "_overlay")),
				consumer);
		items.remove(BotaniaItems.MANA_TABLET);

		GENERATED_2.create(ModelLocationUtils.getModelLocation(BotaniaItems.THIRD_EYE),
				new TextureMapping().put(TextureSlot.LAYER0, TextureMapping.getItemTexture(BotaniaItems.THIRD_EYE, "_0"))
						.put(LAYER1, TextureMapping.getItemTexture(BotaniaItems.THIRD_EYE, "_1"))
						.put(LAYER2, TextureMapping.getItemTexture(BotaniaItems.THIRD_EYE, "_2")),
				consumer);
		items.remove(BotaniaItems.THIRD_EYE);

		takeAll(items, BotaniaItems.ROD_OF_THE_DEPTHS, BotaniaItems.ROD_OF_THE_LANDS,
				BotaniaItems.ROD_OF_THE_PLENTIFUL_MANTLE, BotaniaItems.ELEMENTIUM_AXE, BotaniaItems.ELEMENTIUM_PICKAXE,
				BotaniaItems.ELEMENTIUM_SHOVEL, BotaniaItems.ELEMENTIUM_HOE, BotaniaItems.ELEMENTIUM_SWORD,
				BotaniaItems.ROD_OF_THE_SHIFTING_CRUST, BotaniaItems.ROD_OF_THE_HELLS, BotaniaItems.VITREOUS_PICKAXE,
				BotaniaItems.ROD_OF_THE_SHADED_MESA, BotaniaItems.MANASTEEL_AXE, BotaniaItems.MANASTEEL_PICKAXE,
				BotaniaItems.MANASTEEL_SHEARS, BotaniaItems.MANASTEEL_SHOVEL, BotaniaItems.MANASTEEL_HOE,
				BotaniaItems.ROD_OF_THE_UNSTABLE_RESERVOIR, BotaniaItems.FLORAL_OBEDIENCE_STICK,
				BotaniaItems.ROD_OF_THE_BIFROST, BotaniaItems.ROD_OF_THE_MOLTEN_CORE, BotaniaItems.STARCALLER,
				BotaniaItems.TERRA_BLADE, BotaniaItems.ROD_OF_THE_TERRA_FIRMA, BotaniaItems.THUNDERCALLER,
				BotaniaItems.ROD_OF_THE_SEAS, BotaniaItems.KEY_OF_THE_KINGS_LAW, BotaniaItems.ROD_OF_THE_HIGHLANDS
		).forEach(item -> ModelTemplates.FLAT_HANDHELD_ITEM
				.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), consumer));

		takeAll(items, item -> true).forEach(item -> ModelTemplates.FLAT_ITEM
				.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), consumer));
	}

	private static void singleGeneratedOverride(Item item, ResourceLocation overrideModel, ResourceLocation predicate,
			double value, BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer) {
		ModelTemplates.FLAT_ITEM.create(overrideModel, TextureMapping.layer0(overrideModel), consumer);
		GENERATED_OVERRIDES.create(ModelLocationUtils.getModelLocation(item),
				TextureMapping.layer0(item),
				new OverrideHolder().add(overrideModel, Pair.of(predicate, value)),
				consumer);
	}

	private static void singleGeneratedSuffixOverride(Item item, String suffix, ResourceLocation predicate,
			double value, BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer) {
		singleGeneratedOverride(item, ModelLocationUtils.getModelLocation(item, suffix), predicate, value, consumer);
	}

	private static void singleHandheldOverride(Item item, ResourceLocation overrideModel, ResourceLocation predicate,
			double value, BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer) {
		ModelTemplates.FLAT_HANDHELD_ITEM.create(overrideModel, TextureMapping.layer0(overrideModel), consumer);
		HANDHELD_OVERRIDES.create(ModelLocationUtils.getModelLocation(item),
				TextureMapping.layer0(item),
				new OverrideHolder()
						.add(overrideModel, Pair.of(predicate, value)),
				consumer);
	}

	private static void singleHandheldSuffixOverride(Item item, String suffix, ResourceLocation predicate, double value,
			BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer) {
		singleHandheldOverride(item, ModelLocationUtils.getModelLocation(item, suffix), predicate, value, consumer);
	}

	private static void registerItemOverrides(Set<Item> items, BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer) {
		// Written manually
		items.remove(BotaniaItems.LIVINGWOOD_BOW);
		items.remove(BotaniaItems.CRYSTAL_BOW);

		singleGeneratedSuffixOverride(BotaniaItems.TRINKET_CASE, "_open", botaniaRL("open"), 1.0, consumer);
		items.remove(BotaniaItems.TRINKET_CASE);

		singleGeneratedSuffixOverride(BotaniaItems.BLACK_HOLE_TALISMAN, "_active", botaniaRL("active"), 1.0, consumer);
		items.remove(BotaniaItems.BLACK_HOLE_TALISMAN);

		OverrideHolder flaskOverrides = new OverrideHolder();
		for (int i = 1; i < BaseBrewItem.DEFAULT_USES_FLASK; i++) {
			ResourceLocation overrideModel = ModelLocationUtils.getModelLocation(BotaniaItems.BREW_FLASK, "_" + i);
			GENERATED_1.create(overrideModel,
					TextureMapping.layer0(BotaniaItems.ALFGLASS_FLASK).put(LAYER1, overrideModel),
					consumer);

			flaskOverrides.add(overrideModel, Pair.of(botaniaRL("swigs_taken"), (double) i / (BaseBrewItem.DEFAULT_USES_FLASK - 1)));
		}
		GENERATED_OVERRIDES_1.create(ModelLocationUtils.getModelLocation(BotaniaItems.BREW_FLASK),
				TextureMapping.layer0(BotaniaItems.ALFGLASS_FLASK).put(LAYER1, TextureMapping.getItemTexture(
						BotaniaItems.BREW_FLASK, "_0")),
				flaskOverrides,
				consumer);
		items.remove(BotaniaItems.BREW_FLASK);

		OverrideHolder vialOverrides = new OverrideHolder();
		for (int i = 1; i < BaseBrewItem.DEFAULT_USES_VIAL; i++) {
			ResourceLocation overrideModel = ModelLocationUtils.getModelLocation(BotaniaItems.BREW_VIAL, "_" + i);
			GENERATED_1.create(overrideModel,
					TextureMapping.layer0(BotaniaItems.MANAGLASS_VIAL).put(LAYER1, overrideModel),
					consumer);
			vialOverrides.add(overrideModel, Pair.of(botaniaRL("swigs_taken"), (double) i / (BaseBrewItem.DEFAULT_USES_VIAL - 1)));
		}
		GENERATED_OVERRIDES_1.create(ModelLocationUtils.getModelLocation(BotaniaItems.BREW_VIAL),
				TextureMapping.layer0(BotaniaItems.MANAGLASS_VIAL).put(LAYER1, TextureMapping.getItemTexture(
						BotaniaItems.BREW_VIAL, "_0")),
				vialOverrides, consumer);
		items.remove(BotaniaItems.BREW_VIAL);

		singleHandheldOverride(BotaniaItems.ELEMENTIUM_SHEARS, botaniaRL("item/dammitreddit"), botaniaRL("reddit"), 1, consumer);
		items.remove(BotaniaItems.ELEMENTIUM_SHEARS);

		ResourceLocation vuvuzela = botaniaRL("item/vuvuzela");
		ModelTemplates.FLAT_HANDHELD_ITEM.create(vuvuzela, TextureMapping.layer0(vuvuzela), consumer);
		// defined manually to apply display transforms:
		items.remove(BotaniaItems.HORN_OF_THE_WILD);
		items.remove(BotaniaItems.HORN_OF_THE_CANOPY);
		items.remove(BotaniaItems.HORN_OF_THE_COVERING);

		singleGeneratedOverride(BotaniaItems.FRUIT_OF_GRISAIA, botaniaRL("item/dasboot"), botaniaRL("boot"), 1, consumer);
		items.remove(BotaniaItems.FRUIT_OF_GRISAIA);

		singleGeneratedSuffixOverride(BotaniaItems.LEXICA_BOTANIA, "_elven", botaniaRL("elven"), 1.0, consumer);
		items.remove(BotaniaItems.LEXICA_BOTANIA);

		singleGeneratedSuffixOverride(BotaniaItems.RING_OF_MAGNETIZATION, "_active", botaniaRL("active"), 1.0, consumer);
		items.remove(BotaniaItems.RING_OF_MAGNETIZATION);

		singleGeneratedSuffixOverride(BotaniaItems.GREATER_RING_OF_MAGNETIZATION, "_active", botaniaRL("active"), 1.0, consumer);
		items.remove(BotaniaItems.GREATER_RING_OF_MAGNETIZATION);

		OverrideHolder bottleOverrides = new OverrideHolder();
		for (int i = 1; i < BottledManaItem.SWIGS; i++) {
			ResourceLocation overrideModel = ModelLocationUtils.getModelLocation(BotaniaItems.MANA_IN_A_BOTTLE, "_" + i);
			ModelTemplates.FLAT_ITEM.create(overrideModel, TextureMapping.layer0(overrideModel), consumer);
			bottleOverrides.add(overrideModel, Pair.of(botaniaRL("swigs_taken"), (double) i / (BottledManaItem.SWIGS - 1)));
		}
		GENERATED_OVERRIDES.create(ModelLocationUtils.getModelLocation(BotaniaItems.MANA_IN_A_BOTTLE),
				TextureMapping.layer0(BotaniaItems.MANA_IN_A_BOTTLE),
				bottleOverrides,
				consumer);
		items.remove(BotaniaItems.MANA_IN_A_BOTTLE);

		singleGeneratedOverride(BotaniaItems.BISCUIT_OF_TOTALITY, botaniaRL("item/totalbiscuit"), botaniaRL("totalbiscuit"), 1.0, consumer);
		items.remove(BotaniaItems.BISCUIT_OF_TOTALITY);

		singleHandheldOverride(BotaniaItems.MANASTEEL_SWORD, botaniaRL("item/elucidator"), botaniaRL("elucidator"), 1.0, consumer);
		items.remove(BotaniaItems.MANASTEEL_SWORD);

		singleGeneratedSuffixOverride(BotaniaItems.MANAWEAVE_COWL, "_holiday", botaniaRL("holiday"), 1.0, consumer);
		items.remove(BotaniaItems.MANAWEAVE_COWL);

		singleGeneratedSuffixOverride(BotaniaItems.MANAWEAVE_ROBE_TOP, "_holiday", botaniaRL("holiday"), 1.0, consumer);
		items.remove(BotaniaItems.MANAWEAVE_ROBE_TOP);

		singleGeneratedSuffixOverride(BotaniaItems.MANAWEAVE_ROBE_BOTTOM, "_holiday", botaniaRL("holiday"), 1.0, consumer);
		items.remove(BotaniaItems.MANAWEAVE_ROBE_BOTTOM);

		singleGeneratedSuffixOverride(BotaniaItems.MANAWEAVE_BOOTS, "_holiday", botaniaRL("holiday"), 1.0, consumer);
		items.remove(BotaniaItems.MANAWEAVE_BOOTS);

		singleGeneratedSuffixOverride(BotaniaItems.SLIME_IN_A_BOTTLE, "_active", botaniaRL("active"), 1.0, consumer);
		items.remove(BotaniaItems.SLIME_IN_A_BOTTLE);

		singleGeneratedSuffixOverride(BotaniaItems.LIFE_AGGREGATOR, "_full", botaniaRL("full"), 1.0, consumer);
		items.remove(BotaniaItems.LIFE_AGGREGATOR);

		singleGeneratedSuffixOverride(BotaniaItems.STONE_OF_TEMPERANCE, "_active", botaniaRL("active"), 1.0, consumer);
		items.remove(BotaniaItems.STONE_OF_TEMPERANCE);

		singleHandheldSuffixOverride(BotaniaItems.TERRA_TRUNCATOR, "_active", botaniaRL("active"), 1.0, consumer);
		items.remove(BotaniaItems.TERRA_TRUNCATOR);

		singleGeneratedSuffixOverride(BotaniaItems.MANUFACTORY_HALO, "_active", botaniaRL("active"), 1.0, consumer);
		items.remove(BotaniaItems.MANUFACTORY_HALO);

		ResourceLocation enabledModel = ModelLocationUtils.getModelLocation(BotaniaItems.TERRA_SHATTERER, "_active");
		ModelTemplates.FLAT_HANDHELD_ITEM.create(enabledModel, TextureMapping.layer0(enabledModel), consumer);

		ResourceLocation tippedModel = ModelLocationUtils.getModelLocation(BotaniaItems.TERRA_SHATTERER, "_tipped");
		ModelTemplates.FLAT_HANDHELD_ITEM.create(tippedModel, TextureMapping.layer0(tippedModel), consumer);

		ResourceLocation tippedEnabledModel = ModelLocationUtils.getModelLocation(BotaniaItems.TERRA_SHATTERER, "_tipped_active");
		ModelTemplates.FLAT_HANDHELD_ITEM.create(tippedEnabledModel, TextureMapping.layer0(tippedEnabledModel), consumer);

		HANDHELD_OVERRIDES.create(ModelLocationUtils.getModelLocation(BotaniaItems.TERRA_SHATTERER),
				TextureMapping.layer0(BotaniaItems.TERRA_SHATTERER),
				new OverrideHolder()
						.add(enabledModel, Pair.of(botaniaRL("active"), 1.0))
						.add(tippedModel, Pair.of(botaniaRL("tipped"), 1.0))
						.add(tippedEnabledModel, Pair.of(botaniaRL("tipped"), 1.0), Pair.of(botaniaRL("active"), 1.0)),
				consumer);
		items.remove(BotaniaItems.TERRA_SHATTERER);

		singleHandheldSuffixOverride(BotaniaItems.ROD_OF_THE_SKIES, "_active", botaniaRL("active"), 1.0, consumer);
		items.remove(BotaniaItems.ROD_OF_THE_SKIES);

		registerWandModels(items, consumer, BotaniaItems.WAND_OF_THE_FOREST);
		registerWandModels(items, consumer, BotaniaItems.WAND_OF_THE_ELVEN_FOREST);
	}

	private static void registerWandModels(Set<Item> items, BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer, Item wandType) {
		TextureMapping twigWandTextures = TextureMapping.layer0(wandType)
				.put(LAYER1, TextureMapping.getItemTexture(wandType, "_top"))
				.put(LAYER2, TextureMapping.getItemTexture(wandType, "_bottom"));
		ResourceLocation twigWandBind = ModelLocationUtils.getModelLocation(wandType, "_bind");
		HANDHELD_3.create(twigWandBind,
				twigWandTextures.copyAndUpdate(LAYER3, TextureMapping.getItemTexture(wandType, "_bind")),
				consumer);
		HANDHELD_OVERRIDES_2.create(ModelLocationUtils.getModelLocation(wandType),
				twigWandTextures,
				new OverrideHolder().add(twigWandBind, Pair.of(botaniaRL("bindmode"), 1.0)),
				consumer);
		items.remove(wandType);
	}

	@SuppressWarnings("SuspiciousMethodCalls")
	private void registerItemBlocks(Set<BlockItem> itemBlocks, BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer) {
		// Manually written
		itemBlocks.remove(BotaniaBlocks.CORPOREA_CRYSTAL_CUBE.asItem());

		// Generated by FloatingFlowerModelProvider
		itemBlocks.removeIf(item -> {
			var id = BuiltInRegistries.BLOCK.getKey(item.getBlock());
			return id.getNamespace().equals(BotaniaAPI.MODID) && item.getBlock() instanceof FloatingFlowerBaseBlock;
		});

		GENERATED_1.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.ANIMATED_TORCH.asItem()),
				TextureMapping.layer0(Blocks.REDSTONE_TORCH).put(LAYER1, botaniaRL("block/animated_torch_glimmer")), consumer);
		itemBlocks.remove(BotaniaBlocks.ANIMATED_TORCH.asItem());

		ModelTemplates.SKULL_INVENTORY.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.GAIA_HEAD.asItem()), new TextureMapping(), consumer);
		itemBlocks.remove(BotaniaBlocks.GAIA_HEAD.asItem());

		takeAll(itemBlocks, item -> item.getBlock() instanceof TallMysticalFlowerBlock).forEach(
				item -> ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
						TextureMapping.layer0(TextureMapping.getBlockTexture(item.getBlock(), "_top")), consumer));

		takeAll(itemBlocks, BotaniaBlocks.FRAMED_LIVINGWOOD.asItem(), BotaniaBlocks.FRAMED_DREAMWOOD.asItem())
				.forEach(item -> {
					String name = item == BotaniaBlocks.FRAMED_LIVINGWOOD.asItem() ? "livingwood" : "dreamwood";
					consumer.accept(ModelLocationUtils.getModelLocation(item),
							new DelegatedModel(botaniaRL("block/framed_" + name + "_horizontal_z")));
				});

		consumer.accept(ModelLocationUtils.getModelLocation(BotaniaBlocks.FRAMED_LIVINGWOOD.asItem()),
				new DelegatedModel(botaniaRL("block/framed_livingwood_horizontal_z")));
		consumer.accept(ModelLocationUtils.getModelLocation(BotaniaBlocks.FRAMED_DREAMWOOD.asItem()),
				new DelegatedModel(botaniaRL("block/framed_dreamwood_horizontal_z")));
		itemBlocks.remove(BotaniaBlocks.FRAMED_LIVINGWOOD.asItem());
		itemBlocks.remove(BotaniaBlocks.FRAMED_DREAMWOOD.asItem());

		takeAll(itemBlocks, item -> item.getBlock() instanceof IronBarsBlock).forEach(item -> {
			String name = BuiltInRegistries.ITEM.getKey(item).getPath();
			String baseName = name.substring(0, name.length() - "_pane".length());
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
					TextureMapping.layer0(botaniaRL("block/" + baseName)), consumer);
		});

		Predicate<BlockItem> defaultGenerated = item -> {
			Block block = item.getBlock();
			return block instanceof SpecialFlowerBlock
					|| block instanceof ShimmeringMushroomBlock
					|| block instanceof LuminizerBlock
					|| block instanceof BotaniaFlowerBlock
					|| block == BotaniaBlocks.SPECTRAL_RAIL;
		};
		takeAll(itemBlocks, defaultGenerated).forEach(item -> ModelTemplates.FLAT_ITEM
				.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item.getBlock()), consumer));

		takeAll(itemBlocks, item -> item.getBlock() instanceof FlowerMotifBlock).forEach(item -> {
			String name = BuiltInRegistries.ITEM.getKey(item).getPath();
			ResourceLocation texName = botaniaRL("block/" + name.replace("_motif", ""));
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(texName), consumer);
		});

		takeAll(itemBlocks, item -> item.getBlock() instanceof ManaPoolBlock).forEach(item -> {
			Block baseBlock = ManaPoolBlock.getUndyedBlock((ManaPoolBlock) item.getBlock());
			ResourceLocation fullModel = ModelLocationUtils.getModelLocation(baseBlock, "_full");
			OverrideHolder overrides = new OverrideHolder().add(fullModel, Pair.of(botaniaRL("full"), 1.0));
			consumer.accept(ModelLocationUtils.getModelLocation(item),
					new SimpleModelSupplierWithOverrides(ModelLocationUtils.getModelLocation(baseBlock), overrides));
		});
		takeAll(itemBlocks, Stream
				.of(BotaniaBlocks.LIVINGWOOD_WALL, BotaniaBlocks.STRIPPED_LIVINGWOOD_WALL,
						BotaniaBlocks.DREAMWOOD_WALL, BotaniaBlocks.STRIPPED_DREAMWOOD_WALL)
				.map(block -> (BlockItem) block.asItem())
				.toArray(BlockItem[]::new))
				.forEach(item -> {
					String name = BuiltInRegistries.ITEM.getKey(item).getPath();
					String baseName = name.substring(0, name.length() - "_wall".length()) + "_log";
					ModelTemplates.WALL_INVENTORY.create(ModelLocationUtils.getModelLocation(item),
							new TextureMapping().put(TextureSlot.WALL, botaniaRL("block/" + baseName)), consumer);
				});
		takeAll(itemBlocks, item -> item.getBlock() instanceof ButtonBlock).forEach(
				item -> consumer.accept(ModelLocationUtils.getModelLocation(item),
						new DelegatedModel(ModelLocationUtils.getModelLocation(item.getBlock(), "_inventory"))));
		takeAll(itemBlocks, item -> item.getBlock() instanceof TrapDoorBlock).forEach(
				item -> consumer.accept(ModelLocationUtils.getModelLocation(item),
						new DelegatedModel(ModelLocationUtils.getModelLocation(item.getBlock(), "_bottom"))));

		ModelTemplates.WALL_INVENTORY.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.CORPOREA_WALL.asItem()),
				new TextureMapping().put(TextureSlot.WALL, TextureMapping.getBlockTexture(BotaniaBlocks.CORPOREA_BLOCK)), consumer);
		itemBlocks.remove(BotaniaBlocks.CORPOREA_WALL.asItem());
		takeAll(itemBlocks, item -> item.getBlock() instanceof WallBlock).forEach(item -> {
			String name = BuiltInRegistries.ITEM.getKey(item).getPath();
			String tentativeBaseName = name.substring(0, name.length() - "_wall".length());
			String baseName = tentativeBaseName.endsWith("brick") ? tentativeBaseName + "s" : tentativeBaseName;
			ModelTemplates.WALL_INVENTORY.create(ModelLocationUtils.getModelLocation(item),
					new TextureMapping().put(TextureSlot.WALL, botaniaRL("block/" + baseName)), consumer);
		});

		takeAll(itemBlocks, item -> item.getBlock() instanceof ManaSpreaderBlock).forEach(item -> {
			ManaSpreaderBlock block = (ManaSpreaderBlock) item.getBlock();
			ManaSpreaderBlock baseBlock = ManaSpreaderBlock.getBaseBlock(block);
			String name = BuiltInRegistries.BLOCK.getKey(baseBlock).getPath();
			String outside;
			if (baseBlock == BotaniaBlocks.ELVEN_MANA_SPREADER) {
				outside = "dreamwood_log_3";
			} else if (baseBlock == BotaniaBlocks.GAIA_MANA_SPREADER) {
				outside = name + "_outside";
			} else {
				outside = "livingwood_log";
			}
			String inside;
			if (baseBlock == BotaniaBlocks.ELVEN_MANA_SPREADER) {
				inside = "stripped_dreamwood_log_3";
			} else if (baseBlock == BotaniaBlocks.GAIA_MANA_SPREADER) {
				inside = name + "_inside";
			} else {
				inside = "stripped_livingwood_log";
			}
			TextureMapping textureMapping = new TextureMapping()
					.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(baseBlock, "_side"))
					.put(OUTSIDE, botaniaRL("block/" + outside))
					.put(TextureSlot.BACK, TextureMapping.getBlockTexture(baseBlock, "_back"))
					.put(TextureSlot.INSIDE, botaniaRL("block/" + inside))
					.put(CORE, TextureMapping.getBlockTexture(baseBlock, "_core"));
			SPREADER.create(ModelLocationUtils.getModelLocation(item), textureMapping, consumer);
		});

		takeAll(itemBlocks, BotaniaBlocks.LIVINGWOOD_AVATAR.asItem(), BotaniaBlocks.MANATIDE_BELLOWS.asItem(),
				BotaniaBlocks.BOTANICAL_BREWERY.asItem(), BotaniaBlocks.CORPOREA_INDEX.asItem(), BotaniaBlocks.GAIA_PYLON.asItem(),
				BotaniaBlocks.MANA_PYLON.asItem(), BotaniaBlocks.NATURA_PYLON.asItem())
				.forEach(item -> builtinEntity(item, consumer));

		takeAll(itemBlocks, BotaniaBlocks.HOVERING_HOURGLASS.asItem())
				.forEach(item -> builtinEntity(item, consumer, 1.375));

		takeAll(itemBlocks, BotaniaBlocks.TERU_TERU_BOZU.asItem())
				.forEach(item -> builtinEntity(item, consumer, 1.0, 2.5));

		Predicate<BlockItem> defaultGeneratedItem = item -> item instanceof MysticalPetalItem
				|| item instanceof SignItem
				|| item instanceof BlockItem blockItem && blockItem.getBlock() instanceof DoorBlock;
		takeAll(itemBlocks, defaultGeneratedItem).forEach(
				item -> ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
						TextureMapping.layer0(TextureMapping.getItemTexture(item)), consumer));

		ModelTemplates.FENCE_INVENTORY.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.DREAMWOOD_FENCE.asItem()),
				TextureMapping.defaultTexture(BotaniaBlocks.DREAMWOOD_PLANKS), consumer);
		itemBlocks.remove(BotaniaBlocks.DREAMWOOD_FENCE.asItem());

		ModelTemplates.FENCE_INVENTORY.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.LIVINGWOOD_FENCE.asItem()),
				TextureMapping.defaultTexture(BotaniaBlocks.LIVINGWOOD_PLANKS), consumer);
		itemBlocks.remove(BotaniaBlocks.LIVINGWOOD_FENCE.asItem());

		ModelTemplates.FENCE_INVENTORY.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.SHIMMERWOOD_FENCE.asItem()),
				TextureMapping.defaultTexture(BotaniaBlocks.SHIMMERWOOD_PLANKS), consumer);
		itemBlocks.remove(BotaniaBlocks.SHIMMERWOOD_FENCE.asItem());

		consumer.accept(ModelLocationUtils.getModelLocation(BotaniaBlocks.ALFGLASS.asItem()), new DelegatedModel(botaniaRL("block/alfglass_0")));
		itemBlocks.remove(BotaniaBlocks.ALFGLASS.asItem());

		WALL_INVENTORY.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.TALC_BRICK_WALL.asItem()),
				new TextureMapping()
						.put(TextureSlot.TOP, TextureMapping.getBlockTexture(BotaniaBlocks.TALC_BRICKS, "_top"))
						.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(BotaniaBlocks.TALC_BRICKS, "_top"))
						.put(TextureSlot.WALL, TextureMapping.getBlockTexture(BotaniaBlocks.TALC_BRICKS)),
				consumer);
		itemBlocks.remove(BotaniaBlocks.TALC_BRICK_WALL.asItem());

		WALL_INVENTORY_CHECKERED.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.ROSY_TALC_BRICK_WALL.asItem()),
				new TextureMapping()
						.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(BotaniaBlocks.ROSY_TALC_BRICKS))
						.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(BotaniaBlocks.ROSY_TALC_BRICKS, "_mirrored")),
				consumer);
		itemBlocks.remove(BotaniaBlocks.ROSY_TALC_BRICK_WALL.asItem());

		itemBlocks.forEach(item -> consumer.accept(ModelLocationUtils.getModelLocation(item),
				new DelegatedModel(ModelLocationUtils.getModelLocation(item.getBlock()))));
	}

	// [VanillaCopy] item/chest.json
	// Scuffed af.....but it works :wacko:
	private static final String BUILTIN_ENTITY_DISPLAY_STR =
			"""
					{
						"gui": {
							"rotation": [30, 45, 0],
							"translation": [0, 0, 0],
							"scale": [0.625, 0.625, 0.625]
						},
						"ground": {
							"rotation": [0, 0, 0],
							"translation": [0, 3, 0],
							"scale": [0.25, 0.25, 0.25]
						},
						"head": {
							"rotation": [0, 180, 0],
							"translation": [0, 0, 0],
							"scale": [1, 1, 1]
						},
						"fixed": {
							"rotation": [0, 180, 0],
							"translation": [0, 0, 0],
							"scale": [0.5, 0.5, 0.5]
						},
						"thirdperson_righthand": {
							"rotation": [75, 315, 0],
							"translation": [0, 2.5, 0],
							"scale": [0.375, 0.375, 0.375]
						},
						"firstperson_righthand": {
							"rotation": [0, 315, 0],
							"translation": [0, 0, 0],
							"scale": [0.4, 0.4, 0.4]
						}
					}""";
	private static final JsonElement BUILTIN_ENTITY_DISPLAY = new Gson().fromJson(BUILTIN_ENTITY_DISPLAY_STR, JsonElement.class);

	protected void builtinEntity(Item item, BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer) {
		builtinEntity(item, consumer, 1.0);
	}

	protected void builtinEntity(Item item, BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer, double scale) {
		builtinEntity(item, consumer, scale, 0.0);
	}

	protected void builtinEntity(Item item, BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer, double scale, double handYOffset) {
		final JsonElement display;
		if (handYOffset == 0.0 && scale == 1.0) {
			display = BUILTIN_ENTITY_DISPLAY;
		} else {
			display = BUILTIN_ENTITY_DISPLAY.deepCopy();
			JsonObject displayObject = display.getAsJsonObject();
			var scaleDecimal = new BigDecimal(String.valueOf(scale));
			displayObject.keySet().forEach(key -> {
				var array = displayObject.getAsJsonObject(key).getAsJsonArray("scale");
				for (int i = 0; i < array.size(); i++) {
					array.set(i, new JsonPrimitive(new BigDecimal(array.get(i).toString()).multiply(scaleDecimal).stripTrailingZeros()));
				}
			});
			displayObject.getAsJsonObject("firstperson_righthand")
					.getAsJsonArray("translation").set(1, new JsonPrimitive(handYOffset));
		}
		consumer.accept(ModelLocationUtils.getModelLocation(item), () -> {
			JsonObject json = new JsonObject();
			json.addProperty("parent", "minecraft:builtin/entity");
			json.add("display", display);
			return json;
		});
	}

	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
		List<T> ret = Arrays.asList(items);
		for (T item : items) {
			if (!src.contains(item)) {
				BotaniaAPI.LOGGER.warn("Item {} not found in set", item);
			}
		}
		if (!src.removeAll(ret)) {
			BotaniaAPI.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
		}
		return ret;
	}

	public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
		List<T> ret = new ArrayList<>();

		Iterator<T> iter = src.iterator();
		while (iter.hasNext()) {
			T item = iter.next();
			if (pred.test(item)) {
				iter.remove();
				ret.add(item);
			}
		}

		if (ret.isEmpty()) {
			BotaniaAPI.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
		}
		return ret;
	}

	@Override
	public String getName() {
		return "Botania item models";
	}
}
