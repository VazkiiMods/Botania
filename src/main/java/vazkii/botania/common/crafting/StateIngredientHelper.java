/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.datafixer.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.HashSet;
import java.util.Set;

public class StateIngredientHelper {
	public static StateIngredient of(Block block) {
		return new StateIngredientBlock(block);
	}

	public static StateIngredient of(BlockState state) {
		return new StateIngredientBlockState(state);
	}

	public static StateIngredient of(Tag.Identified<Block> tag) {
		return new StateIngredientTag(tag);
	}

	public static StateIngredient of(Identifier id) {
		return new StateIngredientTag(id);
	}

	public static StateIngredient of(Set<Block> blocks) {
		return new StateIngredientBlocks(blocks);
	}

	public static StateIngredient deserialize(JsonObject object) {
		switch (JsonHelper.getString(object, "type")) {
		case "tag":
			return new StateIngredientTag(BlockTags.register(JsonHelper.getString(object, "tag")));
		case "block":
			return new StateIngredientBlock(ForgeRegistries.BLOCKS.getValue(new Identifier(JsonHelper.getString(object, "block"))));
		case "state":
			return new StateIngredientBlockState(readBlockState(object));
		case "blocks":
			HashSet<Block> blocks = new HashSet<>();
			for (JsonElement element : JsonHelper.getArray(object, "blocks")) {
				blocks.add(ForgeRegistries.BLOCKS.getValue(new Identifier(element.getAsString())));
			}
			return new StateIngredientBlocks(blocks);
		default:
			throw new JsonParseException("Unknown type!");
		}
	}

	public static StateIngredient read(PacketByteBuf buffer) {
		switch (buffer.readVarInt()) {
		case 0:
			int count = buffer.readVarInt();
			Set<Block> set = new HashSet<>();
			for (int i = 0; i < count; i++) {
				Block block = buffer.readRegistryIdUnsafe(ForgeRegistries.BLOCKS);
				set.add(block);
			}
			return new StateIngredientBlocks(set);
		case 1:
			return new StateIngredientBlock(buffer.readRegistryIdUnsafe(ForgeRegistries.BLOCKS));
		case 2:
			return new StateIngredientBlockState(Block.getStateFromRawId(buffer.readVarInt()));
		default:
			throw new IllegalArgumentException("Unknown input discriminator!");
		}
	}

	/**
	 * Writes data about the block state to the provided json object.
	 */
	public static JsonObject serializeBlockState(BlockState state) {
		CompoundTag nbt = NbtHelper.fromBlockState(state);
		ItemNBTHelper.renameTag(nbt, "Name", "name");
		ItemNBTHelper.renameTag(nbt, "Properties", "properties");
		Dynamic<net.minecraft.nbt.Tag> dyn = new Dynamic<>(NbtOps.INSTANCE, nbt);
		return dyn.convert(JsonOps.INSTANCE).getValue().getAsJsonObject();
	}

	/**
	 * Reads the block state from the provided json object.
	 */
	public static BlockState readBlockState(JsonObject object) {
		CompoundTag nbt = (CompoundTag) new Dynamic<>(JsonOps.INSTANCE, object).convert(NbtOps.INSTANCE).getValue();
		ItemNBTHelper.renameTag(nbt, "name", "Name");
		ItemNBTHelper.renameTag(nbt, "properties", "Properties");
		String name = nbt.getString("Name");
		Identifier id = Identifier.tryParse(name);
		if (id == null || !ForgeRegistries.BLOCKS.containsKey(id)) {
			throw new IllegalArgumentException("Invalid or unknown block ID: " + name);
		}
		return NbtHelper.toBlockState(nbt);
	}
}
