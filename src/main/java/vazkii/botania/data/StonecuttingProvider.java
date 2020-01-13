package vazkii.botania.data;

import com.google.gson.JsonObject;
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
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.ResourceLocationHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        for(String color : LibBlockNames.PAVEMENT_VARIANTS) {
            registerForPavement(color, consumer);
        }

        for(String variant : LibBlockNames.QUARTZ_VARIANTS) {
            registerForQuartz(variant, consumer);
        }

        consumer.accept(stonecutting(ModBlocks.shimmerrock, ModFluffBlocks.shimmerrockSlab, 2));
        consumer.accept(stonecutting(ModBlocks.shimmerrock, ModFluffBlocks.shimmerrockStairs));
        consumer.accept(stonecutting(ModBlocks.livingrock, ModFluffBlocks.livingrockSlab, 2));
        consumer.accept(stonecutting(ModBlocks.livingrock, ModFluffBlocks.livingrockStairs));
        consumer.accept(stonecutting(ModBlocks.livingrock, ModFluffBlocks.livingrockWall));
        consumer.accept(stonecutting(ModBlocks.livingrock, ModBlocks.livingrockBrick));
        consumer.accept(stonecutting(ModBlocks.livingrock, ModFluffBlocks.livingrockBrickSlab, 2));
        consumer.accept(stonecutting(ModBlocks.livingrock, ModFluffBlocks.livingrockBrickStairs));
        consumer.accept(stonecutting(ModBlocks.livingrock, ModBlocks.livingrockBrickChiseled));
        consumer.accept(stonecutting(ModBlocks.livingrockBrick, ModFluffBlocks.livingrockBrickSlab, 2));
        consumer.accept(stonecutting(ModBlocks.livingrockBrick, ModFluffBlocks.livingrockBrickStairs));
        consumer.accept(stonecutting(ModBlocks.livingrockBrick, ModBlocks.livingrockBrickChiseled));
        
        List<Item> allAzulejos = IntStream.range(0, 16).mapToObj(i -> "azulejo_" + i)
                .map(ResourceLocationHelper::prefix)
                .map(ForgeRegistries.ITEMS::getValue).collect(Collectors.toList());
		for (Item azulejo : allAzulejos) {
			consumer.accept(azulejoStonecutting(allAzulejos, azulejo));
		}
    }

    private static void registerForQuartz(String variant, Consumer<IFinishedRecipe> consumer) {
        Block base = ForgeRegistries.BLOCKS.getValue(prefix(variant));
        Block slab = ForgeRegistries.BLOCKS.getValue(prefix(variant + LibBlockNames.SLAB_SUFFIX));
        Block stairs = ForgeRegistries.BLOCKS.getValue(prefix(variant + LibBlockNames.STAIR_SUFFIX));
        Block chiseled = ForgeRegistries.BLOCKS.getValue(prefix("chiseled_" + variant));
        Block pillar = ForgeRegistries.BLOCKS.getValue(prefix(variant + "_pillar"));
        consumer.accept(stonecutting(base, slab, 2));
        consumer.accept(stonecutting(base, stairs));
        consumer.accept(stonecutting(base, chiseled));
        consumer.accept(stonecutting(base, pillar));
    }

    private static void registerForPavement(String color, Consumer<IFinishedRecipe> consumer) {
        Block base = ForgeRegistries.BLOCKS.getValue(prefix(color + LibBlockNames.PAVEMENT_SUFFIX));
        Block slab = ForgeRegistries.BLOCKS.getValue(prefix(color + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX));
        Block stair = ForgeRegistries.BLOCKS.getValue(prefix(color + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIR_SUFFIX));
        consumer.accept(stonecutting(base, slab, 2));
        consumer.accept(stonecutting(base, stair));
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

    @Nonnull
    @Override
    public String getName() {
        return "Botania stonecutting recipes";
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

    private static IFinishedRecipe azulejoStonecutting(List<? extends IItemProvider> inputs, IItemProvider output) {
		Ingredient input = Ingredient.fromItems(inputs.stream().filter(obj -> output != obj).toArray(IItemProvider[]::new));
		return new Result(prefix("stonecutting/" + output.asItem().getRegistryName().getPath()), IRecipeSerializer.STONECUTTING, input, output.asItem(), 1);
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
