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
			if(stacks.size() == 1 && !ItemNBTHelper.getBoolean(stacks.get(0), ItemLexicon.TAG_ELVEN_UNLOCK, false))
				return super.match(stacks);
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
