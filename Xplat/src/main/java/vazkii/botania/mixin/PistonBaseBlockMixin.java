package vazkii.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.helper.EthicalTntHelper;
import vazkii.botania.common.helper.ForcePushHelper;

/**
 * Additional logic for pistons:
 * <ul>
 * <li>Detection for "unethical" (duplicated) TNT</li>
 * <li>Hooks for Force Lens and Force Relay pushing behavior. (Reusing piston pushing code instead of copying it allows
 * us to inherit Movable Block Entities modifications by mods like Quark or Carpet.)</li>
 * </ul>
 */
@Mixin(value = PistonBaseBlock.class, priority = 999 /* before MBE implementations like Quark or Carpet */)
public abstract class PistonBaseBlockMixin {
	@Inject(
		at = @At("HEAD"),
		method = "moveBlocks(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Z"
	)
	private void preMoveBlocks(Level level, BlockPos pos, Direction dir, boolean extending,
			CallbackInfoReturnable<Boolean> cir) {
		if (!level.isClientSide()) {
			EthicalTntHelper.startTrackingTntEntities();
			ForcePushHelper.pushMovementTypeContext(extending);
		}
	}

	@Inject(
		at = @At(value = "RETURN"),
		method = "moveBlocks(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Z"
	)
	private void postMoveBlocks(Level level, BlockPos pos, Direction dir, boolean extending,
			CallbackInfoReturnable<Boolean> cir) {
		if (!level.isClientSide()) {
			ForcePushHelper.popMovementTypeContext();
			EthicalTntHelper.endTrackingTntEntitiesAndCheck();
		}
	}

	/**
	 * When force-pushing, set the {@code extending} parameter to {@code false} right before the {@code if} block that
	 * creates a moving piston head. This will skip all the code we don't want to execute.
	 */
	@ModifyVariable(
		method = "moveBlocks",
		at = @At(value = "LOAD", ordinal = 4), // 4th read access within target method (1-based count)
		ordinal = 0, // first local variable (0-based count, including arguments) matching mixin method's argument type
		argsOnly = true // only from among method arguments
	)
	private boolean isExtendingNonForcePusher(boolean extending) {
		return !ForcePushHelper.isForcePush() && extending;
	}
}
