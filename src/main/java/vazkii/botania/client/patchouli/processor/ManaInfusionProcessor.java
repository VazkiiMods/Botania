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
import net.minecraft.util.text.TextFormatting;

import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;
import java.util.stream.Collectors;

public class ManaInfusionProcessor implements IComponentProcessor {
	private List<IManaInfusionRecipe> recipes;
	private boolean hasCustomHeading;

	@Override
	public void setup(IVariableProvider<String> variables) {
		if (variables.has("recipes") && variables.has("group")) {
			Botania.LOGGER.warn("Mana infusion template has both 'recipes' and 'group', ignoring 'recipes'");
		}

		ImmutableList.Builder<IManaInfusionRecipe> builder = ImmutableList.builder();
		if (variables.has("group")) {
			String group = variables.get("group");
			TilePool.manaInfusionRecipes(Minecraft.getInstance().world.getRecipeManager()).stream()
					.filter(r -> r.getGroup().equals(group))
					.forEach(builder::add);
		} else {
			for (String s : variables.get("recipes").split(";")) {
				IRecipe<?> recipe = Minecraft.getInstance().world.getRecipeManager().getRecipes(ModRecipeTypes.MANA_INFUSION_TYPE).get(new ResourceLocation(s));
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
	public String process(String key) {
		if (recipes.isEmpty()) {
			return null;
		}
		switch (key) {
		case "heading":
			if (!hasCustomHeading) {
				return recipes.get(0).getRecipeOutput().getDisplayName().getString();
			}
			return null;
		case "input":
			return PatchouliUtils.interweaveIngredients(recipes.stream().map(r -> r.getIngredients().get(0)).collect(Collectors.toList()));
		case "output":
			return recipes.stream().map(IManaInfusionRecipe::getRecipeOutput).map(PatchouliAPI.instance::serializeItemStack).collect(Collectors.joining(","));
		case "catalyst":
			return recipes.stream().map(IManaInfusionRecipe::getCatalyst)
					.map(state -> {
						if (state == null) {
							return ItemStack.EMPTY;
						}
						return new ItemStack(state.getBlock().asItem());
					})
					.map(PatchouliAPI.instance::serializeItemStack)
					.collect(Collectors.joining(","));
		case "mana":
			return recipes.stream().mapToInt(IManaInfusionRecipe::getManaToConsume).mapToObj(Integer::toString).collect(Collectors.joining(";"));
		case "drop":
			return I18n.format("botaniamisc.drop") + TextFormatting.BOLD + " (?)";
		case "dropTip0":
		case "dropTip1":
			return I18n.format("botaniamisc.dropTip" + key.substring(7),
					TextFormatting.GREEN + RenderHelper.getKeyDisplayString("key.drop") + TextFormatting.WHITE);
		}
		return null;
	}
}
