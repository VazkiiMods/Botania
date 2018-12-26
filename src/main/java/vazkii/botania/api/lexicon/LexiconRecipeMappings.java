/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 6, 2014, 3:54:12 PM (GMT)]
 */
package vazkii.botania.api.lexicon;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.mana.IManaItem;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains mappings for which entry and page correspond to each
 * craftable ItemStack. Use the map method to map an ItemStack to a page in
 * an entry in the lexicon.
 */
public final class LexiconRecipeMappings {

	private static final Map<String, EntryData> mappings = new HashMap<>();

	/**
	 * Maps the given stack to the given page of the entry.
	 */
	public static void map(ItemStack stack, LexiconEntry entry, int page, boolean force) {
		EntryData data = new EntryData(entry, page);
		String str = stackToString(stack);

		if(force || !mappings.containsKey(str))
			mappings.put(str, data);
		if(entry.getIcon().isEmpty())
			entry.setIcon(stack.copy());
	}

	public static void map(ItemStack stack, LexiconEntry entry, int page) {
		map(stack, entry, page, false);
	}

	public static void remove(ItemStack stack) {
		mappings.remove(stackToString(stack));
	}

	public static EntryData getDataForStack(ItemStack stack) {
		String wildKey = stackToString(stack, true);
		if (mappings.containsKey(wildKey))
			return mappings.get(wildKey);
		return mappings.get(stackToString(stack));
	}

	public static String stackToString(ItemStack stack) {
		return stackToString(stack, false);
	}

	public static String stackToString(ItemStack stack, boolean forceIgnore) {
		if(stack.isEmpty())
			return "NULL";

		if(stack.hasTagCompound() && stack.getItem() instanceof IRecipeKeyProvider)
			return ((IRecipeKeyProvider) stack.getItem()).getKey(stack);

		return stack.getTranslationKey() + (forceIgnore || ignoreMeta(stack) ? "" : "~" + stack.getItemDamage());
	}

	public static boolean ignoreMeta(ItemStack stack) {
		return stack.isItemStackDamageable() || stack.getItem() instanceof IManaItem || stack.getItemDamage() == OreDictionary.WILDCARD_VALUE;
	}

	public static class EntryData {

		public final LexiconEntry entry;
		public final int page;

		public EntryData(LexiconEntry entry, int page) {
			this.entry = entry;
			this.page = page;
		}

	}
}
