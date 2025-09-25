/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.brew.BotaniaBrews;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.brew.IncenseStickItem;

import java.util.List;

public class IncensePlateBlockEntity extends ExposedSimpleInventoryBlockEntity implements WorldlyContainer {
	private static final String TAG_TIME_LEFT = "timeLeft";
	private static final int RANGE = 32;

	private int timeLeft = 0;
	public int comparatorOutput = 0;

	public IncensePlateBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.INCENSE_PLATE, pos, state);
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, IncensePlateBlockEntity self) {
		ItemStack stack = self.getItemHandler().getItem(0);
		int newComparator;
		if (stack.is(BotaniaItems.incenseStick) && state.getValue(BlockStateProperties.LIT)) {
			newComparator = 2;
			if (state.getValue(BlockStateProperties.WATERLOGGED) && self.timeLeft > 1) {
				self.timeLeft = 1;
				self.spawnSmokeParticles();
			}

			Brew brew = ((IncenseStickItem) BotaniaItems.incenseStick).getBrew(stack);
			MobEffectInstance effect = brew.getPotionEffects(stack).getFirst();
			if (self.timeLeft > 0) {
				self.timeLeft--;
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
				self.getItemHandler().setItem(0, ItemStack.EMPTY);
				level.setBlockAndUpdate(self.getBlockPos(), state.setValue(BlockStateProperties.LIT, false));
				level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, worldPosition);
			}
		} else {
			self.timeLeft = 0;
			newComparator = stack.isEmpty() ? 0 : 1;
		}

		if (self.comparatorOutput != newComparator) {
			self.comparatorOutput = newComparator;
			level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
			self.setChanged();
		}
	}

	public static void clientTick(Level level, BlockPos worldPosition, BlockState state, IncensePlateBlockEntity self) {
		ItemStack stack = self.getItemHandler().getItem(0);
		if (stack.is(BotaniaItems.incenseStick) && state.getValue(BlockStateProperties.LIT)) {
			if (self.timeLeft > 0) {
				self.timeLeft--;
			}
			Brew brew = ((IncenseStickItem) BotaniaItems.incenseStick).getBrew(stack);
			double x = worldPosition.getX() + 0.5;
			double y = worldPosition.getY() + 0.5;
			double z = worldPosition.getZ() + 0.5;

			int color = brew.getColor(stack);
			float r = FastColor.ARGB32.red(color) / 255f;
			float g = FastColor.ARGB32.green(color) / 255f;
			float b = FastColor.ARGB32.blue(color) / 255f;

			WispParticleData data1 = WispParticleData.wisp(0.05F + (float) Math.random() * 0.02F, r, g, b);
			level.addParticle(data1, x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.005F, 0.005F - (float) Math.random() * 0.01F);
			WispParticleData data = WispParticleData.wisp(0.05F + (float) Math.random() * 0.02F, 0.2F, 0.2F, 0.2F);
			level.addParticle(data, x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.001F, 0.005F - (float) Math.random() * 0.01F);
		}
	}

	public void spawnSmokeParticles() {
		var random = level.getRandom();
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

		if (stack.isEmpty() || getBlockState().getValue(BlockStateProperties.LIT)) {
			return;
		}

		if (getBlockState().getValue(BlockStateProperties.WATERLOGGED)) {
			spawnSmokeParticles();
			return;
		}

		level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.LIT, true));
		Brew brew = ((IncenseStickItem) BotaniaItems.incenseStick).getBrew(stack);
		timeLeft = brew.getPotionEffects(stack).getFirst().getDuration() * IncenseStickItem.TIME_MULTIPLIER;
		level.playSound(null, getBlockPos(), BotaniaSounds.incensePlateIgnite, SoundSource.BLOCKS, 0.5F, 1.75F);
		level.gameEvent(null, GameEvent.BLOCK_ACTIVATE, getBlockPos());
		setChanged();
	}

	@Override
	public void writePacketNBT(CompoundTag tag, HolderLookup.Provider registries) {
		super.writePacketNBT(tag, registries);
		tag.putInt(TAG_TIME_LEFT, timeLeft);
	}

	@Override
	public void readPacketNBT(CompoundTag tag, HolderLookup.Provider registries) {
		super.readPacketNBT(tag, registries);
		timeLeft = tag.getInt(TAG_TIME_LEFT);
	}

	public boolean acceptsItem(ItemStack stack) {
		return !stack.isEmpty() && stack.is(BotaniaItems.incenseStick) && ((IncenseStickItem) BotaniaItems.incenseStick).getBrew(stack) != BotaniaBrews.fallbackBrew;
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
	public boolean canTakeItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
		return false;
	}
}
