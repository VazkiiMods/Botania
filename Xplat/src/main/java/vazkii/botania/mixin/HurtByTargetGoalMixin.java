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

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HurtByTargetGoal.class)
public class HurtByTargetGoalMixin {
	/**
	 * Prevent mobs from punching themselves. This can happen when either the Heisei Dream or Diva Charm brainwash
	 * another mob and that mob alerts other mobs to attack the same target. If the target is among the alerted mobs, it
	 * would attack itself.
	 */
	@WrapWithCondition(
		method = "alertOthers",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ai/goal/target/HurtByTargetGoal;alertOther(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/entity/LivingEntity;)V"
		)
	)
	private boolean checkNotTargetingSelf(HurtByTargetGoal instance, Mob mob, LivingEntity target) {
		return mob != target;
	}
}
