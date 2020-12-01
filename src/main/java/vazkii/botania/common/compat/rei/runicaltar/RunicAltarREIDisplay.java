package vazkii.botania.common.compat.rei.runicaltar;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.compat.rei.BotaniaRecipeDisplay;
import vazkii.botania.common.crafting.RecipeRuneAltar;

public class RunicAltarREIDisplay extends BotaniaRecipeDisplay<RecipeRuneAltar> {
    public RunicAltarREIDisplay(RecipeRuneAltar recipe) {
        super(recipe);
    }

    @Override
    public int getManaCost() {
        return this.recipe.getManaUsage();
    }

    @Override
    public @NotNull Identifier getRecipeCategory() {
        return this.recipe.TYPE_ID;
    }
}
