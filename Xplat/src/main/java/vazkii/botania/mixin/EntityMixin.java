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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.BotaniaDamageTypes;
import vazkii.botania.common.item.EquestrianVirusItem;
import vazkii.botania.common.item.equipment.bauble.CrimsonPendantItem;
import vazkii.botania.common.lib.BotaniaTags;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	abstract EntityType<?> getType();

	/**
	 * Cancels some invulnerabilities conferred by items
	 * 
	 * Adds invulnerability to nonliving entities for key explosions
	 */
	@Inject(at = @At("HEAD"), method = "isInvulnerableTo", cancellable = true)
	private void checkInvulnerabilities(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (((Object) this) instanceof LivingEntity self) {
			if (EquestrianVirusItem.onLivingHurt(self, source)) {
				cir.setReturnValue(true);
			} else if (CrimsonPendantItem.onDamage(self, source)) {
				cir.setReturnValue(true);
			}
		}
		if (source.is(BotaniaDamageTypes.KEY_EXPLOSION) && this.getType().is(BotaniaTags.Entities.KEY_IMMUNE)) {
			cir.setReturnValue(true);
		}
	}

}
