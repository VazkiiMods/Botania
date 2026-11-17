/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.mixin;

import net.minecraft.world.entity.vehicle.AbstractMinecart;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.SpectralRailBlock;

@Mixin(AbstractMinecart.class)
public class AbstractMinecartMixin {
	@Inject(at = @At("RETURN"), method = "tick")
	private void onTick(CallbackInfo ci) {
		AbstractMinecart self = (AbstractMinecart) (Object) this;
		((SpectralRailBlock) BotaniaBlocks.SPECTRAL_RAIL).tickCart(self);
	}
}
