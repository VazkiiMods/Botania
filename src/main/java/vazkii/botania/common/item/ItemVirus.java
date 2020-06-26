/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.horse.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemVirus extends Item {
	public ItemVirus(Properties builder) {
		super(builder);
	}

	@Override
	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity living, Hand hand) {
		if (living instanceof AbstractHorseEntity && !(living instanceof LlamaEntity)) {
			if (player.world.isRemote) {
				return ActionResultType.SUCCESS;
			}
			AbstractHorseEntity horse = (AbstractHorseEntity) living;
			if (horse.isTame()) {
				IItemHandler inv = horse.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new);
				ItemStack saddle = inv.getStackInSlot(0);

				// Not all AbstractHorse's have saddles in slot 0
				if (!saddle.isEmpty() && saddle.getItem() != Items.SADDLE) {
					horse.entityDropItem(saddle, 0);
					saddle = ItemStack.EMPTY;
				}

				for (int i = 1; i < inv.getSlots(); i++) {
					if (!inv.getStackInSlot(i).isEmpty()) {
						horse.entityDropItem(inv.getStackInSlot(i), 0);
					}
				}

				if (horse instanceof AbstractChestedHorseEntity && ((AbstractChestedHorseEntity) horse).hasChest()) {
					horse.entityDropItem(new ItemStack(Blocks.CHEST), 0);
				}

				horse.remove();

				AbstractHorseEntity newHorse = stack.getItem() == ModItems.necroVirus
						? EntityType.ZOMBIE_HORSE.create(player.world)
						: EntityType.SKELETON_HORSE.create(player.world);
				newHorse.setTamedBy(player);
				newHorse.setPositionAndRotation(horse.getPosX(), horse.getPosY(), horse.getPosZ(), horse.rotationYaw, horse.rotationPitch);

				// Put the saddle back
				if (!saddle.isEmpty()) {
					newHorse.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(NullPointerException::new).insertItem(0, saddle, false);
				}

				ModifiableAttributeInstance movementSpeed = newHorse.getAttribute(Attributes.field_233821_d_);
				movementSpeed.setBaseValue(horse.getAttribute(Attributes.field_233821_d_).getBaseValue());
				movementSpeed.func_233769_c_(new AttributeModifier("Ermergerd Virus D:", movementSpeed.getBaseValue(), AttributeModifier.Operation.ADDITION));

				ModifiableAttributeInstance health = newHorse.getAttribute(Attributes.field_233818_a_);
				health.setBaseValue(horse.getAttribute(Attributes.field_233818_a_).getBaseValue());
				health.func_233769_c_(new AttributeModifier("Ermergerd Virus D:", health.getBaseValue(), AttributeModifier.Operation.ADDITION));

				ModifiableAttributeInstance jumpHeight = newHorse.getAttribute(Attributes.field_233830_m_);
				jumpHeight.setBaseValue(horse.getAttribute(Attributes.field_233830_m_).getBaseValue());
				jumpHeight.func_233769_c_(new AttributeModifier("Ermergerd Virus D:", jumpHeight.getBaseValue() * 0.5, AttributeModifier.Operation.ADDITION));

				newHorse.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F + living.world.rand.nextFloat(), living.world.rand.nextFloat() * 0.7F + 1.3F);
				newHorse.onInitialSpawn(player.world, player.world.getDifficultyForLocation(newHorse.func_233580_cy_()), SpawnReason.CONVERSION, null, null);
				newHorse.setGrowingAge(horse.getGrowingAge());
				player.world.addEntity(newHorse);
				newHorse.spawnExplosionParticle();

				stack.shrink(1);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	public static void onLivingHurt(LivingHurtEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (entity.isPassenger() && entity.getRidingEntity() instanceof LivingEntity) {
			entity = (LivingEntity) entity.getRidingEntity();
		}

		if ((entity instanceof ZombieHorseEntity || entity instanceof SkeletonHorseEntity)
				&& event.getSource() == DamageSource.FALL
				&& ((AbstractHorseEntity) entity).isTame()) {
			event.setCanceled(true);
		}
	}
}
