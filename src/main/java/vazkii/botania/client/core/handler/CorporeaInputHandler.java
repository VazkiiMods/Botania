/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * <p>
 * File Created @ [July 04, 2018, 19:16:14 (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.lib.LibMisc;

import java.util.function.Predicate;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, value = Side.CLIENT)
public class CorporeaInputHandler {

	/** Replaced in JEIBotaniaPlugin when JEI's loaded to provide stacks from the JEI item panel. */
	public static Supplier<ItemStack> jeiPanelSupplier = () -> ItemStack.EMPTY;

	/** Filter for usable guis to handle requests. Added to in JEIBotaniaPlugin */
	public static Predicate<GuiScreen> supportedGuiFilter = gui -> gui instanceof GuiContainer;

	@SubscribeEvent
	public static void buttonPressed(KeyboardInputEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.world == null || !supportedGuiFilter.test(mc.currentScreen)
				|| Keyboard.getEventKey() != ClientProxy.CORPOREA_REQUEST.getKeyCode()
				|| !Keyboard.getEventKeyState()
				|| Keyboard.isRepeatEvent()
				|| TileCorporeaIndex.InputHandler.getNearbyIndexes(mc.player).isEmpty())
			return;

		ItemStack stack = getStackUnderMouse();
		if(stack != null && !stack.isEmpty()) {
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
				event.setCanceled(true);
			}
		}
	}

	private static ItemStack getStackUnderMouse() {
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if(screen instanceof GuiContainer) {
			Slot slotUnderMouse = ((GuiContainer) screen).getSlotUnderMouse();
			if(slotUnderMouse != null)
				return slotUnderMouse.getStack();
		}
		return jeiPanelSupplier.get();
	}
}
