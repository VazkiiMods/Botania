/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.brew.ItemIncenseStick;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Random;

public class TileIncensePlate extends TileExposedSimpleInventory implements SidedInventory, Tickable {
	private static final String TAG_TIME_LEFT = "timeLeft";
	private static final String TAG_BURNING = "burning";
	private static final int RANGE = 32;

	private int timeLeft = 0;
	public boolean burning = false;
	public int comparatorOutput = 0;

	public TileIncensePlate() {
		super(ModTiles.INCENSE_PLATE);
	}

	@Override
	public void tick() {
		ItemStack stack = getItemHandler().getStack(0);
		if (!stack.isEmpty() && burning) {
			if (getCachedState().get(Properties.WATERLOGGED) && timeLeft > 1) {
				timeLeft = 1;
				spawnSmokeParticles();
			}

			Brew brew = ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack);
			StatusEffectInstance effect = brew.getPotionEffects(stack).get(0);
			if (timeLeft > 0) {
				timeLeft--;
				if (!world.isClient) {
					List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, new Box(pos.getX() + 0.5 - RANGE, pos.getY() + 0.5 - RANGE, pos.getZ() + 0.5 - RANGE, pos.getX() + 0.5 + RANGE, pos.getY() + 0.5 + RANGE, pos.getZ() + 0.5 + RANGE));
					for (PlayerEntity player : players) {
						StatusEffectInstance currentEffect = player.getStatusEffect(effect.getEffectType());
						boolean nightVision = effect.getEffectType() == StatusEffects.NIGHT_VISION;
						if (currentEffect == null || currentEffect.getDuration() < (nightVision ? 205 : 3)) {
							StatusEffectInstance applyEffect = new StatusEffectInstance(effect.getEffectType(), nightVision ? 285 : 80, effect.getAmplifier(), true, true);
							player.addStatusEffect(applyEffect);
						}
					}

					if (world.random.nextInt(20) == 0) {
						world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.1F, 1);
					}
				} else {
					double x = pos.getX() + 0.5;
					double y = pos.getY() + 0.5;
					double z = pos.getZ() + 0.5;

					int color = brew.getColor(stack);
					float r = (color >> 16 & 0xFF) / 255F;
					float g = (color >> 8 & 0xFF) / 255F;
					float b = (color & 0xFF) / 255F;

					WispParticleData data1 = WispParticleData.wisp(0.05F + (float) Math.random() * 0.02F, r, g, b);
					world.addParticle(data1, x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.005F, 0.005F - (float) Math.random() * 0.01F);
					WispParticleData data = WispParticleData.wisp(0.05F + (float) Math.random() * 0.02F, 0.2F, 0.2F, 0.2F);
					world.addParticle(data, x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.001F, 0.005F - (float) Math.random() * 0.01F);
				}
			} else {
				getItemHandler().setStack(0, ItemStack.EMPTY);
				burning = false;
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		} else {
			timeLeft = 0;
		}

		int newComparator = 0;
		if (!stack.isEmpty()) {
			newComparator = 1;
		}
		if (burning) {
			newComparator = 2;
		}
		if (comparatorOutput != newComparator) {
			comparatorOutput = newComparator;
			world.updateComparators(pos, getCachedState().getBlock());
		}
	}

	public void spawnSmokeParticles() {
		Random random = world.getRandom();
		for (int i = 0; i < 4; ++i) {
			world.addParticle(ParticleTypes.SMOKE,
					pos.getX() + 0.5 + random.nextDouble() / 2.0 * (random.nextBoolean() ? 1 : -1),
					pos.getY() + 1,
					pos.getZ() + 0.5 + random.nextDouble() / 2.0 * (random.nextBoolean() ? 1 : -1),
					0.0D,
					0.05D,
					0.0D);
		}
		world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 0.1F, 1.0F);
	}

	public void ignite() {
		ItemStack stack = getItemHandler().getStack(0);

		if (stack.isEmpty() || burning) {
			return;
		}

		if (getCachedState().get(Properties.WATERLOGGED)) {
			spawnSmokeParticles();
			return;
		}

		burning = true;
		Brew brew = ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack);
		timeLeft = brew.getPotionEffects(stack).get(0).getDuration() * ItemIncenseStick.TIME_MULTIPLIER;
	}

	@Override
	public void writePacketNBT(CompoundTag tag) {
		super.writePacketNBT(tag);
		tag.putInt(TAG_TIME_LEFT, timeLeft);
		tag.putBoolean(TAG_BURNING, burning);
	}

	@Override
	public void readPacketNBT(CompoundTag tag) {
		super.readPacketNBT(tag);
		timeLeft = tag.getInt(TAG_TIME_LEFT);
		burning = tag.getBoolean(TAG_BURNING);
	}

	public boolean acceptsItem(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() == ModItems.incenseStick && ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack) != ModBrews.fallbackBrew;
	}

	@Override
	protected SimpleInventory createItemHandler() {
		return new SimpleInventory(1) {
			@Override
			public boolean isValid(int index, ItemStack stack) {
				return acceptsItem(stack);
			}
		};
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isClient) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	@Override
	public boolean canExtract(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
		return false;
	}
}
