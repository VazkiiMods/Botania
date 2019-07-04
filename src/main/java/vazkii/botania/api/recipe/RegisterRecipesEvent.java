package vazkii.botania.api.recipe;

import net.minecraftforge.eventbus.api.Event;

import java.util.function.Consumer;

/**
 * Fired on {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}
 * whenever Botania wants to receive recipe registrations, i.e. on server start and every /reload.
 *
 * The handles to register recipes are simple {@link Consumer}s that accept the recipe to register it.
 *
 * This will be fired many times during the lifecycle of the game, it's highly encouraged you keep your event
 * handlers as stateless as possible.
 */
public class RegisterRecipesEvent extends Event {
    private final Consumer<RecipePureDaisy> pureDaisy;
    private final Consumer<RecipeManaInfusion> manaInfusion;
    private final Consumer<RecipeBrew> brew;
    private final Consumer<RecipeElvenTrade> elvenTrade;
    private final Consumer<RecipePetals> apothecary;
    private final Consumer<RecipeRuneAltar> runeAltar;

    public RegisterRecipesEvent(Consumer<RecipePureDaisy> pureDaisy, Consumer<RecipeManaInfusion> manaInfusion,
                                Consumer<RecipeBrew> brew, Consumer<RecipeElvenTrade> elvenTrade,
                                Consumer<RecipePetals> apothecary, Consumer<RecipeRuneAltar> runeAltar) {
        this.pureDaisy = pureDaisy;
        this.manaInfusion = manaInfusion;
        this.brew = brew;
        this.elvenTrade = elvenTrade;
        this.apothecary = apothecary;
        this.runeAltar = runeAltar;
    }

    public Consumer<RecipePureDaisy> pureDaisy() {
        return pureDaisy;
    }

    public Consumer<RecipeManaInfusion> manaInfusion() {
        return manaInfusion;
    }

    public Consumer<RecipeBrew> brew() {
        return brew;
    }

    public Consumer<RecipeElvenTrade> elvenTrade() {
        return elvenTrade;
    }

    public Consumer<RecipePetals> apothecary() {
        return apothecary;
    }

    public Consumer<RecipeRuneAltar> runeAltar() {
        return runeAltar;
    }
}
