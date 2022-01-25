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

import net.minecraft.commands.CommandFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

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
	private final CommandFunction.CacheableFunction function;

	/**
	 * @param id       The ID for this recipe.
	 * @param input    The input for the recipe. Can be a Block, BlockState, or Tag&lt;Block&gt;.
	 * @param state    The blockstate to be placed upon recipe completion.
	 * @param time     The amount of time in ticks to complete this recipe. Note that this is ticks on your block, not
	 *                 total time.
	 *                 The Pure Daisy only ticks one block at a time in a round robin fashion.
	 * @param function An mcfunction to run at the converted block after finish. If you don't want one, pass
	 *                 CommandFunction.CacheableFunction.NONE
	 */
	public RecipePureDaisy(ResourceLocation id, StateIngredient input, BlockState state, int time, @Nullable CommandFunction.CacheableFunction function) {
		Preconditions.checkArgument(time >= 0, "Time must be nonnegative");
		this.id = id;
		this.input = input;
		this.outputState = state;
		this.time = time;
		this.function = function;
	}

	@Override
	public boolean matches(Level world, BlockPos pos, TileEntitySpecialFlower pureDaisy, BlockState state) {
		return input.test(state);
	}

	@Override
	public boolean set(Level world, BlockPos pos, TileEntitySpecialFlower pureDaisy) {
		if (!world.isClientSide) {
			boolean success = world.setBlockAndUpdate(pos, outputState);
			if (success) {
				var serverLevel = (ServerLevel) world;
				var server = serverLevel.getServer();
				this.function.get(server.getFunctions()).ifPresent(command -> {
					var context = server.getFunctions().getGameLoopSender()
							.withPosition(Vec3.atBottomCenterOf(pos));
					server.getFunctions().execute(command, context);
				});
			}
			return success;
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

	public static class Serializer extends RecipeSerializerBase<RecipePureDaisy> {
		@Nonnull
		@Override
		public RecipePureDaisy fromJson(@Nonnull ResourceLocation id, JsonObject object) {
			StateIngredient input = StateIngredientHelper.deserialize(GsonHelper.getAsJsonObject(object, "input"));
			BlockState output = StateIngredientHelper.readBlockState(GsonHelper.getAsJsonObject(object, "output"));
			int time = GsonHelper.getAsInt(object, "time", DEFAULT_TIME);
			var functionIdString = GsonHelper.getAsString(object, "success_function", null);
			var functionId = functionIdString == null ? null : new ResourceLocation(functionIdString);
			var function = functionId == null ? CommandFunction.CacheableFunction.NONE : new CommandFunction.CacheableFunction(functionId);
			return new RecipePureDaisy(id, input, output, time, function);
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
			return new RecipePureDaisy(id, input, output, time, CommandFunction.CacheableFunction.NONE);
		}
	}
}
