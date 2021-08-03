/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import vazkii.botania.common.Botania;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class PersistentVariableHelper {

	private static final String TAG_FIRST_LOAD = "firstLoad";
	private static final String TAG_DOG = "dog";

	private static File cacheFile;

	public static boolean firstLoad = true;
	public static boolean dog = true;

	public static void save() {
		CompoundTag cmp = new CompoundTag();
		cmp.putBoolean(TAG_FIRST_LOAD, firstLoad);
		cmp.putBoolean(TAG_DOG, dog);

		injectNBTToFile(cmp, cacheFile);
	}

	public static void load() {
		CompoundTag cmp = getCacheCompound(cacheFile);

		firstLoad = cmp.contains(TAG_FIRST_LOAD) ? cmp.getBoolean(TAG_FIRST_LOAD) : firstLoad;
		dog = cmp.getBoolean(TAG_DOG);
	}

	public static void init() {
		cacheFile = new File(Minecraft.getInstance().gameDirectory, "BotaniaVars.dat");
		try {
			cacheFile.createNewFile();
		} catch (IOException e) {
			Botania.LOGGER.error("Failed to create persistent variable file", e);
		}
		load();
	}

	private static CompoundTag getCacheCompound(File cache) {
		try {
			return NbtIo.readCompressed(new FileInputStream(cache));
		} catch (IOException e) {
			Botania.LOGGER.error("Failed to load persistent variables, overwriting with current state", e);
			save();
			return new CompoundTag();
		}
	}

	private static void injectNBTToFile(CompoundTag cmp, File f) {
		try {
			NbtIo.writeCompressed(cmp, new FileOutputStream(f));
		} catch (IOException e) {
			Botania.LOGGER.error("Failed to save persistent variables", e);
		}
	}

}
