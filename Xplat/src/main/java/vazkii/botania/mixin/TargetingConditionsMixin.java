package vazkii.botania.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import vazkii.botania.common.TargetingConditionsAccess;

@Mixin(TargetingConditions.class)
abstract class TargetingConditionsMixin implements TargetingConditionsAccess {
	private boolean brainwashed;

	@Redirect(
		method = "test",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"
		)
	)
	boolean redirectCanAttack(LivingEntity instance, LivingEntity entity) {
		return this.brainwashed || instance.canAttack(entity);
	}

	@Redirect(
		method = "test",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;canAttackType(Lnet/minecraft/world/entity/EntityType;)Z"
		)
	)
	boolean redirectCanAttackType(LivingEntity instance, EntityType<?> entityType) {
		return this.brainwashed || instance.canAttackType(entityType);
	}

	@Redirect(
		method = "test",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z"
		)
	)
	boolean redirectIsAlliedTo(LivingEntity instance, Entity entity) {
		return !this.brainwashed && instance.isAlliedTo(entity);
	}

	@Override
	public void botania$setBrainwashed() {
		this.brainwashed = true;
	}
}
