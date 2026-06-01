/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.integration.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import vazkii.botania.api.recipe.BotanicalBreweryRecipe;

import java.util.List;
import java.util.stream.Stream;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BotanicalBreweryEmiRecipe extends BotaniaEmiRecipe {
	private static final ResourceLocation TEXTURE = botaniaRL("textures/gui/nei_brewery.png");
	private final List<EmiIngredient> ingredients;
	private final ResourceLocation syntheticId;

	public BotanicalBreweryEmiRecipe(RecipeHolder<? extends BotanicalBreweryRecipe> recipe, ItemStack container) {
		super(BotaniaEmiPlugin.BOTANICAL_BREWERY, recipe);
		this.ingredients = recipe.value().getIngredients().stream().map(EmiIngredient::of).toList();
		this.input = Stream.concat(Stream.of(EmiStack.of(container)), ingredients.stream()).toList();
		this.output = List.of(EmiStack.of(recipe.value().getOutput(container.copy())));
		// since brews can exist for multiple container items, create a synthetic ID
		ResourceLocation id = recipe.id();
		ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(container.getItem());
		this.syntheticId = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "/" + id.getPath()
				+ "/" + itemId.getNamespace() + "/" + itemId.getPath());
	}

	@Override
	public ResourceLocation getId() {
		return syntheticId;
	}

	@Override
	public int getDisplayHeight() {
		return 65;
	}

	@Override
	public int getDisplayWidth() {
		return 120;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.add(new BlendTextureWidget(TEXTURE, 0, 0, 86, 55, 28, 6));
		widgets.addSlot(input.get(output.size() - 1), 10, 35);
		int sx = 58 - (ingredients.size() - 1) * 9;
		for (EmiIngredient stack : ingredients) {
			widgets.addSlot(stack, sx, 1).drawBack(false);
			sx += 18;
		}
		widgets.addSlot(output.getFirst(), 58, 35).recipeContext(this);
	}
}
