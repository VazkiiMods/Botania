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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.lib.LibMisc;

import java.util.function.Predicate;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, value = Dist.CLIENT)
public class CorporeaInputHandler {

	/** Replaced in JEIBotaniaPlugin when JEI's loaded to provide stacks from the JEI item panel. */
	public static Supplier<ItemStack> jeiPanelSupplier = () -> ItemStack.EMPTY;

	/** Filter for usable guis to handle requests. Added to in JEIBotaniaPlugin */
	public static Predicate<Screen> supportedGuiFilter = gui -> gui instanceof ContainerScreen;

	@SubscribeEvent
	public static void buttonPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		if(mc.world == null || !supportedGuiFilter.test(mc.currentScreen)
				|| ClientProxy.CORPOREA_REQUEST.isActiveAndMatches(InputMappings.getInputByCode(event.getKeyCode(), event.getScanCode()))
				|| TileCorporeaIndex.InputHandler.getNearbyIndexes(mc.player).isEmpty())
			return;

		ItemStack stack = getStackUnderMouse();
		if(stack != null && !stack.isEmpty()) {
			int count = 1;
			int max = stack.getMaxStackSize();

			if(Screen.hasShiftDown()) {
				count = max;
				if(Screen.hasControlDown())
					count /= 4;
			} else if(Screen.hasControlDown())
				count = max / 2;

			if(count > 0) {
				String full = count + " " + stack.getDisplayName().getString();

				mc.ingameGUI.getChatGUI().addToSentMessages(full);
				mc.player.sendChatMessage(full);
				event.setCanceled(true);
			}
		}
	}

	private static ItemStack getStackUnderMouse() {
		Screen screen = Minecraft.getInstance().currentScreen;
		if(screen instanceof ContainerScreen) {
			Slot slotUnderMouse = ((ContainerScreen) screen).getSlotUnderMouse();
			if(slotUnderMouse != null)
				return slotUnderMouse.getStack();
		}
		return jeiPanelSupplier.get();
	}
}
