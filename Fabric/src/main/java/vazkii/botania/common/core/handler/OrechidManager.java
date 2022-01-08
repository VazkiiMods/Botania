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

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.xplat.IXplatAbstractions;

import javax.annotation.Nonnull;

import java.util.*;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class OrechidManager implements ResourceManagerReloadListener {
	private static final Map<RecipeType<? extends IOrechidRecipe>, Multimap<Block, ? extends IOrechidRecipe>> DATA = new HashMap<>();

	public static void registerListener() {
		IXplatAbstractions.INSTANCE.registerReloadListener(PackType.SERVER_DATA, prefix("orechid"), new OrechidManager());
	}

	@Override
	public void onResourceManagerReload(@Nonnull ResourceManager manager) {
		DATA.clear();
	}

	public static Multimap<Block, ? extends IOrechidRecipe> getFor(MinecraftServer server,
			RecipeType<? extends IOrechidRecipe> type) {
		return DATA.computeIfAbsent(type, t -> {
			Multimap<Block, IOrechidRecipe> map = ArrayListMultimap.create();
			for (var recipe : server.getRecipeManager().getAllRecipesFor(t)) {
				map.put(recipe.getInput(), recipe);
			}
			for (var list : map.asMap().values()) {
				((List<IOrechidRecipe>) list).sort(Comparator.comparingInt(IOrechidRecipe::getWeight));
			}
			return map;
		});
	}
}
