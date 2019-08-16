/**
 * This class was created by <SoundLogic>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [June 8, 2015, 12:55:20 AM (GMT)]
 */
package vazkii.botania.client.core.handler;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.common.lib.LibObfuscation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class CorporeaAutoCompleteHandler {

	private boolean isAutoCompleted = false;
	private String originalString = "";
	private List<CompletionData> completions = new ArrayList<>();
	private int position;

	static final TreeSet<String> itemNames = new TreeSet<>(String::compareToIgnoreCase);

	private boolean tabLastTick = false;

	public static void updateItemList() {
		itemNames.clear();
		NonNullList<ItemStack> curList = NonNullList.create();

		for (Item item : ForgeRegistries.ITEMS) {
			if(item != null && item.getGroup() != null) {
				curList.clear();
				try {
					item.fillItemGroup(ItemGroup.SEARCH, curList);
					for(ItemStack stack : curList)
						itemNames.add(CorporeaHelper.stripControlCodes(stack.getDisplayName().getString().trim()));
				}
				catch (Exception e) {}
			}
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if(event.phase != TickEvent.Phase.END)
			return;
		Screen screen = Minecraft.getInstance().currentScreen;
		if(!(screen instanceof ChatScreen)) {
			isAutoCompleted = false;
			return;
		}
		ChatScreen chat = (ChatScreen) screen;
		if(isAutoCompleted) {
			boolean valid = false;//ReflectionHelper.getPrivateValue(GuiChat.class, chat, LibObfuscation.COMPLETE_FLAG);
			if(!valid)
				isAutoCompleted = false;
		}
		if(GLFW.glfwGetKey(Minecraft.getInstance().mainWindow.getHandle(), GLFW.GLFW_KEY_TAB) == GLFW.GLFW_PRESS) {
			if(tabLastTick)
				return;
			tabLastTick = true;
		} else {
			tabLastTick = false;
			return;
		}

		if(!CorporeaHelper.shouldAutoComplete())
			return;

		if(!isAutoCompleted)
			buildAutoCompletes(chat.inputField, chat);
		if(isAutoCompleted && !completions.isEmpty())
			advanceAutoComplete(chat.inputField, chat);
	}

	private void advanceAutoComplete(TextFieldWidget inputField, ChatScreen chat) {
		position++;
		if(position >= completions.size())
			position -= completions.size();
		CompletionData data = completions.get(position);
		String str = originalString.substring(0, originalString.length() - data.prefixLength) + data.string;
		inputField.setText(str);
	}

	private void buildAutoCompletes(TextFieldWidget inputField, ChatScreen chat) {
		String leftOfCursor;
		if(inputField.getCursorPosition() == 0)
			leftOfCursor = "";
		else
			leftOfCursor = inputField.getText().substring(0, inputField.getCursorPosition());
		if(leftOfCursor.length() == 0 || leftOfCursor.charAt(0) == '/')
			return;
		completions = getNames(leftOfCursor);
		if(completions.isEmpty())
			return;
		position = -1;
		//ReflectionHelper.setPrivateValue(GuiChat.class, chat, true, LibObfuscation.COMPLETE_FLAG);
		StringBuilder stringbuilder = new StringBuilder();
		CompletionData data;
		for(Iterator<CompletionData> iterator = completions.iterator(); iterator.hasNext(); stringbuilder.append(data.string)) {
			data = iterator.next();
			if(stringbuilder.length() > 0)
				stringbuilder.append(", ");
		}

		Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new StringTextComponent(stringbuilder.toString()), 1);
		isAutoCompleted = true;
		originalString = inputField.getText();
	}

	private List<CompletionData> getNames(String prefix) {
		String s = prefix.trim();
		if(s.isEmpty())
			return ImmutableList.of();

		TreeSet<CompletionData> result = new TreeSet<>();
		String[] words = s.split(" ");
		int i = words.length - 1;
		String curPrefix = words[i];
		while(i >= 0) {
			result.addAll(getNamesStartingWith(curPrefix.toLowerCase()));
			i--;
			if(i >= 0)
				curPrefix = words[i] + " " + curPrefix;
		}
		return new ArrayList<>(result);
	}

	private List<CompletionData> getNamesStartingWith(String prefix) {
		ArrayList<CompletionData> result = new ArrayList<>();
		int length = prefix.length();
		SortedSet<String> after = itemNames.tailSet(prefix);
		for(String str : after) {
			if(str.toLowerCase().startsWith(prefix))
				result.add(new CompletionData(str, length));
			else return result;
		}
		return result;
	}

	private static class CompletionData implements Comparable<CompletionData> {

		private final String string;
		private final int prefixLength;

		public CompletionData(String string, int prefixLength) {
			this.string = string;
			this.prefixLength = prefixLength;
		}

		@Override
		public int compareTo(@Nonnull CompletionData arg0) {
			return string.compareTo(arg0.string);
		}
	}

}
