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
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.recipebook.RecipeBookPage;
import net.minecraft.client.gui.recipebook.RecipeWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;

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
	public static Predicate<Screen> supportedGuiFilter = gui -> gui instanceof ContainerScreen;

	public static void buttonPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.world == null || !supportedGuiFilter.test(mc.currentScreen) || event.getKeyCode() == 0
				|| ClientProxy.CORPOREA_REQUEST.getKey().getKeyCode() != event.getKeyCode()
				|| TileCorporeaIndex.InputHandler.getNearbyIndexes(mc.player).isEmpty()) {
			return;
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
				PacketHandler.sendToServer(new PacketIndexKeybindRequest(requested));
				event.setCanceled(true);
			}
		}
	}

	private static ItemStack getStackUnderMouse() {
		Screen screen = Minecraft.getInstance().currentScreen;
		if (screen instanceof ContainerScreen) {
			Slot slotUnderMouse = ((ContainerScreen) screen).getSlotUnderMouse();
			if (slotUnderMouse != null) {
				ItemStack stack = slotUnderMouse.getStack().copy();
				stack.setTag(null); // Wipe NBT of inventory items before request, as player items will often have data
				return stack; // that's better to ignore. This is still an improvement over matching names only.
			}
		}

		if (screen instanceof InventoryScreen && ((InventoryScreen) screen).getRecipeGui().isVisible()) {
			RecipeBookGui recipeBook = ((InventoryScreen) screen).getRecipeGui();
			RecipeBookPage page = ((AccessorRecipeBookGui) recipeBook).getRecipeBookPage();
			RecipeWidget widget = ((AccessorRecipeBookPage) page).getHoveredButton();
			if (widget != null) {
				return widget.getRecipe().getRecipeOutput();
			}
		}

		return jeiPanelSupplier.get();
	}
}
