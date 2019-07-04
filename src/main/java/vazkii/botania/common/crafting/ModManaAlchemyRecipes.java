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
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.imc.IMC;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RegisterRecipesEvent;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.core.handler.IMCSender;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ModManaAlchemyRecipes {

	public static RecipeManaInfusion leatherRecipe;
	public static List<RecipeManaInfusion> woodRecipes;
	public static List<RecipeManaInfusion> saplingRecipes;
	public static RecipeManaInfusion glowstoneDustRecipe;
	public static List<RecipeManaInfusion> quartzRecipes;
	public static RecipeManaInfusion chiseledBrickRecipe;
	public static RecipeManaInfusion iceRecipe;
	public static List<RecipeManaInfusion> swampFolliageRecipes;
	public static List<RecipeManaInfusion> fishRecipes;
	public static List<RecipeManaInfusion> cropRecipes;
	public static RecipeManaInfusion potatoRecipe;
	public static RecipeManaInfusion netherWartRecipe;
	public static List<RecipeManaInfusion> gunpowderAndFlintRecipes;
	public static RecipeManaInfusion nameTagRecipe;
	public static List<RecipeManaInfusion> stringRecipes;
	public static List<RecipeManaInfusion> slimeballCactusRecipes;
	public static RecipeManaInfusion enderPearlRecipe;
	public static List<RecipeManaInfusion> redstoneToGlowstoneRecipes;
	public static RecipeManaInfusion sandRecipe;
	public static RecipeManaInfusion redSandRecipe;
	public static List<RecipeManaInfusion> clayBreakdownRecipes;
	public static RecipeManaInfusion coarseDirtRecipe;
	public static List<RecipeManaInfusion> stoneRecipes;
	public static List<RecipeManaInfusion> tallgrassRecipes;
	public static List<RecipeManaInfusion> flowersRecipes;
	public static RecipeManaInfusion chorusRecipe;

	// Garden of Glass
	public static List<RecipeManaInfusion> prismarineRecipes;

	@SubscribeEvent
	public static void register(RegisterRecipesEvent evt) {
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("rotten_flesh_to_leather"), new ItemStack(Items.LEATHER), Ingredient.fromItems(Items.ROTTEN_FLESH), 600));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("oak_to_spruce_log"), new ItemStack(Blocks.SPRUCE_LOG), Ingredient.fromItems(Blocks.OAK_LOG), 40));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("spruce_to_birch_log"), new ItemStack(Blocks.BIRCH_LOG), Ingredient.fromItems(Blocks.SPRUCE_LOG), 40));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("birch_to_jungle_log"), new ItemStack(Blocks.JUNGLE_LOG), Ingredient.fromItems(Blocks.BIRCH_LOG), 40));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("jungle_to_acacia_log"), new ItemStack(Blocks.ACACIA_LOG), Ingredient.fromItems(Blocks.JUNGLE_LOG), 40));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("acacia_to_dark_oak_log"), new ItemStack(Blocks.DARK_OAK_LOG), Ingredient.fromItems(Blocks.ACACIA_LOG), 40));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("dark_oak_to_oak_log"), new ItemStack(Blocks.OAK_LOG), Ingredient.fromItems(Blocks.DARK_OAK_LOG), 40));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("oak_to_spruce_sapling"), new ItemStack(Blocks.SPRUCE_SAPLING), Ingredient.fromItems(Blocks.OAK_SAPLING), 120));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("spruce_to_birch_sapling"), new ItemStack(Blocks.BIRCH_SAPLING), Ingredient.fromItems(Blocks.SPRUCE_SAPLING), 120));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("birch_to_jungle_sapling"), new ItemStack(Blocks.JUNGLE_SAPLING), Ingredient.fromItems(Blocks.BIRCH_SAPLING), 120));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("jungle_to_acacia_sapling"), new ItemStack(Blocks.ACACIA_SAPLING), Ingredient.fromItems(Blocks.JUNGLE_SAPLING), 120));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("acacia_to_dark_oak_sapling"), new ItemStack(Blocks.DARK_OAK_SAPLING), Ingredient.fromItems(Blocks.ACACIA_SAPLING), 120));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("dark_oak_to_oak_sapling"), new ItemStack(Blocks.OAK_SAPLING), Ingredient.fromItems(Blocks.DARK_OAK_SAPLING), 120));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("glowstone_deconstruct"), new ItemStack(Items.GLOWSTONE_DUST, 4), Ingredient.fromItems(Blocks.GLOWSTONE), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("quartz_deconstruct"), new ItemStack(Items.QUARTZ, 4), Ingredient.fromItems(Blocks.QUARTZ_BLOCK), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("dark_quartz_deconstruct"), new ItemStack(ModItems.darkQuartz, 4), Ingredient.fromItems(ModFluffBlocks.darkQuartz), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("mana_quartz_deconstruct"), new ItemStack(ModItems.manaQuartz, 4), Ingredient.fromItems(ModFluffBlocks.manaQuartz), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("blaze_quartz_deconstruct"), new ItemStack(ModItems.blazeQuartz, 4), Ingredient.fromItems(ModFluffBlocks.blazeQuartz), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("lavender_quartz_deconstruct"), new ItemStack(ModItems.lavenderQuartz, 4), Ingredient.fromItems(ModFluffBlocks.lavenderQuartz), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("red_quartz_deconstruct"), new ItemStack(ModItems.redQuartz, 4), Ingredient.fromItems(ModFluffBlocks.redQuartz), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("elf_quartz_deconstruct"), new ItemStack(ModItems.elfQuartz, 4), Ingredient.fromItems(ModFluffBlocks.elfQuartz), 25));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("chiseled_stone_bricks"), new ItemStack(Blocks.CHISELED_STONE_BRICKS, 1), Ingredient.fromItems(Blocks.STONE_BRICKS), 150));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("ice"), new ItemStack(Blocks.ICE), Ingredient.fromItems(Blocks.SNOW), 2250));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("vine_to_lily_pad"), new ItemStack(Blocks.LILY_PAD), Ingredient.fromItems(Blocks.VINE), 320));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("lily_pad_to_vine"), new ItemStack(Blocks.VINE), Ingredient.fromItems(Blocks.LILY_PAD), 320));

		Item[] fishes = { Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH };
		for(int i = 0; i < fishes.length; i++) {
			Ingredient in = Ingredient.fromItems(fishes[i]);
			ItemStack out = new ItemStack(i == fishes.length - 1 ? fishes[0] : fishes[i + 1]);
			String id = String.format("%s_to_%s", fishes[i].getRegistryName().getPath(), out.getItem().getRegistryName().getPath());
			evt.manaInfusion().accept(RecipeManaInfusion.alchemy(new ResourceLocation(LibMisc.MOD_ID, id), out, in, 200));
		}

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("cocoa_to_wheat"), new ItemStack(Items.WHEAT_SEEDS), Ingredient.fromItems(Items.COCOA_BEANS), 6000));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("wheat_to_potato"), new ItemStack(Items.POTATO), Ingredient.fromItems(Items.WHEAT), 6000));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("potato_to_carrot"), new ItemStack(Items.CARROT), Ingredient.fromItems(Items.POTATO), 6000));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("carrot_to_beetroot"), new ItemStack(Items.BEETROOT_SEEDS), Ingredient.fromItems(Items.CARROT), 6000));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("beetroot_to_melon"), new ItemStack(Items.MELON_SEEDS), Ingredient.fromItems(Items.BEETROOT_SEEDS), 6000));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("melon_to_pumpkin"), new ItemStack(Items.PUMPKIN_SEEDS), Ingredient.fromItems(Items.MELON_SEEDS), 6000));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("pumpkin_to_cocoa"), new ItemStack(Items.COCOA_BEANS), Ingredient.fromItems(Items.PUMPKIN_SEEDS), 6000));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("potato_unpoison"), new ItemStack(Items.POTATO), Ingredient.fromItems(Items.POISONOUS_POTATO), 1200));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("blaze_rod_to_nether_wart"), new ItemStack(Items.NETHER_WART), Ingredient.fromItems(Items.BLAZE_ROD), 4000));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("gunpowder_to_flint"), new ItemStack(Items.FLINT), Ingredient.fromItems(Items.GUNPOWDER), 200));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("flint_to_gunpowder"), new ItemStack(Items.GUNPOWDER), Ingredient.fromItems(Items.FLINT), 200));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("book_to_name_tag"), new ItemStack(Items.NAME_TAG), Ingredient.fromItems(Items.WRITABLE_BOOK), 16000));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("wool_deconstruct"), new ItemStack(Items.STRING, 3), Ingredient.fromTag(ItemTags.WOOL), 100));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("cactus_to_slime"), new ItemStack(Items.SLIME_BALL), Ingredient.fromItems(Blocks.CACTUS), 1200));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("slime_to_cactus"), new ItemStack(Blocks.CACTUS), Ingredient.fromItems(Items.SLIME_BALL), 1200));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("ender_pearl_from_ghast_tear"), new ItemStack(Items.ENDER_PEARL), Ingredient.fromItems(Items.GHAST_TEAR), 28000));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("glowstone_to_redstone"), new ItemStack(Items.REDSTONE), Ingredient.fromItems(Items.GLOWSTONE_DUST), 300));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("redstone_to_glowstone"), new ItemStack(Items.GLOWSTONE_DUST), Ingredient.fromItems(Items.REDSTONE), 300));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("cobble_to_sand"), new ItemStack(Blocks.SAND), Ingredient.fromItems(Blocks.COBBLESTONE), 50));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("terracotta_to_red_sand"), new ItemStack(Blocks.RED_SAND), Ingredient.fromItems(Blocks.TERRACOTTA), 50));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("clay_deconstruct"), new ItemStack(Items.CLAY_BALL, 4), Ingredient.fromItems(Blocks.CLAY), 25));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("brick_deconstruct"), new ItemStack(Items.BRICK, 4), Ingredient.fromItems(Blocks.BRICKS), 25));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("coarse_dirt"), new ItemStack(Blocks.COARSE_DIRT), Ingredient.fromItems(Blocks.DIRT), 120));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("stone_to_andesite"), new ItemStack(Blocks.ANDESITE), Ingredient.fromItems(Blocks.STONE), 200));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("andesite_to_diorite"), new ItemStack(Blocks.DIORITE), Ingredient.fromItems(Blocks.ANDESITE), 200));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("diorite_to_granite"), new ItemStack(Blocks.GRANITE), Ingredient.fromItems(Blocks.DIORITE), 200));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("granite_to_andesite"), new ItemStack(Blocks.ANDESITE), Ingredient.fromItems(Blocks.GRANITE), 200));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("fern_to_dead_bush"), new ItemStack(Blocks.DEAD_BUSH), Ingredient.fromItems(Blocks.FERN), 500));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("dead_bush_to_grass"), new ItemStack(Blocks.GRASS), Ingredient.fromItems(Blocks.DEAD_BUSH), 500));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("grass_to_fern"), new ItemStack(Blocks.FERN), Ingredient.fromItems(Blocks.GRASS), 500));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("dandelion_to_poppy"), new ItemStack(Blocks.POPPY), Ingredient.fromItems(Blocks.DANDELION), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("poppy_to_blue_orchid"), new ItemStack(Blocks.BLUE_ORCHID), Ingredient.fromItems(Blocks.POPPY), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("blue_orchid_to_allium"), new ItemStack(Blocks.ALLIUM), Ingredient.fromItems(Blocks.BLUE_ORCHID), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("allium_to_azure_bluet"), new ItemStack(Blocks.AZURE_BLUET), Ingredient.fromItems(Blocks.ALLIUM), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("azure_bluet_to_red_tulip"), new ItemStack(Blocks.RED_TULIP), Ingredient.fromItems(Blocks.AZURE_BLUET), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("red_tulip_to_orange_tulip"), new ItemStack(Blocks.ORANGE_TULIP), Ingredient.fromItems(Blocks.RED_TULIP), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("orange_tulip_to_white_tulip"), new ItemStack(Blocks.WHITE_TULIP), Ingredient.fromItems(Blocks.ORANGE_TULIP), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("white_tulip_to_pink_tulip"), new ItemStack(Blocks.PINK_TULIP), Ingredient.fromItems(Blocks.WHITE_TULIP), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("pink_tulip_to_oxeye_daisy"), new ItemStack(Blocks.OXEYE_DAISY), Ingredient.fromItems(Blocks.PINK_TULIP), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("oxeye_daisy_to_sunflower"), new ItemStack(Blocks.SUNFLOWER), Ingredient.fromItems(Blocks.OXEYE_DAISY), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("sunflower_to_lilac"), new ItemStack(Blocks.LILAC), Ingredient.fromItems(Blocks.SUNFLOWER), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("lilac_to_rose_bush"), new ItemStack(Blocks.ROSE_BUSH), Ingredient.fromItems(Blocks.LILAC), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("rose_bush_to_peony"), new ItemStack(Blocks.PEONY), Ingredient.fromItems(Blocks.ROSE_BUSH), 400));
		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("peony_to_dandelion"), new ItemStack(Blocks.DANDELION), Ingredient.fromItems(Blocks.PEONY), 400));

		evt.manaInfusion().accept(RecipeManaInfusion.alchemy(prefix("chorus_fruit_to_flower"), new ItemStack(Blocks.CHORUS_FLOWER), Ingredient.fromItems(Items.POPPED_CHORUS_FRUIT), 10000));
		
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
	
	private static RecipeManaInfusion mini(IItemProvider mini, IItemProvider full) {
		return RecipeManaInfusion.alchemy(mini.asItem().getRegistryName(), new ItemStack(mini), Ingredient.fromItems(full), 2500);
	}
}
