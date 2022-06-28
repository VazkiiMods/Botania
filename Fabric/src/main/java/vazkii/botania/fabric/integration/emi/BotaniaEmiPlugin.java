package vazkii.botania.fabric.integration.emi;

import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.IBrewRecipe;
import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.api.recipe.ITerraPlateRecipe;
import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.lens.ItemLens;
import vazkii.botania.common.lib.ModTags;

import java.util.List;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaEmiPlugin implements EmiPlugin {
	public static EmiRecipeCategory PETAL_APOTHECARY = new EmiRecipeCategory(prefix("petal_apothecary"),
			EmiStack.of(ModBlocks.defaultAltar));
	public static EmiRecipeCategory MANA_INFUSION = new EmiRecipeCategory(prefix("mana_infusion"),
			EmiStack.of(ModBlocks.manaPool));
	public static EmiRecipeCategory RUNIC_ALTAR = new EmiRecipeCategory(prefix("runic_altar"),
			EmiStack.of(ModBlocks.runeAltar));
	public static EmiRecipeCategory TERRESTRIAL_AGGLOMERATION = new EmiRecipeCategory(prefix("terrestrial_agglomeration"),
			EmiStack.of(ModBlocks.terraPlate));
	public static EmiRecipeCategory ELVEN_TRADE = new EmiRecipeCategory(prefix("elven_trade"),
			EmiStack.of(ModBlocks.alfPortal));
	public static EmiRecipeCategory BOTANICAL_BREWERY = new EmiRecipeCategory(prefix("botanical_brewery"),
			EmiStack.of(ModBlocks.brewery));
	public static EmiRecipeCategory PURE_DAISY = new EmiRecipeCategory(prefix("pure_daisy"),
			EmiStack.of(ModSubtiles.pureDaisy));
	public static EmiRecipeCategory ORECHID = new EmiRecipeCategory(prefix("orechid"),
			EmiStack.of(ModSubtiles.orechid));
	public static EmiRecipeCategory ORECHID_IGNEM = new EmiRecipeCategory(prefix("orechid_ignem"),
			EmiStack.of(ModSubtiles.orechidIgnem));
	public static EmiRecipeCategory MARIMORPHOSIS = new EmiRecipeCategory(prefix("marimorphosis"),
			EmiStack.of(ModSubtiles.marimorphosis));

	@Override
	public void register(EmiRegistry registry) {
		CorporeaInputHandler.hoveredStackGetter = () -> {
			EmiIngredient stack = EmiApi.getHoveredStack(true).getStack();
			if (stack.getEmiStacks().size() > 0) {
				return stack.getEmiStacks().get(0).getItemStack();
			}
			return ItemStack.EMPTY;
		};
		registry.addCategory(PETAL_APOTHECARY);
		registry.addCategory(MANA_INFUSION);
		registry.addCategory(RUNIC_ALTAR);
		registry.addCategory(TERRESTRIAL_AGGLOMERATION);
		registry.addCategory(ELVEN_TRADE);
		registry.addCategory(BOTANICAL_BREWERY);
		registry.addCategory(PURE_DAISY);
		registry.addCategory(ORECHID);
		registry.addCategory(ORECHID_IGNEM);
		registry.addCategory(MARIMORPHOSIS);

		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(ModItems.craftingHalo));
		registry.addWorkstation(VanillaEmiRecipeCategories.CRAFTING, EmiStack.of(ModItems.autocraftingHalo));

		registry.addWorkstation(PETAL_APOTHECARY, EmiStack.of(ModBlocks.defaultAltar));
		registry.addWorkstation(PETAL_APOTHECARY, EmiStack.of(ModBlocks.desertAltar));
		registry.addWorkstation(PETAL_APOTHECARY, EmiStack.of(ModBlocks.forestAltar));
		registry.addWorkstation(PETAL_APOTHECARY, EmiStack.of(ModBlocks.fungalAltar));
		registry.addWorkstation(PETAL_APOTHECARY, EmiStack.of(ModBlocks.mesaAltar));
		registry.addWorkstation(PETAL_APOTHECARY, EmiStack.of(ModBlocks.mossyAltar));
		registry.addWorkstation(PETAL_APOTHECARY, EmiStack.of(ModBlocks.mountainAltar));
		registry.addWorkstation(PETAL_APOTHECARY, EmiStack.of(ModBlocks.plainsAltar));
		registry.addWorkstation(PETAL_APOTHECARY, EmiStack.of(ModBlocks.swampAltar));
		registry.addWorkstation(PETAL_APOTHECARY, EmiStack.of(ModBlocks.taigaAltar));
		registry.addWorkstation(MANA_INFUSION, EmiStack.of(ModBlocks.manaPool));
		registry.addWorkstation(MANA_INFUSION, EmiStack.of(ModBlocks.dilutedPool));
		registry.addWorkstation(MANA_INFUSION, EmiStack.of(ModBlocks.fabulousPool));
		registry.addWorkstation(RUNIC_ALTAR, EmiStack.of(ModBlocks.runeAltar));
		registry.addWorkstation(TERRESTRIAL_AGGLOMERATION, EmiStack.of(ModBlocks.terraPlate));
		registry.addWorkstation(ELVEN_TRADE, EmiStack.of(ModBlocks.alfPortal));
		registry.addWorkstation(BOTANICAL_BREWERY, EmiStack.of(ModBlocks.brewery));

		registry.addWorkstation(PURE_DAISY, EmiStack.of(ModSubtiles.pureDaisy));
		registry.addWorkstation(PURE_DAISY, EmiStack.of(ModSubtiles.pureDaisyFloating));
		registry.addWorkstation(ORECHID, EmiStack.of(ModSubtiles.orechid));
		registry.addWorkstation(ORECHID, EmiStack.of(ModSubtiles.orechidFloating));
		registry.addWorkstation(ORECHID_IGNEM, EmiStack.of(ModSubtiles.orechidIgnem));
		registry.addWorkstation(ORECHID_IGNEM, EmiStack.of(ModSubtiles.orechidIgnemFloating));
		registry.addWorkstation(MARIMORPHOSIS, EmiStack.of(ModSubtiles.marimorphosis));
		registry.addWorkstation(MARIMORPHOSIS, EmiStack.of(ModSubtiles.marimorphosisFloating));
		registry.addWorkstation(MARIMORPHOSIS, EmiStack.of(ModSubtiles.marimorphosisChibi));
		registry.addWorkstation(MARIMORPHOSIS, EmiStack.of(ModSubtiles.marimorphosisChibiFloating));

		Function<Comparison, Comparison> compareNbt = c -> c.copy().nbt(true).build();
		registry.setDefaultComparison(ModItems.lexicon, compareNbt);
		registry.setDefaultComparison(ModItems.brewFlask, compareNbt);
		registry.setDefaultComparison(ModItems.brewVial, compareNbt);
		registry.setDefaultComparison(ModItems.bloodPendant, compareNbt);
		registry.setDefaultComparison(ModItems.incenseStick, compareNbt);
		// Disables the ability to see the no wings tiara recipe, probably an nbt mismatch
		//registry.setDefaultComparison(ModItems.flightTiara, compareNbt);

		registry.addRecipe(new AncientWillEmiRecipe(EmiStack.of(ModItems.terrasteelHelm), EmiIngredient.of(List.of(
				EmiStack.of(ModItems.ancientWillAhrim),
				EmiStack.of(ModItems.ancientWillDharok),
				EmiStack.of(ModItems.ancientWillGuthan),
				EmiStack.of(ModItems.ancientWillKaril),
				EmiStack.of(ModItems.ancientWillTorag),
				EmiStack.of(ModItems.ancientWillVerac)
		))));

		registry.addRecipe(new CompositeLensEmiRecipe(
				StreamSupport.stream(Registry.ITEM.getOrCreateTag(ModTags.Items.LENS).spliterator(), false)
						.map(ItemStack::new)
						.filter(s -> !((ItemLens) s.getItem()).isControlLens(s))
						.filter(s -> ((ItemLens) s.getItem()).isCombinable(s))
						.map(EmiStack::of)
						.toList()));

		ItemStack tipped = new ItemStack(ModItems.terraPick);
		ItemTerraPick.setTipped(tipped);
		registry.addRecipe(new EmiCraftingRecipe(List.of(EmiStack.of(ModItems.terraPick),
				EmiStack.of(ModItems.elementiumPick)), EmiStack.of(tipped), null));

		for (IPetalRecipe recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.PETAL_TYPE)) {
			registry.addRecipe(new PetalApothecaryEmiRecipe(recipe));
		}
		for (IManaInfusionRecipe recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.MANA_INFUSION_TYPE)) {
			registry.addRecipe(new ManaInfusionEmiRecipe(recipe));
		}
		for (IRuneAltarRecipe recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.RUNE_TYPE)) {
			registry.addRecipe(new RunicAltarEmiRecipe(recipe));
		}
		for (ITerraPlateRecipe recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.TERRA_PLATE_TYPE)) {
			registry.addRecipe(new TerrestrialAgglomerationEmiRecipe(recipe));
		}
		for (IElvenTradeRecipe recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.ELVEN_TRADE_TYPE)) {
			registry.addRecipe(new ElvenTradeEmiRecipe(recipe));
		}
		List<ItemStack> containers = List.of(ModItems.vial, ModItems.flask, ModItems.incenseStick, ModItems.bloodPendant)
				.stream().map(ItemStack::new).toList();
		for (IBrewRecipe recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.BREW_TYPE)) {
			for (ItemStack container : containers) {
				if (!recipe.getOutput(container.copy()).isEmpty()) {
					registry.addRecipe(new BotanicalBreweryEmiRecipe(recipe, container));
				}
			}
		}
		for (IPureDaisyRecipe recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.PURE_DAISY_TYPE)) {
			registry.addRecipe(new PureDaisyEmiRecipe(recipe));
		}

		Object2IntMap<Block> weights = getWeights(ModRecipeTypes.ORECHID_TYPE, registry.getRecipeManager());
		EmiIngredient flower = EmiStack.of(ModSubtiles.orechid);
		for (IOrechidRecipe recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.ORECHID_TYPE)) {
			registry.addRecipe(new OrechidEmiRecipe(ORECHID, recipe, weights.getInt(recipe.getInput()), flower));
		}
		weights = getWeights(ModRecipeTypes.ORECHID_IGNEM_TYPE, registry.getRecipeManager());
		flower = EmiStack.of(ModSubtiles.orechidIgnem);
		for (IOrechidRecipe recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.ORECHID_IGNEM_TYPE)) {
			registry.addRecipe(new OrechidEmiRecipe(ORECHID_IGNEM, recipe, weights.getInt(recipe.getInput()), flower));
		}
		weights = getWeights(ModRecipeTypes.MARIMORPHOSIS_TYPE, registry.getRecipeManager());
		flower = EmiStack.of(ModSubtiles.marimorphosis);
		for (IOrechidRecipe recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.MARIMORPHOSIS_TYPE)) {
			registry.addRecipe(new OrechidEmiRecipe(MARIMORPHOSIS, recipe, weights.getInt(recipe.getInput()), flower));
		}
	}

	public static Object2IntMap<Block> getWeights(RecipeType<IOrechidRecipe> type, RecipeManager manager) {
		Object2IntOpenHashMap<Block> map = new Object2IntOpenHashMap<>();
		for (IOrechidRecipe recipe : manager.getAllRecipesFor(type)) {
			map.addTo(recipe.getInput(), recipe.getWeight());
		}
		return map;
	}

	public static int rotateXAround(int x, int y, int cx, int cy, double degrees) {
		double rad = Math.toRadians(degrees);
		return (int) (Math.cos(rad) * (x - cx) - Math.sin(rad) * (y - cy) + cx);
	}

	public static int rotateYAround(int x, int y, int cx, int cy, double degrees) {
		double rad = Math.toRadians(degrees);
		return (int) (Math.sin(rad) * (x - cx) - Math.cos(rad) * (y - cy) + cy);
	}
}
