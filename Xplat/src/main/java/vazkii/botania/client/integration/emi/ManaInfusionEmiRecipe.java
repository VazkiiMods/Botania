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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import vazkii.botania.api.recipe.ManaInfusionRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.crafting.StateIngredients;

import java.util.List;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ManaInfusionEmiRecipe extends BotaniaEmiRecipe {
	private static final ResourceLocation TEXTURE = botaniaRL("textures/gui/pure_daisy_overlay.png");
	private static final EmiStack POOL;
	private final int mana;
	private final EmiIngredient catalyst;

	static {
		ItemStack stack = new ItemStack(BotaniaBlocks.MANA_POOL);
		stack.set(BotaniaDataComponents.RENDER_FULL, Unit.INSTANCE);
		POOL = EmiStack.of(stack);
	}

	public ManaInfusionEmiRecipe(RecipeHolder<? extends ManaInfusionRecipe> recipe) {
		super(BotaniaEmiPlugin.MANA_INFUSION, recipe);
		this.input = recipe.value().getIngredients().stream().map(EmiIngredient::of).toList();
		if (recipe.value().getRecipeCatalyst() != StateIngredients.NONE) {
			this.catalyst = EmiIngredient.of(recipe.value().getRecipeCatalyst().getDisplayed().stream()
					.map(s -> EmiStack.of(s.getBlock())).toList());
		} else {
			this.catalyst = EmiStack.EMPTY;
		}
		this.output = List.of(EmiStack.of(recipe.value().getResultItem(getRegistryAccess())));
		mana = recipe.value().getManaToConsume();
	}

	@Override
	public List<EmiIngredient> getCatalysts() {
		return this.catalyst.isEmpty() ? List.of() : List.of(this.catalyst);
	}

	@Override
	public int getDisplayHeight() {
		return 65;
	}

	@Override
	public int getDisplayWidth() {
		return 116;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		widgets.add(new BlendTextureWidget(TEXTURE, 28, 0, 65, 44, 0, 0));
		widgets.add(new ManaWidget(7, 50, mana, ManaPoolBlock.MAX_MANA / 10));
		widgets.addSlot(input.getFirst(), 21, 13).drawBack(false);
		widgets.addSlot(POOL, 50, 13).catalyst(true).drawBack(false);
		if (!catalyst.isEmpty()) {
			widgets.addSlot(catalyst, 0, 13).catalyst(true).drawBack(false);
		}
		widgets.addSlot(output.getFirst(), 79, 13).drawBack(false).recipeContext(this);
	}

}
