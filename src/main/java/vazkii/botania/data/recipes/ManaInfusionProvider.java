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

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.StateIngredientHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ManaInfusionProvider extends BotaniaRecipeProvider {
	public ManaInfusionProvider(DataGenerator gen) {
		super(gen);
	}

	@Override
	public String getName() {
		return "Botania mana pool recipes";
	}

	@Override
	public void registerRecipes(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer) {
		consumer.accept(new FinishedRecipe(id("manasteel"), new ItemStack(ModItems.manaSteel), Ingredient.of(Items.IRON_INGOT), 3000));
		consumer.accept(new FinishedRecipe(id("manasteel_block"), new ItemStack(ModBlocks.manasteelBlock), ingr(Blocks.IRON_BLOCK), 27000));

		consumer.accept(new FinishedRecipe(id("mana_pearl"), new ItemStack(ModItems.manaPearl), ingr(Items.ENDER_PEARL), 6000));

		consumer.accept(new FinishedRecipe(id("mana_diamond"), new ItemStack(ModItems.manaDiamond), Ingredient.of(Items.DIAMOND), 10000));
		consumer.accept(new FinishedRecipe(id("mana_diamond_block"), new ItemStack(ModBlocks.manaDiamondBlock), ingr(Blocks.DIAMOND_BLOCK), 90000));

		Ingredient dust = Ingredient.of(Items.GUNPOWDER, Items.REDSTONE, Items.GLOWSTONE_DUST, Items.SUGAR);
		consumer.accept(new FinishedRecipe(id("mana_powder_dust"), new ItemStack(ModItems.manaPowder), dust, 500));
		Ingredient dyeIngredient = Ingredient.of(Arrays.stream(DyeColor.values()).map(DyeItem::byColor).toArray(Item[]::new));
		consumer.accept(new FinishedRecipe(id("mana_powder_dye"), new ItemStack(ModItems.manaPowder), dyeIngredient, 400));

		consumer.accept(new FinishedRecipe(id("piston_relay"), new ItemStack(ModBlocks.pistonRelay), ingr(Blocks.PISTON), 15000));
		consumer.accept(new FinishedRecipe(id("mana_cookie"), new ItemStack(ModItems.manaCookie), ingr(Items.COOKIE), 20000));
		consumer.accept(new FinishedRecipe(id("grass_seeds"), new ItemStack(ModItems.grassSeeds), ingr(Blocks.GRASS), 2500));
		consumer.accept(new FinishedRecipe(id("podzol_seeds"), new ItemStack(ModItems.podzolSeeds), ingr(Blocks.DEAD_BUSH), 2500));

		consumer.accept(new FinishedRecipe(id("mycel_seeds"), new ItemStack(ModItems.mycelSeeds), Ingredient.of(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM), 6500));

		consumer.accept(new FinishedRecipe(id("mana_quartz"), new ItemStack(ModItems.manaQuartz), ingr(Items.QUARTZ), 250));
		consumer.accept(new FinishedRecipe(id("tiny_potato"), new ItemStack(ModBlocks.tinyPotato), ingr(Items.POTATO), 1337));

		consumer.accept(new FinishedRecipe(id("mana_glass"), new ItemStack(ModBlocks.manaGlass), ingr(Blocks.GLASS), 150));
		consumer.accept(new FinishedRecipe(id("mana_string"), new ItemStack(ModItems.manaString), ingr(Items.STRING), 5000));

		consumer.accept(new FinishedRecipe(id("mana_bottle"), new ItemStack(ModItems.manaBottle), ingr(Items.GLASS_BOTTLE), 5000));

		consumer.accept(FinishedRecipe.alchemy(id("rotten_flesh_to_leather"), new ItemStack(Items.LEATHER), ingr(Items.ROTTEN_FLESH), 600));

		cycle(consumer, 40, "botania:log_cycle", Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG);
		cycle(consumer, 120, "botania:sapling_cycle", Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING);

		consumer.accept(FinishedRecipe.alchemy(id("glowstone_deconstruct"), new ItemStack(Items.GLOWSTONE_DUST, 4), ingr(Blocks.GLOWSTONE), 25));
		consumer.accept(FinishedRecipe.alchemy(id("quartz_deconstruct"), new ItemStack(Items.QUARTZ, 4), ingr(Blocks.QUARTZ_BLOCK), 25));
		consumer.accept(FinishedRecipe.alchemy(id("dark_quartz_deconstruct"), new ItemStack(ModItems.darkQuartz, 4), ingr(ModFluffBlocks.darkQuartz), 25));
		consumer.accept(FinishedRecipe.alchemy(id("mana_quartz_deconstruct"), new ItemStack(ModItems.manaQuartz, 4), ingr(ModFluffBlocks.manaQuartz), 25));
		consumer.accept(FinishedRecipe.alchemy(id("blaze_quartz_deconstruct"), new ItemStack(ModItems.blazeQuartz, 4), ingr(ModFluffBlocks.blazeQuartz), 25));
		consumer.accept(FinishedRecipe.alchemy(id("lavender_quartz_deconstruct"), new ItemStack(ModItems.lavenderQuartz, 4), ingr(ModFluffBlocks.lavenderQuartz), 25));
		consumer.accept(FinishedRecipe.alchemy(id("red_quartz_deconstruct"), new ItemStack(ModItems.redQuartz, 4), ingr(ModFluffBlocks.redQuartz), 25));
		consumer.accept(FinishedRecipe.alchemy(id("elf_quartz_deconstruct"), new ItemStack(ModItems.elfQuartz, 4), ingr(ModFluffBlocks.elfQuartz), 25));

		consumer.accept(FinishedRecipe.alchemy(id("chiseled_stone_bricks"), new ItemStack(Blocks.CHISELED_STONE_BRICKS, 1), ingr(Blocks.STONE_BRICKS), 150));
		consumer.accept(FinishedRecipe.alchemy(id("ice"), new ItemStack(Blocks.ICE), ingr(Blocks.SNOW_BLOCK), 2250));

		consumer.accept(FinishedRecipe.alchemy(id("vine_to_lily_pad"), new ItemStack(Blocks.LILY_PAD), ingr(Blocks.VINE), 320));
		consumer.accept(FinishedRecipe.alchemy(id("lily_pad_to_vine"), new ItemStack(Blocks.VINE), ingr(Blocks.LILY_PAD), 320));

		cycle(consumer, 200, "botania:fish_cycle", Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH);
		cycle(consumer, 6000, "botania:crop_cycle", Items.COCOA_BEANS, Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS);

		consumer.accept(FinishedRecipe.alchemy(id("potato_unpoison"), new ItemStack(Items.POTATO), ingr(Items.POISONOUS_POTATO), 1200));
		consumer.accept(FinishedRecipe.alchemy(id("blaze_rod_to_nether_wart"), new ItemStack(Items.NETHER_WART), ingr(Items.BLAZE_ROD), 4000));

		cycle(consumer, 200, "", Items.GUNPOWDER, Items.FLINT);

		consumer.accept(FinishedRecipe.alchemy(id("book_to_name_tag"), new ItemStack(Items.NAME_TAG), ingr(Items.WRITABLE_BOOK), 6000));

		consumer.accept(FinishedRecipe.alchemy(id("wool_deconstruct"), new ItemStack(Items.STRING, 3), Ingredient.of(ItemTags.WOOL), 100));

		consumer.accept(FinishedRecipe.alchemy(id("cactus_to_slime"), new ItemStack(Items.SLIME_BALL), ingr(Blocks.CACTUS), 1200));
		consumer.accept(FinishedRecipe.alchemy(id("slime_to_cactus"), new ItemStack(Blocks.CACTUS), ingr(Items.SLIME_BALL), 1200));

		consumer.accept(FinishedRecipe.alchemy(id("ender_pearl_from_ghast_tear"), new ItemStack(Items.ENDER_PEARL), ingr(Items.GHAST_TEAR), 28000));

		cycle(consumer, 300, "", Items.GLOWSTONE_DUST, Items.REDSTONE);

		consumer.accept(FinishedRecipe.alchemy(id("cobble_to_sand"), new ItemStack(Blocks.SAND), ingr(Blocks.COBBLESTONE), 50));
		consumer.accept(FinishedRecipe.alchemy(id("terracotta_to_red_sand"), new ItemStack(Blocks.RED_SAND), ingr(Blocks.TERRACOTTA), 50));

		consumer.accept(FinishedRecipe.alchemy(id("clay_deconstruct"), new ItemStack(Items.CLAY_BALL, 4), ingr(Blocks.CLAY), 25));
		consumer.accept(FinishedRecipe.alchemy(id("brick_deconstruct"), new ItemStack(Items.BRICK, 4), ingr(Blocks.BRICKS), 25));

		consumer.accept(FinishedRecipe.alchemy(id("coarse_dirt"), new ItemStack(Blocks.COARSE_DIRT), ingr(Blocks.DIRT), 120));

		consumer.accept(FinishedRecipe.alchemy(id("stone_to_andesite"), new ItemStack(Blocks.ANDESITE), ingr(Blocks.STONE), 200));
		consumer.accept(FinishedRecipe.alchemy(id("andesite_to_diorite"), new ItemStack(Blocks.DIORITE), ingr(Blocks.ANDESITE), 200));
		consumer.accept(FinishedRecipe.alchemy(id("diorite_to_granite"), new ItemStack(Blocks.GRANITE), ingr(Blocks.DIORITE), 200));
		consumer.accept(FinishedRecipe.alchemy(id("granite_to_andesite"), new ItemStack(Blocks.ANDESITE), ingr(Blocks.GRANITE), 200));

		cycle(consumer, 200, "botania:117_stone_cycle", Blocks.TUFF, Blocks.CALCITE, Blocks.DEEPSLATE);

		cycle(consumer, 500, "botania:shrub_cycle", Blocks.FERN, Blocks.DEAD_BUSH, Blocks.GRASS);

		// NB: No wither rose is intentional
		cycle(consumer, 400, "botania:flower_cycle", Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP,
				Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY,
				Blocks.SUNFLOWER, Blocks.LILAC, Blocks.ROSE_BUSH, Blocks.PEONY);

		consumer.accept(FinishedRecipe.alchemy(id("chorus_fruit_to_flower"), new ItemStack(Blocks.CHORUS_FLOWER), ingr(Items.POPPED_CHORUS_FRUIT), 10000));

		cycle(consumer, 240, "botania:berry_cycle", Items.APPLE, Items.SWEET_BERRIES, Items.GLOW_BERRIES);

		consumer.accept(mini(ModSubtiles.agricarnationChibi, ModSubtiles.agricarnation));
		consumer.accept(mini(ModSubtiles.clayconiaChibi, ModSubtiles.clayconia));
		consumer.accept(mini(ModSubtiles.bellethornChibi, ModSubtiles.bellethorn));
		consumer.accept(mini(ModSubtiles.bubbellChibi, ModSubtiles.bubbell));
		consumer.accept(mini(ModSubtiles.hopperhockChibi, ModSubtiles.hopperhock));
		consumer.accept(mini(ModSubtiles.marimorphosisChibi, ModSubtiles.marimorphosis));
		consumer.accept(mini(ModSubtiles.rannuncarpusChibi, ModSubtiles.rannuncarpus));
		consumer.accept(mini(ModSubtiles.solegnoliaChibi, ModSubtiles.solegnolia));

		consumer.accept(FinishedRecipe.alchemy(id("hydroangeas_motif"), new ItemStack(ModBlocks.motifHydroangeas), ingr(ModSubtiles.hydroangeas), 2500));

		consumer.accept(FinishedRecipe.conjuration(id("redstone_dupe"), new ItemStack(Items.REDSTONE, 2), ingr(Items.REDSTONE), 5000));
		consumer.accept(FinishedRecipe.conjuration(id("glowstone_dupe"), new ItemStack(Items.GLOWSTONE_DUST, 2), ingr(Items.GLOWSTONE_DUST), 5000));
		consumer.accept(FinishedRecipe.conjuration(id("quartz_dupe"), new ItemStack(Items.QUARTZ, 2), ingr(Items.QUARTZ), 2500));
		consumer.accept(FinishedRecipe.conjuration(id("coal_dupe"), new ItemStack(Items.COAL, 2), ingr(Items.COAL), 2100));
		consumer.accept(FinishedRecipe.conjuration(id("snowball_dupe"), new ItemStack(Items.SNOWBALL, 2), ingr(Items.SNOWBALL), 200));
		consumer.accept(FinishedRecipe.conjuration(id("netherrack_dupe"), new ItemStack(Blocks.NETHERRACK, 2), ingr(Blocks.NETHERRACK), 200));
		consumer.accept(FinishedRecipe.conjuration(id("soul_sand_dupe"), new ItemStack(Blocks.SOUL_SAND, 2), ingr(Blocks.SOUL_SAND), 1500));
		consumer.accept(FinishedRecipe.conjuration(id("gravel_dupe"), new ItemStack(Blocks.GRAVEL, 2), ingr(Blocks.GRAVEL), 720));

		consumer.accept(FinishedRecipe.conjuration(id("oak_leaves_dupe"), new ItemStack(Blocks.OAK_LEAVES, 2), ingr(Blocks.OAK_LEAVES), 2000));
		consumer.accept(FinishedRecipe.conjuration(id("birch_leaves_dupe"), new ItemStack(Blocks.BIRCH_LEAVES, 2), ingr(Blocks.BIRCH_LEAVES), 2000));
		consumer.accept(FinishedRecipe.conjuration(id("spruce_leaves_dupe"), new ItemStack(Blocks.SPRUCE_LEAVES, 2), ingr(Blocks.SPRUCE_LEAVES), 2000));
		consumer.accept(FinishedRecipe.conjuration(id("jungle_leaves_dupe"), new ItemStack(Blocks.JUNGLE_LEAVES, 2), ingr(Blocks.JUNGLE_LEAVES), 2000));
		consumer.accept(FinishedRecipe.conjuration(id("acacia_leaves_dupe"), new ItemStack(Blocks.ACACIA_LEAVES, 2), ingr(Blocks.ACACIA_LEAVES), 2000));
		consumer.accept(FinishedRecipe.conjuration(id("dark_oak_leaves_dupe"), new ItemStack(Blocks.DARK_OAK_LEAVES, 2), ingr(Blocks.DARK_OAK_LEAVES), 2000));
		consumer.accept(FinishedRecipe.conjuration(id("azalea_leaves_dupe"), new ItemStack(Blocks.AZALEA_LEAVES, 2), ingr(Blocks.AZALEA_LEAVES), 2000));
		consumer.accept(FinishedRecipe.conjuration(id("flowering_azalea_leaves_dupe"), new ItemStack(Blocks.FLOWERING_AZALEA_LEAVES, 2), ingr(Blocks.FLOWERING_AZALEA_LEAVES), 2000));

		consumer.accept(FinishedRecipe.conjuration(id("grass"), new ItemStack(Blocks.GRASS, 2), ingr(Blocks.GRASS), 800));
	}

	protected void cycle(Consumer<net.minecraft.data.recipes.FinishedRecipe> consumer, int cost, String group, ItemLike... items) {
		for (int i = 0; i < items.length; i++) {
			Ingredient in = ingr(items[i]);
			ItemStack out = new ItemStack(i == items.length - 1 ? items[0] : items[i + 1]);
			String id = String.format("%s_to_%s", Registry.ITEM.getKey(items[i].asItem()).getPath(), Registry.ITEM.getKey(out.getItem()).getPath());
			consumer.accept(FinishedRecipe.alchemy(id(id), out, in, cost, group));
		}
	}

	protected FinishedRecipe mini(ItemLike mini, ItemLike full) {
		return FinishedRecipe.alchemy(id(Registry.ITEM.getKey(mini.asItem()).getPath()), new ItemStack(mini), ingr(full), 2500, "botania:flower_shrinking");
	}

	protected ResourceLocation id(String s) {
		return prefix("mana_infusion/" + s);
	}

	protected static Ingredient ingr(ItemLike i) {
		return Ingredient.of(i);
	}

	protected static class FinishedRecipe implements net.minecraft.data.recipes.FinishedRecipe {
		private static final StateIngredient CONJURATION = StateIngredientHelper.of(ModBlocks.conjurationCatalyst);
		private static final StateIngredient ALCHEMY = StateIngredientHelper.of(ModBlocks.alchemyCatalyst);

		private final ResourceLocation id;
		private final Ingredient input;
		private final ItemStack output;
		private final int mana;
		private final String group;
		@Nullable
		private final StateIngredient catalyst;

		public static FinishedRecipe conjuration(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
			return new FinishedRecipe(id, output, input, mana, "", CONJURATION);
		}

		public static FinishedRecipe alchemy(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
			return alchemy(id, output, input, mana, "");
		}

		public static FinishedRecipe alchemy(ResourceLocation id, ItemStack output, Ingredient input, int mana, String group) {
			return new FinishedRecipe(id, output, input, mana, group, ALCHEMY);
		}

		public FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient input, int mana) {
			this(id, output, input, mana, "");
		}

		public FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient input, int mana, String group) {
			this(id, output, input, mana, group, null);
		}

		public FinishedRecipe(ResourceLocation id, ItemStack output, Ingredient input, int mana, String group, @Nullable StateIngredient catalyst) {
			this.id = id;
			this.input = input;
			this.output = output;
			this.mana = mana;
			this.group = group;
			this.catalyst = catalyst;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			json.add("input", input.toJson());
			json.add("output", ItemNBTHelper.serializeStack(output));
			json.addProperty("mana", mana);
			if (!group.isEmpty()) {
				json.addProperty("group", group);
			}
			if (catalyst != null) {
				json.add("catalyst", catalyst.serialize());
			}
		}

		@Override
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ModRecipeTypes.MANA_INFUSION_SERIALIZER;
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
