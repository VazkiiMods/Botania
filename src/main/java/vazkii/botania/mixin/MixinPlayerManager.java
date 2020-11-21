package vazkii.botania.mixin;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {
	@Inject(at = @At("HEAD"), method = "remove")
	private void onLogout(ServerPlayerEntity player, CallbackInfo ci) {
		ItemTravelBelt.playerLoggedOut(player);
		ItemFlightTiara.playerLoggedOut(player);
	}
}
