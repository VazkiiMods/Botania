/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei.brewery;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.compat.rei.CategoryUtils;
import vazkii.botania.common.crafting.RecipeBrew;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BreweryREICategory implements RecipeCategory<BreweryREIDisplay> {
    private EntryStack brewery = EntryStack.create(new ItemStack(ModBlocks.brewery));
    private Identifier BREWERY_OVERLAY = ResourceLocationHelper.prefix("textures/gui/nei_brewery.png");

    @Override
    public @NotNull Identifier getIdentifier() {
        return RecipeBrew.TYPE_ID;
    }

    @Override
    public @NotNull EntryStack getLogo() {
        return this.brewery;
    }

    @Override
    public @NotNull String getCategoryName() {
        return I18n.translate("botania.nei.brewery");
    }

    @Override
    public @NotNull List<Widget> setupDisplay(BreweryREIDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        List<List<EntryStack>> inputs = display.getInputEntries();
        Point center = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 4);

        widgets.add(CategoryUtils.drawRecipeBackground(bounds));
        widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> CategoryUtils.drawOverlay(helper, matrices, BREWERY_OVERLAY, center.x - 35, center.y - 20, 28, 6, 86, 55))));

        widgets.add(Widgets.createSlot(new Point(center.x - 24, center.y + 16)).entries(display.getContainers()));
        int posX = center.x - (inputs.size() - 1) * 9;
        for (List<EntryStack> o : display.getInputEntries()) {
            widgets.add(Widgets.createSlot(new Point(posX, center.y - 22)).entries(o).disableBackground());
            posX += 18;
        }
        widgets.add(Widgets.createSlot(new Point(center.x + 24, center.y + 16)).entries(display.getResultingEntries().get(0)));

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 72;
    }
}
