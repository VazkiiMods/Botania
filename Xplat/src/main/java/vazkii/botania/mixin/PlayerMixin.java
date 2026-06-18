/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.common.PlayerAccess;
import vazkii.botania.common.item.ResoluteIvyItem;
import vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;
import vazkii.botania.common.item.relic.RingOfOdinItem;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerAccess {
	protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@Unique
	private LivingEntity terraWillCritTarget;

	@Override
	public void botania$setCritTarget(LivingEntity entity) {
		this.terraWillCritTarget = entity;
	}

	// Perform damage source modifications and apply the potion effects.
	@ModifyArg(
		method = "attack",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
	)
	private DamageSource onDamageTarget(DamageSource source, float amount) {
		if (this.terraWillCritTarget != null) {
			DamageSource newSource = TerrasteelHelmItem.onEntityAttacked(source, amount, (Player) (Object) this, terraWillCritTarget);
			this.terraWillCritTarget = null;
			return newSource;
		}
		return source;
	}

	// Clear the entity on any return after the capture.
	@Inject(
		at = @At(value = "RETURN"),
		method = "attack"
	)
	private void clearCritTarget(CallbackInfo ci) {
		this.terraWillCritTarget = null;
	}

	@Inject(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"),
		method = "dropEquipment"
	)
	private void captureIvyDrops(CallbackInfo ci) {
		ResoluteIvyItem.keepDropsOnDeath((Player) (Object) this);
	}

	/**
	 * Makes the player invulnerable to certain damage when wearing an Odin Ring
	 */
	@Inject(at = @At("HEAD"), method = "isInvulnerableTo", cancellable = true)
	private void odinRing(DamageSource src, CallbackInfoReturnable<Boolean> cir) {
		if (RingOfOdinItem.onPlayerAttacked((Player) (Object) this, src)) {
			cir.setReturnValue(true);
		}
	}

	/**
	 * Skip the entire XP orb pickup logic if the player is near a Rosa Arcana, as signified by a high takeXpDelay.
	 * (This is needed due to mods like Clumps just ignoring the XP pickup delay altogether.)
	 * 
	 * @see vazkii.botania.common.block.block_entity.flower.generating.RosaArcanaBlockEntity#drainPlayerXp(ServerLevel,
	 *      AABB)
	 */
	@WrapWithCondition(
		method = "aiStep",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/player/Player;touch(Lnet/minecraft/world/entity/Entity;)V",
			ordinal = 0
		),
		slice = @Slice(
			from = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/Util;getRandom(Ljava/util/List;Lnet/minecraft/util/RandomSource;)Ljava/lang/Object;"
			)
		)
	)
	private boolean shouldPickupXp(Player instance, Entity entity) {
		// should only be XP orbs at this point, but just to be sure we always allow other entity types
		return instance.takeXpDelay < 5 || entity.getType() != EntityType.EXPERIENCE_ORB;
	}
}
