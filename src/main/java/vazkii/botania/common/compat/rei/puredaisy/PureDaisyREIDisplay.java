package vazkii.botania.common.compat.rei.puredaisy;

import com.google.common.collect.ImmutableList;
import me.shedaniel.rei.api.EntryStack;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.compat.rei.BotaniaRecipeDisplay;
import vazkii.botania.common.crafting.RecipePureDaisy;

import java.util.Collections;

public class PureDaisyREIDisplay extends BotaniaRecipeDisplay<RecipePureDaisy> {

    public PureDaisyREIDisplay(RecipePureDaisy recipe) {
        super(recipe);
        ImmutableList.Builder<EntryStack> inputs = ImmutableList.builder();
        for (BlockState state : recipe.getInput().getDisplayed()) {
            if (!state.getFluidState().isEmpty()) {
                inputs.add(EntryStack.create(state.getFluidState().getFluid()));
            } else {
                inputs.add(EntryStack.create(state.getBlock()));
            }
        }
        this.inputs = Collections.singletonList(inputs.build());
        this.outputs = Collections.singletonList(EntryStack.create(recipe.getOutputState().getBlock()));
    }

    /*todo implement time-based hints?
    public int getProcessingTime() {
        return recipe.getTime();
    }
    */

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public @NotNull Identifier getRecipeCategory() {
        return recipe.TYPE_ID;
    }
}
