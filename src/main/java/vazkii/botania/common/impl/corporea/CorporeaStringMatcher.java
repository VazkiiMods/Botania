/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl.corporea;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.text.WordUtils;

import vazkii.botania.api.corporea.ICorporeaRequestMatcher;

import java.util.regex.Pattern;

public class CorporeaStringMatcher implements ICorporeaRequestMatcher {

	private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
	public static final String[] WILDCARD_STRINGS = { "...", "~", "+", "?", "*" };
	private static final String TAG_REQUEST_CONTENTS = "requestContents";
	private static final String TAG_REQUEST_CONTAINS = "requestContains";

	private final String expression;
	private final boolean contains;

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
		this.expression = expression;
		this.contains = contains;
	}

	private CorporeaStringMatcher(String expression, boolean contains) {
		this.expression = expression;
		this.contains = contains;
	}

	@Override
	public boolean isStackValid(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		String name = stripControlCodes(stack.getName().getString().toLowerCase().trim());
		return equalOrContain(name)
				|| equalOrContain(name + "s")
				|| equalOrContain(name + "es")
				|| name.endsWith("y") && equalOrContain(name.substring(0, name.length() - 1) + "ies");
	}

	public static CorporeaStringMatcher createFromNBT(CompoundTag tag) {
		String expression = tag.getString(TAG_REQUEST_CONTENTS);
		boolean contains = tag.getBoolean(TAG_REQUEST_CONTAINS);
		return new CorporeaStringMatcher(expression, contains);
	}

	@Override
	public void writeToNBT(CompoundTag tag) {
		tag.putString(TAG_REQUEST_CONTENTS, expression);
		tag.putBoolean(TAG_REQUEST_CONTAINS, contains);
	}

	@Override
	@SuppressWarnings("deprecation")
	public Text getRequestName() {
		return new LiteralText(WordUtils.capitalizeFully(expression));
	}

	private boolean equalOrContain(String str) {
		return contains ? str.contains(expression) : str.equals(expression);
	}

	// Copy from StringUtils
	private static String stripControlCodes(String str) {
		return patternControlCode.matcher(str).replaceAll("");
	}
}
