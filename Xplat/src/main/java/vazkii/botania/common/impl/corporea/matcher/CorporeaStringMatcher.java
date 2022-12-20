/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.corporea.matcher;

import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.Util;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;

import java.util.Arrays;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class CorporeaStringMatcher implements CorporeaRequestMatcher {

	private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
	public static final String[] WILDCARD_STRINGS = {"...", "~", "+", "?"};

	// TODO: remove this when 1.20 comes around
	private static final String TAG_REQUEST_CONTENTS_LEGACY = "requestContents";

	private static final String TAG_GLOB_CONTENTS = "glob";
	private static final String TAG_RANGES_RANGES = "ranges";
	private static final String TAG_RANGES_SINGLETONS = "singletons";
	private static final String TAG_RANGES_HASH = "hash";


	// Stored as a list of segments that must match.
	// *foo*bar is stored as "", "foo", "bar"
	// TODO: stop being public
	private final String[] expression;
	private final RegistryRanges ranges;
	// True if the current registry hash is different than one loaded from NBT.
	//
	// This will only be true when:
	// - A request is intercepted
	// - The server is closed
	// - Mods are added/removed
	// - The server is reopened
	// and in that edge case we log an error and fallback to english string matching on the server


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

		this.ranges = getRegistryRanges(this.expression);
	}

	private CorporeaStringMatcher(String[] expression, RegistryRanges ranges) {
		this.expression = expression;
		this.ranges = ranges;
	}

	@Override
	public boolean test(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		if (this.ranges.registryHash != RegistryRanges.ITEM_REGISTRY_HASH) {
			// then something has gone wrong, and to *try* and make it work do the english search
			return matchGlob(this.expression, stack.getHoverName().getString());
		}

		if (stack.hasCustomHoverName()) {
			// TODO: do we want to *not* get items with anvilled names?
			// if we search `iron ingot` do we want to get an ingot named something else?
			// for now I will write we do but that might change
			if (matchGlob(this.expression, stack.getHoverName().getString())) {
				return true;
			}
		}
		return this.ranges.containsItem(stack.getItem());
	}

	public static CorporeaStringMatcher createFromNBT(CompoundTag tag) {
		if (tag.contains(TAG_REQUEST_CONTENTS_LEGACY, Tag.TAG_LIST)) {
			return new CorporeaStringMatcher(tag.getString(TAG_REQUEST_CONTENTS_LEGACY));
		}

		ListTag contents = tag.getList(TAG_GLOB_CONTENTS, Tag.TAG_STRING);
		String[] expr = new String[contents.size()];
		for (int i = 0; i < contents.size(); i++) {
			expr[i] = contents.getString(i);
		}

		int[] ranges = tag.getIntArray(TAG_RANGES_RANGES);
		int[] singletons = tag.getIntArray(TAG_RANGES_SINGLETONS);
		int hash = tag.getInt(TAG_RANGES_HASH);

		if (hash != RegistryRanges.ITEM_REGISTRY_HASH) {
			BotaniaAPI.LOGGER.warn("Registry hash when a CorporeaStringMatcher was written to NBT ({}) is different " +
					"than the current hash ({}). " +
					"This request will fall back to a server-side I18n check.",
				hash, RegistryRanges.ITEM_REGISTRY_HASH);
		}
		return new CorporeaStringMatcher(expr, new RegistryRanges(ranges, IntSet.of(singletons), hash));
	}

	@Override
	public void writeToNBT(CompoundTag tag) {
		ListTag contents = new ListTag();
		for (var part : this.expression) {
			contents.add(StringTag.valueOf(part));
		}
		tag.put(TAG_GLOB_CONTENTS, contents);
		tag.putIntArray(TAG_RANGES_RANGES, this.ranges.ranges);
		tag.putIntArray(TAG_RANGES_SINGLETONS, this.ranges.singletons.toIntArray());
		tag.putInt(TAG_RANGES_HASH, this.ranges.registryHash);
	}

	public static CorporeaStringMatcher createFromBuf(FriendlyByteBuf buf) {
		var globLen = buf.readVarInt();
		var expr = new String[globLen];
		for (int i = 0; i < globLen; i++) {
			expr[i] = buf.readUtf();
		}

		var ranges = buf.readVarIntArray();
		var singletons = buf.readVarIntArray();
		var hash = buf.readVarInt();

		return new CorporeaStringMatcher(expr, new RegistryRanges(ranges, IntSet.of(singletons), hash));
	}

	@Override
	public void writeToBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(this.expression.length);
		for (var part : this.expression) {
			buf.writeUtf(part);
		}

		// TODO: we could probably write this more efficiently
		// for example, on games with very large registries, it would probably be faster to send
		// sorted deltas of the singletons array...
		buf.writeVarIntArray(this.ranges.ranges);
		buf.writeVarIntArray(this.ranges.singletons.toIntArray());
		// If the hash is not the same on the client and the server there's
		// already going to be waaaaaaaaaaaay bigger problems
		// but we do need to put *something* there on the server and it's like 4 extra bytes we'll live
		buf.writeVarInt(this.ranges.registryHash);
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
			return new RegistryRanges(new int[0], IntSet.of(), RegistryRanges.ITEM_REGISTRY_HASH);
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
		return new RegistryRanges(ranges.toIntArray(), singletons, RegistryRanges.ITEM_REGISTRY_HASH);
	}

	/**
	 * @param ranges       List of int range pairs; {@code [min0, max0, min1, max1, ...] }. Inclusive.
	 * @param singletons   List of singleton ranges
	 * @param registryHash Hash of the registry at creation time. We use this to try and detect if it's changed.
	 *                     If so, the IDs won't be valid anymore, so fallback to the dumb English string checking,
	 *                     which won't work on Quilt, but my god that's a like 1% of 1% chance you'll live
	 */
	private record RegistryRanges(int[] ranges, IntSet singletons, int registryHash) {
		private static final int HASH_SEED = 0xBAD_5EED;

		/**
		 * Recalculated once each time the JVM is launched.
		 */
		public static final int ITEM_REGISTRY_HASH = Util.make(() -> {
			int hash = HASH_SEED;

			// It looks like the for loop is not the same order every time, so use plain addition,
			// which is associative, so it doesn't matter the order
			for (Item item : Registry.ITEM) {
				hash += Registry.ITEM.getKey(item).hashCode();
			}

			return hash;
		});

		public boolean containsItem(Item item) {
			var id = Registry.ITEM.getId(item);
			// TODO: is it faster on average to check set then ranges, or vice versa?

			if (this.singletons.contains(id)) {
				return true;
			}

			int searchIdx = Arrays.binarySearch(this.ranges, id);
			if (searchIdx >= 0) {
				// then we hit one of the edges of a range, so it's in
				return true;
			} else {
				int insertionPoint = -searchIdx + 1;
				// If we have a list [1, 2, 10, 20], the "good" places to insert the number are:
				// [1 HERE 2 10 HERE 20]
				// that is, when the insertion index is odd
				return insertionPoint % 2 == 1;
			}
		}

		@Override
		public String toString() {
			var bob = new StringBuilder("RegistryRanges[");
			for (int i = 0; i < this.ranges.length; i += 2) {
				bob.append(this.ranges[i]);
				bob.append('-');
				bob.append(this.ranges[i + 1]);
				if (i < this.ranges.length - 2) {
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
