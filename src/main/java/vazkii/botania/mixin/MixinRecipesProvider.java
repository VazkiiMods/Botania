/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.data.DataCache;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.data.recipes.BotaniaRecipeProvider;

import java.util.function.Consumer;

@Mixin(RecipesProvider.class)
public class MixinRecipesProvider {
	@ModifyArg(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/data/server/RecipesProvider;generate(Ljava/util/function/Consumer;)V"),
		method = "run"
	)
	private Consumer<RecipeJsonProvider> generate(Consumer<RecipeJsonProvider> consumer) {
		// When we see one of our own generators, call our instance method instead of
		// vanilla's static method, then dummy out the vanilla consumer.
		if (this instanceof BotaniaRecipeProvider) {
			((BotaniaRecipeProvider) this).registerRecipes(consumer);
			return ignored -> {};
		}
		return consumer;
	}

	@Inject(
		at = @At(
			shift = At.Shift.AFTER,
			value = "INVOKE",
			target = "Lnet/minecraft/data/server/RecipesProvider;generate(Ljava/util/function/Consumer;)V"
		),
		method = "run",
		cancellable = true
	)
	private void stop(DataCache cache, CallbackInfo ci) {
		if (this instanceof BotaniaRecipeProvider) {
			// Don't generate the "root.json", stop right after calling generate
			ci.cancel();
		}
	}
}
