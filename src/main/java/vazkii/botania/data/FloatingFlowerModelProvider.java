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

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.Block;

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
	public void run(HashCache cache) throws IOException {
		List<Tuple<String, JsonElement>> jsons = new ArrayList<>();
		for (Block b : Registry.BLOCK) {
			ResourceLocation id = Registry.BLOCK.getKey(b);
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
				jsons.add(new Tuple<>("block/" + name, obj));
			}
		}

		Gson gson = new Gson();
		for (Tuple<String, JsonElement> pair : jsons) {
			Path p = generator.getOutputFolder().resolve("assets/" + LibMisc.MOD_ID + "/models/" + pair.getA() + ".json");
			DataProvider.save(gson, cache, pair.getB(), p);
		}
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania floating flower models";
	}
}
