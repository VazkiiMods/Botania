/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;

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

	public static void save() throws IOException {
		CompoundNBT cmp = new CompoundNBT();
		cmp.putBoolean(TAG_FIRST_LOAD, firstLoad);
		cmp.putBoolean(TAG_DOG, dog);

		injectNBTToFile(cmp, getCacheFile());
	}

	public static void load() throws IOException {
		CompoundNBT cmp = getCacheCompound();

		firstLoad = cmp.contains(TAG_FIRST_LOAD) ? cmp.getBoolean(TAG_FIRST_LOAD) : firstLoad;
		dog = cmp.getBoolean(TAG_DOG);
	}

	public static void setCacheFile(File f) {
		cacheFile = f;
	}

	private static File getCacheFile() throws IOException {
		if (!cacheFile.exists()) {
			cacheFile.createNewFile();
		}

		return cacheFile;
	}

	private static CompoundNBT getCacheCompound() throws IOException {
		return getCacheCompound(getCacheFile());
	}

	private static CompoundNBT getCacheCompound(File cache) throws IOException {
		if (cache == null) {
			throw new RuntimeException("No cache file!");
		}

		try {
			return CompressedStreamTools.readCompressed(new FileInputStream(cache));
		} catch (IOException e) {
			CompoundNBT cmp = new CompoundNBT();
			CompressedStreamTools.writeCompressed(cmp, new FileOutputStream(cache));
			return getCacheCompound(cache);
		}
	}

	private static void injectNBTToFile(CompoundNBT cmp, File f) {
		try {
			CompressedStreamTools.writeCompressed(cmp, new FileOutputStream(f));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
