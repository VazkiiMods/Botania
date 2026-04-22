/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.functional;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AnimalMode;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.helper.DelayHelper;
import vazkii.botania.common.helper.EntityHelper;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.mixin.AnimalAccessor;
import vazkii.botania.mixin.MushroomCowAccessor;

import java.util.*;

public class PollidisiacBlockEntity extends FunctionalFlowerBlockEntity implements Wandable {
	private static final int RANGE = 6;
	private static final int MANA_COST = 12;

	public PollidisiacBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.POLLIDISIAC, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || getMana() < MANA_COST) {
			return;
		}
		List<ItemEntity> items = getItems();
		if (!items.isEmpty()) {
			List<Animal> animals = getAnimals();
			feedAnimal(animals, items);
		}
	}

	/**
	 * Finds items around flower's actual position.
	 */
	private List<ItemEntity> getItems() {
		var pickupBounds = MathHelper.inflateBoxAround(getBlockPos(), RANGE);
		return getLevel().getEntitiesOfClass(ItemEntity.class, pickupBounds,
				itemEntity -> DelayHelper.canInteractWith(this, itemEntity));
	}

	/**
	 * Finds animals around flower's effective position. Depending on mode, adults, babies, or both will be selected.
	 */
	private List<Animal> getAnimals() {
		var bounds = MathHelper.inflateBoxAround(getEffectivePos(), RANGE);
		return getLevel().getEntitiesOfClass(Animal.class, bounds, getMode());
	}

	/**
	 * Attempts to feed an animal with an available item. Only one animal will be fed per call. Feeding adults is
	 * prioritized, but if babies get their turn, they are prioritized by their age, youngest first. Among brown adult
	 * mooshrooms, breeding is prioritized over feeding flowers for suspicious stew, if both item types are available.
	 */
	private void feedAnimal(List<Animal> animals, List<ItemEntity> items) {
		// randomize animals with same age
		Collections.shuffle(animals);
		// feed adults first, then babies, youngest to oldest
		animals.sort(Comparator.comparing(Animal::isBaby).thenComparingInt(animal -> Math.min(animal.getAge(), 0)));

		boolean did = false;
		for (Animal animal : animals) {
			// Note: Empty item stacks are implicitly excluded in Animal::isFood and ItemStack::is(TagKey)
			if (animal.getAge() == 0 && !animal.isInLove() || animal.getAge() < -600 && -animal.getAge() % 100 == 0) {
				for (ItemEntity item : items) {
					if (!animal.isFood(item.getItem())) {
						continue;
					}
					consumeFoodItemAndMana(item);
					did = true;

					if (animal.isBaby()) {
						animal.ageUp(AgeableMob.getSpeedUpSecondsWhenFeeding(-animal.getAge()), true);
					} else {
						animal.setInLoveTime(1200);
						((AnimalAccessor) animal).botania_setLoveCause(null);
					}
					getLevel().broadcastEntityEvent(animal, EntityEvent.IN_LOVE_HEARTS);
					break;
				}

				if (getMana() < MANA_COST) {
					break;
				}
			}

			if (!animal.isBaby() && isBrownMooshroomWithoutEffect(animal)) {
				for (ItemEntity item : items) {
					ItemStack stack = item.getItem();
					if (!stack.is(ItemTags.SMALL_FLOWERS)) {
						continue;
					}
					var effectHolder = SuspiciousEffectHolder.tryGet(stack.getItem());
					if (effectHolder == null) {
						continue;
					}
					consumeFoodItemAndMana(item);
					did = true;

					MushroomCowAccessor cowAccessor = (MushroomCowAccessor) animal;
					cowAccessor.botania_setStewEffects(effectHolder.getSuspiciousEffects());
					animal.playSound(SoundEvents.MOOSHROOM_EAT, 2.0F, 1.0F);
					break;
				}

				if (getMana() < MANA_COST) {
					break;
				}
			}
		}
		if (did) {
			sync();
		}
	}

	private void consumeFoodItemAndMana(ItemEntity itemEntity) {
		EntityHelper.shrinkItem(itemEntity);
		addMana(-MANA_COST);
	}

	private static boolean isBrownMooshroomWithoutEffect(Animal animal) {
		if (animal instanceof MushroomCow mushroomCow && mushroomCow.getVariant() == MushroomCow.MushroomType.BROWN) {
			MushroomCowAccessor cowAccessor = (MushroomCowAccessor) animal;
			return cowAccessor.botania_getStewEffects() == null;
		}
		return false;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Nullable
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

	public AnimalMode getMode() {
		return getBlockState().getValue(BotaniaStateProperties.ANIMAL_MODE);
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
		if (player == null || player.isShiftKeyDown()) {
			level.setBlock(getBlockPos(), getBlockState().cycle(BotaniaStateProperties.ANIMAL_MODE),
					Block.UPDATE_CLIENTS);
			return true;
		}
		return false;
	}

	public static class WandHud extends BindableFlowerWandHud<PollidisiacBlockEntity> {
		public WandHud(PollidisiacBlockEntity flower) {
			super(flower);
		}

		@Override
		public void renderHUD(GuiGraphics gui, Window window, Font font, float partialTick) {
			String filter = I18n.get("botaniamisc.pollidisiac." + flower.getMode().getSerializedName());
			int filterWidth = font.width(filter);
			int filterTextStart = (window.getGuiScaledWidth() - filterWidth) / 2;
			int halfMinWidth = (filterWidth + 4) / 2;
			int centerY = window.getGuiScaledHeight() / 2;

			super.renderHUD(gui, window, font, halfMinWidth, halfMinWidth, 40);
			gui.drawString(font, filter, filterTextStart, centerY + 30, flower.getColor());
		}
	}
}
