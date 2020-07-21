/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.patchouli.processor;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.KeybindText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.text.*;

import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;
import java.util.stream.Collectors;

public class ManaInfusionProcessor implements IComponentProcessor {
	private List<IManaInfusionRecipe> recipes;
	private boolean hasCustomHeading;

	@Override
	public void setup(IVariableProvider variables) {
		if (variables.has("recipes") && variables.has("group")) {
			Botania.LOGGER.warn("Mana infusion template has both 'recipes' and 'group', ignoring 'recipes'");
		}

		ImmutableList.Builder<IManaInfusionRecipe> builder = ImmutableList.builder();
		if (variables.has("group")) {
			String group = variables.get("group").asString();
			TilePool.manaInfusionRecipes(MinecraftClient.getInstance().world).stream()
					.filter(r -> r.getGroup().equals(group))
					.forEach(builder::add);
		} else {
			for (IVariable s : variables.get("recipes").asListOrSingleton()) {
				Recipe<?> recipe = ModRecipeTypes.getRecipes(MinecraftClient.getInstance().world, ModRecipeTypes.MANA_INFUSION_TYPE).get(new Identifier(s.asString()));
				if (recipe instanceof IManaInfusionRecipe) {
					builder.add((IManaInfusionRecipe) recipe);
				} else {
					Botania.LOGGER.warn("Mana infusion template references nonexistent recipe {}", s);
				}
			}
		}

		this.recipes = builder.build();
		this.hasCustomHeading = variables.has("heading");
	}

	@Override
	public IVariable process(String key) {
		if (recipes.isEmpty()) {
			return null;
		}
		switch (key) {
		case "heading":
			if (!hasCustomHeading) {
				return IVariable.from(recipes.get(0).getOutput().getName());
			}
			return null;
		case "input":
			return PatchouliUtils.interweaveIngredients(recipes.stream().map(r -> r.getPreviewInputs().get(0)).collect(Collectors.toList()));
		case "output":
			return IVariable.wrapList(recipes.stream().map(IManaInfusionRecipe::getOutput).map(IVariable::from).collect(Collectors.toList()));
		case "catalyst":
			return IVariable.wrapList(recipes.stream().map(IManaInfusionRecipe::getCatalyst)
					.map(state -> {
						if (state == null) {
							return ItemStack.EMPTY;
						}
						return new ItemStack(state.getBlock().asItem());
					})
					.map(IVariable::from)
					.collect(Collectors.toList()));
		case "mana":
			return IVariable.wrapList(recipes.stream().mapToInt(IManaInfusionRecipe::getManaToConsume).mapToObj(IVariable::wrap).collect(Collectors.toList()));
		case "drop":
			Text q = new LiteralText("(?)").formatted(Formatting.BOLD);
			return IVariable.from(new TranslatableText("botaniamisc.drop").append(" ").append(q));
		case "dropTip0":
		case "dropTip1":
			Text drop = new KeybindText("key.drop").formatted(Formatting.GREEN);
			return IVariable.from(new TranslatableText("botaniamisc." + key, drop));
		}
		return null;
	}
}
