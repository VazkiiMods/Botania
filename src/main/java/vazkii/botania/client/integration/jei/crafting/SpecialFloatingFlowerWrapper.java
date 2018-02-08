package vazkii.botania.client.integration.jei.crafting;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.recipe.SpecialFloatingFlowerRecipe;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

import java.util.ArrayList;
import java.util.List;

public class SpecialFloatingFlowerWrapper implements IRecipeWrapper {
    private final List<List<ItemStack>> inputs = new ArrayList<>();
    private final ItemStack output;

    public SpecialFloatingFlowerWrapper(SpecialFloatingFlowerRecipe recipe) {
        inputs.add(ImmutableList.of(ItemBlockSpecialFlower.ofType(recipe.type)));
        List<ItemStack> normalFloaters = new ArrayList<>();
        for (EnumDyeColor color : EnumDyeColor.values()) {
            normalFloaters.add(new ItemStack(ModBlocks.floatingFlower, 1, color.getMetadata()));
        }
        inputs.add(normalFloaters);
        output = ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), recipe.type);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, output);
    }
}
