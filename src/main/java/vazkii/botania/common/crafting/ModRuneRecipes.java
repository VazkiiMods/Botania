/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 6, 2014, 5:59:28 PM (GMT)]
 */
package vazkii.botania.common.crafting;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.api.recipe.RegisterRecipesEvent;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.recipe.HeadRecipe;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public final class ModRuneRecipes {

	// todo 1.13 re-tagify these once the community settles down on some names
	@SubscribeEvent
	public static void register(RegisterRecipesEvent evt) {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;

		Ingredient manaSteel = Ingredient.fromTag(ModTags.Items.INGOTS_MANASTEEL);
		Ingredient manaDiamond = Ingredient.fromTag(ModTags.Items.GEMS_MANA_DIAMOND);
		Ingredient manaPowder = Ingredient.fromTag(ModTags.Items.DUSTS_MANA);
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("water"), new ItemStack(ModItems.runeWater, 2), costTier1, manaPowder, manaSteel, Ingredient.fromItems(Items.BONE_MEAL), Ingredient.fromItems(Blocks.SUGAR_CANE), Ingredient.fromItems(Items.FISHING_ROD)));
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("fire"), new ItemStack(ModItems.runeFire, 2), costTier1, manaPowder, manaSteel, Ingredient.fromItems(Items.NETHER_BRICK), Ingredient.fromItems(Items.GUNPOWDER), Ingredient.fromItems(Items.NETHER_WART)));

		Ingredient stone = Ingredient.fromTag(Tags.Items.STONE);
		Ingredient coalBlock = Ingredient.fromTag(Tags.Items.STORAGE_BLOCKS_COAL);
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("earth"), new ItemStack(ModItems.runeEarth, 2), costTier1, manaPowder, manaSteel, stone, coalBlock, Ingredient.fromItems(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM)));

		evt.runeAltar().accept(new RecipeRuneAltar(prefix("air"), new ItemStack(ModItems.runeAir, 2), costTier1, manaPowder, manaSteel, Ingredient.fromTag(ItemTags.CARPETS), Ingredient.fromItems(Items.FEATHER), Ingredient.fromItems(Items.STRING)));
		
		Ingredient fire = Ingredient.fromTag(ModTags.Items.RUNES_FIRE);
		Ingredient water = Ingredient.fromTag(ModTags.Items.RUNES_WATER);
		Ingredient earth = Ingredient.fromTag(ModTags.Items.RUNES_EARTH);
		Ingredient air = Ingredient.fromTag(ModTags.Items.RUNES_AIR);

		Ingredient sapling = Ingredient.fromTag(ItemTags.SAPLINGS);
		Ingredient leaves = Ingredient.fromTag(ItemTags.LEAVES);
		Ingredient sand = Ingredient.fromTag(ItemTags.SAND);
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("spring"), new ItemStack(ModItems.runeSpring), costTier2, water, fire, sapling, sapling, sapling, Ingredient.fromItems(Items.WHEAT)));
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("summer"), new ItemStack(ModItems.runeSummer), costTier2, earth, air, sand, sand, Ingredient.fromItems(Items.SLIME_BALL), Ingredient.fromItems(Items.MELON_SLICE)));
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("autumn"), new ItemStack(ModItems.runeAutumn), costTier2, fire, air, leaves, leaves, leaves, Ingredient.fromItems(Items.SPIDER_EYE)));

		evt.runeAltar().accept(new RecipeRuneAltar(prefix("winter"), new ItemStack(ModItems.runeWinter), costTier2, water, earth, Ingredient.fromItems(Blocks.SNOW), Ingredient.fromItems(Blocks.SNOW_BLOCK), Ingredient.fromTag(ItemTags.WOOL), Ingredient.fromItems(Blocks.CAKE)));

		Ingredient spring = Ingredient.fromTag(ModTags.Items.RUNES_SPRING);
		Ingredient summer = Ingredient.fromTag(ModTags.Items.RUNES_SUMMER);
		Ingredient autumn = Ingredient.fromTag(ModTags.Items.RUNES_AUTUMN);
		Ingredient winter = Ingredient.fromTag(ModTags.Items.RUNES_WINTER);
		
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("mana"), new ItemStack(ModItems.runeMana), costTier2, manaSteel, manaSteel, manaSteel, manaSteel, manaSteel, Ingredient.fromItems(ModItems.manaPearl)));

		evt.runeAltar().accept(new RecipeRuneAltar(prefix("lust"), new ItemStack(ModItems.runeLust), costTier3, manaDiamond, manaDiamond, summer, air));
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("gluttony"), new ItemStack(ModItems.runeGluttony), costTier3, manaDiamond, manaDiamond, winter, fire));
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("greed"), new ItemStack(ModItems.runeGreed), costTier3, manaDiamond, manaDiamond, spring, water));
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("sloth"), new ItemStack(ModItems.runeSloth), costTier3, manaDiamond, manaDiamond, autumn, air));
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("wrath"), new ItemStack(ModItems.runeWrath), costTier3, manaDiamond, manaDiamond, winter, earth));
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("envy"), new ItemStack(ModItems.runeEnvy), costTier3, manaDiamond, manaDiamond, winter, water));
		evt.runeAltar().accept(new RecipeRuneAltar(prefix("pride"), new ItemStack(ModItems.runePride), costTier3, manaDiamond, manaDiamond, summer, fire));

		evt.runeAltar().accept(new HeadRecipe(prefix("head"), new ItemStack(Items.PLAYER_HEAD), 22500, Ingredient.fromItems(Items.SKELETON_SKULL), Ingredient.fromItems(ModItems.pixieDust), Ingredient.fromItems(Items.PRISMARINE_CRYSTALS), Ingredient.fromItems(Items.NAME_TAG), Ingredient.fromItems(Items.GOLDEN_APPLE)));

		Ingredient envy = Ingredient.fromTag(ModTags.Items.RUNES_ENVY);
		Ingredient lust = Ingredient.fromTag(ModTags.Items.RUNES_LUST);
		Ingredient greed = Ingredient.fromTag(ModTags.Items.RUNES_GREED);

		ItemStack legs = new ItemStack(Items.GOLDEN_LEGGINGS);
		ItemNBTHelper.setInt(legs, "Unbreakable", 1);
		legs.addAttributeModifier("generic.armor", new AttributeModifier("generic.armor", -100, AttributeModifier.Operation.ADDITION), EquipmentSlotType.LEGS);
		legs.addEnchantment(Enchantments.BINDING_CURSE, 1);

		ITextComponent displayName = new StringTextComponent("Wear it or No Balls");
		legs.setDisplayName(displayName);

		evt.runeAltar().accept(new RecipeRuneAltar(prefix("pants"), legs, costTier3, manaDiamond, manaDiamond, Ingredient.fromItems(Items.GOLDEN_LEGGINGS), envy, lust, greed));
	}
}
