/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.brew.potion.PotionSoulCross;
import vazkii.botania.common.internal_caps.LooniumComponent;
import vazkii.botania.common.item.ItemCraftingHalo;
import vazkii.botania.common.item.equipment.bauble.ItemTravelBelt;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumAxe;
import vazkii.botania.common.item.rod.ItemGravityRod;
import vazkii.botania.fabric.internal_caps.CCAInternalEntityComponents;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

	public MixinLivingEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Shadow
	public abstract ItemStack getItemInHand(InteractionHand hand);

	@Shadow
	protected int lastHurtByPlayerTime;

	@Inject(at = @At("HEAD"), cancellable = true, method = "dropFromLootTable")
	private void dropLoonium(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		LooniumComponent comp = CCAInternalEntityComponents.LOONIUM_DROP.getNullable(this);
		if (comp != null && !comp.getDrop().isEmpty()) {
			spawnAtLocation(comp.getDrop());
			ci.cancel();
		}
	}

	@Inject(at = @At("RETURN"), method = "dropAllDeathLoot")
	private void dropEnd(DamageSource source, CallbackInfo ci) {
		ItemElementiumAxe.onEntityDrops(lastHurtByPlayerTime, source, (LivingEntity) (Object) this);
	}

	/**
	 * Applies soul cross effect when being killed
	 */
	@Inject(at = @At("RETURN"), method = "createWitherRose")
	private void healKiller(@Nullable LivingEntity adversary, CallbackInfo ci) {
		if (!level.isClientSide && adversary != null) {
			PotionSoulCross.onEntityKill((LivingEntity) (Object) this, adversary);
		}

	}

	@Inject(at = @At("HEAD"), method = "swing(Lnet/minecraft/world/InteractionHand;Z)V", cancellable = true)
	private void onSwing(InteractionHand hand, boolean bl, CallbackInfo ci) {
		ItemStack stack = getItemInHand(hand);
		LivingEntity self = (LivingEntity) (Object) this;
		if (!level.isClientSide) {
			if (stack.getItem() instanceof ItemCraftingHalo && ItemCraftingHalo.onEntitySwing(stack, self)) {
				ci.cancel();
			} else if (stack.getItem() instanceof ItemGravityRod) {
				ItemGravityRod.onEntitySwing(self);
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "jumpFromGround")
	private void onJump(CallbackInfo ci) {
		ItemTravelBelt.onPlayerJump((LivingEntity) (Object) this);
	}
}
