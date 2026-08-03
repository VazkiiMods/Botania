/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import vazkii.botania.common.block.block_entity.flower.functional.VinculotusBlockEntity;

@Mixin(EnderMan.class)
public class EnderManFabricMixin {
	@WrapOperation(method = "teleport(DDD)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/EnderMan;randomTeleport(DDDZ)Z"))
	private boolean checkForVincs(EnderMan instance, double x, double y, double z, boolean broadcastTeleport, Operation<Boolean> original) {
		Vec3 vincPos = VinculotusBlockEntity.onEndermanTeleport(instance, x, y, z);
		if (vincPos != null) {
			x = vincPos.x();
			y = vincPos.y();
			z = vincPos.z();
		}
		return original.call(instance, x, y, z, broadcastTeleport);
	}
}
