/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 30, 2014, 6:10:48 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RegisterRecipesEvent;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import java.util.Arrays;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ModManaInfusionRecipes {

	@SubscribeEvent
	public static void register(RegisterRecipesEvent evt) {
		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("manasteel"), new ItemStack(ModItems.manaSteel), Ingredient.fromTag(Tags.Items.INGOTS_IRON), 3000));
		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("manasteel_block"), new ItemStack(ModBlocks.manasteelBlock), Ingredient.fromItems(Blocks.IRON_BLOCK), 27000));

		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mana_pearl"), new ItemStack(ModItems.manaPearl), Ingredient.fromItems(Items.ENDER_PEARL), 6000));

		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mana_diamond"), new ItemStack(ModItems.manaDiamond), Ingredient.fromTag(Tags.Items.GEMS_DIAMOND), 10000));
		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mana_diamond_block"), new ItemStack(ModBlocks.manaDiamondBlock), Ingredient.fromItems(Blocks.DIAMOND_BLOCK), 90000));

		Ingredient dust = Ingredient.fromItems(Items.GUNPOWDER, Items.REDSTONE, Items.GLOWSTONE_DUST, Items.SUGAR);
		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mana_powder_dust"), new ItemStack(ModItems.manaPowder), dust, 500));
		Ingredient dyeIngredient = Ingredient.fromItems(Arrays.stream(DyeColor.values()).map(ModItems::getDye).toArray(Item[]::new));
		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mana_powder_dye"), new ItemStack(ModItems.manaPowder), dyeIngredient, 400));

		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("piston_relay"), new ItemStack(ModBlocks.pistonRelay), Ingredient.fromItems(Blocks.PISTON), 15000));
		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mana_cookie"), new ItemStack(ModItems.manaCookie), Ingredient.fromItems(Items.COOKIE), 20000));
		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("grass_seeds"), new ItemStack(ModItems.grassSeeds), Ingredient.fromItems(Blocks.GRASS), 2500));
		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("podzol_seeds"), new ItemStack(ModItems.podzolSeeds), Ingredient.fromItems(Blocks.DEAD_BUSH), 2500));

		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mycel_seeds"), new ItemStack(ModItems.mycelSeeds), Ingredient.fromItems(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM), 6500));

		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mana_quartz"), new ItemStack(ModItems.manaQuartz), Ingredient.fromItems(Items.QUARTZ), 250));
		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("tiny_potato"), new ItemStack(ModBlocks.tinyPotato), Ingredient.fromItems(Items.POTATO), 1337));

		if(Botania.thaumcraftLoaded) {
			Item inkwell = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", "scribing_tools"));
			evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mana_inkwell"), new ItemStack(ModItems.manaInkwell), Ingredient.fromItems(inkwell), 35000));
		}

		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mana_glass"), new ItemStack(ModBlocks.manaGlass), Ingredient.fromItems(Blocks.GLASS), 150));
		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mana_string"), new ItemStack(ModItems.manaString), Ingredient.fromItems(Items.STRING), 5000));

		if(Botania.gardenOfGlassLoaded)
			evt.manaInfusion().accept(new RecipeManaInfusion(prefix("sugar_cane"), new ItemStack(Blocks.SUGAR_CANE), Ingredient.fromItems(Blocks.HAY_BLOCK), 2000));

		evt.manaInfusion().accept(new RecipeManaInfusion(prefix("mana_bottle"), new ItemStack(ModItems.manaBottle), Ingredient.fromItems(Items.GLASS_BOTTLE), 5000));
	}

}
