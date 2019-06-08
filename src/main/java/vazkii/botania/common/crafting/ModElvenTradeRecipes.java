package vazkii.botania.common.crafting;

import net.minecraft.block.Blocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibOreDict;

import java.util.ArrayList;
import java.util.List;

public class ModElvenTradeRecipes {

	public static RecipeElvenTrade dreamwoodRecipe;
	public static List<RecipeElvenTrade> elementiumRecipes;
	public static RecipeElvenTrade pixieDustRecipe;
	public static List<RecipeElvenTrade> dragonstoneRecipes;
	public static RecipeElvenTrade elvenQuartzRecipe;
	public static RecipeElvenTrade alfglassRecipe;

	public static void init() {
		Ingredient livingwood = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, "livingwood")));
		dreamwoodRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.dreamwood), livingwood);

		Ingredient manaDiamond = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "gems/mana_diamond")));
		Ingredient manaSteel = Ingredient.fromTag(new ItemTags.Wrapper(new ResourceLocation("forge", "ingots/manasteel")));
		elementiumRecipes = new ArrayList<>();
		elementiumRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.elementium), manaSteel, manaSteel));
		elementiumRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.elementiumBlock), Ingredient.fromItems(ModBlocks.manasteelBlock), Ingredient.fromItems(ModBlocks.manasteelBlock)));

		pixieDustRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.pixieDust), Ingredient.fromItems(ModItems.manaPearl));
		dragonstoneRecipes = new ArrayList<>();
		dragonstoneRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.dragonstone), manaDiamond));
		dragonstoneRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.dragonstoneBlock), Ingredient.fromItems(ModBlocks.manaDiamondBlock)));

		elvenQuartzRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.elfQuartz), Ingredient.fromItems(Items.QUARTZ));
		alfglassRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.elfGlass), Ingredient.fromItems(ModBlocks.manaGlass));

		BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Items.IRON_INGOT), Ingredient.fromItems(Items.IRON_INGOT));
		BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Blocks.IRON_BLOCK), Ingredient.fromItems(Blocks.IRON_BLOCK));
		BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Items.ENDER_PEARL), Ingredient.fromItems(Items.ENDER_PEARL));
		BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Items.DIAMOND), Ingredient.fromItems(Items.DIAMOND));
		BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Blocks.DIAMOND_BLOCK), Ingredient.fromItems(Blocks.DIAMOND_BLOCK));
	}

}
