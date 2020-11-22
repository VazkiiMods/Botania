/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import vazkii.botania.common.core.ModStats;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.PixieHandler;
import vazkii.botania.common.item.ItemKeepIvy;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;
import vazkii.botania.common.item.equipment.bauble.*;
import vazkii.botania.common.item.relic.ItemOdinRing;
import vazkii.botania.common.entity.ModEntities;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract void increaseStat(Identifier stat, int amount);

	@Shadow @Final public PlayerInventory inventory;

	/**
	 * Registers the pixie spawn chance attribute on players
	 */
	@Inject(at = @At("RETURN"), method = "createPlayerAttributes")
	private static void addPixieAttribute(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue().add(PixieHandler.PIXIE_SPAWN_CHANCE);
	}

	/**
	 * Makes the player invulnerable to certain damage when wearing an Odin Ring
	 */
	@Inject(at = @At("HEAD"), method = "isInvulnerableTo", cancellable = true)
	private void odinRing(DamageSource src, CallbackInfoReturnable<Boolean> cir) {
		if (ItemOdinRing.onPlayerAttacked((PlayerEntity) (Object) this, src)) {
			cir.setReturnValue(true);
		}
	}

	/**
	 * Updates the distance by luminizer stat
	 */
	@Inject(
		at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;getVehicle()Lnet/minecraft/entity/Entity;"),
		method = "increaseRidingMotionStats", locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void trackLuminizerTravel(double dx, double dy, double dz, CallbackInfo ci, int cm, Entity mount) {
		if (mount.getType() == ModEntities.PLAYER_MOVER) {
			increaseStat(ModStats.LUMINIZER_ONE_CM, cm);
		}
	}

	/**
	 * Performs many reactions when being hit
	 */
	@ModifyArgs(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private void onHurt(Args args) {
		PlayerEntity self = (PlayerEntity) (Object) this;
		DamageSource src = args.get(0);
		MutableFloat amount = new MutableFloat((float) args.get(1));
		Inventory worn = EquipmentHandler.getAllWorn(self);
		for (int i = 0; i < worn.size(); i++) {
			ItemStack stack = worn.getStack(i);
			if (stack.getItem() instanceof ItemHolyCloak) {
				((ItemHolyCloak) stack.getItem()).effectOnDamage(src, amount, self, stack);
			}
		}

		// Should really make a separate inject for this, but putting it here works too
		PixieHandler.onDamageTaken(self, src);

		args.set(1, amount.getValue());
	}

	/**
	 * Tells the magnet ring about item drops
	 */
	@Inject(at = @At("HEAD"), method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;")
	public void onDrop(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
		World world = this.world;
		if (!stack.isEmpty() && !world.isClient) {
			ItemMagnetRing.onTossItem((PlayerEntity) (Object) this);
		}
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void tickBeltTiara(CallbackInfo ci) {
		ItemFlightTiara.updatePlayerFlyStatus((PlayerEntity) (Object) this);
		ItemTravelBelt.updatePlayerStepStatus((PlayerEntity) (Object) this);
	}

	@ModifyArg(index = 0, method = "handleFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;handleFallDamage(FF)Z"))
	private float cushionFall(float originalDist) {
		return ItemTravelBelt.onPlayerFall((PlayerEntity) (Object) this, originalDist);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;updateItems()V"), method = "tickMovement")
	private void tickArmor(CallbackInfo ci) {
		PlayerEntity self = (PlayerEntity) (Object) this;
		for (ItemStack stack : inventory.armor) {
			if (stack.getItem() instanceof ItemManasteelArmor) {
				((ItemManasteelArmor) stack.getItem()).onArmorTick(stack, self.world, self);
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;onAttacking(Lnet/minecraft/entity/Entity;)V"), method = "attack")
	private void onAttack(Entity target, CallbackInfo ci) {
		ItemDivaCharm.onEntityDamaged((PlayerEntity) (Object) this, target);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"), method = "dropInventory")
	private void keepIvy(CallbackInfo ci) {
		ItemKeepIvy.onPlayerDrops((PlayerEntity) (Object) this);
	}
}
