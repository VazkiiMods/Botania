package vazkii.botania.mixin;

import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;

import java.util.function.Consumer;

@Mixin(RecipesProvider.class)
public class MixinRecipesProvider {
	@ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/data/server/RecipesProvider;generate(Ljava/util/function/Consumer;)V"),
		method = "run")
	private Consumer<RecipeJsonProvider> generate(Consumer<RecipeJsonProvider> consumer) {
		// When we see one of our own generators, call our instance method instead of
		// vanilla's static method, then dummy out the vanilla consumer.
		if (this instanceof BotaniaRecipeProvider) {
			((BotaniaRecipeProvider) this).registerRecipes(consumer);
			return ignored -> {};
		}
		return consumer;
	}
}
