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

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.stream.Stream;

/**
 * NBT ingredient that accepts items containing the same NBT data as the provided template stack.
 * In addition, an option to accept items without any NBT data is provided.
 *
 * @see ItemNBTHelper#matchTag(INBT, INBT)
 */
public class FuzzyNBTIngredient extends Ingredient {
	public static final IIngredientSerializer<FuzzyNBTIngredient> SERIALIZER = new Serializer();
	private static final String ACCEPTS_EMPTY_TAG = "accepts_empty_tag";

	private final ItemStack stack;
	private final boolean acceptsEmptyTag;

	/**
	 * Constructs the ingredient.
	 *
	 * @param stack           Template stack for this ingredient
	 * @param acceptsEmptyTag Whether lack of NBT data is accepted by this ingredient in addition to the
	 *                        stack's data.
	 * @throws IllegalArgumentException if the stack has no NBT data.
	 */
	public FuzzyNBTIngredient(ItemStack stack, boolean acceptsEmptyTag) {
		super(Stream.of(new Ingredient.StackEntry(stack)));
		this.stack = stack;
		this.acceptsEmptyTag = acceptsEmptyTag;
		if (!stack.hasTag()) {
			throw new IllegalArgumentException("This ingredient type requires a stack with NBT!");
		}
	}

	/**
	 * Constructs the ingredient.
	 *
	 * @param stack Template stack for this ingredient
	 * @throws IllegalArgumentException if the stack has no NBT data.
	 */
	public FuzzyNBTIngredient(ItemStack stack) {
		this(stack, false);
	}

	@Override
	public boolean test(@Nullable ItemStack input) {
		if (input == null || stack.getItem() != input.getItem()) {
			return false;
		}
		CompoundTag tag = input.getTag();
		if (acceptsEmptyTag && (tag == null || tag.isEmpty())) {
			return true;
		}
		return ItemNBTHelper.matchTag(stack.getTag(), tag);
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Nonnull
	@Override
	public JsonElement toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("type", CraftingHelper.getID(SERIALIZER).toString());
		json.addProperty("item", Registry.ITEM.getId(stack.getItem()).toString());
		json.addProperty("count", stack.getCount());
		json.addProperty("nbt", stack.getTag().toString());
		json.addProperty(ACCEPTS_EMPTY_TAG, acceptsEmptyTag);
		return json;
	}

	@Nonnull
	@Override
	public ItemStack[] getMatchingStacksClient() {
		return new ItemStack[] { stack };
	}

	@Nonnull
	@Override
	public IIngredientSerializer<? extends Ingredient> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer implements IIngredientSerializer<FuzzyNBTIngredient> {
		@Nonnull
		@Override
		public FuzzyNBTIngredient parse(@Nonnull PacketByteBuf buffer) {
			return new FuzzyNBTIngredient(buffer.readItemStack(), buffer.readBoolean());
		}

		@Nonnull
		@Override
		public FuzzyNBTIngredient parse(@Nonnull JsonObject json) {
			boolean acceptsEmptyTag = JsonHelper.getBoolean(json, ACCEPTS_EMPTY_TAG, false);
			return new FuzzyNBTIngredient(CraftingHelper.getItemStack(json, true), acceptsEmptyTag);
		}

		@Override
		public void write(@Nonnull PacketByteBuf buffer, @Nonnull FuzzyNBTIngredient ingredient) {
			buffer.writeItemStack(ingredient.stack);
			buffer.writeBoolean(ingredient.acceptsEmptyTag);
		}
	}
}
