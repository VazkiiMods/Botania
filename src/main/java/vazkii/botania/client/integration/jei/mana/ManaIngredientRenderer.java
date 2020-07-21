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
import net.minecraft.util.text.*;
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
        TranslationTextComponent quantity;
        if(manaIngredient.isCreative()) {
            quantity = new TranslationTextComponent("botaniamisc.creative");
        } else {
            quantity = new TranslationTextComponent(ManaFraction.getClosest(manaIngredient.getAmount()).getKey());
        }
        tooltip.add(quantity.func_240699_a_(TextFormatting.GRAY));
        if(flag.isAdvanced()) {
            tooltip.add(new TranslationTextComponent("botania.page.preventingDecay0")
                    .func_240701_a_(TextFormatting.DARK_GRAY, TextFormatting.OBFUSCATED)
                    .func_230529_a_(new TranslationTextComponent("botania.ingredient.mana.micromanapools")
                            .func_240703_c_(Style.EMPTY.setObfuscated(false).applyFormatting(TextFormatting.DARK_GRAY))));
        }
        return tooltip;
    }

    private enum ManaFraction {
        EMPTY(0),
        ALMOST_EMPTY(1),

        TINY_BIT(100),
        LITTLE_BIT(1000),

        QUARTER_DILUTED(1 / 400F),
        THIRD_DILUTED(1 / 300F),
        HALF_DILUTED(1 / 200F),
        DILUTED(1 / 100F),

        COUPLE_DILUTED(1 / 50F),
        FEW_DILUTED(1 / 25F),
        SEVERAL_DILUTED(1 / 12F),

        TENTH(1 / 10F),
        EIGHTH(1 / 8F),
        QUARTER(1 / 4F),
        THIRD(1 / 3F),
        HALF(1 / 2F),

        TWO_THIRDS(2 / 3F),
        THREE_QUARTERS(3 / 4F),

        ALMOST_FULL(TilePool.MAX_MANA - 1),
        FULL(TilePool.MAX_MANA),

        MORE_THAN_FULL(TilePool.MAX_MANA + 1);

        private final int value;

        ManaFraction(int value) {
            this.value = value;
        }

        ManaFraction(float poolFraction) {
            this((int) (TilePool.MAX_MANA * poolFraction));
        }

        public String getKey() {
            return "botania.ingredient.mana.fraction." + name().toLowerCase();
        }

        public static ManaFraction getClosest(int mana) {
            int low = 0;
            int high = values().length - 1;
            do {
                int mid = (low + high) >>> 1;
                ManaFraction fraction = values()[mid];

                if(mana > fraction.value) {
                    low = mid;
                } else if(mana < fraction.value) {
                    high = mid;
                } else {
                    return fraction;
                }
            } while(high - low > 1);

            ManaFraction lowFraction = values()[low];
            ManaFraction highFraction = values()[high];
            return mana - lowFraction.value < highFraction.value - mana ? lowFraction : highFraction;
        }
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
            IconHelper.renderIcon(ms, buffers.getBuffer(RenderHelper.MANA_POOL_WATER), x, y, MiscellaneousIcons.INSTANCE.manaWater, 16, 16, 1F);

            buffers.finish(RenderHelper.MANA_POOL_WATER);

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
