/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import com.google.common.collect.ImmutableMap;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DefaultUncaughtExceptionHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.ModTags;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class ContributorList {
	private static final ImmutableMap<String, String> LEGACY_FLOWER_NAMES = ImmutableMap.<String, String>builder()
			.put("daybloom", LibBlockNames.MOTIF_DAYBLOOM)
			.put("nightshade", LibBlockNames.MOTIF_NIGHTSHADE)
			.put("puredaisy", LibBlockNames.SUBTILE_PUREDAISY.getPath())
			.put("fallenkanade", LibBlockNames.SUBTILE_FALLEN_KANADE.getPath())
			.put("heiseidream", LibBlockNames.SUBTILE_HEISEI_DREAM.getPath())
			.put("arcanerose", LibBlockNames.SUBTILE_ARCANE_ROSE.getPath())
			.put("jadedamaranthus", LibBlockNames.SUBTILE_JADED_AMARANTHUS.getPath())
			.put("orechidignem", LibBlockNames.SUBTILE_ORECHID_IGNEM.getPath())
			.build();
	private static volatile Map<String, ItemStack> flowerMap = Collections.emptyMap();
	private static boolean startedLoading = false;

	public static final String TAG_HEADFLOWER = "botania:headflower";

	public static void firstStart() {
		if (!startedLoading) {
			Thread thread = new Thread(ContributorList::fetch);
			thread.setName("Botania Contributor Fanciness Thread");
			thread.setDaemon(true);
			thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(Botania.LOGGER));
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
				stack = cachedStacks.computeIfAbsent(ModBlocks.getFlower(DyeColor.byId(i)).asItem(), ContributorList::configureStack);
			} catch (NumberFormatException e) {
				String rawName = value.toLowerCase(Locale.ROOT);
				String flowerName = LEGACY_FLOWER_NAMES.getOrDefault(rawName, rawName);

				Item item = ModTags.Items.CONTRIBUTOR_HEADFLOWERS.getAllElements().stream()
						.filter(flower -> Registry.ITEM.getKey(flower).getPath().equals(flowerName))
						.findFirst().orElse(Items.POPPY);
				stack = cachedStacks.computeIfAbsent(item, ContributorList::configureStack);
			}
			m.put(key, stack);
		}
		flowerMap = m;
	}

	private static ItemStack configureStack(Item item) {
		ItemStack stack = new ItemStack(item);
		Map<Enchantment, Integer> ench = new HashMap<>();
		ench.put(Enchantments.UNBREAKING, 1);
		Registry.ENCHANTMENT.getOptional(new ResourceLocation("charm", "tinted")).ifPresent(e -> ench.put(e, 1));
		EnchantmentHelper.setEnchantments(ench, stack);

		stack.getTag().putBoolean(TAG_HEADFLOWER, true);
		stack.getTag().putString("charm_glint", DyeColor.YELLOW.getString());
		return stack;
	}

	private static void fetch() {
		try {
			URL url = new URL("https://raw.githubusercontent.com/Vazkii/Botania/master/contributors.properties");
			Properties props = new Properties();
			try (InputStreamReader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)) {
				props.load(reader);
				load(props);
			}
		} catch (IOException e) {
			Botania.LOGGER.info("Could not load contributors list. Either you're offline or GitHub is down. Nothing to worry about, carry on~");
		}
	}
}
