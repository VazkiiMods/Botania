/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.ServerWorldAccess;
import vazkii.botania.mixin.AccessorAbstractHorseEntity;

public class ItemVirus extends Item {
	public ItemVirus(Settings builder) {
		super(builder);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity living, Hand hand) {
		if (living instanceof HorseBaseEntity && !(living instanceof LlamaEntity)) {
			if (player.world.isClient) {
				return ActionResult.SUCCESS;
			}
			HorseBaseEntity horse = (HorseBaseEntity) living;
			if (horse.isTame()) {
				SimpleInventory inv = ((AccessorAbstractHorseEntity) horse).getItems();
				ItemStack saddle = inv.getStack(0);

				// Not all AbstractHorse's have saddles in slot 0
				if (!saddle.isEmpty() && saddle.getItem() != Items.SADDLE) {
					horse.dropStack(saddle, 0);
					saddle = ItemStack.EMPTY;
				}

				for (int i = 1; i < inv.size(); i++) {
					if (!inv.getStack(i).isEmpty()) {
						horse.dropStack(inv.getStack(i), 0);
					}
				}

				horse.remove();

				HorseBaseEntity newHorse = stack.getItem() == ModItems.necroVirus
						? EntityType.ZOMBIE_HORSE.create(player.world)
						: EntityType.SKELETON_HORSE.create(player.world);
				newHorse.bondWithPlayer(player);
				newHorse.updatePositionAndAngles(horse.getX(), horse.getY(), horse.getZ(), horse.yaw, horse.pitch);

				// Put the saddle back
				if (!saddle.isEmpty()) {
					SimpleInventory newInv = ((AccessorAbstractHorseEntity) newHorse).getItems();
					newInv.setStack(0, saddle);
				}

				EntityAttributeInstance movementSpeed = newHorse.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
				movementSpeed.setBaseValue(horse.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getBaseValue());
				movementSpeed.addPersistentModifier(new EntityAttributeModifier("Ermergerd Virus D:", movementSpeed.getBaseValue(), EntityAttributeModifier.Operation.ADDITION));

				EntityAttributeInstance health = newHorse.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
				health.setBaseValue(horse.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getBaseValue());
				health.addPersistentModifier(new EntityAttributeModifier("Ermergerd Virus D:", health.getBaseValue(), EntityAttributeModifier.Operation.ADDITION));

				EntityAttributeInstance jumpHeight = newHorse.getAttributeInstance(EntityAttributes.HORSE_JUMP_STRENGTH);
				jumpHeight.setBaseValue(horse.getAttributeInstance(EntityAttributes.HORSE_JUMP_STRENGTH).getBaseValue());
				jumpHeight.addPersistentModifier(new EntityAttributeModifier("Ermergerd Virus D:", jumpHeight.getBaseValue() * 0.5, EntityAttributeModifier.Operation.ADDITION));

				newHorse.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F + living.world.random.nextFloat(), living.world.random.nextFloat() * 0.7F + 1.3F);
				newHorse.initialize((ServerWorldAccess) player.world, player.world.getLocalDifficulty(newHorse.getBlockPos()), SpawnReason.CONVERSION, null, null);
				newHorse.setBreedingAge(horse.getBreedingAge());
				player.world.spawnEntity(newHorse);
				newHorse.playSpawnEffects();

				stack.decrement(1);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}

	public static void onLivingHurt(LivingHurtEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (entity.hasVehicle() && entity.getVehicle() instanceof LivingEntity) {
			entity = (LivingEntity) entity.getVehicle();
		}

		if ((entity instanceof ZombieHorseEntity || entity instanceof SkeletonHorseEntity)
				&& event.getSource() == DamageSource.FALL
				&& ((HorseBaseEntity) entity).isTame()) {
			event.setCanceled(true);
		}
	}
}
