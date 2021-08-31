/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.internal.OrechidOutput;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.Botania;
import vazkii.botania.common.crafting.StateIngredientHelper;
import vazkii.botania.common.impl.BotaniaAPIImpl;

import javax.annotation.Nonnull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

/**
 * Loads the orechid data files.
 * Files for the basic Orechid are located in {@code botania:orechid_ore_weights/orechid.json},
 * files for the Orechid Ignem are in {@code botania:orechid_ore_weights/orechid_ignem.json}.
 *
 * <p>
 * The structure of the file is loosely based on tag jsons. It consists of the following values:
 * <ul>
 * <li>{@code replace} - an optional boolean flag signifying that all values read from preceding
 * datapacks should be discarded.</li>
 * <li>{@code remove} - an optional JSON array of {@linkplain StateIngredient state ingredients} to remove.
 * The exact same ingredients will be removed from the list.</li>
 * <li>{@code values} (<i>required</i>) - a JSON array of serialized {@linkplain StateIngredient state ingredients}
 * with an additional {@code weight} integer value.</li>
 * </ul>
 * </p>
 */
public class OrechidResourceListener extends SimplePreparableReloadListener<OrechidResourceListener.Data> implements IdentifiableResourceReloadListener {
	private static final Gson GSON = new Gson();

	public static void registerListener() {
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new OrechidResourceListener());
	}

	@Nonnull
	@Override
	protected Data prepare(@Nonnull ResourceManager manager, @Nonnull ProfilerFiller profiler) {
		profiler.push("orechidParse");
		Object2IntMap<StateIngredient> weights = loadWeights(manager, prefix("orechid.json"));
		Object2IntMap<StateIngredient> nether = loadWeights(manager, prefix("orechid_ignem.json"));
		profiler.pop();
		return new Data(weights, nether);
	}

	protected static Object2IntMap<StateIngredient> loadWeights(ResourceManager manager, ResourceLocation filename) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		Object2IntMap<StateIngredient> map = new Object2IntOpenHashMap<>();
		ResourceLocation location = new ResourceLocation(filename.getNamespace(), "orechid_ore_weights/" + filename.getPath());
		try {
			for (Resource resource : manager.getResources(location)) {
				readResource(map, resource, location);
			}
		} catch (IOException ignored) {}

		Botania.LOGGER.debug("Read {} data files in {}", filename, stopwatch.stop());
		return map;
	}

	protected static void readResource(Object2IntMap<StateIngredient> map, Resource resource, ResourceLocation location) {
		try (Resource r = resource;
				InputStream stream = r.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			JsonObject json = GSON.fromJson(reader, JsonObject.class);

			if (GsonHelper.getAsBoolean(json, "replace", false)) {
				map.clear();
			}

			if (GsonHelper.isArrayNode(json, "remove")) {
				for (JsonElement value : GsonHelper.getAsJsonArray(json, "remove")) {
					JsonObject object = value.getAsJsonObject();
					StateIngredient state = StateIngredientHelper.tryDeserialize(object);
					if (state != null) {
						map.removeInt(state);
					}
				}
			}

			for (JsonElement value : GsonHelper.getAsJsonArray(json, "values")) {
				JsonObject object = value.getAsJsonObject();
				StateIngredient state = StateIngredientHelper.tryDeserialize(object);
				if (state != null) {
					int weight = GsonHelper.getAsInt(object, "weight");
					if (weight <= 0) {
						Botania.LOGGER.error("Invalid weight: {} in file {} / {}, should be positive!",
								weight, resource.getSourceName(), location);
						continue;
					}
					map.put(state, weight);
				}
			}
		} catch (IllegalArgumentException | JsonParseException | IOException e) {
			Botania.LOGGER.error("Exception parsing orechid weights from {} / {}", resource.getSourceName(), location, e);
		}
	}

	@Override
	protected void apply(@Nonnull Data data, @Nonnull ResourceManager manager, @Nonnull ProfilerFiller profiler) {
		profiler.push("orechidApply");
		BotaniaAPIImpl.weights = postprocess(data.normal);
		BotaniaAPIImpl.netherWeights = postprocess(data.nether);
		profiler.pop();
	}

	protected List<OrechidOutput> postprocess(Object2IntMap<StateIngredient> map) {
		List<OrechidOutput> result = new ArrayList<>();
		for (Object2IntMap.Entry<StateIngredient> entry : map.object2IntEntrySet()) {
			StateIngredient process = entry.getKey().resolveAndFilter(OrechidResourceListener::prioritizeConfig);
			if (process != null) {
				result.add(new OrechidOutput(entry.getIntValue(), process));
			}
		}

		result.sort(Comparator.naturalOrder());
		return result;
	}

	protected static List<Block> prioritizeConfig(List<Block> blocks) {
		List<Block> out = new ArrayList<>();
		for (String mod : ConfigHandler.COMMON.orechidPriorityMods.getValue()) {
			for (Block block : blocks) {
				if (mod.equals(Registry.BLOCK.getKey(block).getNamespace())) {
					out.add(block);
				}
			}
			if (!out.isEmpty()) {
				return out;
			}
		}
		return null;
	}

	@Override
	public ResourceLocation getFabricId() {
		return prefix("orechid");
	}

	protected static class Data {
		protected Object2IntMap<StateIngredient> normal;
		protected Object2IntMap<StateIngredient> nether;

		public Data(Object2IntMap<StateIngredient> normal, Object2IntMap<StateIngredient> nether) {
			this.normal = normal;
			this.nether = nether;
		}
	}
}
