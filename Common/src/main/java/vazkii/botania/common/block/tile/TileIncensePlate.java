/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

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

public class TileIncensePlate extends TileExposedSimpleInventory implements WorldlyContainer {
	private static final String TAG_TIME_LEFT = "timeLeft";
	private static final String TAG_BURNING = "burning";
	private static final int RANGE = 32;

	private int timeLeft = 0;
	public boolean burning = false;
	public int comparatorOutput = 0;

	public TileIncensePlate(BlockPos pos, BlockState state) {
		super(ModTiles.INCENSE_PLATE, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TileIncensePlate self) {
		ItemStack stack = self.getItemHandler().getItem(0);
		if (!stack.isEmpty() && self.burning) {
			if (state.getValue(BlockStateProperties.WATERLOGGED) && self.timeLeft > 1) {
				self.timeLeft = 1;
				self.spawnSmokeParticles();
			}

			Brew brew = ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack);
			MobEffectInstance effect = brew.getPotionEffects(stack).get(0);
			if (self.timeLeft > 0) {
				self.timeLeft--;
				if (!level.isClientSide) {
					List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(worldPosition.getX() + 0.5 - RANGE, worldPosition.getY() + 0.5 - RANGE, worldPosition.getZ() + 0.5 - RANGE, worldPosition.getX() + 0.5 + RANGE, worldPosition.getY() + 0.5 + RANGE, worldPosition.getZ() + 0.5 + RANGE));
					for (Player player : players) {
						MobEffectInstance currentEffect = player.getEffect(effect.getEffect());
						boolean nightVision = effect.getEffect() == MobEffects.NIGHT_VISION;
						if (currentEffect == null || currentEffect.getDuration() < (nightVision ? 205 : 3)) {
							MobEffectInstance applyEffect = new MobEffectInstance(effect.getEffect(), nightVision ? 285 : 80, effect.getAmplifier(), true, true);
							player.addEffect(applyEffect);
						}
					}

					if (level.random.nextInt(20) == 0) {
						level.playSound(null, worldPosition, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 0.1F, 1);
					}
				} else {
					double x = worldPosition.getX() + 0.5;
					double y = worldPosition.getY() + 0.5;
					double z = worldPosition.getZ() + 0.5;

					int color = brew.getColor(stack);
					float r = (color >> 16 & 0xFF) / 255F;
					float g = (color >> 8 & 0xFF) / 255F;
					float b = (color & 0xFF) / 255F;

					WispParticleData data1 = WispParticleData.wisp(0.05F + (float) Math.random() * 0.02F, r, g, b);
					level.addParticle(data1, x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.005F, 0.005F - (float) Math.random() * 0.01F);
					WispParticleData data = WispParticleData.wisp(0.05F + (float) Math.random() * 0.02F, 0.2F, 0.2F, 0.2F);
					level.addParticle(data, x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.001F, 0.005F - (float) Math.random() * 0.01F);
				}
			} else {
				self.getItemHandler().setItem(0, ItemStack.EMPTY);
				self.burning = false;
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(self);
			}
		} else {
			self.timeLeft = 0;
		}

		int newComparator = 0;
		if (!stack.isEmpty()) {
			newComparator = 1;
		}
		if (self.burning) {
			newComparator = 2;
		}
		if (self.comparatorOutput != newComparator) {
			self.comparatorOutput = newComparator;
			level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
		}
	}

	public void spawnSmokeParticles() {
		Random random = level.getRandom();
		for (int i = 0; i < 4; ++i) {
			level.addParticle(ParticleTypes.SMOKE,
					worldPosition.getX() + 0.5 + random.nextDouble() / 2.0 * (random.nextBoolean() ? 1 : -1),
					worldPosition.getY() + 1,
					worldPosition.getZ() + 0.5 + random.nextDouble() / 2.0 * (random.nextBoolean() ? 1 : -1),
					0.0D,
					0.05D,
					0.0D);
		}
		level.playSound(null, worldPosition, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.1F, 1.0F);
	}

	public void ignite() {
		ItemStack stack = getItemHandler().getItem(0);

		if (stack.isEmpty() || burning) {
			return;
		}

		if (getBlockState().getValue(BlockStateProperties.WATERLOGGED)) {
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
		return !stack.isEmpty() && stack.is(ModItems.incenseStick) && ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack) != ModBrews.fallbackBrew;
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(1) {
			@Override
			public boolean canPlaceItem(int index, ItemStack stack) {
				return acceptsItem(stack);
			}
		};
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (level != null && !level.isClientSide) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	@Override
	public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
		return false;
	}
}
