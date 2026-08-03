/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.block.block_entity.flower.functional.VinculotusBlockEntity;

@Mixin(EnderMan.class)
public abstract class EnderManMixin extends Monster {

	protected EnderManMixin(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "teleport(DDD)Z", at = @At("HEAD"), cancellable = true)
	private void preventTeleportation(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
		if (!level().isClientSide() && VinculotusBlockEntity.preventEndermanTeleport(this)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "teleport(DDD)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/EnderMan;isSilent()Z"))
	private void disableFutureTeleportation(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
		if (!level().isClientSide()) {
			VinculotusBlockEntity.postEndermanTeleport(this);
		}
	}
}
