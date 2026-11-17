/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.client.integration.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.category.IRecipeCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;

public abstract class BotaniaRecipeCategoryBase<T> implements IRecipeCategory<T> {
	@Nullable
	private final IDrawableStatic background;
	private final IDrawable icon;
	private final Component localizedName;
	private final int width;
	private final int height;

	public BotaniaRecipeCategoryBase(int width, int height, Component localizedName, IDrawable icon, @Nullable IDrawableStatic background) {
		this.width = width;
		this.height = height;
		this.localizedName = localizedName;
		this.icon = icon;
		this.background = background;
	}

	@Override
	public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		if (background != null) {
			background.draw(guiGraphics);
		}
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	protected RegistryAccess getRegistryAccess() {
		var level = Minecraft.getInstance().level;
		return level != null ? level.registryAccess() : RegistryAccess.EMPTY;
	}
}
