/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.helper.DelayHelper;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.mixin.MushroomCowAccessor;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class PollidisiacBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int RANGE = 6;
	private static final int MANA_COST = 12;

	public PollidisiacBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.POLLIDISIAC, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide && getMana() >= MANA_COST) {
			var pickupBounds = new AABB(getBlockPos()).inflate(RANGE);
			List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class, pickupBounds,
					itemEntity -> DelayHelper.canInteractWith(this, itemEntity));
			if (items.isEmpty()) {
				return;
			}
			var bounds = new AABB(getEffectivePos()).inflate(RANGE);
			List<Animal> animals = getLevel().getEntitiesOfClass(Animal.class, bounds, Predicate.not(Animal::isBaby));
			Collections.shuffle(animals);

			for (Animal animal : animals) {
				// Note: Empty item stacks are implicitly excluded in Animal::isFood and ItemStack::is(TagKey)
				if (!animal.isInLove()) {
					for (ItemEntity item : items) {
						if (!animal.isFood(item.getItem())) {
							continue;
						}
						consumeFoodItem(item);

						animal.setInLoveTime(1200);
						getLevel().broadcastEntityEvent(animal, EntityEvent.IN_LOVE_HEARTS);
						break;
					}

					if (getMana() < MANA_COST) {
						break;
					}
				}

				if (isBrownMooshroomWithoutEffect(animal)) {
					for (ItemEntity item : items) {
						ItemStack stack = item.getItem();
						if (!stack.is(ItemTags.SMALL_FLOWERS)) {
							continue;
						}
						var effect = SuspiciousEffectHolder.tryGet(stack.getItem());
						if (effect == null) {
							continue;
						}
						consumeFoodItem(item);

						MushroomCowAccessor cowAccessor = (MushroomCowAccessor) animal;
						cowAccessor.setEffect(effect.getSuspiciousEffect());
						cowAccessor.setEffectDuration(effect.getEffectDuration());
						animal.playSound(SoundEvents.MOOSHROOM_EAT, 2.0F, 1.0F);
						break;
					}

					if (getMana() < MANA_COST) {
						break;
					}
				}
			}
		}
	}

	private void consumeFoodItem(ItemEntity itemEntity) {
		EntityHelper.shrinkItem(itemEntity);
		addMana(-MANA_COST);
	}

	private static boolean isBrownMooshroomWithoutEffect(Animal animal) {
		if (animal instanceof MushroomCow mushroomCow && mushroomCow.getVariant() == MushroomCow.MushroomType.BROWN) {
			MushroomCowAccessor cowAccessor = (MushroomCowAccessor) animal;
			return cowAccessor.getEffect() == null;
		}
		return false;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public RadiusDescriptor getSecondaryRadius() {
		return getBlockPos().equals(getEffectivePos()) ? null : RadiusDescriptor.Rectangle.square(getBlockPos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 120;
	}

	@Override
	public int getColor() {
		return 0xCF4919;
	}

}
