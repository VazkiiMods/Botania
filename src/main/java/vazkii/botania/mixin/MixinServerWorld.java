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
import net.minecraft.server.world.ServerWorld;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.Botania;
import vazkii.botania.common.world.SkyblockWorldEvents;

@Mixin(ServerWorld.class)
public class MixinServerWorld {
	@Inject(at = @At("RETURN"), method = "loadEntityUnchecked")
	private void onEntityAdd(Entity entity, CallbackInfo ci) {
		if (Botania.gardenOfGlassLoaded) {
			SkyblockWorldEvents.syncGogStatus(entity);
		}
	}
}
