/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.integration.rei;

import me.shedaniel.math.FloatingPoint;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

public class TerraPlateREICategory implements DisplayCategory<TerraPlateREIDisplay> {
	private static final ResourceLocation OVERLAY = ResourceLocationHelper.prefix("textures/gui/terrasteel_jei_overlay.png");
	private final EntryStack<ItemStack> icon = EntryStacks.of(ModBlocks.terraPlate);

	@Override
	public @NotNull CategoryIdentifier<TerraPlateREIDisplay> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.TERRA_PLATE;
	}

	@Override
	public @NotNull Renderer getIcon() {
		return icon;
	}

	@Override
	public @NotNull Component getTitle() {
		return new TranslatableComponent("botania.nei.terraPlate");
	}

	@Override
	public @NotNull List<Widget> setupDisplay(TerraPlateREIDisplay display, Rectangle bounds) {
		List<Widget> widgets = new ArrayList<>();
		List<EntryIngredient> inputs = display.getInputEntries();
		EntryStack<?> output = display.getOutputEntries().get(0).get(0);

		double angleBetweenEach = 360.0 / inputs.size();
		FloatingPoint point = new FloatingPoint(bounds.getCenterX() - 8, bounds.getCenterY() - 44);
		Point center = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 12);
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> {
			CategoryUtils.drawOverlay(helper, matrices, OVERLAY, center.x - 24, center.y - 24, 42, 29, 64, 64);
			HUDHandler.renderManaBar(matrices, center.x - 43, center.y + 52, 0x0000FF, 0.75F, display.getManaCost(), TilePool.MAX_MANA / 10);
		})));

		for (EntryIngredient o : inputs) {
			widgets.add(Widgets.createSlot(point.getLocation()).entries(o).disableBackground());
			point = CategoryUtils.rotatePointAbout(point, center, angleBetweenEach);
		}
		widgets.add(Widgets.createSlot(center).entry(output).disableBackground());

		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 103;
	}
}
