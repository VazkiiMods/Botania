/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AccessorAbstractFurnaceTileEntity {
	@Invoker
	boolean invokeCanSmelt(@Nullable Recipe<?> recipe);

	@Accessor
	RecipeType<? extends AbstractCookingRecipe> getRecipeType();

	@Accessor
	int getBurnTime();

	@Accessor
	void setBurnTime(int burnTime);

	@Accessor
	int getCookTime();

	@Accessor
	void setCookTime(int cookTime);
}
