/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei.puredaisy;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.compat.rei.CategoryUtils;
import vazkii.botania.common.crafting.RecipePureDaisy;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;

@Environment(EnvType.CLIENT)
public class PureDaisyREICategory implements RecipeCategory<PureDaisyREIDisplay> {
	private EntryStack daisy = EntryStack.create(new ItemStack(ModSubtiles.pureDaisy));
	private Identifier OVERLAY = ResourceLocationHelper.prefix("textures/gui/pure_daisy_overlay.png");

	@Override
	public @NotNull Identifier getIdentifier() {
		return RecipePureDaisy.TYPE_ID;
	}

	@Override
	public @NotNull EntryStack getLogo() {
		return daisy;
	}

	@Override
	public @NotNull String getCategoryName() {
		return I18n.translate("botania.nei.pureDaisy");
	}

	@Override
	public @NotNull List<Widget> setupDisplay(PureDaisyREIDisplay display, Rectangle bounds) {
		List<Widget> widgets = new ArrayList<>();
		Point center = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 16);

		widgets.add(CategoryUtils.drawRecipeBackground(bounds));
		widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> CategoryUtils.drawOverlay(helper, matrices, OVERLAY, center.x - 24, center.y - 14, 0, 0, 65, 44))));
		widgets.add(Widgets.createSlot(center).entry(daisy).disableBackground());
		widgets.add(Widgets.createSlot(new Point(center.x - 31, center.y)).entries(display.getInputEntries().get(0)).disableBackground());
		widgets.add(Widgets.createSlot(new Point(center.x + 29, center.y)).entries(display.getResultingEntries().get(0)).disableBackground());
		return widgets;
	}

	//todo paste into next class
	void drawOverlay(DrawableHelper helper, MatrixStack matrices, Identifier texture, Point point) {
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		MinecraftClient.getInstance().getTextureManager().bindTexture(texture);
		helper.drawTexture(matrices, point.x - 24, point.y - 14, 0, 0, 65, 44);
		RenderSystem.disableBlend();
		RenderSystem.disableAlphaTest();
	}

	@Override
	public int getDisplayHeight() {
		return 72;
	}
}
