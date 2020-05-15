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
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StateIngredientHelper {
	public static StateIngredient of(Block block) {
		return new StateIngredientBlock(block);
	}

	public static StateIngredient of(BlockState state) {
		return new StateIngredientBlockState(state);
	}

	public static StateIngredient of(Tag<Block> tag) {
		return new StateIngredientTag(tag);
	}

	public static StateIngredient of(ResourceLocation id) {
		return new StateIngredientTag(id);
	}

	public static StateIngredient of(Set<Block> blocks) {
		return new StateIngredientBlocks(blocks);
	}

	public static StateIngredient deserialize(JsonObject object) {
		switch (JSONUtils.getString(object, "type")) {
		case "tag":
			return new StateIngredientTag(new BlockTags.Wrapper(new ResourceLocation(JSONUtils.getString(object, "tag"))));
		case "block":
			return new StateIngredientBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getString(object, "block"))));
		case "state":
			return new StateIngredientBlockState(readBlockState(object));
		case "blocks":
			HashSet<Block> blocks = new HashSet<>();
			for (JsonElement element : JSONUtils.getJsonArray(object, "blocks")) {
				blocks.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(element.getAsString())));
			}
			return new StateIngredientBlocks(blocks);
		default:
			throw new JsonParseException("Unknown type!");
		}
	}

	public static StateIngredient read(PacketBuffer buffer) {
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
			return new StateIngredientBlockState(Block.getStateById(buffer.readVarInt()));
		default:
			throw new IllegalArgumentException("Unknown input discriminator!");
		}
	}

	/**
	 * Writes data about the block state to the provided json object.
	 */
	public static JsonObject serializeBlockState(BlockState state) {
		CompoundNBT nbt = NBTUtil.writeBlockState(state);
		ItemNBTHelper.renameTag(nbt, "Name", "name");
		ItemNBTHelper.renameTag(nbt, "Properties", "properties");
		Dynamic<INBT> dyn = new Dynamic<>(NBTDynamicOps.INSTANCE, nbt);
		return dyn.convert(JsonOps.INSTANCE).getValue().getAsJsonObject();
	}

	/**
	 * Reads the block state from the provided json object.
	 */
	public static BlockState readBlockState(JsonObject object) {
		CompoundNBT nbt = (CompoundNBT) new Dynamic<>(JsonOps.INSTANCE, object).convert(NBTDynamicOps.INSTANCE).getValue();
		ItemNBTHelper.renameTag(nbt, "name", "Name");
		ItemNBTHelper.renameTag(nbt, "properties", "Properties");
		String name = nbt.getString("Name");
		ResourceLocation id = ResourceLocation.tryCreate(name);
		if (id == null || !ForgeRegistries.BLOCKS.containsKey(id)) {
			throw new IllegalArgumentException("Invalid or unknown block ID: " + name);
		}
		return NBTUtil.readBlockState(nbt);
	}
}
