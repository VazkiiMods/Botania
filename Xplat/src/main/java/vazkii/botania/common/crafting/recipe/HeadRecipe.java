/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
import com.mojang.serialization.MapCodec;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.ProcessingRecipeInput;
import vazkii.botania.common.crafting.RunicAltarRecipe;

import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeadRecipe extends RunicAltarRecipe {
	public static final RecipeSerializer<HeadRecipe> SERIALIZER = new Serializer();
	private static final Pattern PROFILE_PATTERN = Pattern.compile(
			"(?<base64>[A-Za-z0-9+/]{100,}={0,2})" +
					"|(?<url>(?=\\S{50,})https?://(?!bugs|education|feedback)\\w+\\.(?:minecraft\\.net|mojang\\.com)/\\S+)" +
					"|(?<hash>[0-9a-f]{64})");
	public static final String TEXTURE_URL_BASE = "https://textures.minecraft.net/texture/";
	private static final Supplier<Gson> gson = Suppliers.memoize(() -> new GsonBuilder()
			.registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create());
	private static final ResolvableProfile PROFILE_VALID_RESULT = new ResolvableProfile(Optional.of("valid"), Optional.of(Util.NIL_UUID), new PropertyMap());
	private static final LoadingCache<String, UUID> GENERATED_UUID_CACHE = CacheBuilder.newBuilder()
			.expireAfterAccess(1, TimeUnit.MINUTES).build(
					new CacheLoader<>() {
						@Override
						public UUID load(String key) {
							return UUID.nameUUIDFromBytes(key.getBytes(StandardCharsets.UTF_8));
						}
					});

	public HeadRecipe(ItemStack output, Ingredient reagent, int mana, Ingredient... inputs) {
		super(output, reagent, mana, inputs, new Ingredient[0]);
	}

	private HeadRecipe(RunicAltarRecipe recipe) {
		super(recipe.getOutput(), recipe.getReagent(), recipe.getMana(),
				recipe.getIngredients().toArray(Ingredient[]::new), recipe.getCatalysts().toArray(Ingredient[]::new));
	}

	@Override
	public boolean matches(ProcessingRecipeInput input, Level world) {
		boolean matches = super.matches(input, world);
		boolean foundName = false;

		if (matches) {
			for (int i = 0; i < input.size(); i++) {
				ItemStack stack = input.getItem(i);
				if (stack.isEmpty()) {
					break;
				}

				// either exactly one name tag or exactly one written book among ingredients
				if (stack.is(Items.NAME_TAG)) {
					if (foundName || !stack.has(DataComponents.CUSTOM_NAME) || stack.getHoverName().getString().isBlank()) {
						return false;
					}
					foundName = true;
				} else if (stack.is(Items.WRITTEN_BOOK)) {
					if (foundName || !stack.has(DataComponents.WRITTEN_BOOK_CONTENT)
							|| parseProfileFromBook(stack, true) == null) {
						return false;
					}
					foundName = true;
				}
			}
		}

		return matches;
	}

	@Override
	public RecipeSerializer<? extends RunicAltarRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public ItemStack assemble(ProcessingRecipeInput inv, HolderLookup.Provider registries) {
		ItemStack stack = getResultItem(registries).copy();
		for (int i = 0; i < inv.size(); i++) {
			ItemStack ingr = inv.getItem(i);
			if (ingr.is(Items.NAME_TAG)) {
				stack.set(DataComponents.PROFILE, new ResolvableProfile(Optional.of(ingr.getHoverName().getString()),
						Optional.empty(), new PropertyMap()));
				break;
			}
			if (ingr.is(Items.WRITTEN_BOOK)) {
				ResolvableProfile profile = parseProfileFromBook(ingr, false);
				stack.set(DataComponents.PROFILE, profile);
				break;
			}
		}
		return stack;
	}

	@Nullable
	private ResolvableProfile parseProfileFromBook(ItemStack stack, boolean validateOnly) {
		var component = stack.get(DataComponents.WRITTEN_BOOK_CONTENT);
		if (component == null || component.title().raw().isBlank()) {
			return null;
		}

		var pages = component.pages();

		// no-nonsense check; at most the first two pages are scanned, and the check fails at the first error
		int maxPages = Math.min(2, pages.size());
		for (int i = 0; i < maxPages; ++i) {
			String pageText = pages.get(i).raw().getString();

			Matcher matcher = PROFILE_PATTERN.matcher(pageText);
			if (matcher.matches()) {
				// this appears to be the page we were looking for, figure out the skin texture it encodes
				String textureUrl;
				String hash, base64, url;
				if ((hash = matcher.group("hash")) != null) {
					// simplest case: just the texture hash; complete the URL
					textureUrl = TEXTURE_URL_BASE + hash;
				} else if ((url = matcher.group("url")) != null) {
					// an entire URL was specified; make sure it looks valid
					try {
						// just basic URL validation so we don't potentially spam error logs
						URL validUrl = new URI(url).toURL();
						textureUrl = validUrl.toString();
					} catch (Exception e) {
						return null;
					}
				} else if ((base64 = matcher.group("base64")) != null) {
					// complete profile properties; do rudimentary parsing
					try {
						final String json = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
						MinecraftTexturesPayload result = gson.get().fromJson(json, MinecraftTexturesPayload.class);
						MinecraftProfileTexture skinTexture = result.textures().get(MinecraftProfileTexture.Type.SKIN);
						String skinTextureUrl = skinTexture.getUrl();
						if (!PROFILE_PATTERN.matcher(skinTextureUrl).matches()) {
							return null;
						}
						URL validUrl = new URI(skinTextureUrl).toURL();
						textureUrl = validUrl.toString();
					} catch (Exception e) {
						return null;
					}
				} else {
					return null;
				}
				if (validateOnly) {
					return PROFILE_VALID_RESULT;
				}
				// we got something that looks like a valid skin texture URL, now build rudimentary profile data
				String profileTextureJson = "{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}".formatted(textureUrl);
				String propertyBase64 = Base64.getEncoder().encodeToString(profileTextureJson.getBytes(StandardCharsets.UTF_8));
				var profile = new ResolvableProfile(Optional.of(component.title().raw()),
						Optional.of(GENERATED_UUID_CACHE.getUnchecked(propertyBase64)), new PropertyMap());
				profile.properties().put("textures", new Property("textures", propertyBase64));
				return profile;
			}
		}
		return null;
	}

	public static class Serializer implements RecipeSerializer<HeadRecipe> {
		public static final MapCodec<HeadRecipe> CODEC = RunicAltarRecipe.Serializer.CODEC
				.xmap(HeadRecipe::new, Function.identity());
		public static final StreamCodec<RegistryFriendlyByteBuf, HeadRecipe> STREAM_CODEC = RunicAltarRecipe.Serializer.STREAM_CODEC
				.map(HeadRecipe::new, Function.identity());

		@Override
		public MapCodec<HeadRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, HeadRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}

}
