/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

public class PureDaisyREICategory implements DisplayCategory<PureDaisyREIDisplay> {
	private final EntryStack<ItemStack> daisy = EntryStacks.of(new ItemStack(ModSubtiles.pureDaisy));
	private final ResourceLocation OVERLAY = ResourceLocationHelper.prefix("textures/gui/pure_daisy_overlay.png");

	@Override
	public @NotNull CategoryIdentifier<PureDaisyREIDisplay> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.PURE_DAISY;
	}

	@Override
	public @NotNull Renderer getIcon() {
		return daisy;
	}

	@Override
	public @NotNull Component getTitle() {
		return new TranslatableComponent("botania.nei.pureDaisy");
	}

	@Override
	public @NotNull List<Widget> setupDisplay(PureDaisyREIDisplay display, Rectangle bounds) {
		List<Widget> widgets = new ArrayList<>();
		Point center = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 9);

		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> CategoryUtils.drawOverlay(helper, matrices, OVERLAY, center.x - 23, center.y - 13, 0, 0, 65, 44))));
		widgets.add(Widgets.createSlot(center).entry(daisy).disableBackground());
		widgets.add(Widgets.createSlot(new Point(center.x - 30, center.y)).entries(display.getInputEntries().get(0)).disableBackground());
		widgets.add(Widgets.createSlot(new Point(center.x + 29, center.y)).entries(display.getOutputEntries().get(0)).disableBackground());
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 54;
	}
}
