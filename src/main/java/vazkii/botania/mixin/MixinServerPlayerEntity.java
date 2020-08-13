package vazkii.botania.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.subtile.functional.SubTileDaffomill;
import vazkii.botania.common.world.SkyblockWorldEvents;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
	/**
	 * Setups up a player when spawning into a GoG world for the first time
	 */
	@Inject(at = @At("RETURN"), method = "onSpawn")
	private void onLogin(CallbackInfo ci) {
		SkyblockWorldEvents.onPlayerJoin((ServerPlayerEntity) (Object) this);
	}

	/**
	 * Sends any item entity ages to the client when they start being tracked
	 */
	@Inject(at = @At("RETURN"), method = "onStartedTracking")
	private void sendItemAge(Entity entity, CallbackInfo ci) {
		SubTileDaffomill.onItemTrack((ServerPlayerEntity) (Object) this, entity);
	}
}
