package vazkii.botania.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import vazkii.botania.common.HurtByTargetGoalAccess;
import vazkii.botania.common.TargetingConditionsAccess;

@Mixin(HurtByTargetGoal.class)
abstract class HurtByTargetGoalMixin extends TargetGoal implements HurtByTargetGoalAccess {
	@Shadow
	@Final
	private static TargetingConditions HURT_BY_TARGETING;
	@Unique
	private boolean brainwashed;

	private static final TargetingConditions BRAINWASHED_CONDITIONS = HURT_BY_TARGETING.copy();

	static {
		((TargetingConditionsAccess) BRAINWASHED_CONDITIONS).botania$setBrainwashed();
	}

	public HurtByTargetGoalMixin(Mob $$0, boolean $$1) {
		super($$0, $$1);
	}

	@Override
	public void botania$setBrainwashed() {
		this.brainwashed = true;
	}

	@Redirect(
		method = "canUse()Z",
		at = @At(
			target = "Lnet/minecraft/world/entity/ai/goal/target/HurtByTargetGoal;canAttack(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;)Z",
			value = "INVOKE"
		)
	)
	boolean replaceAttackPredicate(HurtByTargetGoal instance, LivingEntity livingEntity, TargetingConditions targetingConditions) {
		return this.canAttack(livingEntity, this.brainwashed ? BRAINWASHED_CONDITIONS : HURT_BY_TARGETING);
	}
}
