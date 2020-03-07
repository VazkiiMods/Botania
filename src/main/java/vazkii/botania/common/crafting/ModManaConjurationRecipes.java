/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RegisterRecipesEvent;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModManaConjurationRecipes {

	public static RecipeManaInfusion redstoneRecipe;
	public static RecipeManaInfusion glowstoneRecipe;
	public static RecipeManaInfusion quartzRecipe;
	public static RecipeManaInfusion coalRecipe;
	public static RecipeManaInfusion snowballRecipe;
	public static RecipeManaInfusion netherrackRecipe;
	public static RecipeManaInfusion soulSandRecipe;
	public static RecipeManaInfusion gravelRecipe;
	public static List<RecipeManaInfusion> leavesRecipes;
	public static RecipeManaInfusion grassRecipe;

	@SubscribeEvent
	public static void register(RegisterRecipesEvent evt) {
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("redstone_dupe"), new ItemStack(Items.REDSTONE, 2), ingr(Items.REDSTONE), 5000));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("glowstone_dupe"), new ItemStack(Items.GLOWSTONE_DUST, 2), ingr(Items.GLOWSTONE_DUST), 5000));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("quartz_dupe"), new ItemStack(Items.QUARTZ, 2), ingr(Items.QUARTZ), 2500));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("coal_dupe"), new ItemStack(Items.COAL, 2), ingr(Items.COAL), 2100));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("snowball_dupe"), new ItemStack(Items.SNOWBALL, 2), ingr(Items.SNOWBALL), 200));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("netherrack_dupe"), new ItemStack(Blocks.NETHERRACK, 2), ingr(Blocks.NETHERRACK), 200));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("soul_sand_dupe"), new ItemStack(Blocks.SOUL_SAND, 2), ingr(Blocks.SOUL_SAND), 1500));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("gravel_dupe"), new ItemStack(Blocks.GRAVEL, 2), ingr(Blocks.GRAVEL), 720));

		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("oak_leaves_dupe"), new ItemStack(Blocks.OAK_LEAVES, 2), ingr(Blocks.OAK_LEAVES), 2000));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("birch_leaves_dupe"), new ItemStack(Blocks.BIRCH_LEAVES, 2), ingr(Blocks.BIRCH_LEAVES), 2000));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("spruce_leaves_dupe"), new ItemStack(Blocks.SPRUCE_LEAVES, 2), ingr(Blocks.SPRUCE_LEAVES), 2000));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("jungle_leaves_dupe"), new ItemStack(Blocks.JUNGLE_LEAVES, 2), ingr(Blocks.JUNGLE_LEAVES), 2000));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("acacia_leaves_dupe"), new ItemStack(Blocks.ACACIA_LEAVES, 2), ingr(Blocks.ACACIA_LEAVES), 2000));
		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("dark_oak_leaves_dupe"), new ItemStack(Blocks.DARK_OAK_LEAVES, 2), ingr(Blocks.DARK_OAK_LEAVES), 2000));

		evt.manaInfusion().accept(RecipeManaInfusion.conjuration(prefix("grass"), new ItemStack(Blocks.GRASS, 2), ingr(Blocks.GRASS), 800));
	}

	private static Ingredient ingr(IItemProvider i) {
		return Ingredient.fromItems(i);
	}

}
