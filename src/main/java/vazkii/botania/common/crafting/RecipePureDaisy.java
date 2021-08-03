/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RecipePureDaisy implements IPureDaisyRecipe {

	public static final int DEFAULT_TIME = 150;

	private final ResourceLocation id;
	private final StateIngredient input;
	private final BlockState outputState;
	private final int time;

	/**
	 * @param id    The ID for this recipe.
	 * @param input The input for the recipe. Can be a Block, BlockState, or Tag&lt;Block&gt;.
	 * @param state The blockstate to be placed upon recipe completion.
	 * @param time  The amount of time in ticks to complete this recipe. Note that this is ticks on your block, not
	 *              total time.
	 *              The Pure Daisy only ticks one block at a time in a round robin fashion.
	 */
	public RecipePureDaisy(ResourceLocation id, StateIngredient input, BlockState state, int time) {
		Preconditions.checkArgument(time >= 0, "Time must be nonnegative");
		this.id = id;
		this.input = input;
		this.outputState = state;
		this.time = time;
	}

	@Override
	public boolean matches(Level world, BlockPos pos, TileEntitySpecialFlower pureDaisy, BlockState state) {
		return input.test(state);
	}

	@Override
	public boolean set(Level world, BlockPos pos, TileEntitySpecialFlower pureDaisy) {
		if (!world.isClientSide) {
			world.setBlockAndUpdate(pos, outputState);
		}
		return true;
	}

	@Override
	public StateIngredient getInput() {
		return input;
	}

	@Override
	public BlockState getOutputState() {
		return outputState;
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeTypes.PURE_DAISY_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<RecipePureDaisy> {
		@Nonnull
		@Override
		public RecipePureDaisy fromJson(@Nonnull ResourceLocation id, JsonObject object) {
			StateIngredient input = StateIngredientHelper.deserialize(GsonHelper.getAsJsonObject(object, "input"));
			BlockState output = StateIngredientHelper.readBlockState(GsonHelper.getAsJsonObject(object, "output"));
			int time = GsonHelper.getAsInt(object, "time", DEFAULT_TIME);
			return new RecipePureDaisy(id, input, output, time);
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buf, RecipePureDaisy recipe) {
			recipe.input.write(buf);
			buf.writeVarInt(Block.getId(recipe.outputState));
			buf.writeVarInt(recipe.time);
		}

		@Nullable
		@Override
		public RecipePureDaisy fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
			StateIngredient input = StateIngredientHelper.read(buf);
			BlockState output = Block.stateById(buf.readVarInt());
			int time = buf.readVarInt();
			return new RecipePureDaisy(id, input, output, time);
		}
	}
}
