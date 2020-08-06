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

import java.util.StringJoiner;
import java.util.regex.Pattern;

public class CorporeaStringMatcher implements ICorporeaRequestMatcher {

	private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
	public static final String[] WILDCARD_STRINGS = { "...", "~", "+", "?" };
	private static final String TAG_REQUEST_CONTENTS = "requestContents";

	private final String[] expression;

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
		this.expression = (contains ? expression : "*" + expression + "*").split("\\*+", -1);
	}

	@Override
	public boolean test(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		String name = stripControlCodes(stack.getName().getString().toLowerCase().trim());
		return matchGlob(name)
				|| matchGlob(name + "s")
				|| matchGlob(name + "es")
				|| name.endsWith("y") && matchGlob(name.substring(0, name.length() - 1) + "ies");
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
	public Text getRequestName() {
		return new LiteralText(WordUtils.capitalizeFully(toString()));
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner("*");
		for (String s : expression) {
			sj.add(s);
		}
		return sj.toString();
	}

	private boolean matchGlob(String str) {
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
}
