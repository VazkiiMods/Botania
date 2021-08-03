/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

import java.util.UUID;

public final class ItemNBTHelper {

	private static final int[] EMPTY_INT_ARRAY = new int[0];

	// SETTERS ///////////////////////////////////////////////////////////////////

	public static void set(ItemStack stack, String tag, Tag nbt) {
		stack.getOrCreateTag().put(tag, nbt);
	}

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

	public static void setCompound(ItemStack stack, String tag, CompoundTag cmp) {
		if (!tag.equalsIgnoreCase("ench")) // not override the enchantments
		{
			stack.getOrCreateTag().put(tag, cmp);
		}
	}

	public static void setString(ItemStack stack, String tag, String s) {
		stack.getOrCreateTag().putString(tag, s);
	}

	public static void setUuid(ItemStack stack, String tag, UUID value) {
		stack.getOrCreateTag().putUUID(tag, value);
	}

	public static void setList(ItemStack stack, String tag, ListTag list) {
		stack.getOrCreateTag().put(tag, list);
	}

	public static void removeEntry(ItemStack stack, String tag) {
		stack.getOrCreateTag().remove(tag);
	}

	// GETTERS ///////////////////////////////////////////////////////////////////

	public static boolean verifyExistance(ItemStack stack, String tag) {
		return !stack.isEmpty() && stack.getOrCreateTag().contains(tag);
	}

	@Nullable
	public static Tag get(ItemStack stack, String tag) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().get(tag) : null;
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

	/**
	 * If nullifyOnFail is true it'll return null if it doesn't find any
	 * compounds, otherwise it'll return a new one.
	 **/
	public static CompoundTag getCompound(ItemStack stack, String tag, boolean nullifyOnFail) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getCompound(tag) : nullifyOnFail ? null : new CompoundTag();
	}

	public static String getString(ItemStack stack, String tag, String defaultExpected) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getString(tag) : defaultExpected;
	}

	@Nullable
	public static UUID getUuid(ItemStack stack, String tag) {
		return verifyExistance(stack, tag + "Most") && verifyExistance(stack, tag + "Least") ? stack.getOrCreateTag().getUUID(tag) : null;
	}

	public static ListTag getList(ItemStack stack, String tag, int objtype, boolean nullifyOnFail) {
		return verifyExistance(stack, tag) ? stack.getOrCreateTag().getList(tag, objtype) : nullifyOnFail ? null : new ListTag();
	}

	/**
	 * Serializes the given stack such that {@link net.minecraft.item.crafting.ShapedRecipe#deserializeItem}
	 * would be able to read the result back
	 */
	public static JsonObject serializeStack(ItemStack stack) {
		CompoundTag nbt = stack.save(new CompoundTag());
		byte c = nbt.getByte("Count");
		if (c != 1) {
			nbt.putByte("count", c);
		}
		nbt.remove("Count");
		renameTag(nbt, "id", "item");
		renameTag(nbt, "tag", "nbt");
		Dynamic<Tag> dyn = new Dynamic<>(NbtOps.INSTANCE, nbt);
		return dyn.convert(JsonOps.INSTANCE).getValue().getAsJsonObject();
	}

	/**
	 * Returns true if the `target` tag contains all of the tags and values present in the `template` tag. Recurses into
	 * compound tags and matches all template keys and values; recurses into list tags and matches the template against
	 * the first elements of target. Empty lists and compounds in the template will match target lists and compounds of
	 * any size.
	 */
	public static boolean matchTag(@Nullable Tag template, @Nullable Tag target) {
		if (template instanceof CompoundTag && target instanceof CompoundTag) {
			return matchTagCompound((CompoundTag) template, (CompoundTag) target);
		} else if (template instanceof ListTag && target instanceof ListTag) {
			return matchTagList((ListTag) template, (ListTag) target);
		} else {
			return template == null || (target != null && target.equals(template));
		}
	}

	private static boolean matchTagCompound(CompoundTag template, CompoundTag target) {
		if (template.size() > target.size()) {
			return false;
		}

		for (String key : template.getAllKeys()) {
			if (!matchTag(template.get(key), target.get(key))) {
				return false;
			}
		}

		return true;
	}

	private static boolean matchTagList(ListTag template, ListTag target) {
		if (template.size() > target.size()) {
			return false;
		}

		for (int i = 0; i < template.size(); i++) {
			if (!matchTag(template.get(i), target.get(i))) {
				return false;
			}
		}

		return true;
	}

	public static void renameTag(CompoundTag nbt, String oldName, String newName) {
		Tag tag = nbt.get(oldName);
		if (tag != null) {
			nbt.remove(oldName);
			nbt.put(newName, tag);
		}
	}
}
