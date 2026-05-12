package vazkii.botania.mixin;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.entity.GaiaGuardianEntity;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin {
	/**
	 * If the beacon is the center of a gaia arena, invalidate the base during the fight to disable it.
	 */
	@Inject(method = "updateBase", at = @At("HEAD"), cancellable = true)
	private static void disableGaiaFightBeacon(Level level, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
		if (GaiaGuardianEntity.isGaiaFightBeacon(level, x, y, z)) {
			cir.setReturnValue(0);
		}
	}
}
