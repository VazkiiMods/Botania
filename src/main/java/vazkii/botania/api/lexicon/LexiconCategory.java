/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 6:23:47 PM (GMT)]
 */
package vazkii.botania.api.lexicon;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class LexiconCategory implements Comparable<LexiconCategory> {

	private static int count = 0;

	public final String unlocalizedName;
	public final List<LexiconEntry> entries = new ArrayList<>();
	private final int sortingId;
	private ResourceLocation icon;
	private int priority = 5;

	/**
	 * @param unlocalizedName The unlocalized name of this category. This will be localized by the client display.
	 */
	public LexiconCategory(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
		sortingId = count;
		count++;
	}

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	/**
	 * Sets the priority for this category for sorting. Higher numbers
	 * means they'll appear first in the book. The basics category
	 * is 9, the miscellaneous category is 0, other vanilla botania categories
	 * are 5. Using 9 and 0 is <b>not</b> recommended, since having your
	 * categories before basics or after miscellaneous is a bad idea.
	 * If two categories have the same priority they'll be sorted
	 * by insertion order.
	 */
	public LexiconCategory setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public int getSortingPriority() {
		return priority;
	}

	public final int getSortingId() {
		return sortingId;
	}

	public LexiconCategory setIcon(ResourceLocation icon) {
		this.icon = icon;
		return this;
	}

	public ResourceLocation getIcon() {
		return icon;
	}

	@Override
	public int compareTo(@Nonnull LexiconCategory category) {
		return priority == category.priority ? sortingId - category.sortingId : category.priority - priority;
	}
}
