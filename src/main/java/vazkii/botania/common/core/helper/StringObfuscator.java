/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class StringObfuscator {

	public static boolean matchesHash(String str, String hash) {
		return getHash(str).equals(hash);
	}

	private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

	private static String getHash(String str) {
		if (str != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				StringBuilder ret = new StringBuilder();
				byte[] bytes = md.digest(dontRainbowTableMeOrMySonEverAgain(str).getBytes(StandardCharsets.UTF_8));

				for (byte b : bytes) {
					ret.append(HEX_CHARS[(b >> 4) & 0xF]);
					ret.append(HEX_CHARS[b & 0xF]);
				}

				return ret.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	private static String dontRainbowTableMeOrMySonEverAgain(String str) throws NoSuchAlgorithmException {
		str += reverseString(str);
		SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
		rand.setSeed(str.getBytes(StandardCharsets.UTF_8));
		int l = str.length();
		int steps = rand.nextInt(l);
		char[] chrs = str.toCharArray();
		for (int i = 0; i < steps; i++) {
			int indA = rand.nextInt(l);
			int indB;
			do {
				indB = rand.nextInt(l);
			} while (indB == indA);
			char c = (char) (chrs[indA] ^ chrs[indB]);
			chrs[indA] = c;
		}

		return String.copyValueOf(chrs);
	}

	private static String reverseString(String str) {
		return new StringBuilder(str).reverse().toString();
	}

}
