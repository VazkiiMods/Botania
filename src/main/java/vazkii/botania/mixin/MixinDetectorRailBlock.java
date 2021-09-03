/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.entity.EntityPoolMinecart;

import java.util.List;
import java.util.function.Predicate;

@Mixin(DetectorRailBlock.class)
public class MixinDetectorRailBlock {
	@Shadow
	private <T extends AbstractMinecart> List<T> getInteractingMinecartOfType(Level level, BlockPos pos, Class<T> clazz, Predicate<Entity> filter) {
		throw new IllegalStateException();
	}

	// Target: before the check for container carts
	@Inject(
		method = "getAnalogOutputSignal", cancellable = true, at = @At(
			value = "CONSTANT", ordinal = 0, args = "classValue=net/minecraft/world/entity/vehicle/AbstractMinecart"
		)
	)
	private void getManaCartOutputSignal(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		List<EntityPoolMinecart> list = this.getInteractingMinecartOfType(level, pos, EntityPoolMinecart.class, entity -> true);
		if (!list.isEmpty()) {
			cir.setReturnValue(list.get(0).getComparatorLevel());
		}
	}
}
