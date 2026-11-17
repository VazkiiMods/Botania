/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;

import java.util.ArrayList;
import java.util.List;

public class PureDaisyREICategory implements DisplayCategory<PureDaisyREIDisplay> {
	private final EntryStack<ItemStack> daisy = EntryStacks.of(new ItemStack(BotaniaBlocks.PURE_DAISY));
	private static final ResourceLocation OVERLAY = BotaniaAPI.botaniaRL("textures/gui/pure_daisy_overlay.png");

	@Override
	public CategoryIdentifier<PureDaisyREIDisplay> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.PURE_DAISY;
	}

	@Override
	public Renderer getIcon() {
		return daisy;
	}

	@Override
	public Component getTitle() {
		return Component.translatable("botania.nei.pureDaisy");
	}

	@Override
	public List<Widget> setupDisplay(PureDaisyREIDisplay display, Rectangle bounds) {
		return setupPureDaisyDisplay(display, bounds, daisy);
	}

	public static List<Widget> setupPureDaisyDisplay(Display display, Rectangle bounds, EntryStack<ItemStack> entryStack) {
		List<Widget> widgets = new ArrayList<>();
		Point center = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 9);

		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createDrawableWidget(((gui, mouseX, mouseY, delta) -> CategoryUtils.drawOverlay(gui, OVERLAY, center.x - 23, center.y - 13, 0, 0, 65, 44))));
		widgets.add(Widgets.createSlot(center).entry(entryStack).disableBackground());
		widgets.add(Widgets.createSlot(new Point(center.x - 30, center.y)).entries(display.getInputEntries().getFirst()).disableBackground());
		widgets.add(Widgets.createSlot(new Point(center.x + 29, center.y)).entries(display.getOutputEntries().getFirst()).disableBackground());
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 54;
	}

	@Override
	public int getDisplayWidth(PureDaisyREIDisplay display) {
		return 112;
	}
}
