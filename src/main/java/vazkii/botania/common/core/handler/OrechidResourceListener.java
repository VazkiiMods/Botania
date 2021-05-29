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
import net.minecraft.block.Block;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.SinglePreparationResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.BotaniaAPI;
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
import java.util.Map;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class OrechidResourceListener extends SinglePreparationResourceReloadListener<OrechidResourceListener.Data> implements IdentifiableResourceReloadListener {
	private static final Gson GSON = new Gson();

	public static void registerListener() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new OrechidResourceListener());
	}

	@Nonnull
	@Override
	protected Data prepare(@Nonnull ResourceManager manager, @Nonnull Profiler profiler) {
		profiler.push("orechidParse");
		Object2IntMap<StateIngredient> weights = loadWeights(manager, "orechid.json");
		Object2IntMap<StateIngredient> nether = loadWeights(manager, "orechid_ignem.json");
		profiler.pop();
		return new Data(weights, nether);
	}

	private static Object2IntMap<StateIngredient> loadWeights(ResourceManager manager, String filename) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		Object2IntMap<StateIngredient> map = new Object2IntOpenHashMap<>();

		for (String namespace : manager.getAllNamespaces()) {
			Identifier location = new Identifier(namespace, "orechid_ore_weights/" + filename);
			try {
				for (Resource resource : manager.getAllResources(location)) {
					readResource(map, resource, location);
				}
			} catch (IOException ignored) {
				// Probably data file missing in namespace, ignored
			}
		}
		Botania.LOGGER.debug("Read {} data files in {}", filename, stopwatch.stop());
		return map;
	}

	private static void readResource(Object2IntMap<StateIngredient> map, Resource resource, Identifier location) {
		try (Resource r = resource;
				InputStream stream = r.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			JsonObject json = GSON.fromJson(reader, JsonObject.class);

			for (JsonElement values : JsonHelper.getArray(json, "values")) {
				JsonObject object = values.getAsJsonObject();
				StateIngredient state = StateIngredientHelper.tryDeserialize(object);
				if (state != null) {
					int weight = JsonHelper.getInt(object, "weight");
					map.put(state, weight);
				}
			}
		} catch (IllegalArgumentException | JsonParseException | IOException e) {
			Botania.LOGGER.error("Exception parsing orechid weights from {} / {}", resource.getResourcePackName(), location, e);
		}
	}

	@Override
	protected void apply(@Nonnull Data data, @Nonnull ResourceManager manager, @Nonnull Profiler profiler) {
		profiler.push("orechidApply");
		Map<Identifier, Integer> map = BotaniaAPI.instance().getOreWeights();
		if (!map.isEmpty()) {
			Botania.LOGGER.warn("{} orechid weights using legacy api found", map.size());
			for (Map.Entry<Identifier, Integer> entry : map.entrySet()) {
				data.normal.put(StateIngredientHelper.of(entry.getKey()), (int) entry.getValue());
			}
		}

		map = BotaniaAPI.instance().getNetherOreWeights();
		if (!map.isEmpty()) {
			Botania.LOGGER.warn("{} nether orechid weights using legacy api found", map.size());
			for (Map.Entry<Identifier, Integer> entry : map.entrySet()) {
				data.nether.put(StateIngredientHelper.of(entry.getKey()), (int) entry.getValue());
			}
		}

		BotaniaAPIImpl.weights = postprocess(data.normal);
		BotaniaAPIImpl.netherWeights = postprocess(data.nether);
		profiler.pop();
	}

	private List<OrechidOutput> postprocess(Object2IntMap<StateIngredient> map) {
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

	private static List<Block> prioritizeConfig(List<Block> blocks) {
		List<Block> out = new ArrayList<>();
		for (String mod : ConfigHandler.COMMON.orechidPriorityMods.getValue()) {
			for (Block block : blocks) {
				if (mod.equals(Registry.BLOCK.getId(block).getNamespace())) {
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
	public Identifier getFabricId() {
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
