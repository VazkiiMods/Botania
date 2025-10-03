/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.IslandType;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.block.FloatingFlowerBaseBlock;
import vazkii.botania.common.item.GrassSeedsItem;
import vazkii.botania.xplat.ClientXplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class FloatingFlowerModelProvider implements DataProvider {
	private final PackOutput packOutput;

	public FloatingFlowerModelProvider(PackOutput packOutput) {
		this.packOutput = packOutput;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		List<Tuple<String, JsonElement>> flowerJsons = new ArrayList<>();
		for (Block b : BuiltInRegistries.BLOCK) {
			ResourceLocation id = BuiltInRegistries.BLOCK.getKey(b);
			if (BotaniaAPI.MODID.equals(id.getNamespace()) && b instanceof FloatingFlowerBaseBlock) {
				String name = id.getPath();
				String nonFloat;
				if (name.endsWith("_floating_flower")) {
					nonFloat = name.replace("_floating_flower", "_mystical_flower");
				} else {
					nonFloat = name.replace("floating_", "");
				}

				JsonObject obj = new JsonObject();
				obj.addProperty("parent", "minecraft:block/block");
				obj.addProperty("loader", ClientXplatAbstractions.FLOATING_FLOWER_MODEL_LOADER_ID.toString());
				JsonObject flower = new JsonObject();
				flower.addProperty("parent", ResourcesLib.PREFIX_MOD + "block/" + nonFloat);
				obj.add("flower", flower);
				flowerJsons.add(new Tuple<>(name, obj));
			}
		}
		List<Tuple<ResourceLocation, JsonElement>> islandJsons = new ArrayList<>();
		Registry<IslandType> islandTypeRegistry = BotaniaAPI.INSTANCE.getIslandTypeRegistry();
		islandTypeRegistry.stream().forEach(islandType -> {
			ResourceLocation id = islandTypeRegistry.getKey(islandType);
			if (BotaniaAPI.MODID.equals(id.getNamespace())) {
				ResourceLocation name = islandType.islandModel();
				Item item = islandType.item().asItem();
				ResourceLocation top;
				ResourceLocation side;
				if (item instanceof GrassSeedsItem grassSeedsItem) {
					Block seedsBlock = grassSeedsItem.getGrassBlock();
					if (seedsBlock == Blocks.GRASS_BLOCK) {
						top = botaniaRL("block/island_top");
						side = botaniaRL("block/island_side");
					} else {
						top = TextureMapping.getBlockTexture(seedsBlock, "_top");
						side = TextureMapping.getBlockTexture(seedsBlock, "_side");
					}
				} else if (item == Items.SNOWBALL) {
					top = TextureMapping.getBlockTexture(Blocks.SNOW);
					side = TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_snow");
				} else {
					throw new IllegalStateException("Don't know how to generate island model " + name);
				}

				JsonObject obj = new JsonObject();
				obj.addProperty("parent", "botania:block/shapes/mini_island");
				JsonObject textures = new JsonObject();
				textures.addProperty("top", top.toString());
				textures.addProperty("side", side.toString());
				obj.add("textures", textures);
				islandJsons.add(new Tuple<>(name, obj));
			}
		});
		List<CompletableFuture<?>> output = new ArrayList<>();
		PackOutput.PathProvider blocks = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
		PackOutput.PathProvider items = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
		for (Tuple<String, JsonElement> pair : flowerJsons) {
			output.add(DataProvider.saveStable(cache, pair.getB(), blocks.json(botaniaRL(pair.getA()))));
			output.add(DataProvider.saveStable(cache, pair.getB(), items.json(botaniaRL(pair.getA()))));
		}
		PackOutput.PathProvider islands = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
		for (Tuple<ResourceLocation, JsonElement> pair : islandJsons) {
			output.add(DataProvider.saveStable(cache, pair.getB(), islands.json(pair.getA())));
		}

		return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
	}

	@Override
	public String getName() {
		return "Botania floating flower models";
	}
}
