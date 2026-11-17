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

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.ManaInfusionRecipe;
import vazkii.botania.client.patchouli.PatchouliUtils;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;

public class ManaInfusionProcessor implements IComponentProcessor {
	@SuppressWarnings("NotNullFieldNotInitialized")
	private List<ManaInfusionRecipe> recipes;
	private boolean hasCustomHeading;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		if (variables.has("recipes") && variables.has("group")) {
			BotaniaAPI.LOGGER.warn("Mana infusion template has both 'recipes' and 'group', ignoring 'recipes'");
		}

		ImmutableList.Builder<ManaInfusionRecipe> builder = ImmutableList.builder();
		if (variables.has("group")) {
			String group = variables.get("group", level.registryAccess()).asString();
			builder.addAll(PatchouliUtils.getRecipeGroup(BotaniaRecipeTypes.MANA_INFUSION_TYPE, group));
		} else {
			for (IVariable s : variables.get("recipes", level.registryAccess()).asListOrSingleton(level.registryAccess())) {
				ManaInfusionRecipe recipe = PatchouliUtils.getRecipe(level, BotaniaRecipeTypes.MANA_INFUSION_TYPE, ResourceLocation.parse(s.asString()));
				if (recipe != null) {
					builder.add(recipe);
				}
			}
		}

		this.recipes = builder.build();
		this.hasCustomHeading = variables.has("heading");
	}

	@Nullable
	@Override
	public IVariable process(Level level, String key) {
		if (recipes.isEmpty()) {
			return null;
		}
		switch (key) {
			case "heading":
				if (!hasCustomHeading) {
					return IVariable.from(recipes.getFirst().getResultItem(level.registryAccess()).getHoverName(), level.registryAccess());
				}
				return null;
			case "input":
				return PatchouliUtils.interweaveIngredients(recipes.stream()
						.map(recipe -> recipe.getIngredients().getFirst())
						.toList(), level);
			case "output":
				return IVariable.wrapList(recipes.stream()
						.map(recipe -> recipe.getResultItem(level.registryAccess()))
						.map(stack -> IVariable.from(stack, level.registryAccess()))
						.toList(), level.registryAccess());
			case "catalyst":
				return IVariable.wrapList(recipes.stream().map(ManaInfusionRecipe::getRecipeCatalyst)
						.flatMap(ingr -> ingr.getDisplayedStacks().stream())
						.map(stack -> IVariable.from(stack, level.registryAccess()))
						.toList(), level.registryAccess());
			case "mana":
				return IVariable.wrapList(recipes.stream().mapToInt(ManaInfusionRecipe::getManaToConsume)
						.mapToObj(mana -> IVariable.wrap(mana, level.registryAccess()))
						.toList(), level.registryAccess());
			case "drop":
				Component q = Component.literal("(?)").withStyle(ChatFormatting.BOLD);
				return IVariable.from(Component.translatable("botaniamisc.drop").append(" ").append(q), level.registryAccess());
			case "dropTip2":
			case "dropTip1":
				Component drop = Component.keybind("key.drop").withStyle(ChatFormatting.GREEN);
				return IVariable.from(Component.translatable("botaniamisc." + key, drop), level.registryAccess());
			case "dropTip3":
				return IVariable.from(Component.translatable("botaniamisc." + key), level.registryAccess());
		}
		return null;
	}
}
