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
	 * Checks that one tag is a subset of another - that is, it's ok if the superset has more NBT keys than the subset, but the subset has to have at least all the same keys as the superset, and the values of the keys that *are* shared between the two must match.
	 * 
	 * This is useful when, e.g. matching NBT tags in recipes.
	 */
	public static boolean isTagSubset(@Nullable NBTTagCompound subset, @Nullable NBTTagCompound superset) {
		//an empty set is a subset of everything
		if(subset == null || subset.isEmpty()) return true;
		//an empty set is a superset of only another empty set (which was already checked above)
		if(superset == null || superset.isEmpty()) return false;
		//a subset can't be bigger than its superset
		if(subset.getKeySet().size() > superset.getKeySet().size()) return false;
		
		//it's not an easy case, so we actually have to check the contents of each tag
		for(String key : superset.getKeySet()) {
			//it's ok if the subset is missing a key from the superset
			if(!subset.hasKey(key)) continue;
			
			NBTBase supersetEntry = superset.getTag(key);
			NBTBase subsetEntry = subset.getTag(key);
			
			//if a value is present on both tags, but they do not match, fail
			if(supersetEntry instanceof NBTTagCompound && subsetEntry instanceof NBTTagCompound) {
				//recurse into tag compounds (this properly compares nested tag compounds)
				if(!isTagSubset((NBTTagCompound) subsetEntry, (NBTTagCompound) supersetEntry)) return false;
			} else {
				if(!supersetEntry.equals(subsetEntry)) return false;
			}
		}
		
		return true;
	}
}
