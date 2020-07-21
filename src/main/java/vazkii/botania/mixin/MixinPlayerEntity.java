/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.core.handler.PixieHandler;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
	/**
	 * Registers the pixie spawn chance attribute on players
	 */
	@Inject(at = @At("RETURN"), method = "func_234570_el_")
	private static void addPixieAttribute(CallbackInfoReturnable<AttributeModifierMap.MutableAttribute> cir) {
		cir.getReturnValue().func_233814_a_(PixieHandler.PIXIE_SPAWN_CHANCE);
	}
}
