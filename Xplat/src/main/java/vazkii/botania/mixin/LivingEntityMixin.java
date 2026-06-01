/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.api.internal.GaiaFightParticipant;
import vazkii.botania.api.item.BlockChangedListenerBauble;
import vazkii.botania.common.handler.EquipmentHandler;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "onChangedBlock", at = @At("HEAD"))
	void notifyBlockChangedListenerItems(ServerLevel level, BlockPos pos, CallbackInfo ci) {
		LivingEntity living = botania_self();
		Container equipment = EquipmentHandler.getAllWorn(living);

		for (int slot = 0; slot < equipment.getContainerSize(); slot++) {
			ItemStack stack = equipment.getItem(slot);
			if (stack.getItem() instanceof BlockChangedListenerBauble bauble) {
				bauble.onChangedBlock(stack, living, level, pos);
			}
		}
	}

	@WrapOperation(
		method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/LivingEntity;canBeSeenAsEnemy()Z"
		)
	)
	private boolean isValidTargetWhileInGaiaFight(LivingEntity target, Operation<Boolean> original) {
		return original.call(target) && (!(botania_self() instanceof Mob mob)
				|| !GaiaFightParticipant.HOLDER.existsFor(mob)
				|| mob.isWithinRestriction(target.blockPosition()));
	}

	@Unique
	private LivingEntity botania_self() {
		return (LivingEntity) (Object) this;
	}
}
