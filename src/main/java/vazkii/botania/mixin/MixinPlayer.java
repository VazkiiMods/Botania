/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import vazkii.botania.common.core.ModStats;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.PixieHandler;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.item.ItemKeepIvy;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;
import vazkii.botania.common.item.equipment.bauble.*;
import vazkii.botania.common.item.relic.ItemOdinRing;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {

	protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Shadow
	public abstract void awardStat(ResourceLocation stat, int amount);

	@Shadow
	@Final
	public Inventory inventory;

	@Unique
	private LivingEntity critTarget;

	@Unique
	private boolean critting;

	/**
	 * Registers the pixie spawn chance attribute on players
	 */
	@Inject(at = @At("RETURN"), method = "createAttributes")
	private static void addPixieAttribute(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		cir.getReturnValue().add(PixieHandler.PIXIE_SPAWN_CHANCE);
	}

	/**
	 * Makes the player invulnerable to certain damage when wearing an Odin Ring
	 */
	@Inject(at = @At("HEAD"), method = "isInvulnerableTo", cancellable = true)
	private void odinRing(DamageSource src, CallbackInfoReturnable<Boolean> cir) {
		if (ItemOdinRing.onPlayerAttacked((Player) (Object) this, src)) {
			cir.setReturnValue(true);
		}
	}

	/**
	 * Updates the distance by luminizer stat
	 */
	@Inject(
		at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/player/Player;getVehicle()Lnet/minecraft/world/entity/Entity;"),
		method = "checkRidingStatistics", locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void trackLuminizerTravel(double dx, double dy, double dz, CallbackInfo ci, int cm, Entity mount) {
		if (mount.getType() == ModEntities.PLAYER_MOVER) {
			awardStat(ModStats.LUMINIZER_ONE_CM, cm);
		}
	}

	/**
	 * Performs many reactions when being hit
	 */
	@ModifyArgs(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private void onHurt(Args args) {
		Player self = (Player) (Object) this;
		DamageSource src = args.get(0);
		MutableFloat amount = new MutableFloat((float) args.get(1));
		Container worn = EquipmentHandler.getAllWorn(self);
		for (int i = 0; i < worn.getContainerSize(); i++) {
			ItemStack stack = worn.getItem(i);
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
	@Inject(at = @At("HEAD"), method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;")
	public void onDrop(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
		Level world = this.level;
		if (!stack.isEmpty() && !world.isClientSide) {
			ItemMagnetRing.onTossItem((Player) (Object) this);
		}
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void tickBeltTiara(CallbackInfo ci) {
		ItemFlightTiara.updatePlayerFlyStatus((Player) (Object) this);
		ItemTravelBelt.updatePlayerStepStatus((Player) (Object) this);
	}

	@ModifyArg(index = 0, method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;causeFallDamage(FF)Z"))
	private float cushionFall(float originalDist) {
		return ItemTravelBelt.onPlayerFall((Player) (Object) this, originalDist);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;tick()V"), method = "aiStep")
	private void tickArmor(CallbackInfo ci) {
		Player self = (Player) (Object) this;
		for (ItemStack stack : inventory.armor) {
			if (stack.getItem() instanceof ItemManasteelArmor) {
				((ItemManasteelArmor) stack.getItem()).onArmorTick(stack, self.level, self);
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setLastHurtMob(Lnet/minecraft/world/entity/Entity;)V"), method = "attack")
	private void onAttack(Entity target, CallbackInfo ci) {
		ItemDivaCharm.onEntityDamaged((Player) (Object) this, target);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"), method = "dropEquipment")
	private void keepIvy(CallbackInfo ci) {
		ItemKeepIvy.onPlayerDrops((Player) (Object) this);
	}

	// Capture the entity we are attacking, at the start of the method part dealing damage.
	@Inject(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getKnockbackBonus(Lnet/minecraft/world/entity/LivingEntity;)I"),
		method = "attack"
	)
	private void captureTarget(Entity target, CallbackInfo ci) {
		if (target instanceof LivingEntity) {
			this.critTarget = (LivingEntity) target;
		}
	}

	// Clear the entity on any return after the capture.
	@Inject(
		at = @At(value = "RETURN"), method = "attack",
		slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getKnockbackBonus(Lnet/minecraft/world/entity/LivingEntity;)I"))
	)
	private void clearTarget(CallbackInfo ci) {
		this.critTarget = null;
		this.critting = false;
	}

	// Multiply the damage on crit. This might be a little bit brittle.
	// Because of a Mixin bug you can't actually use a slice for LOAD.
	// See https://github.com/SpongePowered/Mixin/issues/429
	@ModifyVariable(at = @At(value = "LOAD", ordinal = 2), method = "attack", ordinal = 0)
	private float onCritMul(float f) {
		this.critting = true;
		return ((ItemTerrasteelHelm) ModItems.terrasteelHelm).onCritDamageCalc(f, (Player) (Object) this);
	}

	// Perform damage source modifications and apply the potion effects.
	@ModifyArg(
		method = "attack",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z")
	)
	private DamageSource onDamageTarget(DamageSource source, float amount) {
		if (this.critting && this.critTarget != null) {
			((ItemTerrasteelHelm) ModItems.terrasteelHelm).onEntityAttacked(source, amount, (Player) (Object) this, critTarget);
		}
		return source;
	}
}
