package vazkii.botania.mixin;

import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(Ingredient.class)
public interface AccessorIngredient {
	@Invoker("ofEntries")
	static Ingredient botania_ofEntries(Stream<? extends Ingredient.Entry> entries) {
		throw new IllegalStateException("");
	}
}
