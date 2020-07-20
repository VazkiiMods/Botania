package vazkii.botania.client.integration.jei.mana;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.botania.api.mana.IManaIngredient;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ManaIngredientRenderer implements IIngredientRenderer<IManaIngredient> {

    @Override
    public FontRenderer getFontRenderer(Minecraft minecraft, IManaIngredient ingredient) {
        return minecraft.fontRenderer;
    }

    @Override
    public List<ITextComponent> getTooltip(IManaIngredient manaIngredient, ITooltipFlag flag) {
        ArrayList<ITextComponent> tooltip = new ArrayList<>();
        tooltip.add(new TranslationTextComponent("botania.ingredient.mana"));
        return tooltip; // TODO
    }

    /**
     * Renders as a square, like in the ingredient list.
     */
    public static class Square extends ManaIngredientRenderer {

        public static IIngredientRenderer<IManaIngredient> INSTANCE = new Square();

        private Square() {
        }

        @Override
        public void render(MatrixStack ms, int x, int y, @Nullable IManaIngredient manaIngredient) {
            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();

            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);

            RenderSystem.color4f(1, 1, 1, 1);

            IRenderTypeBuffer.Impl buffers = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
            IconHelper.renderIcon(ms, buffers.getBuffer(RenderHelper.ICON_OVERLAY), x, y, MiscellaneousIcons.INSTANCE.manaWater, 16, 16, 1F);

            buffers.finish(RenderHelper.ICON_OVERLAY);

            RenderSystem.disableAlphaTest();
            RenderSystem.disableBlend();
        }

    }

    /**
     * Renders as a bar, like in a recipe.
     */
    public static class Bar extends ManaIngredientRenderer {

        public static IIngredientRenderer<IManaIngredient> INSTANCE = new Bar();

        private Bar() {
        }

        @Override
        public void render(MatrixStack ms, int x, int y, @Nullable IManaIngredient manaIngredient) {
            int amount = 0;
            if(manaIngredient != null) {
                amount = manaIngredient.getAmount();
            }

            HUDHandler.renderManaBar(ms, x, y, 0xFF0000FF, 0.75F, amount, TilePool.MAX_MANA / 10);
        }

    }

}
