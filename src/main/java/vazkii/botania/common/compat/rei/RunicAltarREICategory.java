/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
public class RunicAltarREICategory implements DisplayCategory<RunicAltarREIDisplay> {
	private EntryStack<ItemStack> altar = EntryStacks.of(new ItemStack(ModBlocks.runeAltar));
	private ResourceLocation PETAL_OVERLAY = ResourceLocationHelper.prefix("textures/gui/petal_overlay.png");

	@Override
	public @NotNull CategoryIdentifier<RunicAltarREIDisplay> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.RUNE_ALTAR;
	}

	@Override
	public @NotNull Renderer getIcon() {
		return altar;
	}

	@Override
	public @NotNull Component getTitle() {
		return new TranslatableComponent("botania.nei.runicAltar");
	}

	@Override
	public @NotNull List<Widget> setupDisplay(RunicAltarREIDisplay display, Rectangle bounds) {
		List<Widget> widgets = new ArrayList<>();
		List<EntryIngredient> inputs = display.getInputEntries();
		EntryStack<?> output = display.getOutputEntries().get(0).get(0);

		double angleBetweenEach = 360.0 / inputs.size();
		Point point = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 35), center = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 4);

		widgets.add(CategoryUtils.drawRecipeBackground(bounds));
		widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> {
			CategoryUtils.drawOverlay(helper, matrices, PETAL_OVERLAY, center.x - 23, center.y - 40, 42, 11, 85, 82);
			HUDHandler.renderManaBar(matrices, center.x - 44, center.y + 51, 0x0000FF, 0.75F, display.getManaCost(), TilePool.MAX_MANA / 10);
		})));
		widgets.add(Widgets.createSlot(center).entry(altar).disableBackground());
		for (EntryIngredient o : inputs) {
			widgets.add(Widgets.createSlot(point).entries(o).disableBackground());
			point = CategoryUtils.rotatePointAbout(point, center, angleBetweenEach);
		}
		widgets.add(Widgets.createSlot(new Point(center.x + 39, center.y - 33)).entry(output).disableBackground());
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 120;
	}
}
