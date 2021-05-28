/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IAncientWillContainer;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.*;
import vazkii.botania.common.item.ItemAncientWill;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.lens.ItemLens;

import java.util.*;
import java.util.stream.Collectors;

import static vazkii.botania.common.compat.rei.CategoryUtils.doesOreExist;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import me.shedaniel.rei.plugin.crafting.DefaultCustomDisplay;

@Environment(EnvType.CLIENT)
public class BotaniaREIPlugin implements REIPluginV0 {
	public static final Identifier PLUGIN = prefix("rei_plugin");

	@Override
	public Identifier getPluginIdentifier() {
		return PLUGIN;
	}

	@Override
	public void registerPluginCategories(RecipeHelper helper) {
		helper.registerCategories(
				new BreweryREICategory(),
				new PureDaisyREICategory(),
				new RunicAltarREICategory(),
				new PetalApothecaryREICategory(),
				new ElvenTradeREICategory(),
				new ManaPoolREICategory(),
				new OrechidREICategory(ModSubtiles.orechid),
				new OrechidREICategory(ModSubtiles.orechidIgnem)
		);
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper helper) {
		registerAncientWillRecipeWrapper(helper);
		registerCompositeLensRecipeWrapper(helper);
		registerTerraPickTippingRecipeWrapper(helper);

		helper.registerRecipes(RecipePetals.TYPE_ID, RecipePetals.class, PetalApothecaryREIDisplay::new);
		helper.registerRecipes(RecipeBrew.TYPE_ID, RecipeBrew.class, BreweryREIDisplay::new);
		//rawtyped predicate due to rei oversight?
		//helper.registerRecipes(RecipeElvenTrade.TYPE_ID, (Predicate<Recipe>) recipe -> recipe instanceof RecipeElvenTrade && recipe.getId().getNamespace().equals(LibMisc.MOD_ID) && !recipe.getId().getPath().contains("return"), ElvenTradeREIDisplay::new);
		helper.registerRecipes(RecipeElvenTrade.TYPE_ID, RecipeElvenTrade.class, ElvenTradeREIDisplay::new);
		helper.registerRecipes(RecipeManaInfusion.TYPE_ID, RecipeManaInfusion.class, ManaPoolREIDisplay::new);
		registerOrechidRecipes(helper, false);
		registerOrechidRecipes(helper, true);
		helper.registerRecipes(RecipePureDaisy.TYPE_ID, RecipePureDaisy.class, PureDaisyREIDisplay::new);
		helper.registerRecipes(RecipeRuneAltar.TYPE_ID, RecipeRuneAltar.class, RunicAltarREIDisplay::new);
	}

	@Override
	public void registerOthers(RecipeHelper helper) {
		Set<ItemConvertible> apothecaries = ImmutableSet.of(
				ModBlocks.defaultAltar,
				ModBlocks.desertAltar,
				ModBlocks.forestAltar,
				ModBlocks.fungalAltar,
				ModBlocks.mesaAltar,
				ModBlocks.mossyAltar,
				ModBlocks.mountainAltar,
				ModBlocks.plainsAltar,
				ModBlocks.swampAltar,
				ModBlocks.taigaAltar);
		for (ItemConvertible altar : apothecaries) {
			helper.registerWorkingStations(RecipePetals.TYPE_ID, EntryStack.create(altar));
		}
		helper.registerWorkingStations(RecipeBrew.TYPE_ID, EntryStack.create(ModBlocks.brewery));
		helper.registerWorkingStations(RecipeElvenTrade.TYPE_ID, EntryStack.create(ModBlocks.alfPortal));
		Set<ItemConvertible> manaPools = ImmutableSet.of(
				ModBlocks.manaPool,
				ModBlocks.dilutedPool,
				ModBlocks.fabulousPool
		);
		for (ItemConvertible pool : manaPools) {
			helper.registerWorkingStations(RecipeManaInfusion.TYPE_ID, EntryStack.create(pool));
		}
		helper.registerWorkingStations(prefix("orechid"), EntryStack.create(ModSubtiles.orechid), EntryStack.create(ModSubtiles.orechidFloating));
		helper.registerWorkingStations(prefix("orechid_ignem"), EntryStack.create(ModSubtiles.orechidIgnem), EntryStack.create(ModSubtiles.orechidIgnemFloating));
		helper.registerWorkingStations(RecipePureDaisy.TYPE_ID, EntryStack.create(ModSubtiles.pureDaisy), EntryStack.create(ModSubtiles.pureDaisyFloating));
		helper.registerWorkingStations(RecipeRuneAltar.TYPE_ID, EntryStack.create(ModBlocks.runeAltar));

		helper.removeAutoCraftButton(RecipePetals.TYPE_ID);
		helper.removeAutoCraftButton(RecipeBrew.TYPE_ID);
		helper.removeAutoCraftButton(RecipeElvenTrade.TYPE_ID);
		helper.removeAutoCraftButton(RecipeManaInfusion.TYPE_ID);
		helper.removeAutoCraftButton(prefix("orechid"));
		helper.removeAutoCraftButton(prefix("orechid_ignem"));
		helper.removeAutoCraftButton(RecipePureDaisy.TYPE_ID);
		helper.removeAutoCraftButton(RecipeRuneAltar.TYPE_ID);
	}

	void registerAncientWillRecipeWrapper(RecipeHelper helper) {
		ImmutableList.Builder<List<EntryStack>> input = ImmutableList.builder();
		ImmutableList.Builder<EntryStack> output = ImmutableList.builder();
		Set<ItemStack> wills = ImmutableSet.of(new ItemStack(ModItems.ancientWillAhrim), new ItemStack(ModItems.ancientWillDharok), new ItemStack(ModItems.ancientWillGuthan), new ItemStack(ModItems.ancientWillKaril), new ItemStack(ModItems.ancientWillTorag), new ItemStack(ModItems.ancientWillVerac));
		IAncientWillContainer container = (IAncientWillContainer) ModItems.terrasteelHelm;

		ItemStack helmet = new ItemStack(ModItems.terrasteelHelm);
		input.add(Collections.singletonList(EntryStack.create(helmet)));
		input.add(EntryStack.ofItemStacks(wills));
		for (ItemStack will : wills) {
			ItemStack copy = helmet.copy();
			container.addAncientWill(copy, ((ItemAncientWill) will.getItem()).type);
			output.add(EntryStack.create(copy));
		}
		helper.registerDisplay(new DefaultCustomDisplay(null, input.build(), output.build()));
	}

	void registerCompositeLensRecipeWrapper(RecipeHelper helper) {
		List<ItemStack> lensStacks = ItemTags.getTagGroup().getTagOrEmpty(prefix("lens"))
				.values().stream()
				.map(ItemStack::new)
				.filter(s -> !((ItemLens) s.getItem()).isControlLens(s))
				.filter(s -> ((ItemLens) s.getItem()).isCombinable(s))
				.collect(Collectors.toList());
		List<Item> lenses = lensStacks.stream().map(ItemStack::getItem).collect(Collectors.toList());
		List<List<EntryStack>> inputs = Arrays.asList(EntryStack.ofItemStacks(lensStacks), Collections.singletonList(EntryStack.create(new ItemStack(Items.SLIME_BALL))), EntryStack.ofItemStacks(lensStacks));
		int end = lenses.size() - 1;

		List<ItemStack> firstInput = new ArrayList<>();
		List<ItemStack> secondInput = new ArrayList<>();
		List<ItemStack> outputs = new ArrayList<>();
		for (int i = 1; i <= end; i++) {
			ItemStack firstLens = new ItemStack(lenses.get(i));
			for (Item secondLens : lenses) {
				if (secondLens == firstLens.getItem()) {
					continue;
				}

				ItemStack secondLensStack = new ItemStack(secondLens);
				if (((ItemLens) firstLens.getItem()).canCombineLenses(firstLens, secondLensStack)) {
					firstInput.add(firstLens);
					secondInput.add(secondLensStack);
					outputs.add(((ItemLens) firstLens.getItem()).setCompositeLens(firstLens.copy(), secondLensStack));
				}
			}
		}
		inputs.set(0, EntryStack.ofItemStacks(firstInput));
		inputs.set(2, EntryStack.ofItemStacks(secondInput));

		helper.registerDisplay(new DefaultCustomDisplay(null, inputs, EntryStack.ofItemStacks(outputs)));
	}

	void registerTerraPickTippingRecipeWrapper(RecipeHelper helper) {
		List<List<EntryStack>> inputs = ImmutableList.of(ImmutableList.of(EntryStack.create(ModItems.terraPick)), ImmutableList.of(EntryStack.create(ModItems.elementiumPick)));
		ItemStack output = new ItemStack(ModItems.terraPick);
		ItemTerraPick.setTipped(output);

		helper.registerDisplay(new DefaultCustomDisplay(null, inputs, Collections.singletonList(EntryStack.create(output))));
	}

	void registerOrechidRecipes(RecipeHelper helper, boolean isIgnem) {
		Map<Identifier, Integer> oreWeights = isIgnem ? BotaniaAPI.instance().getNetherOreWeights() : BotaniaAPI.instance().getOreWeights();
		List<OrechidRecipeWrapper> orechidRecipes = oreWeights.entrySet().stream()
				.filter(e -> doesOreExist(e.getKey()))
				.map(OrechidRecipeWrapper::new)
				.sorted()
				.collect(Collectors.toList());
		for (OrechidRecipeWrapper recipe : orechidRecipes) {
			if (isIgnem) {
				helper.registerDisplay(new OrechidIgnemREIDisplay(recipe));
			} else {
				helper.registerDisplay(new OrechidREIDisplay(recipe));
			}
		}
	}
}
