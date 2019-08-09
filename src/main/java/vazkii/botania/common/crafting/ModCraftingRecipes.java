/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 19, 2014, 3:54:48 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibMisc;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class ModCraftingRecipes {


	public static void init() {
	}

	private static ResourceLocation gogPath(String path) {
		if(Botania.gardenOfGlassLoaded) {
			return new ResourceLocation(LibMisc.MOD_ID, "garden_of_glass/" + path);
		} else {
			return path(path);
		}
	}

	private static ResourceLocation path(String path) {
		return new ResourceLocation(LibMisc.MOD_ID, path);
	}

	private static List<ResourceLocation> allOfGroup(String group) {
		return allOfGroup(new ResourceLocation(LibMisc.MOD_ID, group));
	}

	private static List<ResourceLocation> allOfGroup(ResourceLocation group) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if(server == null)
			return Collections.emptyList();
		String jsonGroup = group.toString();

		return server.getRecipeManager().getRecipes(IRecipeType.CRAFTING)
				.values()
				.stream()
				.filter(r -> jsonGroup.equals(r.getGroup()))
				.map(IRecipe::getId)
				.collect(Collectors.toList());
	}
}
