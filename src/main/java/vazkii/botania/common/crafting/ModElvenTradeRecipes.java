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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RegisterRecipesEvent;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.ModTags;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ModElvenTradeRecipes {

	@SubscribeEvent
	public static void register(RegisterRecipesEvent evt) {
		ItemStack elvenLexicon = new ItemStack(ModItems.lexicon);
		elvenLexicon.getOrCreateTag().putBoolean(ItemLexicon.TAG_ELVEN_UNLOCK, true);
		evt.elvenTrade().accept(new RecipeLexicon(prefix("elven_lexicon"), elvenLexicon, Ingredient.fromStacks(new ItemStack(ModItems.lexicon))));
	}

	private static class RecipeLexicon extends RecipeElvenTrade {
		RecipeLexicon(ResourceLocation id, ItemStack output, Ingredient input) {
			super(id, output, input);
		}

		@Override
		public boolean containsItem(ItemStack stack) {
			return super.containsItem(stack) && !ItemNBTHelper.getBoolean(stack, ItemLexicon.TAG_ELVEN_UNLOCK, false);
		}

		@Override
		public Optional<List<ItemStack>> match(List<ItemStack> stacks) {
			if (stacks.size() == 1 && !ItemNBTHelper.getBoolean(stacks.get(0), ItemLexicon.TAG_ELVEN_UNLOCK, false)) {
				return super.match(stacks);
			}
			return Optional.empty();
		}

		@Override
		public List<ItemStack> getOutputs(List<ItemStack> inputs) {
			ItemStack stack = inputs.get(0).copy();
			stack.getOrCreateTag().putBoolean(ItemLexicon.TAG_ELVEN_UNLOCK, true);
			return Collections.singletonList(stack);
		}
	}
}
