/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 2, 2014, 7:50:07 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RegisterRecipesEvent;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nullable;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ModManaAlchemyRecipes {

	@SubscribeEvent
	public static void register(RegisterRecipesEvent evt) {
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("rotten_flesh_to_leather"), new ItemStack(Items.LEATHER), Ingredient.fromItems(Items.ROTTEN_FLESH), 600));

		cycle(evt.manaInfusion(), 40, "botania:log_cycle", Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG);
		cycle(evt.manaInfusion(), 120, "botania:sapling_cycle", Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING);

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("glowstone_deconstruct"), new ItemStack(Items.GLOWSTONE_DUST, 4), Ingredient.fromItems(Blocks.GLOWSTONE), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("quartz_deconstruct"), new ItemStack(Items.QUARTZ, 4), Ingredient.fromItems(Blocks.QUARTZ_BLOCK), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("dark_quartz_deconstruct"), new ItemStack(ModItems.darkQuartz, 4), Ingredient.fromItems(ModFluffBlocks.darkQuartz), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("mana_quartz_deconstruct"), new ItemStack(ModItems.manaQuartz, 4), Ingredient.fromItems(ModFluffBlocks.manaQuartz), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("blaze_quartz_deconstruct"), new ItemStack(ModItems.blazeQuartz, 4), Ingredient.fromItems(ModFluffBlocks.blazeQuartz), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("lavender_quartz_deconstruct"), new ItemStack(ModItems.lavenderQuartz, 4), Ingredient.fromItems(ModFluffBlocks.lavenderQuartz), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("red_quartz_deconstruct"), new ItemStack(ModItems.redQuartz, 4), Ingredient.fromItems(ModFluffBlocks.redQuartz), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("elf_quartz_deconstruct"), new ItemStack(ModItems.elfQuartz, 4), Ingredient.fromItems(ModFluffBlocks.elfQuartz), 25));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("chiseled_stone_bricks"), new ItemStack(Blocks.CHISELED_STONE_BRICKS, 1), Ingredient.fromItems(Blocks.STONE_BRICKS), 150));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("ice"), new ItemStack(Blocks.ICE), Ingredient.fromItems(Blocks.SNOW_BLOCK), 2250));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("vine_to_lily_pad"), new ItemStack(Blocks.LILY_PAD), Ingredient.fromItems(Blocks.VINE), 320));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("lily_pad_to_vine"), new ItemStack(Blocks.VINE), Ingredient.fromItems(Blocks.LILY_PAD), 320));

		cycle(evt.manaInfusion(), 200, "botania:fish_cycle", Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH);
		cycle(evt.manaInfusion(), 6000, "botania:crop_cycle", Items.COCOA_BEANS, Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS);

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("potato_unpoison"), new ItemStack(Items.POTATO), Ingredient.fromItems(Items.POISONOUS_POTATO), 1200));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("blaze_rod_to_nether_wart"), new ItemStack(Items.NETHER_WART), Ingredient.fromItems(Items.BLAZE_ROD), 4000));

		cycle(evt.manaInfusion(), 200, null, Items.GUNPOWDER, Items.FLINT);

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("book_to_name_tag"), new ItemStack(Items.NAME_TAG), Ingredient.fromItems(Items.WRITABLE_BOOK), 16000));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("wool_deconstruct"), new ItemStack(Items.STRING, 3), Ingredient.fromTag(ItemTags.WOOL), 100));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("cactus_to_slime"), new ItemStack(Items.SLIME_BALL), Ingredient.fromItems(Blocks.CACTUS), 1200));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("slime_to_cactus"), new ItemStack(Blocks.CACTUS), Ingredient.fromItems(Items.SLIME_BALL), 1200));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("ender_pearl_from_ghast_tear"), new ItemStack(Items.ENDER_PEARL), Ingredient.fromItems(Items.GHAST_TEAR), 28000));

		cycle(evt.manaInfusion(), 300, null, Items.GLOWSTONE_DUST, Items.REDSTONE);

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("cobble_to_sand"), new ItemStack(Blocks.SAND), Ingredient.fromItems(Blocks.COBBLESTONE), 50));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("terracotta_to_red_sand"), new ItemStack(Blocks.RED_SAND), Ingredient.fromItems(Blocks.TERRACOTTA), 50));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("clay_deconstruct"), new ItemStack(Items.CLAY_BALL, 4), Ingredient.fromItems(Blocks.CLAY), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("brick_deconstruct"), new ItemStack(Items.BRICK, 4), Ingredient.fromItems(Blocks.BRICKS), 25));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("coarse_dirt"), new ItemStack(Blocks.COARSE_DIRT), Ingredient.fromItems(Blocks.DIRT), 120));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("stone_to_andesite"), new ItemStack(Blocks.ANDESITE), Ingredient.fromItems(Blocks.STONE), 200));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("andesite_to_diorite"), new ItemStack(Blocks.DIORITE), Ingredient.fromItems(Blocks.ANDESITE), 200));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("diorite_to_granite"), new ItemStack(Blocks.GRANITE), Ingredient.fromItems(Blocks.DIORITE), 200));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("granite_to_andesite"), new ItemStack(Blocks.ANDESITE), Ingredient.fromItems(Blocks.GRANITE), 200));

		cycle(evt.manaInfusion(), 500, "botania:shrub_cycle", Blocks.FERN, Blocks.DEAD_BUSH, Blocks.GRASS);

		// NB: No wither rose is intentional
		cycle(evt.manaInfusion(), 400, "botania:flower_cycle", Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP,
				Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY,
				Blocks.SUNFLOWER, Blocks.LILAC, Blocks.ROSE_BUSH, Blocks.PEONY);

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("chorus_fruit_to_flower"), new ItemStack(Blocks.CHORUS_FLOWER), Ingredient.fromItems(Items.POPPED_CHORUS_FRUIT), 10000));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("heart_of_the_sea"), new ItemStack(Items.HEART_OF_THE_SEA), Ingredient.fromItems(Items.NAUTILUS_SHELL), 20000));

		if(Botania.gardenOfGlassLoaded) {
			evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("prismarine_shard_gog"), new ItemStack(Items.PRISMARINE_SHARD), Ingredient.fromItems(Items.QUARTZ), 1000));
			evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("prismarine_crystals_gog"), new ItemStack(Items.PRISMARINE_CRYSTALS), Ingredient.fromItems(Items.PRISMARINE_SHARD), 500));
		}

		evt.manaInfusion().accept(mini(ModSubtiles.agricarnationChibi, ModSubtiles.agricarnation));
		evt.manaInfusion().accept(mini(ModSubtiles.clayconiaChibi, ModSubtiles.clayconia));
		evt.manaInfusion().accept(mini(ModSubtiles.bellethornChibi, ModSubtiles.bellethorn));
		evt.manaInfusion().accept(mini(ModSubtiles.bubbellChibi, ModSubtiles.bubbell));
		evt.manaInfusion().accept(mini(ModSubtiles.hopperhockChibi, ModSubtiles.hopperhock));
		evt.manaInfusion().accept(mini(ModSubtiles.marimorphosisChibi, ModSubtiles.marimorphosis));
		evt.manaInfusion().accept(mini(ModSubtiles.rannuncarpusChibi, ModSubtiles.rannuncarpus));
		evt.manaInfusion().accept(mini(ModSubtiles.solegnoliaChibi, ModSubtiles.solegnolia));
	}

	private static void cycle(Consumer<RecipeManaInfusion> consumer, int cost, @Nullable String group, IItemProvider... items) {
		for(int i = 0; i < items.length; i++) {
			Ingredient in = Ingredient.fromItems(items[i]);
			ItemStack out = new ItemStack(i == items.length - 1 ? items[0] : items[i + 1]);
			String id = String.format("%s_to_%s", items[i].asItem().getRegistryName().getPath(), out.getItem().getRegistryName().getPath());
			consumer.accept(RecipeManaInfusion.alchemy(new ResourceLocation(LibMisc.MOD_ID, id), out, in, cost, group));
		}
	}
	
	private static RecipeManaInfusion mini(IItemProvider mini, IItemProvider full) {
		return RecipeManaInfusion.alchemy(mini.asItem().getRegistryName(), new ItemStack(mini), Ingredient.fromItems(full), 2500);
	}
}
