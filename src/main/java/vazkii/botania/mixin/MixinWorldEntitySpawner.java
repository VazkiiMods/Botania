/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.brew.potion.PotionBloodthirst;

@Mixin(SpawnHelper.class)
public class MixinWorldEntitySpawner {
	// Jump over entity.canSpawn(pos, reason) and entity.canSpawn(pos) under Bloodlust
	@Inject(at = @At(value = "RETURN", ordinal = 1), cancellable = true, method = "isValidSpawn")
	private static void bloodthirstOverride(ServerWorld world, MobEntity entity, double p_234974_2_, CallbackInfoReturnable<Boolean> cir) {
		if (PotionBloodthirst.overrideSpawn(world, entity.getBlockPos(), entity.getType().getSpawnGroup())) {
			cir.setReturnValue(true);
		}
	}

}
