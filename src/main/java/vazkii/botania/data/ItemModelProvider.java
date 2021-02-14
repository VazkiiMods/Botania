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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.client.model.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import vazkii.botania.common.Botania;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.decor.BlockMotifFlower;
import vazkii.botania.common.block.decor.BlockPetalBlock;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.mana.BlockSpreader;
import vazkii.botania.common.item.lens.ItemLens;
import vazkii.botania.common.item.material.ItemPetal;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.data.util.ModelWithOverrides;
import vazkii.botania.data.util.OverrideHolder;
import vazkii.botania.data.util.SimpleModelSupplierWithOverrides;
import vazkii.botania.mixin.AccessorTextureKey;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static vazkii.botania.common.item.ModItems.*;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;
import static vazkii.botania.data.BlockstateProvider.takeAll;

public class ItemModelProvider implements DataProvider {
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private static final TextureKey LAYER1 = AccessorTextureKey.make("layer1");
	private static final TextureKey LAYER2 = AccessorTextureKey.make("layer2");
	private static final TextureKey LAYER3 = AccessorTextureKey.make("layer3");
	private static final Model GENERATED_1 = new Model(Optional.of(new Identifier("item/generated")), Optional.empty(), TextureKey.LAYER0, LAYER1);
	private static final Model GENERATED_2 = new Model(Optional.of(new Identifier("item/generated")), Optional.empty(), TextureKey.LAYER0, LAYER1, LAYER2);
	private static final Model HANDHELD_1 = new Model(Optional.of(new Identifier("item/handheld")), Optional.empty(), TextureKey.LAYER0, LAYER1);
	private static final Model HANDHELD_3 = new Model(Optional.of(new Identifier("item/handheld")), Optional.empty(), TextureKey.LAYER0, LAYER1, LAYER2, LAYER3);
	private static final TextureKey MATERIAL = AccessorTextureKey.make("material");
	private static final TextureKey INSIDE = AccessorTextureKey.make("inside");
	private static final Model SPREADER = new Model(Optional.of(prefix("block/shapes/spreader_item")), Optional.empty(), TextureKey.SIDE, MATERIAL, INSIDE);
	private static final ModelWithOverrides GENERATED_OVERRIDES = new ModelWithOverrides(new Identifier("item/generated"), TextureKey.LAYER0);
	private static final ModelWithOverrides GENERATED_OVERRIDES_1 = new ModelWithOverrides(new Identifier("item/generated"), TextureKey.LAYER0, LAYER1);
	private static final ModelWithOverrides HANDHELD_OVERRIDES = new ModelWithOverrides(new Identifier("item/handheld"), TextureKey.LAYER0);
	private static final ModelWithOverrides HANDHELD_OVERRIDES_2 = new ModelWithOverrides(new Identifier("item/handheld"), TextureKey.LAYER0, LAYER1, LAYER2);

	private final DataGenerator generator;

	public ItemModelProvider(DataGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void run(DataCache cache) throws IOException {
		Set<Item> items = Registry.ITEM.stream().filter(i -> LibMisc.MOD_ID.equals(Registry.ITEM.getId(i).getNamespace()))
				.collect(Collectors.toSet());
		Map<Identifier, Supplier<JsonElement>> map = new HashMap<>();
		registerItemBlocks(takeAll(items, i -> i instanceof BlockItem).stream().map(i -> (BlockItem) i).collect(Collectors.toSet()), map::put);
		registerItemOverrides(items, map::put);
		registerItems(items, map::put);

		for (Map.Entry<Identifier, Supplier<JsonElement>> e : map.entrySet()) {
			Identifier id = e.getKey();
			Path out = generator.getOutput().resolve("assets/" + id.getNamespace() + "/models/" + id.getPath() + ".json");
			try {
				DataProvider.writeToPath(GSON, cache, e.getValue().get(), out);
			} catch (IOException ex) {
				Botania.LOGGER.error("Failed to generate {}", out, ex);
			}
		}
	}

	private static void registerItems(Set<Item> items, BiConsumer<Identifier, Supplier<JsonElement>> consumer) {
		// Written manually
		items.remove(manaGun);

		takeAll(items, i -> i instanceof ItemLens).forEach(i -> GENERATED_1.upload(ModelIds.getItemModelId(i),
				Texture.layer0(prefix("item/lens"))
						.put(LAYER1, Texture.getId(i)),
				consumer));

		GENERATED_1.upload(ModelIds.getItemModelId(bloodPendant),
				Texture.layer0(Texture.getId(bloodPendant))
						.put(LAYER1, Texture.getSubId(bloodPendant, "_overlay")),
				consumer);
		items.remove(bloodPendant);

		HANDHELD_1.upload(ModelIds.getItemModelId(enderDagger),
				Texture.layer0(Texture.getId(enderDagger))
						.put(LAYER1, Texture.getSubId(enderDagger, "_overlay")),
				consumer);
		items.remove(enderDagger);

		GENERATED_1.upload(ModelIds.getItemModelId(incenseStick),
				Texture.layer0(Texture.getId(incenseStick))
						.put(LAYER1, Texture.getSubId(incenseStick, "_overlay")),
				consumer);
		items.remove(incenseStick);

		GENERATED_1.upload(ModelIds.getItemModelId(manaMirror),
				Texture.layer0(Texture.getId(manaMirror))
						.put(LAYER1, Texture.getSubId(manaMirror, "_overlay")),
				consumer);
		items.remove(manaMirror);

		GENERATED_1.upload(ModelIds.getItemModelId(manaTablet),
				Texture.layer0(Texture.getId(manaTablet))
						.put(LAYER1, Texture.getSubId(manaTablet, "_overlay")),
				consumer);
		items.remove(manaTablet);

		GENERATED_2.upload(ModelIds.getItemModelId(thirdEye),
				new Texture().put(TextureKey.LAYER0, Texture.getSubId(thirdEye, "_0"))
						.put(LAYER1, Texture.getSubId(thirdEye, "_1"))
						.put(LAYER2, Texture.getSubId(thirdEye, "_2")),
				consumer);
		items.remove(thirdEye);

		takeAll(items, cobbleRod, dirtRod, diviningRod, elementiumAxe, elementiumPick, elementiumShovel, elementiumHoe, elementiumSword,
				exchangeRod, fireRod, glassPick, gravityRod, manasteelAxe, manasteelPick, manasteelShears, manasteelShovel, manasteelHoe,
				missileRod, obedienceStick, rainbowRod, smeltRod, starSword, terraSword, terraformRod, thunderSword, waterRod,
				kingKey, skyDirtRod).forEach(i -> Models.HANDHELD.upload(ModelIds.getItemModelId(i), Texture.layer0(i), consumer));

		takeAll(items, i -> true).forEach(i -> Models.GENERATED.upload(ModelIds.getItemModelId(i), Texture.layer0(i), consumer));
	}

	private static void singleGeneratedOverride(Item item, Identifier overrideModel, Identifier predicate, double value, BiConsumer<Identifier, Supplier<JsonElement>> consumer) {
		Models.GENERATED.upload(overrideModel, Texture.layer0(overrideModel), consumer);
		GENERATED_OVERRIDES.upload(ModelIds.getItemModelId(item),
				Texture.layer0(item),
				new OverrideHolder()
						.add(overrideModel, Pair.of(predicate, value)),
				consumer);
	}

	private static void singleGeneratedSuffixOverride(Item item, String suffix, Identifier predicate, double value, BiConsumer<Identifier, Supplier<JsonElement>> consumer) {
		singleGeneratedOverride(item, ModelIds.getItemSubModelId(item, suffix), predicate, value, consumer);
	}

	private static void singleHandheldOverride(Item item, Identifier overrideModel, Identifier predicate, double value, BiConsumer<Identifier, Supplier<JsonElement>> consumer) {
		Models.HANDHELD.upload(overrideModel, Texture.layer0(overrideModel), consumer);
		HANDHELD_OVERRIDES.upload(ModelIds.getItemModelId(item),
				Texture.layer0(item),
				new OverrideHolder()
						.add(overrideModel, Pair.of(predicate, value)),
				consumer);
	}

	private static void singleHandheldSuffixOverride(Item item, String suffix, Identifier predicate, double value, BiConsumer<Identifier, Supplier<JsonElement>> consumer) {
		singleHandheldOverride(item, ModelIds.getItemSubModelId(item, suffix), predicate, value, consumer);
	}

	private static void registerItemOverrides(Set<Item> items, BiConsumer<Identifier, Supplier<JsonElement>> consumer) {
		// Written manually
		items.remove(livingwoodBow);
		items.remove(crystalBow);

		singleGeneratedSuffixOverride(blackHoleTalisman, "_active", prefix("active"), 1.0, consumer);
		items.remove(blackHoleTalisman);

		OverrideHolder flaskOverrides = new OverrideHolder();
		for (int i = 1; i <= 5; i++) {
			Identifier overrideModel = ModelIds.getItemSubModelId(brewFlask, "_" + i);
			GENERATED_1.upload(overrideModel,
					Texture.layer0(flask).put(LAYER1, overrideModel),
					consumer);

			flaskOverrides.add(overrideModel, Pair.of(prefix("swigs_taken"), (double) i));
		}
		GENERATED_OVERRIDES_1.upload(ModelIds.getItemModelId(brewFlask),
				Texture.layer0(flask).put(LAYER1, Texture.getSubId(brewFlask, "_0")),
				flaskOverrides,
				consumer);
		items.remove(brewFlask);

		OverrideHolder vialOverrides = new OverrideHolder();
		for (int i = 1; i <= 3; i++) {
			Identifier overrideModel = ModelIds.getItemSubModelId(brewVial, "_" + i);
			GENERATED_1.upload(overrideModel,
					Texture.layer0(vial).put(LAYER1, overrideModel),
					consumer);
			vialOverrides.add(overrideModel, Pair.of(prefix("swigs_taken"), (double) i));
		}
		GENERATED_OVERRIDES_1.upload(ModelIds.getItemModelId(brewVial),
				Texture.layer0(vial).put(LAYER1, Texture.getSubId(brewVial, "_0")),
				vialOverrides, consumer);
		items.remove(brewVial);

		singleHandheldOverride(elementiumShears, prefix("item/dammitreddit"), prefix("reddit"), 1, consumer);
		items.remove(elementiumShears);

		Identifier vuvuzela = prefix("item/vuvuzela");
		Models.HANDHELD.upload(vuvuzela, Texture.layer0(vuvuzela), consumer);
		for (Item i : new Item[] { grassHorn, leavesHorn, snowHorn }) {
			GENERATED_OVERRIDES.upload(ModelIds.getItemModelId(i),
					Texture.layer0(i),
					new OverrideHolder()
							.add(vuvuzela, Pair.of(prefix("vuvuzela"), 1.0)),
					consumer
			);
		}
		items.remove(grassHorn);
		items.remove(leavesHorn);
		items.remove(snowHorn);

		singleGeneratedOverride(infiniteFruit, prefix("item/dasboot"), prefix("boot"), 1, consumer);
		items.remove(infiniteFruit);

		singleGeneratedSuffixOverride(lexicon, "_elven", prefix("elven"), 1.0, consumer);
		items.remove(lexicon);

		singleGeneratedSuffixOverride(magnetRing, "_active", prefix("active"), 1.0, consumer);
		items.remove(magnetRing);

		singleGeneratedSuffixOverride(magnetRingGreater, "_active", prefix("active"), 1.0, consumer);
		items.remove(magnetRingGreater);

		OverrideHolder bottleOverrides = new OverrideHolder();
		for (int i = 1; i <= 5; i++) {
			Identifier overrideModel = ModelIds.getItemSubModelId(manaBottle, "_" + i);
			Models.GENERATED.upload(overrideModel, Texture.layer0(overrideModel), consumer);
			bottleOverrides.add(overrideModel, Pair.of(prefix("swigs_taken"), (double) i));
		}
		GENERATED_OVERRIDES.upload(ModelIds.getItemModelId(manaBottle),
				Texture.layer0(manaBottle),
				bottleOverrides,
				consumer);
		items.remove(manaBottle);

		singleGeneratedOverride(manaCookie, prefix("item/totalbiscuit"), prefix("totalbiscuit"), 1.0, consumer);
		items.remove(manaCookie);

		singleHandheldOverride(manasteelSword, prefix("item/elucidator"), prefix("elucidator"), 1.0, consumer);
		items.remove(manasteelSword);

		singleGeneratedSuffixOverride(manaweaveHelm, "_holiday", prefix("holiday"), 1.0, consumer);
		items.remove(manaweaveHelm);

		singleGeneratedSuffixOverride(manaweaveChest, "_holiday", prefix("holiday"), 1.0, consumer);
		items.remove(manaweaveChest);

		singleGeneratedSuffixOverride(manaweaveLegs, "_holiday", prefix("holiday"), 1.0, consumer);
		items.remove(manaweaveLegs);

		singleGeneratedSuffixOverride(manaweaveBoots, "_holiday", prefix("holiday"), 1.0, consumer);
		items.remove(manaweaveBoots);

		singleGeneratedSuffixOverride(slimeBottle, "_active", prefix("active"), 1.0, consumer);
		items.remove(slimeBottle);

		singleGeneratedSuffixOverride(spawnerMover, "_full", prefix("full"), 1.0, consumer);
		items.remove(spawnerMover);

		singleGeneratedSuffixOverride(temperanceStone, "_active", prefix("active"), 1.0, consumer);
		items.remove(temperanceStone);

		singleHandheldSuffixOverride(terraAxe, "_active", prefix("active"), 1.0, consumer);
		items.remove(terraAxe);

		Identifier enabledModel = ModelIds.getItemSubModelId(terraPick, "_active");
		HANDHELD_1.upload(enabledModel, Texture.layer0(terraPick).put(LAYER1, enabledModel), consumer);

		Identifier tippedModel = ModelIds.getItemSubModelId(terraPick, "_tipped");
		Models.HANDHELD.upload(tippedModel, Texture.layer0(tippedModel), consumer);

		Identifier tippedEnabledModel = ModelIds.getItemSubModelId(terraPick, "_tipped_active");
		HANDHELD_1.upload(tippedEnabledModel,
				Texture.layer0(tippedModel).put(LAYER1, Texture.getSubId(terraPick, "_active")),
				consumer);

		HANDHELD_OVERRIDES.upload(ModelIds.getItemModelId(terraPick),
				Texture.layer0(terraPick),
				new OverrideHolder()
						.add(enabledModel, Pair.of(prefix("active"), 1.0))
						.add(tippedModel, Pair.of(prefix("tipped"), 1.0))
						.add(tippedEnabledModel, Pair.of(prefix("tipped"), 1.0), Pair.of(prefix("active"), 1.0)),
				consumer);
		items.remove(terraPick);

		singleHandheldSuffixOverride(tornadoRod, "_active", prefix("active"), 1.0, consumer);
		items.remove(tornadoRod);

		Texture twigWandTextures = Texture.layer0(twigWand)
				.put(LAYER1, Texture.getSubId(twigWand, "_top"))
				.put(LAYER2, Texture.getSubId(twigWand, "_bottom"));
		Identifier twigWandBind = ModelIds.getItemSubModelId(twigWand, "_bind");
		HANDHELD_3.upload(twigWandBind,
				twigWandTextures.copyAndAdd(LAYER3, Texture.getSubId(twigWand, "_bind")),
				consumer);
		HANDHELD_OVERRIDES_2.upload(ModelIds.getItemModelId(twigWand),
				twigWandTextures,
				new OverrideHolder()
						.add(twigWandBind, Pair.of(prefix("bindmode"), 1.0)),
				consumer);
		items.remove(twigWand);
	}

	private static void registerItemBlocks(Set<BlockItem> itemBlocks, BiConsumer<Identifier, Supplier<JsonElement>> consumer) {
		// Manually written
		itemBlocks.remove(ModBlocks.corporeaCrystalCube.asItem());

		GENERATED_1.upload(ModelIds.getItemModelId(ModBlocks.animatedTorch.asItem()),
				Texture.layer0(Blocks.REDSTONE_TORCH).put(LAYER1, prefix("block/animated_torch_glimmer")), consumer);
		itemBlocks.remove(ModBlocks.animatedTorch.asItem());

		Models.TEMPLATE_SKULL.upload(ModelIds.getItemModelId(ModBlocks.gaiaHead.asItem()), new Texture(), consumer);
		itemBlocks.remove(ModBlocks.gaiaHead.asItem());

		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockModDoubleFlower).forEach(i -> {
			Models.GENERATED.upload(ModelIds.getItemModelId(i), Texture.layer0(Texture.getSubId(i.getBlock(), "_top")), consumer);
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockPetalBlock).forEach(i -> {
			consumer.accept(ModelIds.getItemModelId(i), new SimpleModelSupplier(prefix("block/petal_block")));
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof PaneBlock).forEach(i -> {
			String name = Registry.ITEM.getId(i).getPath();
			String baseName = name.substring(0, name.length() - "_pane".length());
			Models.GENERATED.upload(ModelIds.getItemModelId(i), Texture.layer0(prefix("block/" + baseName)), consumer);
		});

		Predicate<BlockItem> defaultGenerated = i -> {
			Block b = i.getBlock();
			return b instanceof BlockSpecialFlower || b instanceof BlockModMushroom
					|| b instanceof BlockLightRelay
					|| b instanceof BlockModFlower
					|| b == ModBlocks.ghostRail;
		};
		takeAll(itemBlocks, defaultGenerated).forEach(i -> {
			Models.GENERATED.upload(ModelIds.getItemModelId(i), Texture.layer0(i.getBlock()), consumer);
		});

		takeAll(itemBlocks, b -> b.getBlock() instanceof BlockMotifFlower).forEach(i -> {
			String name = Registry.ITEM.getId(i).getPath();
			Identifier texName = prefix("block/" + name.replace("_motif", ""));
			Models.GENERATED.upload(ModelIds.getItemModelId(i), Texture.layer0(texName), consumer);
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockPool).forEach(i -> {
			Identifier fullModel = ModelIds.getBlockSubModelId(i.getBlock(), "_full");
			OverrideHolder overrides = new OverrideHolder().add(fullModel, Pair.of(prefix("full"), 1.0));
			consumer.accept(ModelIds.getItemModelId(i),
					new SimpleModelSupplierWithOverrides(ModelIds.getBlockModelId(i.getBlock()), overrides));
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof WallBlock).forEach(i -> {
			String name = Registry.ITEM.getId(i).getPath();
			String baseName = name.substring(0, name.length() - "_wall".length());
			Models.WALL_INVENTORY.upload(ModelIds.getItemModelId(i),
					new Texture().put(TextureKey.WALL, prefix("block/" + baseName)), consumer);
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof BlockSpreader).forEach(i -> {
			String name = Registry.ITEM.getId(i).getPath();
			String material;
			if (i.getBlock() == ModBlocks.elvenSpreader) {
				material = "dreamwood";
			} else if (i.getBlock() == ModBlocks.gaiaSpreader) {
				material = name + "_material";
			} else {
				material = "livingwood";
			}
			SPREADER.upload(ModelIds.getItemModelId(i),
					new Texture().put(TextureKey.SIDE, Texture.getSubId(i.getBlock(), "_side"))
							.put(MATERIAL, prefix("block/" + material))
							.put(INSIDE, Texture.getSubId(i.getBlock(), "_inside")),
					consumer);
		});

		takeAll(itemBlocks, ModBlocks.avatar.asItem(), ModBlocks.bellows.asItem(),
				ModBlocks.brewery.asItem(), ModBlocks.corporeaIndex.asItem(), ModBlocks.gaiaPylon.asItem(),
				ModBlocks.hourglass.asItem(), ModBlocks.manaPylon.asItem(), ModBlocks.naturaPylon.asItem(), ModBlocks.teruTeruBozu.asItem())
						.forEach(i -> builtinEntity(i, consumer));

		takeAll(itemBlocks, i -> i instanceof ItemPetal).forEach(i -> {
			Models.GENERATED.upload(ModelIds.getItemModelId(i), Texture.layer0(prefix("item/petal")), consumer);
		});

		Models.FENCE_INVENTORY.upload(ModelIds.getItemModelId(ModFluffBlocks.dreamwoodFence.asItem()),
				Texture.texture(ModBlocks.dreamwoodPlanks), consumer);
		itemBlocks.remove(ModFluffBlocks.dreamwoodFence.asItem());

		Models.FENCE_INVENTORY.upload(ModelIds.getItemModelId(ModFluffBlocks.livingwoodFence.asItem()),
				Texture.texture(ModBlocks.livingwoodPlanks), consumer);
		itemBlocks.remove(ModFluffBlocks.livingwoodFence.asItem());

		consumer.accept(ModelIds.getItemModelId(ModBlocks.elfGlass.asItem()), new SimpleModelSupplier(prefix("block/elf_glass_0")));
		itemBlocks.remove(ModBlocks.elfGlass.asItem());

		itemBlocks.forEach(i -> {
			consumer.accept(ModelIds.getItemModelId(i), new SimpleModelSupplier(ModelIds.getBlockModelId(i.getBlock())));
		});
	}

	// [VanillaCopy] item/chest.json
	// Scuffed af.....but it works :wacko:
	private static final String BUILTIN_ENTITY_DISPLAY_STR =
			"{\n" +
					"        \"gui\": {\n" +
					"            \"rotation\": [ 30, 45, 0 ],\n" +
					"            \"translation\": [ 0, 0, 0],\n" +
					"            \"scale\":[ 0.625, 0.625, 0.625 ]\n" +
					"        },\n" +
					"        \"ground\": {\n" +
					"            \"rotation\": [ 0, 0, 0 ],\n" +
					"            \"translation\": [ 0, 3, 0],\n" +
					"            \"scale\":[ 0.25, 0.25, 0.25 ]\n" +
					"        },\n" +
					"        \"head\": {\n" +
					"            \"rotation\": [ 0, 180, 0 ],\n" +
					"            \"translation\": [ 0, 0, 0],\n" +
					"            \"scale\":[ 1, 1, 1]\n" +
					"        },\n" +
					"        \"fixed\": {\n" +
					"            \"rotation\": [ 0, 180, 0 ],\n" +
					"            \"translation\": [ 0, 0, 0],\n" +
					"            \"scale\":[ 0.5, 0.5, 0.5 ]\n" +
					"        },\n" +
					"        \"thirdperson_righthand\": {\n" +
					"            \"rotation\": [ 75, 315, 0 ],\n" +
					"            \"translation\": [ 0, 2.5, 0],\n" +
					"            \"scale\": [ 0.375, 0.375, 0.375 ]\n" +
					"        },\n" +
					"        \"firstperson_righthand\": {\n" +
					"            \"rotation\": [ 0, 315, 0 ],\n" +
					"            \"translation\": [ 0, 0, 0],\n" +
					"            \"scale\": [ 0.4, 0.4, 0.4 ]\n" +
					"        }\n" +
					"    }";
	private static final JsonElement BUILTIN_ENTITY_DISPLAY = GSON.fromJson(BUILTIN_ENTITY_DISPLAY_STR, JsonElement.class);

	private static void builtinEntity(Item i, BiConsumer<Identifier, Supplier<JsonElement>> consumer) {
		consumer.accept(ModelIds.getItemModelId(i), () -> {
			JsonObject json = new JsonObject();
			json.addProperty("parent", "minecraft:builtin/entity");
			json.add("display", BUILTIN_ENTITY_DISPLAY);
			return json;
		});
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania item models";
	}
}
