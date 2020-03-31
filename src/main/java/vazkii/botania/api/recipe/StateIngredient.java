/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

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
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class StateIngredient implements Predicate<BlockState> {

	public static StateIngredient of(Block block) {
		return new OfBlock(block);
	}

	public static StateIngredient of(BlockState state) {
		return new OfState(state);
	}

	public static StateIngredient of(Tag<Block> tag) {
		return new OfTag(tag);
	}

	public static StateIngredient of(ResourceLocation id) {
		return new OfTag(id);
	}

	public static StateIngredient deserialize(JsonObject object) {
		switch (JSONUtils.getString(object, "type")) {
		case "tag":
			return new OfTag(new BlockTags.Wrapper(new ResourceLocation(JSONUtils.getString(object, "tag"))));
		case "block":
			return new OfBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getString(object, "block"))));
		case "state":
			return new OfState(readBlockState(object));
		default:
			throw new JsonParseException("Unknown type!");
		}
	}

	public static StateIngredient read(PacketBuffer buffer) {
		switch (buffer.readVarInt()) {
		case 0:
			return new OfTag(buffer.readResourceLocation());
		case 1:
			return new OfBlock(buffer.readRegistryIdUnsafe(ForgeRegistries.BLOCKS));
		case 2:
			return new OfState(Block.getStateById(buffer.readVarInt()));
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

	public abstract JsonObject serialize();

	public abstract void write(PacketBuffer buffer);

	public abstract List<BlockState> getDisplayed();

	private static class OfBlock extends StateIngredient {
		private final net.minecraft.block.Block block;

		private OfBlock(net.minecraft.block.Block block) {
			this.block = block;
		}

		@Override
		public boolean test(BlockState blockState) {
			return block == blockState.getBlock();
		}

		@Override
		public JsonObject serialize() {
			JsonObject object = new JsonObject();
			object.addProperty("type", "block");
			object.addProperty("block", block.getRegistryName().toString());
			return object;
		}

		@Override
		public void write(PacketBuffer buffer) {
			buffer.writeVarInt(1);
			buffer.writeRegistryIdUnsafe(ForgeRegistries.BLOCKS, block);
		}

		@Override
		public List<BlockState> getDisplayed() {
			return Collections.singletonList(block.getDefaultState());
		}

	}

	private static class OfState extends StateIngredient {
		private final BlockState state;

		private OfState(BlockState state) {
			this.state = state;
		}

		@Override
		public boolean test(BlockState blockState) {
			return false;
		}

		@Override
		public JsonObject serialize() {
			JsonObject object = serializeBlockState(state);
			object.addProperty("type", "state");
			return object;
		}

		@Override
		public void write(PacketBuffer buffer) {
			buffer.writeVarInt(2);
			buffer.writeVarInt(Block.getStateId(state));
		}

		@Override
		public List<BlockState> getDisplayed() {
			return Collections.singletonList(state);
		}

	}

	private static class OfTag extends StateIngredient {
		private final Tag<Block> tag;

		public OfTag(Tag<Block> tag) {
			this.tag = tag;
		}

		public OfTag(ResourceLocation id) {
			this(new BlockTags.Wrapper(id));
		}

		@Override
		public boolean test(BlockState state) {
			return tag.contains(state.getBlock());
		}

		@Override
		public JsonObject serialize() {
			JsonObject object = new JsonObject();
			object.addProperty("type", "tag");
			object.addProperty("tag", tag.getId().toString());
			return object;
		}

		@Override
		public void write(PacketBuffer buffer) {
			buffer.writeVarInt(0);
			buffer.writeResourceLocation(tag.getId());
		}

		@Override
		public List<BlockState> getDisplayed() {
			return tag.getAllElements().stream().map(Block::getDefaultState).collect(Collectors.toList());
		}

	}
}
