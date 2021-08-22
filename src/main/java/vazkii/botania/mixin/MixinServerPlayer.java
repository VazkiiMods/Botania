/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.SleepingHandler;
import vazkii.botania.common.world.SkyblockWorldEvents;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {
	/**
	 * Setups up a player when spawning into a GoG world for the first time
	 */
	@Inject(at = @At("RETURN"), method = "initMenu")
	private void onLogin(CallbackInfo ci) {
		if (Botania.gardenOfGlassLoaded) {
			SkyblockWorldEvents.onPlayerJoin((ServerPlayer) (Object) this);
		}
	}

	@Inject(at = @At("HEAD"), method = "startSleepInBed", cancellable = true)
	private void preventGaiaSleep(BlockPos pos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir) {
		Player.BedSleepingProblem fail = SleepingHandler.trySleep((Player) (Object) this);
		if (fail != null) {
			cir.setReturnValue(Either.left(fail));
		}
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void onTick(CallbackInfo ci) {
		if (EquipmentHandler.instance instanceof EquipmentHandler.InventoryEquipmentHandler) {
			((EquipmentHandler.InventoryEquipmentHandler) EquipmentHandler.instance).onPlayerTick((Player) (Object) this);
		}
	}

}
