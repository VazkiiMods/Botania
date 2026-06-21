/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */
package vazkii.botania.fabric.data.xplat;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.data.recipes.BotaniaRecipeProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class StonecuttingProvider extends BotaniaRecipeProvider {
	public StonecuttingProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	public void buildRecipes(RecipeOutput consumer) {
		for (String variant : LibBlockNames.METAMORPHIC_VARIANTS) {
			registerForMetamorphic(variant, consumer);
		}

		for (String color : LibBlockNames.PAVEMENT_VARIANTS) {
			registerForPavement(color, consumer);
		}

		for (String variant : LibBlockNames.QUARTZ_VARIANTS) {
			registerForQuartz(variant, consumer);
		}

		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.LIVINGROCK_SLAB, 2);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.LIVINGROCK_STAIRS);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.LIVINGROCK_WALL);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.POLISHED_LIVINGROCK);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.POLISHED_LIVINGROCK_SLAB, 2);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.POLISHED_LIVINGROCK_STAIRS);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.POLISHED_LIVINGROCK_WALL);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.LIVINGROCK_BRICKS);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.LIVINGROCK_BRICK_SLAB, 2);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.LIVINGROCK_BRICK_STAIRS);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.LIVINGROCK_BRICK_WALL);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.CHISELED_LIVINGROCK_BRICKS);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK, BotaniaBlocks.LIVINGROCK_SLATE);
		stonecutting(consumer, BotaniaBlocks.POLISHED_LIVINGROCK, BotaniaBlocks.POLISHED_LIVINGROCK_SLAB, 2);
		stonecutting(consumer, BotaniaBlocks.POLISHED_LIVINGROCK, BotaniaBlocks.POLISHED_LIVINGROCK_STAIRS);
		stonecutting(consumer, BotaniaBlocks.POLISHED_LIVINGROCK, BotaniaBlocks.POLISHED_LIVINGROCK_WALL);
		stonecutting(consumer, BotaniaBlocks.POLISHED_LIVINGROCK, BotaniaBlocks.LIVINGROCK_BRICKS);
		stonecutting(consumer, BotaniaBlocks.POLISHED_LIVINGROCK, BotaniaBlocks.LIVINGROCK_BRICK_SLAB, 2);
		stonecutting(consumer, BotaniaBlocks.POLISHED_LIVINGROCK, BotaniaBlocks.LIVINGROCK_BRICK_STAIRS);
		stonecutting(consumer, BotaniaBlocks.POLISHED_LIVINGROCK, BotaniaBlocks.LIVINGROCK_BRICK_WALL);
		stonecutting(consumer, BotaniaBlocks.POLISHED_LIVINGROCK, BotaniaBlocks.CHISELED_LIVINGROCK_BRICKS);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK_BRICKS, BotaniaBlocks.LIVINGROCK_BRICK_SLAB, 2);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK_BRICKS, BotaniaBlocks.LIVINGROCK_BRICK_STAIRS);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK_BRICKS, BotaniaBlocks.LIVINGROCK_BRICK_WALL);
		stonecutting(consumer, BotaniaBlocks.LIVINGROCK_BRICKS, BotaniaBlocks.CHISELED_LIVINGROCK_BRICKS);
		stonecutting(consumer, BotaniaBlocks.MOSSY_LIVINGROCK_BRICKS, BotaniaBlocks.MOSSY_LIVINGROCK_BRICK_SLAB, 2);
		stonecutting(consumer, BotaniaBlocks.MOSSY_LIVINGROCK_BRICKS, BotaniaBlocks.MOSSY_LIVINGROCK_BRICK_STAIRS);
		stonecutting(consumer, BotaniaBlocks.MOSSY_LIVINGROCK_BRICKS, BotaniaBlocks.MOSSY_LIVINGROCK_BRICK_WALL);
		stonecutting(consumer, BotaniaBlocks.SHIMMERROCK, BotaniaBlocks.SHIMMERROCK_SLAB, 2);
		stonecutting(consumer, BotaniaBlocks.SHIMMERROCK, BotaniaBlocks.SHIMMERROCK_STAIRS);
		stonecutting(consumer, BotaniaBlocks.SHIMMERROCK, BotaniaBlocks.SHIMMERROCK_WALL);

		stonecutting(consumer, BotaniaBlocks.CORPOREA_BLOCK, BotaniaBlocks.CORPOREA_SLAB, 2);
		stonecutting(consumer, BotaniaBlocks.CORPOREA_BLOCK, BotaniaBlocks.CORPOREA_STAIRS);
		stonecutting(consumer, BotaniaBlocks.CORPOREA_BLOCK, BotaniaBlocks.CORPOREA_WALL);
		stonecutting(consumer, BotaniaBlocks.CORPOREA_BLOCK, BotaniaBlocks.CORPOREA_BRICKS);
		stonecutting(consumer, BotaniaBlocks.CORPOREA_BLOCK, BotaniaBlocks.CORPOREA_BRICK_SLAB, 2);
		stonecutting(consumer, BotaniaBlocks.CORPOREA_BLOCK, BotaniaBlocks.CORPOREA_BRICK_STAIRS);
		stonecutting(consumer, BotaniaBlocks.CORPOREA_BLOCK, BotaniaBlocks.CORPOREA_BRICK_WALL);
		stonecutting(consumer, BotaniaBlocks.CORPOREA_BRICKS, BotaniaBlocks.CORPOREA_BRICK_SLAB, 2);
		stonecutting(consumer, BotaniaBlocks.CORPOREA_BRICKS, BotaniaBlocks.CORPOREA_BRICK_STAIRS);
		stonecutting(consumer, BotaniaBlocks.CORPOREA_BRICKS, BotaniaBlocks.CORPOREA_BRICK_WALL);

		List<Item> allAzulejos = IntStream.range(0, 16).mapToObj(i -> "azulejo_" + i)
				.map(BotaniaAPI::botaniaRL)
				.map(BuiltInRegistries.ITEM::get)
				.collect(Collectors.toList());
		anyToAnyStonecutting(consumer, allAzulejos);
	}

	private void registerForQuartz(String variant, RecipeOutput consumer) {
		Block base = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.BLOCK_SUFFIX));
		Block slab = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.SLAB_SUFFIX));
		Block stairs = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.STAIRS_SUFFIX));
		Block chiseled = BuiltInRegistries.BLOCK.get(botaniaRL(LibBlockNames.CHISELED_PREFIX + variant + LibBlockNames.BLOCK_SUFFIX));
		Block pillar = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.PILLAR_SUFFIX));
		stonecutting(consumer, base, slab, 2);
		stonecutting(consumer, base, stairs);
		stonecutting(consumer, base, chiseled);
		stonecutting(consumer, base, pillar);

		// TODO: smooth block to stairs and slab recipes
	}

	private void registerForPavement(String color, RecipeOutput consumer) {
		Block base = BuiltInRegistries.BLOCK.get(botaniaRL(color + LibBlockNames.PAVEMENT_SUFFIX));
		Block slab = BuiltInRegistries.BLOCK.get(botaniaRL(color + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX));
		Block stair = BuiltInRegistries.BLOCK.get(botaniaRL(color + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIRS_SUFFIX));
		stonecutting(consumer, base, slab, 2);
		stonecutting(consumer, base, stair);
	}

	private void registerForMetamorphic(String variant, RecipeOutput consumer) {
		Block base = BuiltInRegistries.BLOCK.get(botaniaRL(variant));
		Block slab = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.SLAB_SUFFIX));
		Block stair = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.STAIRS_SUFFIX));
		Block wall = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.WALL_SUFFIX));
		Block brick = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.BRICKS_SUFFIX));
		Block brickSlab = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX));
		Block brickStair = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX));
		Block brickWall = BuiltInRegistries.BLOCK.get(botaniaRL(variant + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX));
		Block chiseledBrick = BuiltInRegistries.BLOCK.get(botaniaRL(LibBlockNames.CHISELED_PREFIX + variant + LibBlockNames.BRICKS_SUFFIX));
		Block cobble = BuiltInRegistries.BLOCK.get(botaniaRL(LibBlockNames.COBBLED_PREFIX + variant));
		Block cobbleSlab = BuiltInRegistries.BLOCK.get(botaniaRL(LibBlockNames.COBBLED_PREFIX + variant + LibBlockNames.SLAB_SUFFIX));
		Block cobbleStair = BuiltInRegistries.BLOCK.get(botaniaRL(LibBlockNames.COBBLED_PREFIX + variant + LibBlockNames.STAIRS_SUFFIX));
		Block cobbleWall = BuiltInRegistries.BLOCK.get(botaniaRL(LibBlockNames.COBBLED_PREFIX + variant + LibBlockNames.WALL_SUFFIX));

		stonecutting(consumer, base, slab, 2);
		stonecutting(consumer, base, stair);
		stonecutting(consumer, base, wall);
		stonecutting(consumer, base, brick);
		stonecutting(consumer, base, brickSlab, 2);
		stonecutting(consumer, base, brickStair);
		stonecutting(consumer, base, brickWall);
		stonecutting(consumer, base, chiseledBrick);

		stonecutting(consumer, brick, brickSlab, 2);
		stonecutting(consumer, brick, brickStair);
		stonecutting(consumer, brick, brickWall);
		stonecutting(consumer, brick, chiseledBrick);

		stonecutting(consumer, cobble, cobbleSlab, 2);
		stonecutting(consumer, cobble, cobbleStair);
		stonecutting(consumer, cobble, cobbleWall);
	}

	@Override
	public String getName() {
		return "Botania stonecutting recipes";
	}

	protected ResourceLocation idFor(ItemLike a, ItemLike b) {
		ResourceLocation aId = BuiltInRegistries.ITEM.getKey(a.asItem());
		ResourceLocation bId = BuiltInRegistries.ITEM.getKey(b.asItem());
		return botaniaRL("stonecutting/" + aId.getPath() + "_to_" + bId.getPath());
	}

	protected void stonecutting(RecipeOutput consumer, ResourceLocation id, Ingredient input, ItemLike output) {
		stonecutting(consumer, id, input, output, 1);
	}

	protected void stonecutting(RecipeOutput consumer, ResourceLocation id, Ingredient input, ItemLike output, int count) {
		ItemStack outputStack = new ItemStack(output);
		outputStack.setCount(count);
		consumer.accept(id, new StonecutterRecipe("", input, outputStack), null);
	}

	protected void stonecutting(RecipeOutput consumer, ItemLike input, ItemLike output) {
		stonecutting(consumer, input, output, 1);
	}

	protected void stonecutting(RecipeOutput consumer, ItemLike input, ItemLike output, int count) {
		ItemStack outputStack = new ItemStack(output);
		outputStack.setCount(count);
		consumer.accept(idFor(input, output), new StonecutterRecipe("", Ingredient.of(input), outputStack), null);
	}

	protected void anyToAnyStonecutting(RecipeOutput consumer, List<? extends ItemLike> inputs) {
		for (ItemLike output : inputs) {
			Ingredient input = Ingredient.of(inputs.stream().filter(thisInput -> output != thisInput).toArray(ItemLike[]::new));
			ResourceLocation id = botaniaRL("stonecutting/" + BuiltInRegistries.ITEM.getKey(output.asItem()).getPath());
			stonecutting(consumer, id, input, output);
		}
	}
}
