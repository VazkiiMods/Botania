/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import net.minecraft.client.gui.screens.recipebook.RecipeButton;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.core.RecipeBookAccess;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.mixin.client.AccessorAbstractContainerScreen;
import vazkii.botania.mixin.client.AccessorRecipeBookComponent;
import vazkii.botania.mixin.client.AccessorRecipeBookPage;
import vazkii.botania.network.serverbound.PacketIndexKeybindRequest;
import vazkii.botania.xplat.IClientXplatAbstractions;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class CorporeaInputHandler {

	/** Replaced in JEIBotaniaPlugin when JEI's loaded to provide stacks from the JEI item panel. */
	public static Supplier<ItemStack> jeiPanelSupplier = () -> ItemStack.EMPTY;

	/** Filter for usable guis to handle requests. Added to in JEIBotaniaPlugin */
	public static Predicate<Screen> supportedGuiFilter = gui -> gui instanceof AbstractContainerScreen;

	public static boolean buttonPressed(int keyCode, int scanCode) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.level == null || !supportedGuiFilter.test(mc.screen)
				|| !ClientProxy.CORPOREA_REQUEST.matches(keyCode, scanCode)
				|| TileCorporeaIndex.InputHandler.getNearbyIndexes(mc.player).isEmpty()) {
			return false;
		}

		ItemStack stack = getStackUnderMouse();
		if (stack != null && !stack.isEmpty()) {
			int count = 1;
			int max = stack.getMaxStackSize();

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
				IClientXplatAbstractions.INSTANCE.sendToServer(new PacketIndexKeybindRequest(requested));
				return true;
			}
		}
		return false;
	}

	private static ItemStack getStackUnderMouse() {
		Screen screen = Minecraft.getInstance().screen;
		if (screen instanceof AbstractContainerScreen) {
			Slot slotUnderMouse = ((AccessorAbstractContainerScreen) screen).getHoveredSlot();
			if (slotUnderMouse != null && slotUnderMouse.hasItem()) {
				ItemStack stack = slotUnderMouse.getItem().copy();
				stack.setTag(null); // Wipe NBT of inventory items before request, as player items will often have data
				return stack; // that's better to ignore. This is still an improvement over matching names only.
			}

			if (screen instanceof RecipeUpdateListener recipeScreen && recipeScreen.getRecipeBookComponent().isVisible()) {
				RecipeBookComponent recipeBook = recipeScreen.getRecipeBookComponent();
				RecipeBookPage page = ((AccessorRecipeBookComponent) recipeBook).getRecipesArea();
				RecipeButton widget = ((AccessorRecipeBookPage) page).getHoveredButton();
				if (widget != null) {
					return widget.getRecipe().getResultItem();
				}
				ItemStack stack = ((RecipeBookAccess) recipeBook).getHoveredGhostRecipeStack();
				if (stack != null) {
					return stack;
				}
			}
		}

		return jeiPanelSupplier.get();
	}
}
