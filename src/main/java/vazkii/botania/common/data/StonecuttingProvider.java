package vazkii.botania.common.data;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ResourceLocationHelper;

import javax.annotation.Nullable;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class StonecuttingProvider extends RecipeProvider {
    public StonecuttingProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        for(String variant : LibBlockNames.METAMORPHIC_VARIANTS) {
            registerForMetamorphic(variant, consumer);
        }
    }

    private static void registerForMetamorphic(String variant, Consumer<IFinishedRecipe> consumer) {
        Block base = ForgeRegistries.BLOCKS.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone"));
        Block slab = ForgeRegistries.BLOCKS.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.SLAB_SUFFIX));
        Block stair = ForgeRegistries.BLOCKS.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.STAIR_SUFFIX));
        Block brick = ForgeRegistries.BLOCKS.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks"));
        Block brickSlab = ForgeRegistries.BLOCKS.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.SLAB_SUFFIX));
        Block brickStair = ForgeRegistries.BLOCKS.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.STAIR_SUFFIX));
        Block chiseledBrick = ForgeRegistries.BLOCKS.getValue(prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks"));
        Block cobble = ForgeRegistries.BLOCKS.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone"));
        Block cobbleSlab = ForgeRegistries.BLOCKS.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.SLAB_SUFFIX));
        Block cobbleStair = ForgeRegistries.BLOCKS.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.STAIR_SUFFIX));
        Block cobbleWall = ForgeRegistries.BLOCKS.getValue(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.WALL_SUFFIX));

        consumer.accept(stonecutting(base, slab, 2));
        consumer.accept(stonecutting(base, stair));
        consumer.accept(stonecutting(base, brick));
        consumer.accept(stonecutting(base, brickSlab, 2));
        consumer.accept(stonecutting(base, brickStair));
        consumer.accept(stonecutting(base, chiseledBrick));

        consumer.accept(stonecutting(brick, brickSlab, 2));
        consumer.accept(stonecutting(brick, brickStair));
        consumer.accept(stonecutting(brick, chiseledBrick));

        consumer.accept(stonecutting(cobble, cobbleSlab, 2));
        consumer.accept(stonecutting(cobble, cobbleStair));
        consumer.accept(stonecutting(cobble, cobbleWall));
    }

    private static ResourceLocation idFor(IItemProvider a, IItemProvider b) {
        return prefix("stonecutting/" + a.asItem().getRegistryName().getPath() + "_to_" + b.asItem().getRegistryName().getPath());
    }

    private static IFinishedRecipe stonecutting(IItemProvider input, IItemProvider output) {
        return stonecutting(input, output, 1);
    }

    private static IFinishedRecipe stonecutting(IItemProvider input, IItemProvider output, int count) {
        return new Result(idFor(input, output), IRecipeSerializer.STONECUTTING, Ingredient.fromItems(input), output.asItem(), count);
    }

    // Wrapper without advancements
    public static class Result extends SingleItemRecipeBuilder.Result {
        public Result(ResourceLocation id, IRecipeSerializer<?> serializer, Ingredient input, Item result, int count) {
            super(id, serializer, "", input, result, count, null, null);
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}
