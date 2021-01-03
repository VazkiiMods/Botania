/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.MobEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.brew.potion.PotionBloodthirst;

@Mixin(WorldEntitySpawner.class)
public class MixinWorldEntitySpawner {
	@Inject(at = @At(value = "RETURN", ordinal = 1), cancellable = true, method = "func_234974_a_") // unmapped; fabric calls it isValidSpawn
	private static void bloodthirstOverride(ServerWorld world, MobEntity entity, double p_234974_2_, CallbackInfoReturnable<Boolean> cir) {
		if (PotionBloodthirst.overrideSpawn(world, entity.getPosition(), entity.getType().getClassification())) {
			cir.setReturnValue(true);
		}
	}

}
