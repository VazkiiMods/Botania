/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.commands.CommandFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.PureDaisyRecipe;
import vazkii.botania.common.crafting.RecipeSerializerBase;
import vazkii.botania.common.crafting.StateIngredientHelper;

/**
 * Recipe that copies state properties to the new block on crafting.
 */
public class StateCopyingPureDaisyRecipe extends PureDaisyRecipe {
	public StateCopyingPureDaisyRecipe(ResourceLocation id, StateIngredient input, Block block, int time) {
		super(id, input, block.defaultBlockState(), time, CommandFunction.CacheableFunction.NONE);
	}

	@Override
	public boolean matches(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy, BlockState state) {
		return input.test(state) && outputState.getBlock().withPropertiesOf(state) != state;
	}

	@Override
	public boolean set(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy) {
		if (!world.isClientSide) {
			Block block = getOutputState().getBlock();
			world.setBlockAndUpdate(pos, block.withPropertiesOf(world.getBlockState(pos)));
		}
		return true;
	}

	public static class Serializer extends RecipeSerializerBase<StateCopyingPureDaisyRecipe> {
		@NotNull
		@Override
		public StateCopyingPureDaisyRecipe fromJson(@NotNull ResourceLocation id, JsonObject object) {
			StateIngredient input = StateIngredientHelper.deserialize(GsonHelper.getAsJsonObject(object, "input"));
			ResourceLocation blockId = new ResourceLocation(GsonHelper.getAsString(object, "output"));
			Block output = Registry.BLOCK.getOptional(blockId)
					.orElseThrow(() -> new JsonSyntaxException("Unknown block id: " + blockId));

			int time = GsonHelper.getAsInt(object, "time", DEFAULT_TIME);
			return new StateCopyingPureDaisyRecipe(id, input, output, time);
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf buf, StateCopyingPureDaisyRecipe recipe) {
			recipe.getInput().write(buf);
			buf.writeVarInt(Registry.BLOCK.getId(recipe.getOutputState().getBlock()));
			buf.writeVarInt(recipe.getTime());
		}

		@Nullable
		@Override
		public StateCopyingPureDaisyRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
			StateIngredient input = StateIngredientHelper.read(buf);
			Block output = Registry.BLOCK.byId(buf.readVarInt());
			int time = buf.readVarInt();
			return new StateCopyingPureDaisyRecipe(id, input, output, time);
		}
	}
}
