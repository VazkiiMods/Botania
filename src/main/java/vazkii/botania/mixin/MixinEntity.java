/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.item.ItemVirus;
import vazkii.botania.common.item.equipment.bauble.ItemSuperLavaPendant;

@Mixin(Entity.class)
public class MixinEntity {
	/**
	 * Cancels some invulnerabilities conferred by items
	 */
	@Inject(at = @At("HEAD"), method = "isInvulnerableTo", cancellable = true)
	private void checkInvulnerabilities(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (((Object) this) instanceof LivingEntity) {
			LivingEntity self = (LivingEntity) (Object) this;
			if (ItemVirus.onLivingHurt(self, source)) {
				cir.setReturnValue(true);
			} else if (ItemSuperLavaPendant.onDamage(self, source)) {
				cir.setReturnValue(true);
			}
		}
	}

}
