/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.corporea;

import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;

import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class CorporeaStringMatcher implements CorporeaRequestMatcher {

	private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
	public static final String[] WILDCARD_STRINGS = {"...", "~", "+", "?"};
	private static final String TAG_REQUEST_CONTENTS = "requestContents";

	// Stored as a list of segments that must match.
	// *foo*bar is stored as "", "foo", "bar"
	// TODO: stop being public
	public final String[] expression;

	public CorporeaStringMatcher(String expression) {
		boolean contains = false;
		for (String wc : WILDCARD_STRINGS) {
			if (expression.endsWith(wc)) {
				contains = true;
				expression = expression.substring(0, expression.length() - wc.length());
			} else if (expression.startsWith(wc)) {
				contains = true;
				expression = expression.substring(wc.length());
			}

			if (contains) {
				break;
			}
		}
		this.expression = (contains ? "*" + expression + "*" : expression).split("\\*+", -1);
	}

	@Override
	public boolean test(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		// TODO: replace this with the proper `or` test
		return matchGlob(this.expression, stack.getHoverName().getString());
		/*
		if (stack.hasCustomHoverName()) {
			return testString(stack.getHoverName().getString());
		} else {
			// `getHoverName` returns the english translation without anvil renaming,
			// and we don't want that!
			return false;
		}
		 */
	}

	public static CorporeaStringMatcher createFromNBT(CompoundTag tag) {
		String expression = tag.getString(TAG_REQUEST_CONTENTS);
		return new CorporeaStringMatcher(expression);
	}

	@Override
	public void writeToNBT(CompoundTag tag) {
		tag.putString(TAG_REQUEST_CONTENTS, toString());
	}

	@Override
	@SuppressWarnings("deprecation")
	public Component getRequestName() {
		String value = WordUtils.capitalizeFully(toString());
		// cope with initial globs
		if (value.charAt(0) == '*' && value.length() >= 2) {
			value = "*" + Character.toUpperCase(value.charAt(1)) + value.substring(2);
		}
		return Component.literal(value);
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner("*");
		for (String s : expression) {
			sj.add(s);
		}
		return sj.toString();
	}

	private static boolean matchGlob(String[] expression, String str) {
		str = stripControlCodes(str.toLowerCase(Locale.ROOT).trim());

		if (expression.length == 1) {
			return expression[0].equals(str);
		}

		if (!str.startsWith(expression[0])) {
			return false;
		}

		int offset = expression[0].length();
		for (int i = 1; i < expression.length - 1; i++) {
			String section = expression[i];
			int found = str.indexOf(section, offset);
			if (found == -1) {
				return false;
			}
			offset = found + section.length();
		}
		return str.substring(offset).endsWith(expression[expression.length - 1]);
	}

	// Copy from StringUtils
	private static String stripControlCodes(String str) {
		return patternControlCode.matcher(str).replaceAll("");
	}

	/**
	 * Client side only, get the ranges of registry IDs the glob matches from the langfile
	 */
	public static RegistryRanges getRegistryRanges(String[] glob) {
		// TODO what kind of set might be fastest, RB, AVL?
		// I'm pretty sure inserting sorted is faster than inserting all and then sorting;
		// they're both O(n log n) but the n in the log n is smaller when inserting sorted because
		// there's fewer to cmp against ...
		IntSortedSet regiMatches = new IntRBTreeSet();
		for (Item item : Registry.ITEM) {
			var name = I18n.get(item.getDescriptionId());
			if (matchGlob(glob, name)) {
				var id = Registry.ITEM.getId(item);
				regiMatches.add(id);
			}
		}

		// Compose these ints into a set of ranges (for compactness over the wire)
		if (regiMatches.isEmpty()) {
			return new RegistryRanges(IntList.of(), IntSet.of(), RegistryRanges.ITEM_REGISTRY_HASH);
		}

		// guess on the out size of the list, idk if this is a good one or not
		var ranges = new IntArrayList(regiMatches.size() / 4);
		var singletons = new IntOpenHashSet(regiMatches.size() / 4);
		var regiIter = regiMatches.intIterator();
		var anchor = regiIter.nextInt();
		var prev = anchor;

		while (regiIter.hasNext()) {
			var here = regiIter.nextInt();
			assert here > prev;
			if (here != prev + 1) {
				// Then we've come to a gap
				if (anchor == prev) {
					singletons.add(anchor);
				} else {
					// Start a new range
					ranges.push(anchor);
					ranges.push(prev);
				}
				anchor = here;
			}

			prev = here;
		}
		// and do it again to get the last element
		if (anchor == prev) {
			singletons.add(anchor);
		} else {
			// Start a new range
			ranges.push(anchor);
			ranges.push(prev);
		}
		assert ranges.size() % 2 == 0;
		return new RegistryRanges(ranges, singletons, RegistryRanges.ITEM_REGISTRY_HASH);
	}

	public static class RegistryRanges {
		private static final int HASH_SEED = 0xBAD_5EED;

		/**
		 * Recalculated once each time the JVM is launched.
		 */
		public static final int ITEM_REGISTRY_HASH = Util.make(() -> {
			int hash = HASH_SEED;

			for (Item item : Registry.ITEM) {
				hash = Objects.hash(hash, Registry.ITEM.getKey(item));
			}

			return hash;
		});

		/**
		 * List of int range pairs; {@code [min0, max0, min0, max0, ...] }
		 */
		private final IntList ranges;
		/**
		 * List of singleton ranges
		 */
		private final IntSet singletons;

		/**
		 * Hash of the registry at creation time. We use this to try and detect if it's changed.
		 * If so, the IDs won't be valid anymore, so fallback to the dumb English string checking,
		 * which won't work on Quilt, but my god that's a like 1% of 1% chance you'll live
		 */
		private final int registryHash;

		private RegistryRanges(IntList ranges, IntSet singletons, int registryHash) {
			this.ranges = ranges;
			this.singletons = singletons;
			this.registryHash = registryHash;
		}

		@Override
		public String toString() {
			var bob = new StringBuilder("RegistryRanges[");
			for (int i = 0; i < this.ranges.size(); i += 2) {
				bob.append(this.ranges.getInt(i));
				bob.append('-');
				bob.append(this.ranges.getInt(i + 1));
				if (i < this.ranges.size() - 2) {
					bob.append(',');
				} else {
					bob.append(';');
				}
				bob.append(' ');
			}

			var first = true;
			for (var item : this.singletons) {
				if (!first) {
					bob.append(", ");
				}
				bob.append(item);
				first = false;
			}

			bob.append("; #");
			bob.append(this.registryHash);
			bob.append(']');
			return bob.toString();
		}
	}
}
