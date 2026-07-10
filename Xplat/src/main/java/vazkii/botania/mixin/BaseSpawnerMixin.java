/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.LifeImbuerBlockEntity;

import java.util.Optional;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {
	@WrapOperation(
		method = "clientTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/BaseSpawner;isNearPlayer(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"
		)
	)
	private boolean isNearPlayerClient(BaseSpawner instance, Level level, BlockPos pos, Operation<Boolean> original) {
		boolean isNearPlayer = original.call(instance, level, pos);
		if (!isNearPlayer) {
			Optional<LifeImbuerBlockEntity> imbuerOptional = level.getBlockEntity(pos.above(), BotaniaBlockEntities.LIFE_IMBUER);
			return imbuerOptional.isPresent() && imbuerOptional.get().clientTickActive();
		}
		return true;
	}

	@WrapOperation(
		method = "serverTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/BaseSpawner;isNearPlayer(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"
		)
	)
	private boolean isNearPlayerServer(BaseSpawner instance, Level level, BlockPos pos, Operation<Boolean> original,
			@Share("hasImbuerWithMana") LocalRef<Boolean> hasImbuerWithMana) {
		boolean nearPlayer = original.call(instance, level, pos);

		Optional<LifeImbuerBlockEntity> imbuerOptional = level.getBlockEntity(pos.above(), BotaniaBlockEntities.LIFE_IMBUER);
		boolean imbuerWithMana = imbuerOptional.filter(imbuer -> imbuer.tryConsumeMana(nearPlayer)).isPresent();
		hasImbuerWithMana.set(imbuerWithMana);

		return nearPlayer || imbuerWithMana;
	}

	@Inject(
		method = "serverTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/SpawnData;getEquipment()Ljava/util/Optional;"
		)
	)
	private void applySlowDespawn(ServerLevel serverLevel, BlockPos pos, CallbackInfo ci, @Local Mob mob,
			@Share("hasImbuerWithMana") LocalRef<@Nullable Boolean> hasImbuerWithMana) {
		// ensure null-safety
		if (Boolean.TRUE.equals(hasImbuerWithMana.get())) {
			LifeImbuerBlockEntity.applySlowDespawn(serverLevel, pos, mob);
		}
	}

	@WrapOperation(
		method = "serverTick",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;noCollision(Lnet/minecraft/world/phys/AABB;)Z")
	)
	private boolean shouldSpawnMob(ServerLevel serverLevel, AABB aabb, Operation<Boolean> original) {
		// don't spawn mobs in lazy chunks
		return serverLevel.isPositionEntityTicking(BlockPos.containing(aabb.getBottomCenter()))
				&& original.call(serverLevel, aabb);
	}
}
