/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.AnimatedResultButton;
import net.minecraft.client.gui.screen.recipebook.RecipeBookResults;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.network.PacketIndexKeybindRequest;
import vazkii.botania.mixin.AccessorRecipeBookGui;
import vazkii.botania.mixin.AccessorRecipeBookPage;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class CorporeaInputHandler {

	/** Replaced in JEIBotaniaPlugin when JEI's loaded to provide stacks from the JEI item panel. */
	public static Supplier<ItemStack> jeiPanelSupplier = () -> ItemStack.EMPTY;

	/** Filter for usable guis to handle requests. Added to in JEIBotaniaPlugin */
	public static Predicate<Screen> supportedGuiFilter = gui -> gui instanceof HandledScreen;

	public static void buttonPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {
		MinecraftClient mc = MinecraftClient.getInstance();

		if (mc.world == null || !supportedGuiFilter.test(mc.currentScreen) || event.getKeyCode() == 0
				|| ClientProxy.CORPOREA_REQUEST.getKey().getCode() != event.getKeyCode()
				|| TileCorporeaIndex.InputHandler.getNearbyIndexes(mc.player).isEmpty()) {
			return;
		}

		ItemStack stack = getStackUnderMouse();
		if (stack != null && !stack.isEmpty()) {
			int count = 1;
			int max = stack.getMaxCount();

			if (Screen.hasShiftDown()) {
				count = max;
				if (Screen.hasControlDown()) {
					count /= 4;
				}
			} else if (Screen.hasControlDown()) {
				count = max / 2;
			}

			if (count > 0) {
				ItemStack requested = stack.copy();
				requested.setCount(count);
				PacketIndexKeybindRequest.send(requested);
				event.setCanceled(true);
			}
		}
	}

	private static ItemStack getStackUnderMouse() {
		Screen screen = MinecraftClient.getInstance().currentScreen;
		if (screen instanceof HandledScreen) {
			Slot slotUnderMouse = ((HandledScreen) screen).getSlotUnderMouse();
			if (slotUnderMouse != null) {
				ItemStack stack = slotUnderMouse.getStack().copy();
				stack.setTag(null); // Wipe NBT of inventory items before request, as player items will often have data
				return stack; // that's better to ignore. This is still an improvement over matching names only.
			}
		}

		if (screen instanceof InventoryScreen && ((InventoryScreen) screen).getRecipeBookWidget().isOpen()) {
			RecipeBookWidget recipeBook = ((InventoryScreen) screen).getRecipeBookWidget();
			RecipeBookResults page = ((AccessorRecipeBookGui) recipeBook).getRecipesArea();
			AnimatedResultButton widget = ((AccessorRecipeBookPage) page).getHoveredResultButton();
			if (widget != null) {
				return widget.currentRecipe().getOutput();
			}
		}

		return jeiPanelSupplier.get();
	}
}
