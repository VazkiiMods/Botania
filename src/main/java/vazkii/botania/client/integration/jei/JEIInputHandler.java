/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [04/07/2018, ‏‎19:16:14 (GMT)]
 */
package vazkii.botania.client.integration.jei;

import mezz.jei.api.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;

public class JEIInputHandler {

	private final IJeiRuntime jeiRuntime;
	private boolean wasPressed = false;

	public JEIInputHandler(IJeiRuntime jeiRuntime) {
		this.jeiRuntime = jeiRuntime;
	}

	@SubscribeEvent
	public void buttonPressed(KeyboardInputEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.world == null || !(mc.currentScreen instanceof GuiContainer)
				|| TileCorporeaIndex.InputHandler.getNearbyIndexes(mc.player).isEmpty())
			return;
		
		int keybind = JEIBotaniaPlugin.CORPOREA_REQUEST.getKeyCode();
		if(keybind <= 0 || keybind > 255)
			return; //isKeyDown can't handle those values, and we can't query the keybind directly on this event

		if(Keyboard.isKeyDown(keybind)) {
			if(!wasPressed) {
				wasPressed = true;
				ItemStack stack = getStackUnderMouse();

				if(stack != null) {
					int count = 1;
					int max = stack.getMaxStackSize();

					if(GuiScreen.isShiftKeyDown()) {
						count = max;
						if(GuiScreen.isCtrlKeyDown())
							count /= 4;
					} else if(GuiScreen.isCtrlKeyDown())
						count = max / 2;

					if(count > 0) {
						String name = CorporeaHelper.stripControlCodes(stack.getDisplayName());
						String full = count + " " + name;

						mc.ingameGUI.getChatGUI().addToSentMessages(full);
						mc.player.sendChatMessage(full);
					}
				}
			}
		} else {
			wasPressed = false;
		}
	}

	private ItemStack getStackUnderMouse() {
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if(screen instanceof GuiContainer) {
			Slot slotUnderMouse = ((GuiContainer) screen).getSlotUnderMouse();
			if(slotUnderMouse != null) {
				ItemStack stack = slotUnderMouse.getStack();
				if(!stack.isEmpty())
					return stack;
			}
		}
		Object o = jeiRuntime.getIngredientListOverlay().getIngredientUnderMouse();
		if(o instanceof ItemStack)
			return (ItemStack) o;
		
		return null;
	}
}
