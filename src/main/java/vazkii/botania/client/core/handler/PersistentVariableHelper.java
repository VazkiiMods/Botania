/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 24, 2015, 5:54:45 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import vazkii.botania.client.challenge.Challenge;
import vazkii.botania.client.challenge.ModChallenges;
import vazkii.botania.common.lib.LibMisc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

public final class PersistentVariableHelper {

	private static final String TAG_FIRST_LOAD = "firstLoad";
	private static final String TAG_DOG = "dog";
	private static final String TAG_CHALLENGES = "challenges";
	private static final String TAG_LEXICON_NOTES = "lexiconNotes";
	private static final String TAG_LAST_BOTANIA_VERSION = "lastBotaniaVersion";

	private static File cacheFile;

	public static boolean firstLoad = true;
	public static boolean dog = true;
	public static String lastBotaniaVersion = "";

	public static void save() throws IOException {
		CompoundNBT cmp = new CompoundNBT();

		CompoundNBT challengesCmp = new CompoundNBT();
		for(Challenge c : ModChallenges.challengeLookup.values())
			c.writeToNBT(challengesCmp);
		cmp.put(TAG_CHALLENGES, challengesCmp);

		/*
		CompoundNBT notesCmp = new CompoundNBT();
		for(String s : GuiLexicon.notes.keySet()) {
			String note = GuiLexicon.notes.get(s);
			if(note != null && !note.trim().isEmpty())
				notesCmp.putString(s, note);
		}
		cmp.put(TAG_LEXICON_NOTES, notesCmp);
		 */

		cmp.putBoolean(TAG_FIRST_LOAD, firstLoad);
		cmp.putBoolean(TAG_DOG, dog);
		cmp.putString(TAG_LAST_BOTANIA_VERSION, lastBotaniaVersion);

		injectNBTToFile(cmp, getCacheFile());
	}

	public static void load() throws IOException {
		CompoundNBT cmp = getCacheCompound();

		if(cmp.contains(TAG_CHALLENGES)) {
			CompoundNBT challengesCmp = cmp.getCompound(TAG_CHALLENGES);
			for(Challenge c : ModChallenges.challengeLookup.values())
				c.readFromNBT(challengesCmp);
		}

		if(cmp.contains(TAG_LEXICON_NOTES)) {
			CompoundNBT notesCmp = cmp.getCompound(TAG_LEXICON_NOTES);
			Set<String> keys = notesCmp.keySet();
			// GuiLexicon.notes.clear();
			for(String key : keys)
				;// GuiLexicon.notes.put(key, notesCmp.getString(key));
		}

		lastBotaniaVersion = cmp.contains(TAG_LAST_BOTANIA_VERSION) ? cmp.getString(TAG_LAST_BOTANIA_VERSION) : "(N/A)";

		firstLoad = cmp.contains(TAG_FIRST_LOAD) ? cmp.getBoolean(TAG_FIRST_LOAD) : firstLoad;
		if(firstLoad)
			lastBotaniaVersion = LibMisc.VERSION;

		dog = cmp.getBoolean(TAG_DOG);
	}

	public static void saveSafe() {
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setCacheFile(File f) {
		cacheFile = f;
	}

	private static File getCacheFile() throws IOException {
		if(!cacheFile.exists())
			cacheFile.createNewFile();

		return cacheFile;
	}

	private static CompoundNBT getCacheCompound() throws IOException {
		return getCacheCompound(getCacheFile());
	}

	private static CompoundNBT getCacheCompound(File cache) throws IOException {
		if(cache == null)
			throw new RuntimeException("No cache file!");

		try {
			return CompressedStreamTools.readCompressed(new FileInputStream(cache));
		} catch(IOException e) {
			CompoundNBT cmp = new CompoundNBT();
			CompressedStreamTools.writeCompressed(cmp, new FileOutputStream(cache));
			return getCacheCompound(cache);
		}
	}

	private static void injectNBTToFile(CompoundNBT cmp, File f) {
		try {
			CompressedStreamTools.writeCompressed(cmp, new FileOutputStream(f));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
