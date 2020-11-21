package vazkii.botania.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.subtile.functional.SubTileDaffomill;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.SleepingHandler;
import vazkii.botania.common.world.SkyblockWorldEvents;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
	/**
	 * Setups up a player when spawning into a GoG world for the first time
	 */
	@Inject(at = @At("RETURN"), method = "onSpawn")
	private void onLogin(CallbackInfo ci) {
		if (Botania.gardenOfGlassLoaded) {
			SkyblockWorldEvents.onPlayerJoin((ServerPlayerEntity) (Object) this);
		}
	}

	/**
	 * Sends any item entity ages to the client when they start being tracked
	 */
	@Inject(at = @At("RETURN"), method = "onStartedTracking")
	private void sendItemAge(Entity entity, CallbackInfo ci) {
		SubTileDaffomill.onItemTrack((ServerPlayerEntity) (Object) this, entity);
	}

	@Inject(at = @At("HEAD"), method = "trySleep", cancellable = true)
	private void preventGaiaSleep(BlockPos pos, CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> cir) {
		PlayerEntity.SleepFailureReason fail = SleepingHandler.trySleep((PlayerEntity) (Object) this);
		if (fail != null) {
			cir.setReturnValue(Either.left(fail));
		}
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void onTick(CallbackInfo ci) {
		if (EquipmentHandler.instance instanceof EquipmentHandler.InventoryEquipmentHandler) {
			((EquipmentHandler.InventoryEquipmentHandler) EquipmentHandler.instance).onPlayerTick((PlayerEntity) (Object) this);
		}
	}


}
