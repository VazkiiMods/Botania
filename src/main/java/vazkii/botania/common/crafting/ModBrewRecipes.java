/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 9:15:15 PM (GMT)]
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RegisterRecipesEvent;
import vazkii.botania.common.Botania;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibOreDict;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModBrewRecipes {

	@SubscribeEvent
	public static void register(RegisterRecipesEvent evt) {
		evt.brew().accept(new RecipeBrew(prefix("speed"), ModBrews.speed, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SUGAR), Ingredient.fromItems(Items.REDSTONE)));
		evt.brew().accept(new RecipeBrew(prefix("strength"), ModBrews.strength, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.BLAZE_POWDER), Ingredient.fromItems(Items.GLOWSTONE_DUST)));
		evt.brew().accept(new RecipeBrew(prefix("haste"), ModBrews.haste, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SUGAR), Ingredient.fromItems(Items.GOLD_NUGGET)));
		evt.brew().accept(new RecipeBrew(prefix("healing"), ModBrews.healing, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GLISTERING_MELON_SLICE), Ingredient.fromItems(Items.POTATO)));
		evt.brew().accept(new RecipeBrew(prefix("jump_boost"), ModBrews.jumpBoost, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.FEATHER), Ingredient.fromItems(Items.CARROT)));
		evt.brew().accept(new RecipeBrew(prefix("regeneration"), ModBrews.regen, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GHAST_TEAR), Ingredient.fromItems(Items.GLOWSTONE_DUST)));
		evt.brew().accept(new RecipeBrew(prefix("weak_regeneration"), ModBrews.regenWeak, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GHAST_TEAR), Ingredient.fromItems(Items.REDSTONE)));
		evt.brew().accept(new RecipeBrew(prefix("resistance"), ModBrews.resistance, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.IRON_INGOT), Ingredient.fromItems(Items.LEATHER)));
		evt.brew().accept(new RecipeBrew(prefix("fire_resistance"), ModBrews.fireResistance, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.MAGMA_CREAM), Ingredient.fromItems(Blocks.NETHERRACK)));
		evt.brew().accept(new RecipeBrew(prefix("water_breathing"), ModBrews.waterBreathing, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.PRISMARINE_CRYSTALS), Ingredient.fromItems(Items.GLOWSTONE_DUST)));
		evt.brew().accept(new RecipeBrew(prefix("invisibility"), ModBrews.invisibility, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SNOWBALL), Ingredient.fromItems(Items.GLOWSTONE_DUST)));
		evt.brew().accept(new RecipeBrew(prefix("night_vision"), ModBrews.nightVision, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.SPIDER_EYE), Ingredient.fromItems(Items.GOLDEN_CARROT)));
		evt.brew().accept(new RecipeBrew(prefix("absorption"), ModBrews.absorption, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GOLDEN_APPLE), Ingredient.fromItems(Items.POTATO)));

		evt.brew().accept(new RecipeBrew(prefix("overload"), ModBrews.overload, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.BLAZE_POWDER), Ingredient.fromItems(Items.SUGAR), Ingredient.fromItems(Items.GLOWSTONE_DUST), Ingredient.fromItems(ModItems.manaSteel), Ingredient.fromItems(Items.SPIDER_EYE)));
		evt.brew().accept(new RecipeBrew(prefix("soul_cross"), ModBrews.soulCross, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Blocks.SOUL_SAND), Ingredient.fromItems(Items.PAPER), Ingredient.fromItems(Items.APPLE), Ingredient.fromItems(Items.BONE)));
		evt.brew().accept(new RecipeBrew(prefix("feather_feet"), ModBrews.featherfeet, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.FEATHER), Ingredient.fromItems(Items.LEATHER), Ingredient.fromTag(ItemTags.WOOL)));
		evt.brew().accept(new RecipeBrew(prefix("emptiness"), ModBrews.emptiness, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.GUNPOWDER), Ingredient.fromItems(Items.ROTTEN_FLESH), Ingredient.fromItems(Items.BONE), Ingredient.fromItems(Items.STRING), Ingredient.fromItems(Items.ENDER_PEARL)));
		evt.brew().accept(new RecipeBrew(prefix("bloodthirst"), ModBrews.bloodthirst, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE), Ingredient.fromItems(Items.LAPIS_LAZULI), Ingredient.fromItems(Items.FIRE_CHARGE), Ingredient.fromItems(Items.IRON_INGOT)));
		evt.brew().accept(new RecipeBrew(prefix("allure"), ModBrews.allure, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.COD), Ingredient.fromItems(Items.QUARTZ), Ingredient.fromItems(Items.GOLDEN_CARROT)));
		evt.brew().accept(new RecipeBrew(prefix("clear"), ModBrews.clear, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(Items.QUARTZ), Ingredient.fromItems(Items.EMERALD), Ingredient.fromItems(Items.MELON_SLICE)));

		if(Botania.thaumcraftLoaded) {
			Item salisMundus = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", "salis_mundus"));
			Item bathSalts = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", "bath_salts"));
			Item amber = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", "amber"));

			evt.brew().accept(new RecipeBrew(prefix("warp_ward"), ModBrews.warpWard, Ingredient.fromItems(Items.NETHER_WART), Ingredient.fromItems(salisMundus), Ingredient.fromItems(bathSalts), Ingredient.fromItems(amber)));
		}
	}
}
