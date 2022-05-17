/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.block.tile.TileSpawnerClaw;

@Mixin(BaseSpawner.class)
public class MixinBaseSpawner {
	@Inject(at = @At("RETURN"), method = "isNearPlayer", cancellable = true)
	private void injectNearPlayer(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		// If vanilla is out of range, then we do our work
		if (!cir.getReturnValueZ()) {
			TileSpawnerClaw.onSpawnerNearPlayer(level, pos, cir);
		}
	}
}
