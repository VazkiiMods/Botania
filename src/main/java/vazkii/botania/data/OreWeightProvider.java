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

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;

import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.util.Util;

import vazkii.botania.common.lib.LibMisc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class OreWeightProvider implements DataProvider {
	protected final DataGenerator root;

	public OreWeightProvider(DataGenerator root) {
		this.root = root;
	}

	@Override
	public void run(DataCache cache) throws IOException {
		final Map<String, Integer> regular = defaultOreWeights();
		final Map<String, Integer> ignem = defaultNetherOreWeights();
		final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

		DataProvider.writeToPath(GSON, cache, GSON.toJsonTree(regular), getOutput("orechid"));
		DataProvider.writeToPath(GSON, cache, GSON.toJsonTree(ignem), getOutput("orechid_ignem"));
	}

	protected Path getOutput(String orechid) {
		return this.root.getOutput().resolve("data/" + LibMisc.MOD_ID + "/orechid_ore_weights/" + orechid + "/" + orechid + ".json");
	}

	private Map<String, Integer> defaultOreWeights() {
		return Util.make(new Object2IntArrayMap<>(), map -> {
			// Vanilla
			map.put("c:coal_ores", 67415);
			map.put("c:diamond_ores", 883);
			map.put("c:emerald_ores", 1239);
			map.put("c:gold_ores", 2647);
			map.put("c:iron_ores", 29371);
			map.put("c:lapis_ores", 1079);
			map.put("c:redstone_ores", 7654);
			// Common Metals
			map.put("c:aluminium_ores", 13762);
			map.put("c:aluminum_ores", 13762);
			map.put("c:copper_ores", 5567);
			map.put("c:ferrous_ores", 558);
			map.put("c:galena_ores", 4096);
			map.put("c:lead_ores", 4093);
			map.put("c:mithril_ores", 6485);
			map.put("c:mythril_ores", 6485);
			map.put("c:nickel_ores", 2275);
			map.put("c:osmium_ores", 6915);
			map.put("c:platinum_ores", 956);
			map.put("c:silver_ores", 4315);
			map.put("c:tin_ores", 8251);
			map.put("c:tungsten_ores", 140);
			map.put("c:uranium_ores", 230);
			map.put("c:zinc_ores", 838);
			// Common Gems
			map.put("c:amber_ores", 902);
			map.put("c:ruby_ores", 1384);
			map.put("c:sapphire_ores", 1287);
			map.put("c:topaz_ores", 6436);
			map.put("c:amethyst_ores", 1307);
			map.put("c:malachite_ores", 160);
			// Extreme Reactors
			map.put("c:yellorite_ores", 3520);
			// Blue Power
			map.put("c:teslatite_ores", 4312);
			// EvilCraft
			map.put("c:dark_ores", 1350);
			// Forestry
			map.put("c:apatite_ores", 250);
			// Mystical Agriculture
			map.put("c:inferium_ores", 10000);
			map.put("c:prosperity_ores", 7420);
			// Project RED
			map.put("c:olivine_ores", 1100);
			// Railcraft
			map.put("c:sulfur_ores", 1105);
			map.put("c:sulphur_ores", 1105);
			// Simple Ores 2
			map.put("c:adamantium_ores", 1469);
			// Silent Mechanisms
			map.put("c:bismuth_ores", 2407);
			// Thaumcraft
			map.put("c:cinnabar_ores", 2585);
		});
	}

	private Map<String, Integer> defaultNetherOreWeights() {
		return Util.make(new Object2IntArrayMap<>(), map -> {
			// Vanilla
			map.put("c:nether_quartz_ores", 19600);
			map.put("c:nether_gold_ores", 3635);
			map.put("c:ancient_debris", 148);
			// Nether Ores
			map.put("c:nether_coal_ores", 17000);
			map.put("c:nether_copper_ores", 4700);
			map.put("c:nether_diamond_ores", 175);
			map.put("c:nether_iron_ores", 5790);
			map.put("c:nether_lapis_ores", 3250);
			map.put("c:nether_lead_ores", 2790);
			map.put("c:nether_nickel_ores", 1790);
			map.put("c:nether_platinum_ores", 170);
			map.put("c:nether_redstone_ores", 5600);
			map.put("c:nether_silver_ores", 1550);
			map.put("c:nether_steel_ores", 1690);
			map.put("c:nether_tin_ores", 3750);
			// Mystical Agriculture
			map.put("c:nether_inferium_ores", 100000);
			map.put("c:nether_prosperity_ores", 7420);
			// Netherrocks
			map.put("c:argonite_ores", 1000);
			map.put("c:ashstone_ores", 1000);
			map.put("c:dragonstone_ores", 175);
			map.put("c:fyrite_ores", 1000);
			// Railcraft
			map.put("c:firestone_ores", 5);
			// Simple Ores 2
			map.put("c:onyx_ores", 500);
			// Tinkers Construct
			map.put("c:ardite_ores", 500);
			map.put("c:cobalt_ores", 500);
		});
	}

	@Override
	public String getName() {
		return "Botania Orechid Ore Weights";
	}
}
