/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.crafting;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.Optional;

public abstract class BlockStateRecipe implements vazkii.botania.api.recipe.BlockStateRecipe {
	private final StateIngredient input;
	private final StateIngredient output;
	@Nullable
	private final CacheableFunction preUpdateFunction;
	@Nullable
	private final CacheableFunction successFunction;

	public BlockStateRecipe(StateIngredient input, StateIngredient output,
			@Nullable CacheableFunction preUpdateFunction, @Nullable CacheableFunction successFunction) {
		this.input = input;
		this.output = output;
		this.preUpdateFunction = preUpdateFunction;
		this.successFunction = successFunction;
	}

	public StateIngredient getInput() {
		return input;
	}

	public StateIngredient getOutput() {
		return output;
	}

	public Optional<CacheableFunction> getPreUpdateFunction() {
		return Optional.ofNullable(this.preUpdateFunction);
	}

	public Optional<CacheableFunction> getSuccessFunction() {
		return Optional.ofNullable(this.successFunction);
	}

	public static void replaceBlock(BlockPos pos, vazkii.botania.api.recipe.BlockStateRecipe recipe,
			BlockState stateToPlace, ServerLevel serverLevel, Runnable successCallback) {
		executeFunction(serverLevel, pos, recipe.getPreUpdateFunction());

		if (serverLevel.setBlockAndUpdate(pos, stateToPlace)) {
			if (BotaniaConfig.common().blockBreakParticles()) {
				serverLevel.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(stateToPlace));
			}
			successCallback.run();
			serverLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
			executeFunction(serverLevel, pos, recipe.getSuccessFunction());
		}
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private static void executeFunction(ServerLevel serverLevel, BlockPos pos, Optional<CacheableFunction> optionalCacheableFunction) {
		MinecraftServer server = serverLevel.getServer();
		optionalCacheableFunction
				.flatMap(cached -> cached.get(server.getFunctions()))
				.ifPresent(command -> {
					var context = server.getFunctions().getGameLoopSender()
							.withLevel(serverLevel)
							.withPosition(Vec3.atBottomCenterOf(pos));
					server.getFunctions().execute(command, context);
				});
	}
}
