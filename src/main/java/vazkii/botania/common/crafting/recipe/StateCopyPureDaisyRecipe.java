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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.common.crafting.RecipePureDaisy;
import vazkii.botania.common.crafting.StateIngredientHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Recipe that copies state properties to the new block on crafting.
 */
public class StateCopyPureDaisyRecipe extends RecipePureDaisy {
	public StateCopyPureDaisyRecipe(ResourceLocation id, StateIngredient input, Block block, int time) {
		super(id, input, block.defaultBlockState(), time);
	}

	@Override
	public boolean set(Level world, BlockPos pos, TileEntitySpecialFlower pureDaisy) {
		if (!world.isClientSide) {
			Block block = getOutputState().getBlock();
			world.setBlockAndUpdate(pos, block.withPropertiesOf(world.getBlockState(pos)));
		}
		return true;
	}

	public static class Serializer implements RecipeSerializer<StateCopyPureDaisyRecipe> {
		@Nonnull
		@Override
		public StateCopyPureDaisyRecipe fromJson(@Nonnull ResourceLocation id, JsonObject object) {
			StateIngredient input = StateIngredientHelper.deserialize(GsonHelper.getAsJsonObject(object, "input"));
			ResourceLocation blockId = new ResourceLocation(GsonHelper.getAsString(object, "output"));
			Block output = Registry.BLOCK.getOptional(blockId)
					.orElseThrow(() -> new JsonSyntaxException("Unknown block id: " + blockId));

			int time = GsonHelper.getAsInt(object, "time", DEFAULT_TIME);
			return new StateCopyPureDaisyRecipe(id, input, output, time);
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buf, StateCopyPureDaisyRecipe recipe) {
			recipe.getInput().write(buf);
			buf.writeVarInt(Registry.BLOCK.getId(recipe.getOutputState().getBlock()));
			buf.writeVarInt(recipe.getTime());
		}

		@Nullable
		@Override
		public StateCopyPureDaisyRecipe fromNetwork(@Nonnull ResourceLocation id, @Nonnull FriendlyByteBuf buf) {
			StateIngredient input = StateIngredientHelper.read(buf);
			Block output = Registry.BLOCK.byId(buf.readVarInt());
			int time = buf.readVarInt();
			return new StateCopyPureDaisyRecipe(id, input, output, time);
		}
	}
}
