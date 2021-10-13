/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.StateIngredientHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiConsumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class OrechidProvider implements IDataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final DataGenerator generator;

	public OrechidProvider(DataGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void act(DirectoryCache cache) throws IOException {
		registerMaps((Object2IntMap<StateIngredient> map, String loc) -> {
			try {
				IDataProvider.save(GSON, cache, convertMapToJson(map), getPath(generator.getOutputFolder(), prefix(loc)));
			} catch (IOException e) {
				Botania.LOGGER.error("Exception writing file", e);
			}
		});
	}

	protected void registerMaps(BiConsumer<Object2IntMap<StateIngredient>, String> consumer) {
		consumer.accept(orechidMap(), "orechid");
		consumer.accept(netherOrechidMap(), "orechid_ignem");
	}

	protected JsonObject convertMapToJson(Object2IntMap<StateIngredient> map) {
		JsonObject o = new JsonObject();
		JsonArray weights = map.object2IntEntrySet().stream().map(entry -> {
			JsonObject serialized = entry.getKey().serialize();
			serialized.addProperty("weight", entry.getIntValue());
			return serialized;
		}).collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
		o.add("values", weights);
		return o;
	}

	protected Object2IntMap<StateIngredient> orechidMap() {
		Object2IntMap<StateIngredient> map = new Object2IntArrayMap<>();

		map.put(forBlock(Blocks.COAL_ORE), 67415);
		map.put(forBlock(Blocks.IRON_ORE), 29371);
		map.put(forBlock(Blocks.REDSTONE_ORE), 7654);
		map.put(forBlock(Blocks.GOLD_ORE), 2647);
		map.put(forBlock(Blocks.EMERALD_ORE), 1239);
		map.put(forBlock(Blocks.LAPIS_ORE), 1079);
		map.put(forBlock(Blocks.DIAMOND_ORE), 883);
		// Common Metals
		map.put(forOreTag("aluminium"), 13762);
		map.put(forOreTag("aluminum"), 13762);
		map.put(forOreTag("copper"), 5567);
		map.put(forOreTag("ferrous"), 558);
		map.put(forOreTag("galena"), 4096);
		map.put(forOreTag("lead"), 4093);
		map.put(forOreTag("mithril"), 6485);
		map.put(forOreTag("mythril"), 6485);
		map.put(forOreTag("nickel"), 2275);
		map.put(forOreTag("osmium"), 6915);
		map.put(forOreTag("platinum"), 956);
		map.put(forOreTag("silver"), 4315);
		map.put(forOreTag("tin"), 8251);
		map.put(forOreTag("tungsten"), 140);
		map.put(forOreTag("uranium"), 230);
		map.put(forOreTag("zinc"), 838);
		// Common Gems
		map.put(forOreTag("amber"), 902);
		map.put(forOreTag("ruby"), 1384);
		map.put(forOreTag("sapphire"), 1287);
		map.put(forOreTag("topaz"), 6436);
		map.put(forOreTag("amethyst"), 1307);
		map.put(forOreTag("malachite"), 160);
		// Extreme Reactors
		map.put(forOreTag("yellorite"), 3520);
		// Blue Power
		map.put(forOreTag("teslatite"), 4312);
		// EvilCraft
		map.put(forOreTag("dark"), 1350);
		// Forestry
		map.put(forOreTag("apatite"), 250);
		// Mystical Agriculture
		map.put(forOreTag("inferium"), 10000);
		map.put(forOreTag("prosperity"), 7420);
		// Project RED
		map.put(forOreTag("olivine"), 1100);
		// Railcraft
		map.put(forOreTag("sulfur"), 1105);
		map.put(forOreTag("sulphur"), 1105);
		map.put(forOreTag("niter"), 1105);
		// Simple Ores 2
		map.put(forOreTag("adamantium"), 1469);
		// Silent Mechanisms
		map.put(forOreTag("bismuth"), 2407);
		// Thaumcraft
		map.put(forOreTag("cinnabar"), 2585);

		return map;
	}

	protected Object2IntMap<StateIngredient> netherOrechidMap() {
		Object2IntMap<StateIngredient> map = new Object2IntArrayMap<>();
		// Vanilla
		map.put(forBlock(Blocks.NETHER_QUARTZ_ORE), 19600);
		map.put(forBlock(Blocks.NETHER_GOLD_ORE), 3635);
		map.put(forBlock(Blocks.ANCIENT_DEBRIS), 148);
		// Mystical Agriculture
		map.put(forOreTag("nether/inferium"), 10000);
		map.put(forOreTag("nether/prosperity"), 7420);
		// Nether Ores
		map.put(forOreTag("nether/coal"), 17000);
		map.put(forOreTag("nether/copper"), 4700);
		map.put(forOreTag("nether/diamond"), 175);
		map.put(forOreTag("nether/iron"), 5790);
		map.put(forOreTag("nether/lapis"), 3250);
		map.put(forOreTag("nether/lead"), 2790);
		map.put(forOreTag("nether/nickel"), 1790);
		map.put(forOreTag("nether/platinum"), 170);
		map.put(forOreTag("nether/redstone"), 5600);
		map.put(forOreTag("nether/silver"), 1550);
		map.put(forOreTag("nether/steel"), 1690);
		map.put(forOreTag("nether/tin"), 3750);
		// Netherrocks
		map.put(forOreTag("argonite"), 1000);
		map.put(forOreTag("ashstone"), 1000);
		map.put(forOreTag("dragonstone"), 175);
		map.put(forOreTag("fyrite"), 1000);
		// Railcraft
		map.put(forOreTag("firestone"), 5);
		// Simple Ores 2
		map.put(forOreTag("onyx"), 500);
		// Tinkers Construct
		map.put(forOreTag("ardite"), 500);
		map.put(forOreTag("cobalt"), 500);

		return map;
	}

	private static Path getPath(Path root, ResourceLocation id) {
		return root.resolve("data/" + id.getNamespace() + "/orechid_ore_weights/" + id.getPath() + ".json");
	}

	protected static StateIngredient forOreTag(String oreTag) {
		return StateIngredientHelper.of(new ResourceLocation("forge", "ores/" + oreTag));
	}

	protected static StateIngredient forBlock(Block block) {
		return StateIngredientHelper.of(block);
	}

	@Override
	public String getName() {
		return "Botania Orechid weight data";
	}
}
