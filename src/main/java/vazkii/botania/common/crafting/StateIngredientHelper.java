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
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StateIngredientHelper {
	public static StateIngredient of(Block block) {
		return new StateIngredientBlock(block);
	}

	public static StateIngredient of(BlockState state) {
		return new StateIngredientBlockState(state);
	}

	public static StateIngredient of(ITag.INamedTag<Block> tag) {
		return of(tag.getName());
	}

	public static StateIngredient of(ResourceLocation id) {
		return new StateIngredientTag(id);
	}

	public static StateIngredient of(Collection<Block> blocks) {
		return new StateIngredientBlocks(blocks);
	}

	public static StateIngredient deserialize(JsonObject object) {
		switch (JSONUtils.getString(object, "type")) {
		case "tag":
			return new StateIngredientTag(new ResourceLocation(JSONUtils.getString(object, "tag")));
		case "block":
			return new StateIngredientBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getString(object, "block"))));
		case "state":
			return new StateIngredientBlockState(readBlockState(object));
		case "blocks":
			List<Block> blocks = new ArrayList<>();
			for (JsonElement element : JSONUtils.getJsonArray(object, "blocks")) {
				blocks.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(element.getAsString())));
			}
			return new StateIngredientBlocks(blocks);
		default:
			throw new JsonParseException("Unknown type!");
		}
	}

	/**
	 * Deserializes a state ingredient, but removes air from its data,
	 * and returns null if the ingredient only matched air.
	 * It does not resolve tag data, as usage of this method is expected during early resource reload.
	 */
	@Nullable
	public static StateIngredient tryDeserialize(JsonObject object) {
		StateIngredient ingr = deserialize(object);
		if (ingr instanceof StateIngredientTag) {
			return ingr;
		}
		if (ingr instanceof StateIngredientBlock || ingr instanceof StateIngredientBlockState) {
			if (ingr.test(Blocks.AIR.getDefaultState())) {
				return null;
			}
		} else if (ingr instanceof StateIngredientBlocks) {
			Collection<Block> blocks = ((StateIngredientBlocks) ingr).blocks;
			List<Block> list = new ArrayList<>(blocks);
			if (list.removeIf(b -> b == Blocks.AIR)) {
				if (list.size() == 0) {
					return null;
				}
				return of(list);
			}
		}
		return ingr;
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

	@Nonnull
	public static List<ItemStack> toStackList(StateIngredient input) {
		return input.getDisplayed().stream()
				.map(BlockState::getBlock)
				.filter(b -> b.asItem() != Items.AIR)
				.map(ItemStack::new)
				.collect(Collectors.toList());
	}
}
