/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

@Mixin(AbstractFurnaceTileEntity.class)
public interface AccessorAbstractFurnaceTileEntity {
	@Invoker
	boolean invokeCanSmelt(@Nullable IRecipe<?> recipe);

	@Accessor
	IRecipeType<? extends AbstractCookingRecipe> getRecipeType();

	@Accessor
	int getBurnTime();

	@Accessor
	void setBurnTime(int burnTime);

	@Accessor
	int getCookTime();

	@Accessor
	void setCookTime(int cookTime);
}
