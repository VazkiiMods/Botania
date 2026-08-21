/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.rei;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import me.shedaniel.math.Point;
import me.shedaniel.math.impl.PointHelper;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.screen.DisplayScreen;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.overlay.OverlayListWidget;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.DefaultStrippingDisplay;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.AncientWillContainer;
import vazkii.botania.api.recipe.*;
import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.handler.AxeStrippingData;
import vazkii.botania.common.item.AncientWillItem;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import vazkii.botania.common.item.lens.LensItem;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class BotaniaREIPlugin implements REIClientPlugin {
	private static final Supplier<ItemStack> HOVERED_STACK_GETTER = BotaniaREIPlugin::getHoveredREIStack;

	public BotaniaREIPlugin() {
		if (!CorporeaInputHandler.hoveredStackGetters.contains(HOVERED_STACK_GETTER)) {
			CorporeaInputHandler.hoveredStackGetters.add(HOVERED_STACK_GETTER);
		}
		CorporeaInputHandler.supportedGuiFilter = CorporeaInputHandler.supportedGuiFilter
				.or(screen -> screen instanceof DisplayScreen);
	}

	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(List.of(
				new PetalApothecaryREICategory(),
				new PureDaisyREICategory(),
				new ManaPoolREICategory(),
				new RunicAltarREICategory(),
				new ElvenTradeREICategory(),
				new BreweryREICategory(),
				new TerrestrialAgglomerationREICategory(),
				new OrechidREICategory(BotaniaREICategoryIdentifiers.ORECHID, BotaniaBlocks.ORECHID),
				new OrechidREICategory(BotaniaREICategoryIdentifiers.ORECHID_IGNEM, BotaniaBlocks.ORECHID_IGNEM),
				new OrechidREICategory(BotaniaREICategoryIdentifiers.MARIMORPHOSIS, BotaniaBlocks.MARIMORPHOSIS)
		));

		registry.addWorkstations(BuiltinPlugin.CRAFTING,
				EntryStacks.of(BotaniaItems.ASSEMBLY_HALO),
				EntryStacks.of(BotaniaItems.MANUFACTORY_HALO));
		for (Block apothecary : BotaniaBlocks.ALL_APOTHECARIES) {
			registry.addWorkstations(BotaniaREICategoryIdentifiers.PETAL_APOTHECARY, EntryStacks.of(apothecary));
		}
		registry.addWorkstations(BotaniaREICategoryIdentifiers.BREWERY,
				EntryStacks.of(BotaniaBlocks.BOTANICAL_BREWERY));
		registry.addWorkstations(BotaniaREICategoryIdentifiers.ELVEN_TRADE,
				EntryStacks.of(BotaniaBlocks.ELVEN_GATEWAY_CORE));
		Set<Block> manaPools = ImmutableSet.of(
				BotaniaBlocks.MANA_POOL,
				BotaniaBlocks.DILUTED_MANA_POOL,
				BotaniaBlocks.FABULOUS_MANA_POOL
		);
		for (Block pool : manaPools) {
			registry.addWorkstations(BotaniaREICategoryIdentifiers.MANA_INFUSION, EntryStacks.of(pool));
		}
		registry.addWorkstations(BotaniaREICategoryIdentifiers.ORECHID,
				EntryStacks.of(BotaniaBlocks.ORECHID),
				EntryStacks.of(BotaniaBlocks.FLOATING_ORECHID));
		registry.addWorkstations(BotaniaREICategoryIdentifiers.ORECHID_IGNEM,
				EntryStacks.of(BotaniaBlocks.ORECHID_IGNEM),
				EntryStacks.of(BotaniaBlocks.FLOATING_ORECHID_IGNEM));
		registry.addWorkstations(BotaniaREICategoryIdentifiers.MARIMORPHOSIS,
				EntryStacks.of(BotaniaBlocks.MARIMORPHOSIS),
				EntryStacks.of(BotaniaBlocks.FLOATING_MARIMORPHOSIS),
				EntryStacks.of(BotaniaBlocks.MARIMORPHOSIS_PETITE),
				EntryStacks.of(BotaniaBlocks.FLOATING_MARIMORPHOSIS_PETITE));
		registry.addWorkstations(BotaniaREICategoryIdentifiers.PURE_DAISY,
				EntryStacks.of(BotaniaBlocks.PURE_DAISY),
				EntryStacks.of(BotaniaBlocks.FLOATING_PURE_DAISY));
		registry.addWorkstations(BotaniaREICategoryIdentifiers.RUNE_ALTAR,
				EntryStacks.of(BotaniaBlocks.RUNIC_ALTAR));
		registry.addWorkstations(BotaniaREICategoryIdentifiers.TERRA_PLATE,
				EntryStacks.of(BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE));

		registry.setPlusButtonArea(BotaniaREICategoryIdentifiers.PETAL_APOTHECARY, null);
		registry.setPlusButtonArea(BotaniaREICategoryIdentifiers.BREWERY, null);
		registry.setPlusButtonArea(BotaniaREICategoryIdentifiers.ELVEN_TRADE, null);
		registry.setPlusButtonArea(BotaniaREICategoryIdentifiers.MANA_INFUSION, null);
		registry.setPlusButtonArea(BotaniaREICategoryIdentifiers.ORECHID, null);
		registry.setPlusButtonArea(BotaniaREICategoryIdentifiers.ORECHID_IGNEM, null);
		registry.setPlusButtonArea(BotaniaREICategoryIdentifiers.MARIMORPHOSIS, null);
		registry.setPlusButtonArea(BotaniaREICategoryIdentifiers.PURE_DAISY, null);
		registry.setPlusButtonArea(BotaniaREICategoryIdentifiers.RUNE_ALTAR, null);
		registry.setPlusButtonArea(BotaniaREICategoryIdentifiers.TERRA_PLATE, null);
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		registerAncientWillRecipeWrapper(registry);
		registerCompositeLensRecipeWrapper(registry);
		registerTerraPickTippingRecipeWrapper(registry);

		registry.registerRecipeFiller(PetalApothecaryRecipe.class,
				BotaniaRecipeTypes.PETAL_APOTHECARY_TYPE,
				PetalApothecaryREIDisplay::new);
		registry.registerRecipeFiller(BotanicalBreweryRecipe.class,
				BotaniaRecipeTypes.BREW_TYPE,
				BreweryREIDisplay::new);
		registry.registerRecipeFiller(ElvenTradeRecipe.class,
				BotaniaRecipeTypes.ELVEN_TRADE_TYPE::equals,
				recipe -> !recipe.value().isReturnRecipe(),
				ElvenTradeREIDisplay::new);
		registry.registerRecipeFiller(ManaInfusionRecipe.class,
				BotaniaRecipeTypes.MANA_INFUSION_TYPE,
				ManaPoolREIDisplay::new);
		registry.registerRecipeFiller(PureDaisyRecipe.class,
				BotaniaRecipeTypes.PURE_DAISY_TYPE,
				PureDaisyREIDisplay::new);
		registry.registerRecipeFiller(RunicAltarRecipe.class,
				BotaniaRecipeTypes.RUNIC_ALTAR_TYPE,
				RunicAltarREIDisplay::new);
		registry.registerRecipeFiller(TerrestrialAgglomerationRecipe.class,
				BotaniaRecipeTypes.TERRA_PLATE_TYPE,
				TerrestrialAgglomerationREIDisplay::new);

		try {
			for (var block : AxeStrippingData.getCustomStrippables()) {
				registry.add(new DefaultStrippingDisplay(EntryStacks.of(block), EntryStacks.of(AxeStrippingData.getCustomStrippable(block))));
			}
		} catch (Exception e) {
			BotaniaAPI.LOGGER.error("Error adding strippable entry to REI", e);
		}

		registry.registerRecipeFiller(OrechidRecipe.class,
				BotaniaRecipeTypes.ORECHID_TYPE,
				OrechidREIDisplay::new);
		registry.registerRecipeFiller(OrechidRecipe.class,
				BotaniaRecipeTypes.ORECHID_IGNEM_TYPE,
				OrechidIgnemREIDisplay::new);
		registry.registerRecipeFiller(OrechidRecipe.class,
				BotaniaRecipeTypes.MARIMORPHOSIS_TYPE,
				MarimorphosisREIDisplay::new);
	}

	void registerAncientWillRecipeWrapper(DisplayRegistry helper) {
		ImmutableList.Builder<EntryIngredient> input = ImmutableList.builder();
		ImmutableList.Builder<EntryStack<ItemStack>> output = ImmutableList.builder();
		Set<ItemStack> wills = ImmutableSet.of(
				new ItemStack(BotaniaItems.WILL_OF_AHRIM), new ItemStack(BotaniaItems.WILL_OF_DHAROK),
				new ItemStack(BotaniaItems.WILL_OF_GUTHAN), new ItemStack(BotaniaItems.WILL_OF_KARIL),
				new ItemStack(BotaniaItems.WILL_OF_TORAG), new ItemStack(BotaniaItems.WILL_OF_VERAC));
		AncientWillContainer container = (AncientWillContainer) BotaniaItems.TERRASTEEL_HELMET;

		ItemStack helmet = new ItemStack(BotaniaItems.TERRASTEEL_HELMET);
		input.add(EntryIngredients.of(helmet));
		input.add(EntryIngredients.ofItemStacks(wills));
		for (ItemStack will : wills) {
			ItemStack copy = helmet.copy();
			container.addAncientWill(copy, ((AncientWillItem) will.getItem()).type);
			output.add(EntryStacks.of(copy));
		}
		helper.add(new DefaultCustomDisplay(null, input.build(),
				Collections.singletonList(EntryIngredient.of(output.build()))));
	}

	void registerCompositeLensRecipeWrapper(DisplayRegistry helper) {
		List<ItemStack> lensStacks =
				StreamSupport.stream(BuiltInRegistries.ITEM.getTagOrEmpty(BotaniaTags.Items.LENS).spliterator(), false)
						.map(ItemStack::new)
						.filter(stack -> !((LensItem) stack.getItem()).isControlLens(stack))
						.filter(stack -> ((LensItem) stack.getItem()).isCombinable(stack))
						.toList();
		List<Item> lenses = lensStacks.stream().map(ItemStack::getItem).toList();
		List<EntryIngredient> inputs = Arrays.asList(
				EntryIngredients.ofItemStacks(lensStacks),
				EntryIngredients.of(new ItemStack(Items.SLIME_BALL)),
				EntryIngredients.ofItemStacks(lensStacks));
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
				if (((LensItem) firstLens.getItem()).canCombineLenses(firstLens, secondLensStack)) {
					firstInput.add(firstLens);
					secondInput.add(secondLensStack);
					outputs.add(((LensItem) firstLens.getItem()).setCompositeLens(firstLens.copy(), secondLensStack));
				}
			}
		}
		inputs.set(0, EntryIngredients.ofItemStacks(firstInput));
		inputs.set(2, EntryIngredients.ofItemStacks(secondInput));

		helper.add(new DefaultCustomDisplay(null, inputs, Collections.singletonList(EntryIngredients.ofItemStacks(outputs))));
	}

	void registerTerraPickTippingRecipeWrapper(DisplayRegistry helper) {
		List<EntryIngredient> inputs = ImmutableList.of(
				EntryIngredients.of(BotaniaItems.TERRA_SHATTERER),
				EntryIngredients.of(BotaniaItems.ELEMENTIUM_PICKAXE));
		ItemStack output = new ItemStack(BotaniaItems.TERRA_SHATTERER);
		TerraShattererItem.setTipped(output);

		helper.add(new DefaultCustomDisplay(null, inputs, Collections.singletonList(EntryIngredients.of(output))));
	}

	private static ItemStack getHoveredREIStack() {
		return REIRuntime.getInstance().getOverlay().map(o -> {
			ItemStack stack;
			if (REIRuntime.getInstance().isOverlayVisible()) {
				stack = unwrapEntry(o.getEntryList().getFocusedStack());
				if (!stack.isEmpty()) {
					return stack;
				}
				stack = o.getFavoritesList()
						.map(OverlayListWidget::getFocusedStack)
						.map(BotaniaREIPlugin::unwrapEntry)
						.orElse(ItemStack.EMPTY);
				if (!stack.isEmpty()) {
					return stack;
				}
			}
			if (Minecraft.getInstance().screen instanceof DisplayScreen) {
				Point point = PointHelper.ofMouse();
				for (var child : Minecraft.getInstance().screen.children()) {
					if (child.isMouseOver(point.x, point.y) && child instanceof Slot slot) {
						stack = unwrapEntry(slot.getCurrentEntry());
						if (!stack.isEmpty()) {
							return stack;
						}
					}
				}
			}
			return ItemStack.EMPTY;
		}).orElse(ItemStack.EMPTY);
	}

	private static ItemStack unwrapEntry(@Nullable EntryStack<?> stack) {
		if (stack != null && !stack.isEmpty() && stack.getType() == VanillaEntryTypes.ITEM) {
			return (ItemStack) stack.getValue();
		}
		return ItemStack.EMPTY;
	}
}
