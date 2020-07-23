/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.flowers;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import vazkii.botania.client.integration.jei.flowers.generating.EndoflameCategory;
import vazkii.botania.client.integration.jei.flowers.generating.EntropinnyumCategory;
import vazkii.botania.client.integration.jei.flowers.generating.KekimurusCategory;
import vazkii.botania.client.integration.jei.flowers.generating.MunchdewCategory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractFlowerCategory<T> implements IRecipeCategory<T> {

	private static final Map<ResourceLocation, AbstractFlowerCategory<?>> categories = new HashMap<>();

	protected final ItemStack flower;
	protected final ItemStack floatingFlower;
	private final IDrawable icon;

	public AbstractFlowerCategory(IGuiHelper guiHelper, Block flower, Block floatingFlower) {
		this.flower = new ItemStack(flower);
		this.floatingFlower = new ItemStack(floatingFlower);
		icon = guiHelper.createDrawableIngredient(this.flower);
		categories.put(getUid(), this);
	}

	@Override
	public ResourceLocation getUid() {
		return Objects.requireNonNull(flower.getItem().getRegistryName());
	}

	@Override
	public String getTitle() {
		return flower.getDisplayName().getString();
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	protected abstract Collection<T> makeRecipes(IIngredientManager ingredientManager, IJeiHelpers helpers);

	public static void registerCategories(IRecipeCategoryRegistration registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(
				new EndoflameCategory(guiHelper),
				new KekimurusCategory(guiHelper),
				new MunchdewCategory(guiHelper),
				new EntropinnyumCategory(guiHelper)
		);
	}

	public static void registerRecipes(@Nonnull IRecipeRegistration registry) {
		categories.forEach((uid, category) -> registry.addRecipes(category.makeRecipes(registry.getIngredientManager(), registry.getJeiHelpers()), uid));
	}

	public static void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		categories.forEach((uid, category) -> {
			registry.addRecipeCatalyst(category.flower, uid);
			registry.addRecipeCatalyst(category.floatingFlower, uid);
		});
	}

}
