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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;

public final class ItemNBTHelper {

	private static final int[] EMPTY_INT_ARRAY = new int[0];

	/** Gets the NBTTagCompound in an ItemStack. Tries to init it
	 * previously in case there isn't one present **/
	public static NBTTagCompound getNBT(ItemStack stack) {
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		return stack.getTagCompound();
	}

	// SETTERS ///////////////////////////////////////////////////////////////////

	public static void setBoolean(ItemStack stack, String tag, boolean b) {
		getNBT(stack).setBoolean(tag, b);
	}

	public static void setByte(ItemStack stack, String tag, byte b) {
		getNBT(stack).setByte(tag, b);
	}

	public static void setShort(ItemStack stack, String tag, short s) {
		getNBT(stack).setShort(tag, s);
	}

	public static void setInt(ItemStack stack, String tag, int i) {
		getNBT(stack).setInteger(tag, i);
	}

	public static void setIntArray(ItemStack stack, String tag, int[] val) {
		getNBT(stack).setIntArray(tag, val);
	}

	public static void setLong(ItemStack stack, String tag, long l) {
		getNBT(stack).setLong(tag, l);
	}

	public static void setFloat(ItemStack stack, String tag, float f) {
		getNBT(stack).setFloat(tag, f);
	}

	public static void setDouble(ItemStack stack, String tag, double d) {
		getNBT(stack).setDouble(tag, d);
	}

	public static void setCompound(ItemStack stack, String tag, NBTTagCompound cmp) {
		if(!tag.equalsIgnoreCase("ench")) // not override the enchantments
			getNBT(stack).setTag(tag, cmp);
	}

	public static void setString(ItemStack stack, String tag, String s) {
		getNBT(stack).setString(tag, s);
	}

	public static void setList(ItemStack stack, String tag, NBTTagList list) {
		getNBT(stack).setTag(tag, list);
	}

	public static void removeEntry(ItemStack stack, String tag) {
		getNBT(stack).removeTag(tag);
	}

	// GETTERS ///////////////////////////////////////////////////////////////////

	public static boolean verifyExistance(ItemStack stack, String tag) {
		return !stack.isEmpty() && getNBT(stack).hasKey(tag);
	}

	public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected) {
		return verifyExistance(stack, tag) ? getNBT(stack).getBoolean(tag) : defaultExpected;
	}

	public static byte getByte(ItemStack stack, String tag, byte defaultExpected) {
		return verifyExistance(stack, tag) ? getNBT(stack).getByte(tag) : defaultExpected;
	}

	public static short getShort(ItemStack stack, String tag, short defaultExpected) {
		return verifyExistance(stack, tag) ? getNBT(stack).getShort(tag) : defaultExpected;
	}

	public static int getInt(ItemStack stack, String tag, int defaultExpected) {
		return verifyExistance(stack, tag) ? getNBT(stack).getInteger(tag) : defaultExpected;
	}

	public static int[] getIntArray(ItemStack stack, String tag) {
		return verifyExistance(stack, tag) ? getNBT(stack).getIntArray(tag) : EMPTY_INT_ARRAY;
	}

	public static long getLong(ItemStack stack, String tag, long defaultExpected) {
		return verifyExistance(stack, tag) ? getNBT(stack).getLong(tag) : defaultExpected;
	}

	public static float getFloat(ItemStack stack, String tag, float defaultExpected) {
		return verifyExistance(stack, tag) ? getNBT(stack).getFloat(tag) : defaultExpected;
	}

	public static double getDouble(ItemStack stack, String tag, double defaultExpected) {
		return verifyExistance(stack, tag) ? getNBT(stack).getDouble(tag) : defaultExpected;
	}

	/** If nullifyOnFail is true it'll return null if it doesn't find any
	 * compounds, otherwise it'll return a new one. **/
	public static NBTTagCompound getCompound(ItemStack stack, String tag, boolean nullifyOnFail) {
		return verifyExistance(stack, tag) ? getNBT(stack).getCompoundTag(tag) : nullifyOnFail ? null : new NBTTagCompound();
	}

	public static String getString(ItemStack stack, String tag, String defaultExpected) {
		return verifyExistance(stack, tag) ? getNBT(stack).getString(tag) : defaultExpected;
	}

	public static NBTTagList getList(ItemStack stack, String tag, int objtype, boolean nullifyOnFail) {
		return verifyExistance(stack, tag) ? getNBT(stack).getTagList(tag, objtype) : nullifyOnFail ? null : new NBTTagList();
	}

	/**
	 * Returns true if the `target` tag contains all of the tags and values present in the `template` tag. Recurses into
	 * compound tags and matches all template keys and values; recurses into list tags and matches the template against
	 * the first elements of target. Empty lists and compounds in the template will match target lists and compounds of
	 * any size.
	 */

	public static boolean matchTag(@Nullable NBTBase template, @Nullable NBTBase target) {
		if(template instanceof NBTTagCompound && target instanceof NBTTagCompound) {
			return matchTagCompound((NBTTagCompound) template, (NBTTagCompound) target);
		} else if(template instanceof NBTTagList && target instanceof NBTTagList) {
			return matchTagList((NBTTagList) template, (NBTTagList) target);
		} else {
			return template == null || (target != null && target.equals(template));
		}
	}

	private static boolean matchTagCompound(NBTTagCompound template, NBTTagCompound target) {
		if(template.getSize() > target.getSize()) return false;
		
		for(String key : template.getKeySet()) {
			if (!matchTag(template.getTag(key), target.getTag(key))) return false;
		}
		
		return true;
	}

	private static boolean matchTagList(NBTTagList template, NBTTagList target) {
		if (template.tagCount() > target.tagCount()) return false;

		for (int i = 0; i < template.tagCount(); i++) {
			if (!matchTag(template.get(i), target.get(i))) return false;
		}

		return true;
	}

}
