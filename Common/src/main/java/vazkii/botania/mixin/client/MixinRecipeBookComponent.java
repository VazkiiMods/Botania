/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin.client;

import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import vazkii.botania.client.core.RecipeBookAccess;

@Mixin(RecipeBookComponent.class)
public class MixinRecipeBookComponent implements RecipeBookAccess {
	@Unique
	private ItemStack hoveredGhostRecipeStack;

	@Override
	public ItemStack getHoveredGhostRecipeStack() {
		return hoveredGhostRecipeStack;
	}

	// Captures the stack that had its tooltip drawn.
	@ModifyVariable(method = "renderGhostRecipeTooltip", at = @At("RETURN"), ordinal = 0, require = 0)
	private ItemStack captureHoveredGhostStack(ItemStack stack) {
		hoveredGhostRecipeStack = stack;
		return stack;
	}
}
