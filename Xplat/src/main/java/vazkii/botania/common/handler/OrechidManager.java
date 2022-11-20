/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.handler;

import com.google.common.collect.ImmutableList;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.OrechidRecipe;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class OrechidManager implements ResourceManagerReloadListener {
	private static final Map<RecipeType<? extends OrechidRecipe>, Map<BlockState, List<? extends OrechidRecipe>>> BY_TYPE = new IdentityHashMap<>();

	public static void registerListener() {
		XplatAbstractions.INSTANCE.registerReloadListener(PackType.SERVER_DATA, prefix("orechid"), new OrechidManager());
	}

	@Override
	public void onResourceManagerReload(@NotNull ResourceManager manager) {
		BY_TYPE.clear();
	}

	public static <T extends OrechidRecipe> Collection<T> getMatchingRecipes(
			RecipeManager manager,
			RecipeType<T> type,
			BlockState state) {
		var byState = BY_TYPE.get(type);
		if (byState == null) {
			byState = new IdentityHashMap<>();
			BY_TYPE.put(type, byState);
		}

		var list = byState.get(state);
		if (list == null) {
			var builder = ImmutableList.<T>builder();
			for (var recipe : manager.getAllRecipesFor(type)) {
				if (recipe.getInput().test(state)) {
					builder.add(recipe);
				}
			}
			list = builder.build();
			byState.put(state, list);
		}

		@SuppressWarnings("unchecked") // we only add T's to this list in the above loop
		List<T> result = (List<T>) list;
		return result;
	}
}
