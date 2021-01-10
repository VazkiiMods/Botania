/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.brew.potion.PotionBloodthirst;

import java.util.Random;

@Mixin(EntitySpawnPlacementRegistry.class)
public class MixinEntitySpawnPlacementRegistry {
	// This injection is kind of scuffed:
	// In vanilla code, canSpawnEntity calls a per-entity predicate to determine spawnability.
	// Many (but not all) hostile mobs register their own canSpawn predicates or fall back to MonsterEntity::canSpawnInLightLevel,
	// which ordinarily should be jumped over under Bloodthirst.
	// However, certain mobs (e.g. slimes) use the predicate for actual logic (e.g. slimechunks),
	// and we jump over that in Bloodthirst as well.
	@Inject(at = @At("RETURN"), cancellable = true, method = "canSpawnEntity")
	private static <T extends Entity> void bloodthirstOverride(EntityType<T> type, IServerWorld world, SpawnReason reason, BlockPos position, Random rng, CallbackInfoReturnable<Boolean> cir) {
		if (reason == SpawnReason.NATURAL && PotionBloodthirst.overrideSpawn(world, position, type.getClassification())) {
			cir.setReturnValue(true);
		}
	}

}
