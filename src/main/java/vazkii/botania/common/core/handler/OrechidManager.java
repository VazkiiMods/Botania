/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.common.Botania;

import javax.annotation.Nonnull;

import java.util.*;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class OrechidManager implements SimpleSynchronousResourceReloadListener {
	private static final Map<RecipeType<? extends IOrechidRecipe>, Multimap<Block, ? extends IOrechidRecipe>> DATA = new HashMap<>();
	private static final Map<RecipeType<? extends IOrechidRecipe>, Object2IntMap<Block>> WEIGHTS = new HashMap<>();

	public static void registerListener() {
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new OrechidManager());
	}

	@Override
	public void onResourceManagerReload(@Nonnull ResourceManager manager) {
		DATA.clear();
		WEIGHTS.clear();
	}

	public static Multimap<Block, ? extends IOrechidRecipe> getFor(RecipeType<? extends IOrechidRecipe> type) {
		return DATA.computeIfAbsent(type, t -> {
			Multimap<Block, IOrechidRecipe> map = ArrayListMultimap.create();
			for (var recipe : Botania.currentServer.getRecipeManager().getAllRecipesFor(t)) {
				map.put(recipe.getInput(), recipe);
			}
			for (var list : map.asMap().values()) {
				((List<IOrechidRecipe>) list).sort(Comparator.comparingInt(IOrechidRecipe::getWeight));
			}
			return map;
		});
	}

	public static int getBaseTotalWeight(IOrechidRecipe recipe) {
		@SuppressWarnings("unchecked")
		var type = (RecipeType<? extends IOrechidRecipe>) recipe.getType();
		var block = recipe.getInput();

		return WEIGHTS.computeIfAbsent(type, k -> new Object2IntOpenHashMap<>())
				.computeIntIfAbsent(block, b -> getFor(type).get(block).stream()
						.mapToInt(IOrechidRecipe::getWeight)
						.sum());
	}

	@Override
	public ResourceLocation getFabricId() {
		return prefix("orechid");
	}
}
