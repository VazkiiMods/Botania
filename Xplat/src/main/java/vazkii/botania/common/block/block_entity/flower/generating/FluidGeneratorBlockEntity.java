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
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.mixin.FlowingFluidAccessor;

import java.util.Arrays;
import java.util.Collections;

public abstract class FluidGeneratorBlockEntity extends GeneratingFlowerBlockEntity {
	private static final String TAG_BURN_TIME = "burnTime";
	public static final String TAG_COOLDOWN = "cooldown";

	protected int burnTime, cooldown;
	private final TagKey<Fluid> consumedFluid;
	private final int startBurnTime, manaPerTick, cooldownTime;

	protected FluidGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, TagKey<Fluid> consumedFluid, int startBurnTime, int manaPerTick, int cooldownTime) {
		super(type, pos, state);
		this.consumedFluid = consumedFluid;
		this.startBurnTime = startBurnTime;
		this.manaPerTick = manaPerTick;
		this.cooldownTime = cooldownTime;
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (level.isClientSide()) {
			if (getBlockState().getValue(BotaniaStateProperties.GENERATING)) {
				if (level.random.nextInt(8) == 0) {
					doBurnParticles();
				}
			} else if (getBlockState().getValue(BotaniaStateProperties.ON_COOLDOWN)) {
				doCooldownParticles(this);
			}
			return;
		}

		if (burnTime > 0) {
			if (shouldUpdateThisTick()) {
				addMana(getUpdateInterval() <= 1 ? getOvergrowthFactor() * manaPerTick : manaPerTick);
				// TODO: only sync mana if a nearby player cares
				sync();
			}
			burnTime -= getOvergrowthFactor();
			level.blockEntityChanged(getBlockPos());
			return;
		}

		if (cooldown > 0) {
			cooldown -= getOvergrowthFactor();
			level.blockEntityChanged(getBlockPos());
		}
		// flower is not generating mana anymore, but may or may not be in cooldown now

		if (getMana() < getMaxMana()) {

			for (BlockPos pos : getShuffledPositionsAround(getEffectivePos())) {

				FluidState fluidState = level.getFluidState(pos);
				if (!fluidState.is(consumedFluid) || !fluidState.isSource()) {
					continue;
				}
				BlockState blockState = level.getBlockState(pos);
				if (!(fluidState.getType() instanceof FlowingFluid flowing)
						|| !((FlowingFluidAccessor) flowing).botania_getNewLiquid(level, pos, blockState).isSource()) {
					// liquid would not form a new source here if this one was removed, so consume it
					if (!(blockState.getBlock() instanceof BucketPickup bucketPickup)
							|| bucketPickup.pickupBlock(null, level, pos, blockState).isEmpty()) {
						continue;
					}
					level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
				}

				if (cooldown == 0) {
					// slightly vary actual burn time to prevent flowers from eternally being in sync,
					// thus requiring a proper infinite source setup
					burnTime += startBurnTime + level.getRandom().nextInt(2) - level.getRandom().nextInt(2);
					if (!getBlockState().getValue(BotaniaStateProperties.GENERATING)) {
						level.setBlock(getBlockPos(),
								getBlockState()
										.setValue(BotaniaStateProperties.GENERATING, true)
										.setValue(BotaniaStateProperties.ON_COOLDOWN, false),
								Block.UPDATE_CLIENTS);
					}
					level.gameEvent(null, GameEvent.BLOCK_ACTIVATE, getBlockPos());
				}
				// reset cooldown here, as it should be set anyway when the flower is broken while generating mana
				cooldown = cooldownTime;

				setChanged();
				playSound();
				return;
			}
		}

		if (getBlockState().getValue(BotaniaStateProperties.GENERATING)) {
			level.setBlock(getBlockPos(),
					getBlockState()
							.setValue(BotaniaStateProperties.GENERATING, false)
							.setValue(BotaniaStateProperties.ON_COOLDOWN, cooldown > 0),
					Block.UPDATE_CLIENTS);
			level.gameEvent(null, GameEvent.BLOCK_DEACTIVATE, getBlockPos());

		} else if (cooldown == 0 && getBlockState().getValue(BotaniaStateProperties.ON_COOLDOWN)) {
			level.setBlock(getBlockPos(),
					getBlockState().setValue(BotaniaStateProperties.ON_COOLDOWN, false),
					Block.UPDATE_CLIENTS);
		}
	}

	public static void doCooldownParticles(SpecialFlowerBlockEntity self) {
		for (int i = 0; i < 3; i++) {
			WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.1F, 0.1F, 0.1F, 1);
			self.emitParticle(data, 0.5 + Math.random() * 0.2 - 0.1, 0.5 + Math.random() * 0.2 - 0.1,
					0.5 + Math.random() * 0.2 - 0.1, 0, (float) Math.random() / 30, 0);
		}
	}

	private static BlockPos[] getShuffledPositionsAround(BlockPos pos) {
		BlockPos[] posArray = new BlockPos[9];
		int i = 0;
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				posArray[i++] = pos.offset(x, 0, z);
			}
		}
		Collections.shuffle(Arrays.asList(posArray));
		return posArray;
	}

	public int getUpdateInterval() {
		return 1;
	}

	public abstract void doBurnParticles();

	public abstract void playSound();

	@Override
	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.writeToPacketNBT(cmp, registries);

		cmp.putInt(TAG_BURN_TIME, burnTime);
		cmp.putInt(TAG_COOLDOWN, cooldown);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.readFromPacketNBT(cmp, registries);

		burnTime = cmp.getInt(TAG_BURN_TIME);
		cooldown = cmp.getInt(TAG_COOLDOWN);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), 1);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		if (cooldown > 0) {
			components.set(BotaniaDataComponents.COOLDOWN, cooldown);
		}
	}

	@Override
	protected void applyImplicitComponents(DataComponentInput componentInput) {
		cooldown = componentInput.getOrDefault(BotaniaDataComponents.COOLDOWN, 0);
	}
}
