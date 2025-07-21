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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.RunicAltarRecipe;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeadRecipe extends RunicAltarRecipe {
	private static final Pattern PROFILE_PATTERN = Pattern.compile(
			"(?<base64>[A-Za-z0-9+/]{100,}={0,2})" +
					"|(?<url>(?=\\S{50,})https?://(?!bugs|education|feedback)\\w+\\.(?:minecraft\\.net|mojang\\.com)/\\S+)" +
					"|(?<hash>[0-9a-f]{64})");
	public static final String TEXTURE_URL_BASE = "https://textures.minecraft.net/texture/";
	private static final Supplier<Gson> gson = Suppliers.memoize(() -> new GsonBuilder()
			.registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create());
	private static final GameProfile PROFILE_VALID_RESULT = new GameProfile(null, "valid");

	public HeadRecipe(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs) {
		super(id, output, mana, inputs);
	}

	@Override
	public boolean matches(Container inv, @NotNull Level world) {
		boolean matches = super.matches(inv, world);
		boolean foundName = false;

		if (matches) {
			for (int i = 0; i < inv.getContainerSize(); i++) {
				ItemStack stack = inv.getItem(i);
				if (stack.isEmpty()) {
					break;
				}

				// either exactly one name tag or exactly one written book among ingredients
				if (stack.is(Items.NAME_TAG)) {
					if (foundName || !stack.hasCustomHoverName() || stack.getHoverName().getString().isBlank()) {
						return false;
					}
					foundName = true;
				} else if (stack.is(Items.WRITTEN_BOOK)) {
					if (foundName || !WrittenBookItem.makeSureTagIsValid(stack.getTag())
							|| parseProfileFromBook(stack, true) == null) {
						return false;
					}
					foundName = true;
				}
			}
		}

		return matches;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull Container inv, @NotNull RegistryAccess registries) {
		ItemStack stack = getResultItem(registries).copy();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack ingr = inv.getItem(i);
			if (ingr.is(Items.NAME_TAG)) {
				ItemNBTHelper.setString(stack, "SkullOwner", ingr.getHoverName().getString());
				break;
			}
			if (ingr.is(Items.WRITTEN_BOOK)) {
				GameProfile profile = parseProfileFromBook(ingr, false);
				ItemNBTHelper.setCompound(stack, "SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), profile));
				break;
			}
		}
		return stack;
	}

	private GameProfile parseProfileFromBook(ItemStack stack, boolean validateOnly) {
		// tag has been validated, so we know all relevant elements exist
		CompoundTag tag = stack.getTag();
		String name = tag.getString(WrittenBookItem.TAG_TITLE);
		if (name.isBlank()) {
			return null;
		}

		ListTag pages = tag.getList(WrittenBookItem.TAG_PAGES, Tag.TAG_STRING);

		// no-nonsense check; at most the first two pages are scanned, and the check fails at the first error
		int maxPages = Math.min(2, pages.size());
		for (int i = 0; i < maxPages; ++i) {
			String pageJson = pages.getString(i);
			String pageText = parsePage(pageJson);

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
						URL validUrl = new URL(url);
						textureUrl = validUrl.toString();
					} catch (Exception e) {
						return null;
					}
				} else if ((base64 = matcher.group("base64")) != null) {
					// complete profile properties; do rudimentary parsing
					try {
						final String json = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
						MinecraftTexturesPayload result = gson.get().fromJson(json, MinecraftTexturesPayload.class);
						MinecraftProfileTexture skinTexture = result.getTextures().get(MinecraftProfileTexture.Type.SKIN);
						String skinTextureUrl = skinTexture.getUrl();
						if (!PROFILE_PATTERN.matcher(skinTextureUrl).matches()) {
							return null;
						}
						URL validUrl = new URL(skinTextureUrl);
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
				String profileTextureJson = "{textures:{SKIN:{url:\"%s\"}}}".formatted(textureUrl);
				var profile = new GameProfile(null, name);
				String propertyBase64 = Base64.getEncoder().encodeToString(profileTextureJson.getBytes(StandardCharsets.UTF_8));
				profile.getProperties().put("textures", new Property("Value", propertyBase64));
				return profile;
			}
		}
		return null;
	}

	private static String parsePage(String pageJson) {
		try {
			FormattedText formattedtext = Component.Serializer.fromJson(pageJson);
			return formattedtext != null ? formattedtext.getString() : pageJson;
		} catch (Exception exception) {
			return pageJson;
		}
	}

	public static class Serializer implements RecipeSerializer<HeadRecipe> {

		@NotNull
		@Override
		public HeadRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
			int mana = GsonHelper.getAsInt(json, "mana");
			JsonArray ingrs = GsonHelper.getAsJsonArray(json, "ingredients");
			List<Ingredient> inputs = new ArrayList<>();
			for (JsonElement e : ingrs) {
				inputs.add(Ingredient.fromJson(e));
			}
			return new HeadRecipe(id, output, mana, inputs.toArray(new Ingredient[0]));
		}

		@Override
		public HeadRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
			Ingredient[] inputs = new Ingredient[buf.readVarInt()];
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = Ingredient.fromNetwork(buf);
			}
			ItemStack output = buf.readItem();
			int mana = buf.readVarInt();
			return new HeadRecipe(id, output, mana, inputs);
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull HeadRecipe recipe) {
			BotaniaRecipeTypes.RUNE_SERIALIZER.toNetwork(buf, recipe);
		}
	}

}
