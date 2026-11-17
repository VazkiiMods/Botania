package vazkii.botania.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import vazkii.botania.api.block.EdibleBlockWithEffects;
import vazkii.botania.common.lib.BotaniaTags;

/**
 * Allows sheep to eat Botania grass variants and applies effects of blocks that are an {@link EdibleBlockWithEffects}.
 */
@Mixin(EatBlockGoal.class)
public class EatBlockGoalMixin {
	@Unique
	private static final String BOTANIA_EATEN_BLOCK_POS_REF = "eatenBlockPos";
	@Unique
	private static final String BOTANIA_EATEN_BLOCK_STATE_REF = "eatenBlockState";

	@Final
	@Shadow
	private Level level;

	/**
	 * Ensure the mob can properly use this goal not just for vanilla grass blocks, but also for those added by Botania.
	 */
	@WrapOperation(
		method = { "canUse", "tick" },
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z",
			ordinal = 0
		),
		slice = @Slice(
			from = @At(
				value = "FIELD",
				target = "Lnet/minecraft/world/level/block/Blocks;GRASS_BLOCK:Lnet/minecraft/world/level/block/Block;",
				opcode = Opcodes.GETSTATIC
			)
		)
	)
	private boolean canEatBotaniaGrasses(BlockState instance, Block block, Operation<Boolean> original) {
		return original.call(instance, block) || instance.is(BotaniaTags.Blocks.SHEEP_EDIBLE_GRASSES);
	}

	/**
	 * After eating an {@link EdibleBlockWithEffects}, apply that block's eating effect(s)
	 */
	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;ate()V"))
	private void addEffectsAfterEating(Mob mob, Operation<Void> original,
			@Share(BOTANIA_EATEN_BLOCK_POS_REF) LocalRef<@Nullable BlockPos> posRef,
			@Share(BOTANIA_EATEN_BLOCK_STATE_REF) LocalRef<@Nullable BlockState> stateRef) {
		@Nullable
		BlockPos pos = posRef.get();
		@Nullable
		BlockState state = stateRef.get();
		original.call(mob);
		if (pos == null || state == null) {
			// failed to capture values
			return;
		}
		EdibleBlockWithEffects capability = EdibleBlockWithEffects.LOOKUP.find(level, pos, state, null);
		if (capability != null) {
			capability.onEatenBy(pos, state, mob);
		}
	}

	// helper methods for capturing relevant values for addEffectsAfterEating()

	@ModifyExpressionValue(
		method = "tick",
		at = {
				@At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;blockPosition()Lnet/minecraft/core/BlockPos;"),
				@At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;below()Lnet/minecraft/core/BlockPos;")
		}
	)
	private BlockPos captureEatenBlockPosition(BlockPos value,
			@Share(BOTANIA_EATEN_BLOCK_POS_REF) LocalRef<BlockPos> posRef) {
		posRef.set(value);
		return value;
	}

	@ModifyExpressionValue(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
		)
	)
	private BlockState captureEatenBlockState(BlockState value,
			@Share(BOTANIA_EATEN_BLOCK_STATE_REF) LocalRef<BlockState> stateRef) {
		stateRef.set(value);
		return value;
	}
}
