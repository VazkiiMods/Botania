package vazkii.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonStructureResolver;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.helper.ForcePushHelper;

@Mixin(value = PistonStructureResolver.class)
public class PistonStructureResolverMixin {
	// not final, due to access widener/transformer definition:
	@SuppressWarnings("ShadowModifiers")
	@Shadow
	private BlockPos pistonPos;

	/**
	 * Since the pushing piston block is handled separately via its position,
	 * replace it with the force relay's position when pushing blocks that way.
	 */
	@Inject(method = "<init>", at = @At(value = "RETURN"))
	private void modifyForcePushOrigin(Level level, BlockPos pistonPos, Direction pistonDirection, boolean extending, CallbackInfo ci) {
		this.pistonPos = ForcePushHelper.isForcePush() ? ForcePushHelper.getForcePushOrigin() : pistonPos;
	}
}
