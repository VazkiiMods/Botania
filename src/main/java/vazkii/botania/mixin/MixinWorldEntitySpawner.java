/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.NaturalSpawner;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.brew.potion.PotionBloodthirst;

@Mixin(NaturalSpawner.class)
public class MixinWorldEntitySpawner {
	// Jump over entity.checkSpawnRules(pos, reason) and entity.checkSpawnObstruction(pos) under Bloodlust
	@Inject(at = @At(value = "RETURN", ordinal = 1), cancellable = true, method = "isValidPositionForMob")
	private static void bloodthirstOverride(ServerLevel world, Mob entity, double p_234974_2_, CallbackInfoReturnable<Boolean> cir) {
		if (PotionBloodthirst.overrideSpawn(world, entity.blockPosition(), entity.getType().getCategory())) {
			cir.setReturnValue(true);
		}
	}

}
