/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.lib;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

import cpw.mods.modlauncher.api.INameMappingService;

public final class LibObfuscation {
	// EntityLiving
	public static final String GET_LIVING_SOUND = "func_184639_G";

	// RecipeBookGui
	public static final String RECIPE_BOOK_PAGE = "field_193022_s";

	// RecipeBookPage
	public static final String HOVERED_BUTTON = "field_194201_b";

	// HoeItem
	public static final String HOE_LOOKUP = "field_195973_b";

	public static MethodHandle getGetter(Class<?> clazz, String srg) {
		try {
			// From ObfuscationReflectionHelper.findField, because it has weird generics that fail when called alone
			Field f = clazz.getDeclaredField(ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, srg));
			f.setAccessible(true);
			return MethodHandles.publicLookup().unreflectGetter(f);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			throw new IllegalStateException("Can't find " + srg, e);
		}
	}

	public static MethodHandle getMethod(Class<?> clazz, String srg, Class<?>... paramTypes) {
		try {
			return MethodHandles.publicLookup().unreflect(ObfuscationReflectionHelper.findMethod(clazz, srg, paramTypes));
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Can't find " + srg, e);
		}
	}
}
