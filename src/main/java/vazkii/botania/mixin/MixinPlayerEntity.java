/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.core.handler.PixieHandler;
import vazkii.botania.common.item.relic.ItemOdinRing;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {
	/**
	 * Registers the pixie spawn chance attribute on players
	 */
	@Inject(at = @At("RETURN"), method = "createPlayerAttributes")
	private static void addPixieAttribute(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue().add(PixieHandler.PIXIE_SPAWN_CHANCE);
	}

	/**
	 * Makes the player invulnerable to certain damage when wearing an Odin Ring
	 */
	@Inject(at = @At("HEAD"), method = "isInvulnerableTo")
	private void odinRing(DamageSource src, CallbackInfoReturnable<Boolean> cir) {
		if (ItemOdinRing.onPlayerAttacked((PlayerEntity) (Object) this, src)) {
			cir.setReturnValue(true);
		}
	}
}
