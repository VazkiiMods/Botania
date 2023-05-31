/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data.recipes;

import com.google.gson.JsonObject;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFluffBlocks;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StonecuttingProvider extends BotaniaRecipeProvider {
	public StonecuttingProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	public void buildRecipes(Consumer<FinishedRecipe> consumer) {
		for (String variant : LibBlockNames.METAMORPHIC_VARIANTS) {
			registerForMetamorphic(variant, consumer);
		}

		for (String color : LibBlockNames.PAVEMENT_VARIANTS) {
			registerForPavement(color, consumer);
		}

		for (String variant : LibBlockNames.QUARTZ_VARIANTS) {
			registerForQuartz(variant, consumer);
		}

		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaFluffBlocks.livingrockSlab, 2));
		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaFluffBlocks.livingrockStairs));
		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaFluffBlocks.livingrockWall));
		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaBlocks.livingrockPolished));
		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaFluffBlocks.livingrockPolishedSlab, 2));
		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaFluffBlocks.livingrockPolishedStairs));
		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaFluffBlocks.livingrockPolishedWall));
		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaBlocks.livingrockBrick));
		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaFluffBlocks.livingrockBrickSlab, 2));
		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaFluffBlocks.livingrockBrickStairs));
		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaFluffBlocks.livingrockBrickWall));
		consumer.accept(stonecutting(BotaniaBlocks.livingrock, BotaniaBlocks.livingrockBrickChiseled));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockPolished, BotaniaFluffBlocks.livingrockPolishedSlab, 2));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockPolished, BotaniaFluffBlocks.livingrockPolishedStairs));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockPolished, BotaniaFluffBlocks.livingrockPolishedWall));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockPolished, BotaniaBlocks.livingrockBrick));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockPolished, BotaniaFluffBlocks.livingrockBrickSlab, 2));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockPolished, BotaniaFluffBlocks.livingrockBrickStairs));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockPolished, BotaniaFluffBlocks.livingrockBrickWall));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockPolished, BotaniaBlocks.livingrockBrickChiseled));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockBrick, BotaniaFluffBlocks.livingrockBrickSlab, 2));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockBrick, BotaniaFluffBlocks.livingrockBrickStairs));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockBrick, BotaniaFluffBlocks.livingrockBrickWall));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockBrick, BotaniaBlocks.livingrockBrickChiseled));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockBrickMossy, BotaniaFluffBlocks.livingrockBrickMossySlab, 2));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockBrickMossy, BotaniaFluffBlocks.livingrockBrickMossyStairs));
		consumer.accept(stonecutting(BotaniaBlocks.livingrockBrickMossy, BotaniaFluffBlocks.livingrockBrickMossyWall));
		consumer.accept(stonecutting(BotaniaBlocks.shimmerrock, BotaniaFluffBlocks.shimmerrockSlab, 2));
		consumer.accept(stonecutting(BotaniaBlocks.shimmerrock, BotaniaFluffBlocks.shimmerrockStairs));

		consumer.accept(stonecutting(BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaSlab, 2));
		consumer.accept(stonecutting(BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaStairs));
		consumer.accept(stonecutting(BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaBrick));
		consumer.accept(stonecutting(BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaBrickSlab, 2));
		consumer.accept(stonecutting(BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaBrickStairs));
		consumer.accept(stonecutting(BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaBrickWall));
		consumer.accept(stonecutting(BotaniaBlocks.corporeaBrick, BotaniaBlocks.corporeaBrickSlab, 2));
		consumer.accept(stonecutting(BotaniaBlocks.corporeaBrick, BotaniaBlocks.corporeaBrickStairs));
		consumer.accept(stonecutting(BotaniaBlocks.corporeaBrick, BotaniaBlocks.corporeaBrickWall));

		List<Item> allAzulejos = IntStream.range(0, 16).mapToObj(i -> "azulejo_" + i)
				.map(ResourceLocationHelper::prefix)
				.map(BuiltInRegistries.ITEM::get)
				.collect(Collectors.toList());
		for (Item azulejo : allAzulejos) {
			consumer.accept(anyToAnyStonecutting(allAzulejos, azulejo));
		}
	}

	private void registerForQuartz(String variant, Consumer<FinishedRecipe> consumer) {
		Block base = BuiltInRegistries.BLOCK.get(prefix(variant));
		Block slab = BuiltInRegistries.BLOCK.get(prefix(variant + LibBlockNames.SLAB_SUFFIX));
		Block stairs = BuiltInRegistries.BLOCK.get(prefix(variant + LibBlockNames.STAIR_SUFFIX));
		Block chiseled = BuiltInRegistries.BLOCK.get(prefix("chiseled_" + variant));
		Block pillar = BuiltInRegistries.BLOCK.get(prefix(variant + "_pillar"));
		consumer.accept(stonecutting(base, slab, 2));
		consumer.accept(stonecutting(base, stairs));
		consumer.accept(stonecutting(base, chiseled));
		consumer.accept(stonecutting(base, pillar));
	}

	private void registerForPavement(String color, Consumer<FinishedRecipe> consumer) {
		Block base = BuiltInRegistries.BLOCK.get(prefix(color + LibBlockNames.PAVEMENT_SUFFIX));
		Block slab = BuiltInRegistries.BLOCK.get(prefix(color + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX));
		Block stair = BuiltInRegistries.BLOCK.get(prefix(color + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIR_SUFFIX));
		consumer.accept(stonecutting(base, slab, 2));
		consumer.accept(stonecutting(base, stair));
	}

	private void registerForMetamorphic(String variant, Consumer<FinishedRecipe> consumer) {
		Block base = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone"));
		Block slab = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.SLAB_SUFFIX));
		Block stair = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.STAIR_SUFFIX));
		Block wall = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.WALL_SUFFIX));
		Block brick = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks"));
		Block brickSlab = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.SLAB_SUFFIX));
		Block brickStair = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.STAIR_SUFFIX));
		Block brickWall = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.WALL_SUFFIX));
		Block chiseledBrick = BuiltInRegistries.BLOCK.get(prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks"));
		Block cobble = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone"));
		Block cobbleSlab = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.SLAB_SUFFIX));
		Block cobbleStair = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.STAIR_SUFFIX));
		Block cobbleWall = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.WALL_SUFFIX));

		consumer.accept(stonecutting(base, slab, 2));
		consumer.accept(stonecutting(base, stair));
		consumer.accept(stonecutting(base, wall));
		consumer.accept(stonecutting(base, brick));
		consumer.accept(stonecutting(base, brickSlab, 2));
		consumer.accept(stonecutting(base, brickStair));
		consumer.accept(stonecutting(base, brickWall));
		consumer.accept(stonecutting(base, chiseledBrick));

		consumer.accept(stonecutting(brick, brickSlab, 2));
		consumer.accept(stonecutting(brick, brickStair));
		consumer.accept(stonecutting(brick, brickWall));
		consumer.accept(stonecutting(brick, chiseledBrick));

		consumer.accept(stonecutting(cobble, cobbleSlab, 2));
		consumer.accept(stonecutting(cobble, cobbleStair));
		consumer.accept(stonecutting(cobble, cobbleWall));
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania stonecutting recipes";
	}

	protected ResourceLocation idFor(ItemLike a, ItemLike b) {
		ResourceLocation aId = BuiltInRegistries.ITEM.getKey(a.asItem());
		ResourceLocation bId = BuiltInRegistries.ITEM.getKey(b.asItem());
		return prefix("stonecutting/" + aId.getPath() + "_to_" + bId.getPath());
	}

	protected FinishedRecipe stonecutting(ItemLike input, ItemLike output) {
		return stonecutting(input, output, 1);
	}

	protected FinishedRecipe stonecutting(ItemLike input, ItemLike output, int count) {
		return new Result(idFor(input, output), RecipeSerializer.STONECUTTER, Ingredient.of(input), output.asItem(), count);
	}

	protected FinishedRecipe anyToAnyStonecutting(List<? extends ItemLike> inputs, ItemLike output) {
		Ingredient input = Ingredient.of(inputs.stream().filter(obj -> output != obj).toArray(ItemLike[]::new));
		return new Result(prefix("stonecutting/" + BuiltInRegistries.ITEM.getKey(output.asItem()).getPath()), RecipeSerializer.STONECUTTER, input, output.asItem(), 1);
	}

	protected ResourceLocation prefix(String path) {
		return ResourceLocationHelper.prefix(path);
	}

	// Wrapper without advancements
	public static class Result extends SingleItemRecipeBuilder.Result {
		public Result(ResourceLocation id, RecipeSerializer<?> serializer, Ingredient input, Item result, int count) {
			super(id, serializer, "", input, result, count, null, null);
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}
}
