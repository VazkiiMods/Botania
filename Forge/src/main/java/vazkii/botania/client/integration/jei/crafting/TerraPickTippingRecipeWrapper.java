/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.crafting;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.crafting.recipe.TerraPickTippingRecipe;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;

import java.util.Collections;
import java.util.List;

public class TerraPickTippingRecipeWrapper implements ICraftingCategoryExtension {
	private final List<List<ItemStack>> inputs;
	private final ItemStack output;
	private final ResourceLocation name;

	public TerraPickTippingRecipeWrapper(TerraPickTippingRecipe recipe) {
		inputs = ImmutableList.of(ImmutableList.of(new ItemStack(ModItems.terraPick)), ImmutableList.of(new ItemStack(ModItems.elementiumPick)));
		output = new ItemStack(ModItems.terraPick);
		ItemTerraPick.setTipped(output);

		this.name = recipe.getId();
	}

	@SuppressWarnings("removal") // todo 1.19 suppressing to unblock the build, address this before release
	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ICraftingGridHelper helper, @NotNull IFocusGroup focuses) {
		helper.setInputs(builder, VanillaTypes.ITEM_STACK, inputs, 0, 0);
		helper.setOutputs(builder, VanillaTypes.ITEM_STACK, Collections.singletonList(output));
	}

	@Nullable
	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}
}
