/**
 * This class was created by <Alwinfy>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Dec 22, 2019, 11:13:14 PM (GMT)]
 */
package vazkii.botania.api.corporea;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.text.WordUtils;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.regex.Pattern;

/**
 * An interface for a Corporea Request matcher. Accepts an ItemStack and returns whether it fulfills the request.
 * Needs to be registered over in {@link vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer#addCorporeaRequestMatcher}.
 */
public final class CorporeaRequestDefaultMatchers {

	public static class CorporeaStringMatcher implements ICorporeaRequestMatcher {

		private static final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
		public static final String[] WILDCARD_STRINGS = { "...", "~", "+", "?" , "*" };
		private static final String TAG_REQUEST_CONTENTS = "requestContents";
		private static final String TAG_REQUEST_CONTAINS = "requestContains";

		private final String expression;
		private final boolean contains;

		public CorporeaStringMatcher(String expression) {
			boolean contains = false;
			for(String wc : WILDCARD_STRINGS) {
				if(expression.endsWith(wc)) {
					contains = true;
					expression = expression.substring(0, expression.length() - wc.length());
				}
				else if(expression.startsWith(wc)) {
					contains = true;
					expression = expression.substring(wc.length());
				}

				if(contains)
					break;
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
			if(stack.isEmpty())
				return false;

			String name = stripControlCodes(stack.getDisplayName().getString().toLowerCase().trim());
			return equalOrContain(name)
				|| equalOrContain(name + "s")
				|| equalOrContain(name + "es")
				|| name.endsWith("y") && equalOrContain(name.substring(0, name.length() - 1) + "ies");
		}

		public static ICorporeaRequestMatcher createFromNBT(CompoundNBT tag) {
			String expression = tag.getString(TAG_REQUEST_CONTENTS);
			boolean contains = tag.getBoolean(TAG_REQUEST_CONTAINS);
			return new CorporeaStringMatcher(expression, contains);
		}

		@Override
		public void writeToNBT(CompoundNBT tag) {
			tag.putString(TAG_REQUEST_CONTENTS, expression);
			tag.putBoolean(TAG_REQUEST_CONTAINS, contains);
		}

		@Override
		@SuppressWarnings("deprecation")
		public ITextComponent getRequestName() {
			return new StringTextComponent(WordUtils.capitalizeFully(expression));
		}

		private boolean equalOrContain(String str) {
			return contains ? str.contains(expression) : str.equals(expression);
		}

		// Copy from StringUtils
		private static String stripControlCodes(String str) {
			return patternControlCode.matcher(str).replaceAll("");
		}
	}

	public static class CorporeaItemStackMatcher implements ICorporeaRequestMatcher {
		private static final String TAG_REQUEST_STACK = "requestStack";
		private static final String TAG_REQUEST_CHECK_NBT = "requestCheckNBT";

		private final ItemStack match;
		private final boolean checkNBT;

		public CorporeaItemStackMatcher(ItemStack match, boolean checkNBT) {
			this.match = match;
			this.checkNBT = checkNBT;
		}

		@Override
		public boolean isStackValid(ItemStack stack) {
			return !stack.isEmpty() && !match.isEmpty() && stack.isItemEqual(match) && (!checkNBT || ItemNBTHelper.matchTag(match.getTag(), stack.getTag()));
		}

		public static ICorporeaRequestMatcher createFromNBT(CompoundNBT tag) {
			return new CorporeaItemStackMatcher(ItemStack.read(tag.getCompound(TAG_REQUEST_STACK)), tag.getBoolean(TAG_REQUEST_CHECK_NBT));
		}

		@Override
		public void writeToNBT(CompoundNBT tag) {
			CompoundNBT cmp = match.write(new CompoundNBT());
			tag.put(TAG_REQUEST_STACK, cmp);
			tag.putBoolean(TAG_REQUEST_CHECK_NBT, checkNBT);
		}

		@Override
		public ITextComponent getRequestName() {
			return match.getTextComponent();
		}
	}

	private CorporeaRequestDefaultMatchers() {}
}
