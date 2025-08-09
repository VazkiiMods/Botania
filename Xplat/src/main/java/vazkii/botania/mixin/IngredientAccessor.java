package vazkii.botania.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Ingredient.class)
public interface IngredientAccessor {
	@Accessor("itemStacks")
	void botania_setItemStacks(@Nullable ItemStack[] itemStacks);
}
