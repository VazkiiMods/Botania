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

import net.minecraft.block.Block;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

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
	public void run(DataCache cache) throws IOException {
		List<Pair<String, JsonElement>> jsons = new ArrayList<>();
		for (Block b : Registry.BLOCK) {
			Identifier id = Registry.BLOCK.getId(b);
			if (LibMisc.MOD_ID.equals(id.getNamespace()) && b instanceof BlockFloatingFlower) {
				String name = id.getPath();
				String nonFloat;
				if (name.endsWith("_floating_flower")) {
					nonFloat = name.replace("_floating_flower", "_mystical_flower");
				} else {
					nonFloat = name.replace("floating_", "");
				}

				JsonObject obj = new JsonObject();
				obj.addProperty("flower", LibResources.PREFIX_MOD + "block/" + nonFloat);
				jsons.add(new Pair<>("block/" + name, obj));
			}
		}

		Gson gson = new Gson();
		for (Pair<String, JsonElement> pair : jsons) {
			Path p = generator.getOutput().resolve("assets/" + LibMisc.MOD_ID + "/models/" + pair.getLeft() + ".json");
			DataProvider.writeToPath(gson, cache, pair.getRight(), p);
		}
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania floating flower models";
	}
}
