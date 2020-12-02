/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class OrechidResourceListener implements SimpleResourceReloadListener<OrechidResourceListener.OrechidResources> {
	Gson GSON = new GsonBuilder().create();

	@Override
	public CompletableFuture<OrechidResources> load(ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.supplyAsync(() -> {
			BotaniaAPI.instance().clearOreWeights();

			Collection<Identifier> regular = manager.findResources("orechid_ore_weights/orechid", s -> s.endsWith(".json"));
			Collection<Identifier> ignem = manager.findResources("orechid_ore_weights/orechid_ignem", s -> s.endsWith(".json"));
			return new OrechidResources(regular, ignem);
		}, executor);
	}

	@Override
	public CompletableFuture<Void> apply(OrechidResources resources, ResourceManager manager, Profiler profiler, Executor executor) {
		return CompletableFuture.runAsync(() -> {
			resources.regular.forEach(id -> {
				try {
					InputStream is = manager.getResource(id).getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					JsonObject json = GSON.fromJson(br, JsonObject.class);
					json.entrySet().forEach(e -> handleOrechidJson(e, false));
				} catch (IOException e) {
					Botania.LOGGER.error("Caught exception while trying to parse Orechid Ore Weights: " + e);
				}
			});
			resources.ignem.forEach(id -> {
				try {
					InputStream is = manager.getResource(id).getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					JsonObject json = GSON.fromJson(br, JsonObject.class);
					json.entrySet().forEach(e -> handleOrechidJson(e, true));
				} catch (IOException e) {
					Botania.LOGGER.error("Caught exception while trying to parse Orechid Ignem Ore Weights: " + e);
				}
			});
		}, executor);
	}

	private void handleOrechidJson(Map.Entry<String, JsonElement> entry, boolean isIgnem) {
		String key = entry.getKey();
		Identifier tag = new Identifier(key);
		int weight;
		try {
			weight = Integer.parseInt(entry.getValue().getAsString());
		} catch (NumberFormatException e) {
			Botania.LOGGER.error("Could not parse ore weight for '{}'.", key);
			return;
		}
		if (isIgnem) {
			BotaniaAPI.instance().registerNetherOreWeight(tag, weight);
		} else {
			BotaniaAPI.instance().registerOreWeight(tag, weight);
		}
	}

	@Override
	public Identifier getFabricId() {
		return ResourceLocationHelper.prefix("orechid_deserializer");
	}

	static class OrechidResources {
		Collection<Identifier> regular;
		Collection<Identifier> ignem;

		public OrechidResources(Collection<Identifier> regular, Collection<Identifier> ignem) {
			this.regular = regular;
			this.ignem = ignem;
		}
	}
}
