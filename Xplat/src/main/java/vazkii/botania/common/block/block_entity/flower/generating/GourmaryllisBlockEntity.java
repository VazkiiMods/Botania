/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.helper.DelayHelper;
import vazkii.botania.common.helper.MathHelper;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public class GourmaryllisBlockEntity extends GeneratingFlowerBlockEntity {
	private static final String TAG_COOLDOWN = "cooldown";
	private static final String TAG_DIGESTING_MANA = "digestingMana";
	public static final String TAG_LAST_FOODS = "lastFoods";
	public static final String TAG_LAST_FOOD_COUNT = "lastFoodCount";
	public static final String TAG_STREAK_LENGTH = "streakLength";
	private static final int RANGE = 1;
	private static final double[] STREAK_MULTIPLIERS = { 0, 1, 1.3, 1.5, 1.6, 1.7, 1.75, 1.8 };
	private static final int MAX_FOOD_VALUE = 12;
	private static final int FOOD_COOLDOWN_FACTOR = 10;
	private static final int FOOD_MANA_FACTOR = 70;
	private static final int MAX_MANA = getDigestingMana(MAX_FOOD_VALUE, STREAK_MULTIPLIERS[STREAK_MULTIPLIERS.length - 1]);

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private final Optional<HolderSet.Named<DataComponentType<?>>> RELEVANT_COMPONENTS_TAG =
			BuiltInRegistries.DATA_COMPONENT_TYPE.getTag(BotaniaTags.DataComponentTypes.GOURMARYLLIS_RELEVANT);

	private int cooldown = 0;
	private int digestingMana = 0;
	private final List<ItemStack> lastFoods = new LinkedList<>();
	private int streakLength = -1;
	private int lastFoodCount = 0;

	public GourmaryllisBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.GOURMARYLLIS, pos, state);
	}

	public static int getMaxStreak() {
		return STREAK_MULTIPLIERS.length - 1;
	}

	private double getMultiplierForStreak(int index) {
		// special-case repeated first foods
		if (index == 0) {
			return 1.0 / ++lastFoodCount;
		} else {
			lastFoodCount = 1;
			return STREAK_MULTIPLIERS[index];
		}
	}

	/**
	 * Processes a food, placing it in the appropriate place in the history.
	 * 
	 * @return the last time the food showed up in history.
	 */
	private int processFood(ItemStack food) {
		// strip any non-standard data components from the stack, unless they seem relevant for the flower
		DataComponentPatch patch = food.getComponentsPatch().forget(this::isIrrelevantComponent);
		ItemStack newestFood = new ItemStack(food.getItemHolder(), 1, patch);

		for (ListIterator<ItemStack> it = lastFoods.listIterator(); it.hasNext();) {
			int index = it.nextIndex();
			ItemStack streakFood = it.next();
			if (ItemStack.isSameItemSameComponents(streakFood, newestFood)) {
				it.remove();
				lastFoods.addFirst(streakFood);
				return index;
			}
		}
		lastFoods.addFirst(newestFood);
		if (lastFoods.size() >= getMaxStreak()) {
			lastFoods.removeLast();
		}
		return getMaxStreak();
	}

	private boolean isIrrelevantComponent(DataComponentType<?> dataComponentType) {
		return !RELEVANT_COMPONENTS_TAG.map(
				holders -> BuiltInRegistries.DATA_COMPONENT_TYPE.getResourceKey(dataComponentType)
						.flatMap(BuiltInRegistries.DATA_COMPONENT_TYPE::getHolder).map(holders::contains)
						.orElseThrow()
		).orElse(dataComponentType == DataComponents.FOOD);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (level.isClientSide) {
			return;
		}

		if (cooldown > -1) {
			cooldown--;
			markForPersisting();
		}
		if (digestingMana != 0) {
			int munchInterval = 2 + (2 * lastFoodCount);

			if (cooldown == 0) {
				addMana(digestingMana);
				digestingMana = 0;

				float burpPitch = (float) Math.pow(2.0, (streakLength == 0 ? -lastFoodCount : streakLength) / 12.0);
				//Usage of vanilla sound event: Subtitle is just "Burp", at least in English, and not specific to players.
				level.playSound(null, getEffectivePos(), SoundEvents.PLAYER_BURP, SoundSource.BLOCKS, 1, burpPitch);
				level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, getEffectivePos());
			} else if (cooldown % munchInterval == 0) {
				//Usage of vanilla sound event: Subtitle is "Eating", generic sounds are meant to be reused.
				level.playSound(null, getEffectivePos(), SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 0.5f, 1);

				Vec3 offset = level.getBlockState(getEffectivePos()).getOffset(level, getEffectivePos()).add(0.5, 0.6, 0.5);

				((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, lastFoods.getFirst()),
						getEffectivePos().getX() + offset.x,
						getEffectivePos().getY() + offset.y,
						getEffectivePos().getZ() + offset.z,
						10, 0.1, 0.1, 0.1, 0.03);
			}
		}

		List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class,
				MathHelper.inflateBoxAround(getEffectivePos(), RANGE),
				item -> DelayHelper.canInteractWithImmediate(this, item)
						&& item.getItem().getItem().components().has(DataComponents.FOOD));

		for (ItemEntity item : items) {
			level.gameEvent(null, GameEvent.EAT, item.position());
			if (cooldown <= 0) {
				ItemStack stack = item.getItem();
				streakLength = Math.min(streakLength + 1, processFood(stack));

				int val = getFoodValue(stack);
				digestingMana = getDigestingMana(val, getMultiplierForStreak(streakLength));
				cooldown = getCooldown(val);
				//Usage of vanilla sound event: Subtitle is "Eating", generic sounds are meant to be reused.
				item.playSound(SoundEvents.GENERIC_EAT, 0.2f, 0.6f);
				level.gameEvent(null, GameEvent.BLOCK_ACTIVATE, getEffectivePos());
				setChanged();
				((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack),
						item.getX(), item.getY(), item.getZ(),
						20, 0.1, 0.1, 0.1, 0.05);
			}

			item.discard();
		}

		boolean isEating = digestingMana != 0;
		if (getBlockState().getValue(BotaniaStateProperties.GENERATING) != isEating) {
			level.setBlock(getBlockPos(), getBlockState().setValue(BotaniaStateProperties.GENERATING, isEating),
					Block.UPDATE_CLIENTS);
		}
	}

	private static int getCooldown(int foodValue) {
		return Math.max(1, foodValue * FOOD_COOLDOWN_FACTOR);
	}

	private static int getDigestingMana(int foodValue, double streakFactor) {
		return Math.max(1, (int) (foodValue * foodValue * FOOD_MANA_FACTOR * streakFactor));
	}

	private static int getFoodValue(ItemStack stack) {
		// support for Forge's NBT-based food properties
		FoodProperties foodProperties = XplatAbstractions.INSTANCE.getFoodProperties(stack);
		int nutrition = foodProperties != null ? foodProperties.nutrition() : 0;
		return Math.min(MAX_FOOD_VALUE, nutrition);
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.saveAdditional(cmp, registries);
		cmp.putInt(TAG_COOLDOWN, cooldown);
		cmp.putInt(TAG_DIGESTING_MANA, digestingMana);
		ListTag foodList = new ListTag();
		for (ItemStack food : lastFoods) {
			foodList.add(food.save(registries));
		}
		cmp.put(TAG_LAST_FOODS, foodList);
		cmp.putInt(TAG_LAST_FOOD_COUNT, lastFoodCount);
		cmp.putInt(TAG_STREAK_LENGTH, streakLength);
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.loadAdditional(cmp, registries);
		cooldown = cmp.getInt(TAG_COOLDOWN);
		digestingMana = cmp.getInt(TAG_DIGESTING_MANA);
		lastFoods.clear();
		ListTag foodList = cmp.getList(TAG_LAST_FOODS, Tag.TAG_COMPOUND);
		for (int i = 0; i < foodList.size(); i++) {
			lastFoods.add(ItemStack.parseOptional(registries, foodList.getCompound(i)));
		}
		lastFoodCount = cmp.getInt(TAG_LAST_FOOD_COUNT);
		streakLength = cmp.getInt(TAG_STREAK_LENGTH);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return MAX_MANA;
	}

	@Override
	public int getColor() {
		return 0xD3D604;
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		if (streakLength >= 0) {
			components.set(BotaniaDataComponents.STREAK_LENGTH, streakLength);
		}
		if (lastFoodCount > 0) {
			components.set(BotaniaDataComponents.LAST_REPEATS, lastFoodCount);
		}
		if (!lastFoods.isEmpty()) {
			components.set(BotaniaDataComponents.LAST_FOODS, lastFoods);
		}
	}

	@Override
	protected void applyImplicitComponents(DataComponentInput componentInput) {
		streakLength = componentInput.getOrDefault(BotaniaDataComponents.STREAK_LENGTH, -1);
		lastFoodCount = componentInput.getOrDefault(BotaniaDataComponents.LAST_REPEATS, 0);
		lastFoods.clear();
		lastFoods.addAll(componentInput.getOrDefault(BotaniaDataComponents.LAST_FOODS, Collections.emptyList()));
	}
}
