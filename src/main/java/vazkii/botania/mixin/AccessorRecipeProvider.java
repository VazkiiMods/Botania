/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.RecipeProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.file.Path;

@Mixin(RecipeProvider.class)
public interface AccessorRecipeProvider {
	@Invoker("inventoryTrigger")
	static InventoryChangeTrigger.TriggerInstance botania_condition(ItemPredicate... predicates) {
		throw new IllegalStateException("");
	}

	@Invoker
	public static void callSaveRecipe(HashCache dataCache, JsonObject jsonObject, Path path) {
		throw new IllegalStateException();
	}

	@Invoker("saveAdvancement")
	public static void callSaveRecipeAdvancement(HashCache dataCache, JsonObject jsonObject, Path path) {
		throw new IllegalStateException();
	}
}
