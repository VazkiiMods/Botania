/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.scores.Team;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.block.block_entity.flower.functional.LooniumBlockEntity;
import vazkii.botania.common.entity.GaiaGuardianEntity;
import vazkii.botania.common.item.EquestrianVirusItem;
import vazkii.botania.common.item.equipment.bauble.CrimsonPendantItem;
import vazkii.botania.xplat.XplatAbstractions;

@Mixin(Entity.class)
public abstract class EntityMixin {
	/**
	 * Cancels some invulnerabilities conferred by items
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
	}

	/**
	 * Puts mobs spawned by a Loonium or gaia fight on their own team.
	 * This is used both for easier identification in advancements and to prevent in-fighting.
	 */
	@Inject(at = @At("HEAD"), method = "getTeam", cancellable = true)
	private void getVirtualTeam(CallbackInfoReturnable<Team> cir) {
		if (((Object) this) instanceof Mob self) {
			if (XplatAbstractions.instance().getLooniumDrop(self) != null) {
				cir.setReturnValue(LooniumBlockEntity.LOONIUM_TEAM);
			} else if (self instanceof GaiaGuardianEntity || self.level() instanceof ServerLevel serverLevel
					&& XplatAbstractions.instance().getGaiaFightParticipant(self)
							.filter(gaiaFightParticipant -> gaiaFightParticipant.getGaiaGuardian(serverLevel) != null)
							.isPresent()) {
				cir.setReturnValue(GaiaGuardianEntity.GAIA_FIGHT_TEAM);
			}
		}
	}
}
