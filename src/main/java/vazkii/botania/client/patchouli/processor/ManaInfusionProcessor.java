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

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
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
			TilePool.manaInfusionRecipes(Minecraft.getInstance().world).stream()
					.filter(r -> r.getGroup().equals(group))
					.forEach(builder::add);
		} else {
			for (IVariable s : variables.get("recipes").asListOrSingleton()) {
				IRecipe<?> recipe = ModRecipeTypes.getRecipes(Minecraft.getInstance().world, ModRecipeTypes.MANA_INFUSION_TYPE).get(new ResourceLocation(s.asString()));
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
				return IVariable.from(recipes.get(0).getRecipeOutput().getDisplayName());
			}
			return null;
		case "input":
			return PatchouliUtils.interweaveIngredients(recipes.stream().map(r -> r.getIngredients().get(0)).collect(Collectors.toList()));
		case "output":
			return IVariable.wrapList(recipes.stream().map(IManaInfusionRecipe::getRecipeOutput).map(IVariable::from).collect(Collectors.toList()));
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
			ITextComponent q = new StringTextComponent("(?)").func_240699_a_(TextFormatting.BOLD);
			return IVariable.from(new TranslationTextComponent("botaniamisc.drop").func_240702_b_(" ").func_230529_a_(q));
		case "dropTip0":
		case "dropTip1":
			ITextComponent drop = new KeybindTextComponent("key.drop").func_240699_a_(TextFormatting.GREEN);
			return IVariable.from(new TranslationTextComponent("botaniamisc." + key, drop));
		}
		return null;
	}
}
