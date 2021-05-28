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
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.RecipePetals;
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
public class PetalApothecaryREICategory implements RecipeCategory<BotaniaRecipeDisplay<RecipePetals>> {
	private EntryStack apothecary = EntryStack.create(new ItemStack(ModBlocks.defaultAltar));
	private Identifier PETAL_OVERLAY = ResourceLocationHelper.prefix("textures/gui/petal_overlay.png");

	@Override
	public @NotNull Identifier getIdentifier() {
		return RecipePetals.TYPE_ID;
	}

	@Override
	public @NotNull EntryStack getLogo() {
		return this.apothecary;
	}

	@Override
	public @NotNull String getCategoryName() {
		return I18n.translate("botania.nei.petalApothecary");
	}

	@Override
	public @NotNull List<Widget> setupDisplay(BotaniaRecipeDisplay<RecipePetals> display, Rectangle bounds) {
		List<Widget> widgets = new ArrayList<>();
		List<List<EntryStack>> inputs = display.getInputEntries();
		EntryStack output = display.getResultingEntries().get(0).get(0);

		double angleBetweenEach = 360.0 / inputs.size();
		Point point = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 35), center = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 4);
		widgets.add(CategoryUtils.drawRecipeBackground(bounds));
		widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> CategoryUtils.drawOverlay(helper, matrices, PETAL_OVERLAY, center.x - 23, center.y - 40, 42, 11, 85, 82))));

		for (List<EntryStack> o : inputs) {
			widgets.add(Widgets.createSlot(point).entries(o).disableBackground());
			point = CategoryUtils.rotatePointAbout(point, center, angleBetweenEach);
		}
		widgets.add(Widgets.createSlot(center).entry(this.apothecary).disableBackground());
		widgets.add(Widgets.createSlot(new Point(center.x + 39, center.y - 33)).entry(output).disableBackground());

		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 120;
	}
}
