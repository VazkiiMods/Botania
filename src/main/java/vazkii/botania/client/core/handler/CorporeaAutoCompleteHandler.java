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
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
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
		Iterator<Item> iterator = Item.REGISTRY.iterator();
		NonNullList<ItemStack> curList = NonNullList.create();

		while(iterator.hasNext()) {
			Item item = iterator.next();

			if(item != null && item.getCreativeTab() != null) {
				curList.clear();
				try {
					item.getSubItems(null, curList);
					for(ItemStack stack : curList)
						itemNames.add(CorporeaHelper.stripControlCodes(stack.getDisplayName().trim()));
				}
				catch (Exception e) {}
			}
		}
	}

	@SubscribeEvent
	public void onTick(ClientTickEvent event) {
		if(event.phase != Phase.END)
			return;
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if(!(screen instanceof GuiChat)) {
			isAutoCompleted = false;
			return;
		}
		GuiChat chat = (GuiChat) screen;
		if(isAutoCompleted) {
			boolean valid = false;//ReflectionHelper.getPrivateValue(GuiChat.class, chat, LibObfuscation.COMPLETE_FLAG);
			if(!valid)
				isAutoCompleted = false;
		}
		if(Keyboard.isKeyDown(15)) {
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

	private void advanceAutoComplete(GuiTextField inputField, GuiChat chat) {
		position++;
		if(position >= completions.size())
			position -= completions.size();
		CompletionData data = completions.get(position);
		String str = originalString.substring(0, originalString.length() - data.prefixLength) + data.string;
		inputField.setText(str);
	}

	private void buildAutoCompletes(GuiTextField inputField, GuiChat chat) {
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

		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(stringbuilder.toString()), 1);
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
