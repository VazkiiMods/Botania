/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.handler;

import com.google.common.collect.ImmutableMap;

import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.lib.LibBlockNames;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.StreamSupport;

public class ContributorList {
	private static final ImmutableMap<String, String> LEGACY_FLOWER_NAMES = ImmutableMap.<String, String>builder()
			.put("daybloom", LibBlockNames.MOTIF_DAYBLOOM)
			.put("nightshade", LibBlockNames.MOTIF_NIGHTSHADE)
			.put("puredaisy", LibBlockNames.SUBTILE_PUREDAISY)
			.put("fallenkanade", LibBlockNames.SUBTILE_FALLEN_KANADE)
			.put("heiseidream", LibBlockNames.SUBTILE_HEISEI_DREAM)
			.put("arcanerose", LibBlockNames.SUBTILE_ARCANE_ROSE)
			.put("jadedamaranthus", LibBlockNames.SUBTILE_JADED_AMARANTHUS)
			.put("orechidignem", LibBlockNames.SUBTILE_ORECHID_IGNEM)
			.put("bellethorn", LibBlockNames.SUBTILE_BELLETHORN)
			.put("dreadthorn", LibBlockNames.SUBTILE_DREADTHORN)
			.build();
	private static volatile Map<String, ItemStack> flowerMap = Collections.emptyMap();
	private static boolean startedLoading = false;

	public static final String TAG_HEADFLOWER = "botania:headflower";

	public static void firstStart() {
		if (!startedLoading) {
			Thread thread = new Thread(ContributorList::fetch);
			thread.setName("Botania Contributor Fanciness Thread");
			thread.setDaemon(true);
			thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(BotaniaAPI.LOGGER));
			thread.start();

			startedLoading = true;
		}
	}

	public static ItemStack getFlower(String name) {
		return flowerMap.getOrDefault(name, ItemStack.EMPTY);
	}

	public static boolean hasFlower(String name) {
		return flowerMap.containsKey(name);
	}

	private static void load(Properties props) {
		Map<String, ItemStack> m = new HashMap<>();
		Map<Item, ItemStack> cachedStacks = new HashMap<>();
		for (String key : props.stringPropertyNames()) {
			String value = props.getProperty(key);

			ItemStack stack;
			try {
				int i = Integer.parseInt(value);
				if (i < 0 || i >= 16) {
					throw new NumberFormatException();
				}
				stack = cachedStacks.computeIfAbsent(BotaniaBlocks.getFlower(DyeColor.byId(i)).asItem(), ContributorList::configureStack);
			} catch (NumberFormatException e) {
				String rawName = value.toLowerCase(Locale.ROOT);
				boolean petite = rawName.endsWith("_chibi") || rawName.endsWith("_petite");
				String searchName = petite ? rawName.substring(0, rawName.lastIndexOf('_')) : rawName;
				String flowerName = LEGACY_FLOWER_NAMES.getOrDefault(searchName, searchName) + (petite ? "_petite" : "");

				var item = StreamSupport.stream(BuiltInRegistries.ITEM.getTagOrEmpty(BotaniaTags.Items.CONTRIBUTOR_HEADFLOWERS).spliterator(), false)
						.filter(h -> h.is(resKey -> resKey.location().getPath().equals(flowerName)))
						.findFirst()
						.map(Holder::value)
						.orElse(Items.POPPY);
				stack = cachedStacks.computeIfAbsent(item, ContributorList::configureStack);
			}
			m.put(key, stack);
		}
		flowerMap = m;
	}

	private static ItemStack configureStack(Item item) {
		ItemStack stack = new ItemStack(item);
		stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);

		/*todo what is this used for?
		stack.getTag().putBoolean(TAG_HEADFLOWER, true);
		stack.getTag().putString("charm_glint", DyeColor.YELLOW.getSerializedName());
		 */
		return stack;
	}

	private static void fetch() {
		InputStream stream;
		try {
			URL url = new URI("https://raw.githubusercontent.com/Vazkii/Botania/master/contributors.properties").toURL();
			stream = url.openStream();
		} catch (IOException | URISyntaxException e) {
			BotaniaAPI.LOGGER.info("Could not load live contributors list. Either you're offline or GitHub is down. Loading bundled copy.");
			stream = ContributorList.class.getClassLoader().getResourceAsStream("contributors.properties");
			if (stream == null) {
				BotaniaAPI.LOGGER.info("Could not load bundled contributors list, somehow.");
				return;
			}
		}
		try {
			Properties props = new Properties();
			props.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
			load(props);
			stream.close();
		} catch (IOException e) {
			BotaniaAPI.LOGGER.info("Error while reading contributors list.");
		}
	}
}
