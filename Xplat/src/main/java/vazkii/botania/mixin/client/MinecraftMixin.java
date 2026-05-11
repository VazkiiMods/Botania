package vazkii.botania.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.Music;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.entity.GaiaGuardianEntity;
import vazkii.botania.common.handler.BotaniaSounds;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow
	public LocalPlayer player;

	/**
	 * Check for active gaia fight involving the local player, and return the appropriate gaia fight music.
	 */
	@Inject(
		method = "getSituationalMusic",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/player/LocalPlayer;level()Lnet/minecraft/world/level/Level;",
			ordinal = 1
		),
		cancellable = true
	)
	void checkForGaiaFight(CallbackInfoReturnable<Music> cir) {
		Boolean gaiaFightHardMode = GaiaGuardianEntity.isGaiaFightHardMode(this.player);
		if (gaiaFightHardMode != null) {
			cir.setReturnValue(gaiaFightHardMode ? BotaniaSounds.GAIA2_BOSS : BotaniaSounds.GAIA1_BOSS);
		}
	}
}
