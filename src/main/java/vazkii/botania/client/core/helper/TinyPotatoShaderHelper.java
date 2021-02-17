/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.helper;

import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;

import javax.annotation.Nullable;

import java.util.Objects;

public class TinyPotatoShaderHelper {
	private static final Trie<ShaderHelper.BotaniaShader> SHADERS = new Trie<>();

	static {
		SHADERS.put("gaia", ShaderHelper.BotaniaShader.DOPPLEGANGER);
		SHADERS.put("hot", ShaderHelper.BotaniaShader.HALO);
		SHADERS.put("magic", ShaderHelper.BotaniaShader.ENCHANTER_RUNE);
		SHADERS.put("gold", ShaderHelper.BotaniaShader.GOLD);
		SHADERS.put("snoop", ShaderHelper.BotaniaShader.TERRA_PLATE);
	}

	public static Pair<ShaderHelper.BotaniaShader, String> stripShaderName(String name) {
		Trie.TrieResult<ShaderHelper.BotaniaShader> result = SHADERS.lookup(name);
		if (result == null) {
			return Pair.of(null, name);
		} else {
			return Pair.of(result.value, name.substring(result.keyLength).trim());
		}
	}

	private static class Trie<T> {
		private final TrieNode root = new TrieNode();

		public void put(String key, T value) {
			TrieNode node = root;
			for (int i = 0; i < key.length(); i++) {
				char c = key.charAt(i);
				node = node.children.computeIfAbsent(c, $ -> new TrieNode());
			}
			node.value = value;
		}

		@Nullable
		public TrieResult<T> lookup(String key) {
			TrieNode node = root;
			int keyLength = 0;
			T found = node.value;
			int i;
			for (i = 0; i < key.length(); i++) {
				node = node.children.get(key.charAt(i));
				if (node == null) {
					break;
				}
				if (node.value != null) {
					keyLength = i + 1;
					found = node.value;
				}
			}
			if (found != null) {
				return new TrieResult<>(keyLength, found);
			}
			return null;
		}

		@Override
		public String toString() {
			return "Trie" + root;
		}

		public static class TrieResult<T> {
			public final int keyLength;
			public final T value;

			TrieResult(int keyLength, T value) {
				this.keyLength = keyLength;
				this.value = value;
			}

			@Override
			public String toString() {
				return "TrieResult{" +
						"keyLength=" + keyLength +
						", value=" + value +
						'}';
			}

			@Override
			public boolean equals(Object o) {
				if (this == o) {
					return true;
				}
				if (o == null || getClass() != o.getClass()) {
					return false;
				}
				TrieResult<?> that = (TrieResult<?>) o;
				return keyLength == that.keyLength && Objects.equals(value, that.value);
			}

			@Override
			public int hashCode() {
				return Objects.hash(keyLength, value);
			}
		}

		private class TrieNode {
			@Nullable
			T value;
			final Char2ObjectMap<TrieNode> children = new Char2ObjectOpenHashMap<>();

			@Override
			public String toString() {
				StringBuilder sb = new StringBuilder("TrieNode");
				if (value != null) {
					sb.append("[").append(value).append("]");
				}
				sb.append(children);
				return sb.toString();
			}
		}
	}
}
