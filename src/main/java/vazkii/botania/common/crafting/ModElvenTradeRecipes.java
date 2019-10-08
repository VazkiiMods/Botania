package vazkii.botania.common.crafting;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RegisterRecipesEvent;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModElvenTradeRecipes {

	@SubscribeEvent
	public static void register(RegisterRecipesEvent evt) {
		Ingredient livingwood = Ingredient.fromTag(ModTags.Items.LIVINGWOOD);
		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("dreamwood"), new ItemStack(ModBlocks.dreamwood), livingwood));

		Ingredient manaDiamond = Ingredient.fromTag(ModTags.Items.GEMS_MANA_DIAMOND);
		Ingredient manaSteel = Ingredient.fromTag(ModTags.Items.INGOTS_MANASTEEL);
		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("elementium"), new ItemStack(ModItems.elementium), manaSteel, manaSteel));
		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("elementium_block"), new ItemStack(ModBlocks.elementiumBlock), Ingredient.fromItems(ModBlocks.manasteelBlock), Ingredient.fromItems(ModBlocks.manasteelBlock)));

		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("pixie_dust"), new ItemStack(ModItems.pixieDust), Ingredient.fromItems(ModItems.manaPearl)));
		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("dragonstone"), new ItemStack(ModItems.dragonstone), manaDiamond));
		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("dragonstone_block"), new ItemStack(ModBlocks.dragonstoneBlock), Ingredient.fromItems(ModBlocks.manaDiamondBlock)));

		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("elf_quartz"), new ItemStack(ModItems.elfQuartz), Ingredient.fromItems(Items.QUARTZ)));
		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("elf_glass"), new ItemStack(ModBlocks.elfGlass), Ingredient.fromItems(ModBlocks.manaGlass)));

		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("iron_return"), new ItemStack(Items.IRON_INGOT), Ingredient.fromItems(Items.IRON_INGOT)));
		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("iron_block_return"), new ItemStack(Blocks.IRON_BLOCK), Ingredient.fromItems(Blocks.IRON_BLOCK)));
		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("ender_pearl_return"), new ItemStack(Items.ENDER_PEARL), Ingredient.fromItems(Items.ENDER_PEARL)));
		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("diamond_return"), new ItemStack(Items.DIAMOND), Ingredient.fromItems(Items.DIAMOND)));
		evt.elvenTrade().accept(new RecipeElvenTrade(prefix("diamond_block_return"), new ItemStack(Blocks.DIAMOND_BLOCK), Ingredient.fromItems(Blocks.DIAMOND_BLOCK)));
	}

}
