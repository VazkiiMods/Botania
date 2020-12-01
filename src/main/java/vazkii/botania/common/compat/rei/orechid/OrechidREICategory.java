/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei.orechid;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.widget.Widget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.compat.rei.CategoryUtils;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class OrechidREICategory implements RecipeCategory<OrechidBaseREIDisplay> {
    private EntryStack orechid;
    private Identifier ID;
    private boolean isIgnem;
    private Identifier OVERLAY = ResourceLocationHelper.prefix("textures/gui/pure_daisy_overlay.png");

    public OrechidREICategory(Block orechid, Identifier id) {
        this.orechid = EntryStack.create(orechid);
        ID = id;
        isIgnem = orechid == ModSubtiles.orechidIgnem;
    }

    @Override
    public @NotNull Identifier getIdentifier() {
        return ID;
    }

    @Override
    public @NotNull EntryStack getLogo() {
        return orechid;
    }

    @Override
    public @NotNull String getCategoryName() {
        return I18n.translate(isIgnem ? "botania.nei.orechidIgnem" : "botania.nei.orechid");
    }

    @Override
    public @NotNull List<Widget> setupDisplay(OrechidBaseREIDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point center = new Point(bounds.getCenterX() - 8, bounds.getCenterY() - 16);

        widgets.add(CategoryUtils.drawRecipeBackground(bounds));
        widgets.add(Widgets.createDrawableWidget(((helper, matrices, mouseX, mouseY, delta) -> CategoryUtils.drawOverlay(helper, matrices, OVERLAY, center.x - 24, center.y - 14, 0, 0, 65, 44))));
        widgets.add(Widgets.createSlot(center).entry(orechid).disableBackground());
        widgets.add(Widgets.createSlot(new Point(center.x - 31, center.y)).entries(display.getInputEntries().get(0)).disableBackground());
        widgets.add(Widgets.createSlot(new Point(center.x + 29, center.y)).entries(display.getResultingEntries().get(0)).disableBackground());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 72;
    }
}
