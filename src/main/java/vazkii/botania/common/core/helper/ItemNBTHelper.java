/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the ThaumicTinkerer Mod.
 *
 * ThaumicTinkerer is Open Source and distributed under a
 * Botania License: http://botaniamod.net/license.php
 *
 * ThaumicTinkerer is a Derivative Work on Thaumcraft 4.
 * Thaumcraft 4 (c) Azanor 2012
 * (http://www.minecraftforum.net/topic/1585216-)
 *
 * File Created @ [8 Sep 2013, 19:36:25 (GMT)]
 */
package vazkii.botania.common.core.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import javax.annotation.Nullable;
import java.util.UUID;

public final class ItemNBTHelper {

	private static final int[] EMPTY_INT_ARRAY = new int[0];

	// SETTERS ///////////////////////////////////////////////////////////////////

	public static void setBoolean(ItemStack stack, String tag, boolean b) {
		stack.getOrCreateTag().putBoolean(tag, b);
	}

	public static void setByte(ItemStack stack, String tag, byte b) {
		stack.getOrCreateTag().putByte(tag, b);
	}

	public static void setShort(ItemStack stack, String tag, short s) {
		stack.getOrCreateTag().putShort(tag, s);
	}

	public static void setInt(ItemStack stack, String tag, int i) {
		stack.getOrCreateTag().putInt(tag, i);
	}

	public static void setIntArray(ItemStack stack, String tag, int[] val) {
		stack.getOrCreateTag().putIntArray(tag, val);
	}

	public static void setLong(ItemStack stack, String tag, long l) {
		stack.getOrCreateTag().putLong(tag, l);
	}

	public static void setFloat(ItemStack stack, String tag, float f) {
		stack.getOrCreateTag().putFloat(tag, f);
	}

	public static void setDouble(ItemStack stack, String tag, double d) {
		stack.getOrCreateTag().putDouble(tag, d);
	}

	public static void setCompound(ItemStack stack, String tag, CompoundNBT cmp) {
		if(!tag.equalsIgnoreCase("ench")) // not override the enchantments
			stack.getOrCreateTag().put(tag, cmp);
	}

	public static void setString(ItemStack stack, String tag, String s) {
		stack.getOrCreateTag().putString(tag, s);
	}

	public static void setUuid(ItemStack stack, String tag, UUID value) {
		stack.getOrCreateTag().putUniqueId(tag, value);
	}

	public static void setList(ItemStack stack, String tag, ListNBT list) {
		stack.getOrCreateTag().put(tag, list);
	}

	public static void removeEntry(ItemStack stack, String tag) {
		stack.getOrCreateTag().remove(tag);
	}

	// GETTERS ///////////////////////////////////////////////////////////////////

	public static boolean verifyExistance(ItemStack stack, String tag) {
		return !stack.isEmpty() && stack.getOrCreateTag().contains(tag);
	}

	public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getBoolean(tag) : defaultExpected;
	}

	public static byte getByte(ItemStack stack, String tag, byte defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getByte(tag) : defaultExpected;
	}

	public static short getShort(ItemStack stack, String tag, short defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getShort(tag) : defaultExpected;
	}

	public static int getInt(ItemStack stack, String tag, int defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getInt(tag) : defaultExpected;
	}

	public static int[] getIntArray(ItemStack stack, String tag) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getIntArray(tag) : EMPTY_INT_ARRAY;
	}

	public static long getLong(ItemStack stack, String tag, long defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getLong(tag) : defaultExpected;
	}

	public static float getFloat(ItemStack stack, String tag, float defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getFloat(tag) : defaultExpected;
	}

	public static double getDouble(ItemStack stack, String tag, double defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getDouble(tag) : defaultExpected;
	}

	/** If nullifyOnFail is true it'll return null if it doesn't find any
	 * compounds, otherwise it'll return a new one. **/
	public static CompoundNBT getCompound(ItemStack stack, String tag, boolean nullifyOnFail) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getCompound(tag) : nullifyOnFail ? null : new CompoundNBT();
	}

	public static String getString(ItemStack stack, String tag, String defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getString(tag) : defaultExpected;
	}

	@Nullable
	public static UUID getUuid(ItemStack stack, String tag) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getUniqueId(tag) : null;
	}

	public static ListNBT getList(ItemStack stack, String tag, int objtype, boolean nullifyOnFail) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getList(tag, objtype) : nullifyOnFail ? null : new ListNBT();
	}

	/**
	 * Returns true if the `target` tag contains all of the tags and values present in the `template` tag. Recurses into
	 * compound tags and matches all template keys and values; recurses into list tags and matches the template against
	 * the first elements of target. Empty lists and compounds in the template will match target lists and compounds of
	 * any size.
	 */
	public static boolean matchTag(@Nullable INBT template, @Nullable INBT target) {
		if(template instanceof CompoundNBT && target instanceof CompoundNBT) {
			return matchTagCompound((CompoundNBT) template, (CompoundNBT) target);
		} else if(template instanceof ListNBT && target instanceof ListNBT) {
			return matchTagList((ListNBT) template, (ListNBT) target);
		} else {
			return template == null || (target != null && target.equals(template));
		}
	}

	private static boolean matchTagCompound(CompoundNBT template, CompoundNBT target) {
		if(template.size() > target.size()) return false;
		
		for(String key : template.keySet()) {
			if (!matchTag(template.get(key), target.get(key))) return false;
		}
		
		return true;
	}

	private static boolean matchTagList(ListNBT template, ListNBT target) {
		if (template.size() > target.size()) return false;

		for (int i = 0; i < template.size(); i++) {
			if (!matchTag(template.get(i), target.get(i))) return false;
		}

		return true;
	}

}
