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
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.block.decor.FloatingFlowerBlock;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.xplat.ClientXplatAbstractions;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FloatingFlowerModelProvider implements DataProvider {
	private final DataGenerator generator;

	public FloatingFlowerModelProvider(DataGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void run(CachedOutput cache) throws IOException {
		List<Tuple<String, JsonElement>> jsons = new ArrayList<>();
		for (Block b : Registry.BLOCK) {
			ResourceLocation id = Registry.BLOCK.getKey(b);
			if (LibMisc.MOD_ID.equals(id.getNamespace()) && b instanceof FloatingFlowerBlock) {
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
				jsons.add(new Tuple<>(name, obj));
			}
		}

		for (Tuple<String, JsonElement> pair : jsons) {
			Path blockPath = generator.getOutputFolder().resolve("assets/" + LibMisc.MOD_ID + "/models/block/" + pair.getA() + ".json");
			Path itemPath = generator.getOutputFolder().resolve("assets/" + LibMisc.MOD_ID + "/models/item/" + pair.getA() + ".json");
			DataProvider.saveStable(cache, pair.getB(), blockPath);
			DataProvider.saveStable(cache, pair.getB(), itemPath);
		}
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania floating flower models";
	}
}
