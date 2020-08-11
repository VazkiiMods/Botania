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
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import vazkii.botania.common.core.ModStats;
import vazkii.botania.common.core.handler.PixieHandler;
import vazkii.botania.common.entity.ModEntities;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {
	@Shadow
	public abstract void addStat(ResourceLocation id, int amount);

	/**
	 * Registers the pixie spawn chance attribute on players
	 */
	@Inject(at = @At("RETURN"), method = "func_234570_el_")
	private static void addPixieAttribute(CallbackInfoReturnable<AttributeModifierMap.MutableAttribute> cir) {
		cir.getReturnValue().createMutableAttribute(PixieHandler.PIXIE_SPAWN_CHANCE);
	}

	/**
	 * Updates the distance by luminizer stat
	 */
	@Inject(
		at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;getRidingEntity()Lnet/minecraft/entity/Entity;"),
		method = "addMountedMovementStat", locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void trackLuminizerTravel(double dx, double dy, double dz, CallbackInfo ci, int cm, Entity mount) {
		if (mount.getType() == ModEntities.PLAYER_MOVER) {
			addStat(ModStats.LUMINIZER_ONE_CM, cm);
		}
	}
}
