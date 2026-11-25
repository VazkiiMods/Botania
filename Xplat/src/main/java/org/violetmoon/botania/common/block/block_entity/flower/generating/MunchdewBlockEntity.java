/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.block.block_entity.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import org.violetmoon.botania.api.block_entity.GeneratingFlowerBlockEntity;
import org.violetmoon.botania.api.block_entity.RadiusDescriptor;
import org.violetmoon.botania.api.state.BotaniaStateProperties;
import org.violetmoon.botania.client.fx.WispParticleData;
import org.violetmoon.botania.common.block.block_entity.BotaniaBlockEntities;
import org.violetmoon.botania.common.component.BotaniaDataComponents;
import org.violetmoon.botania.common.helper.SectionScanHelper;
import org.violetmoon.botania.common.lib.BotaniaTags;
import org.violetmoon.botania.xplat.BotaniaConfig;

import java.util.*;

public class MunchdewBlockEntity extends GeneratingFlowerBlockEntity {
	public static final String TAG_COOLDOWN = "cooldown";

	private static final int RANGE = 8;
	private static final int RANGE_Y = 16;
	private static final int MANA_PER_LEAF = 160;
	private static final int COOLDOWN_TICKS = 1600;

	private int cooldown = 0;

	public MunchdewBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.MUNCHDEW, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (level.isClientSide()) {
			if (getBlockState().getValue(BotaniaStateProperties.ON_COOLDOWN)) {
				if (Math.random() < 0.5) {
					Vec3 offset = level.getBlockState(getBlockPos()).getOffset(level, getBlockPos());
					double x = getBlockPos().getX() + offset.x + 0.2 + Math.random() * 0.6;
					double y = getBlockPos().getY() + offset.y + 0.6 + Math.random() * 0.3;
					double z = getBlockPos().getZ() + offset.z + 0.2 + Math.random() * 0.6;
					WispParticleData data = WispParticleData.wisp(0.05F, 0.5F, 0.5F, 0.5F);
					level.addParticle(data, x, y, z, 0, 0.025F, 0);
				}
			}
			return;
		}

		if (cooldown > 0) {
			cooldown--;
			markForPersisting();
			return;
		}

		if (shouldUpdateThisTick()) {
			if (getMaxMana() - getMana() >= MANA_PER_LEAF && eatLeaves()) {
				if (!getBlockState().getValue(BotaniaStateProperties.GENERATING)) {
					level.setBlock(getBlockPos(),
							getBlockState()
									.setValue(BotaniaStateProperties.GENERATING, true)
									.setValue(BotaniaStateProperties.ON_COOLDOWN, false),
							Block.UPDATE_CLIENTS);
				}
				return;
			}
			if (getBlockState().getValue(BotaniaStateProperties.GENERATING)) {
				level.setBlock(getBlockPos(),
						getBlockState()
								.setValue(BotaniaStateProperties.GENERATING, false)
								.setValue(BotaniaStateProperties.ON_COOLDOWN, true),
						Block.UPDATE_CLIENTS);
				cooldown = COOLDOWN_TICKS;
				setChanged();
				return;
			}
		}
		if (getBlockState().getValue(BotaniaStateProperties.ON_COOLDOWN)) {
			level.setBlock(getBlockPos(),
					getBlockState().setValue(BotaniaStateProperties.ON_COOLDOWN, false),
					Block.UPDATE_CLIENTS);
		}
	}

	@Override
	protected int getUpdateInterval() {
		return 4;
	}

	private boolean eatLeaves() {
		Map<BlockPos, Float> coordsMap = new HashMap<>();
		RandomSource rng = level.getRandom();
		BlockPos effectivePos = getEffectivePos();

		BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
		// This read the whole area every 4 ticks, also with no leaves in range; skip sections
		// without consumable blocks. Same positions in the same order, so the random values are drawn the same way.
		SectionScanHelper.forEachMatching(level, effectivePos.offset(-RANGE, 0, -RANGE),
				effectivePos.offset(RANGE, RANGE_Y, RANGE),
				state -> state.is(BotaniaTags.Blocks.MUNCHDEW_CONSUMABLE), (pos, state) -> {
					for (Direction dir : Direction.values()) {
						if (level.isEmptyBlock(checkPos.setWithOffset(pos, dir))) {
							coordsMap.put(pos.immutable(), (state.hasProperty(LeavesBlock.DISTANCE)
									? state.getValue(LeavesBlock.DISTANCE) : 1) + 2.0f * rng.nextFloat());
							break;
						}
					}
				});

		if (coordsMap.isEmpty()) {
			return false;
		}

		float maxDistance = 0F;
		for (float distance : coordsMap.values()) {
			maxDistance = Math.max(maxDistance, distance);
		}

		float finalMaxDistance = maxDistance;
		coordsMap.values().removeIf(dist -> dist < finalMaxDistance - 1f);
		List<BlockPos> coords = new ArrayList<>(coordsMap.keySet());

		BlockPos breakCoords = coords.get(rng.nextInt(coords.size()));
		BlockState state = level.getBlockState(breakCoords);
		level.removeBlock(breakCoords, false);
		if (BotaniaConfig.common().blockBreakParticles()) {
			level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, breakCoords, Block.getId(state));
			Vec3 offset = level.getBlockState(effectivePos).getOffset(level, effectivePos).add(0.5, 0.75, 0.5);
			((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(state.getBlock())),
					effectivePos.getX() + offset.x, effectivePos.getY() + offset.y, effectivePos.getZ() + offset.z,
					5, 0.1, 0.1, 0.1, 0.03);
		}
		level.gameEvent(null, GameEvent.BLOCK_DESTROY, breakCoords);
		addMana(MANA_PER_LEAF);
		return true;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	protected void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.saveAdditional(cmp, registries);

		cmp.putInt(TAG_COOLDOWN, cooldown);
	}

	@Override
	protected void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.loadAdditional(cmp, registries);

		cooldown = cmp.getInt(TAG_COOLDOWN);
	}

	@Override
	public int getColor() {
		return 0x79C42F;
	}

	@Override
	public int getMaxMana() {
		return 10000;
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		if (cooldown > 0) {
			components.set(BotaniaDataComponents.COOLDOWN, cooldown);
		} else if (getBlockState().getValue(BotaniaStateProperties.GENERATING)) {
			components.set(BotaniaDataComponents.COOLDOWN, COOLDOWN_TICKS);
		}
	}

	@Override
	protected void applyImplicitComponents(DataComponentInput componentInput) {
		cooldown = componentInput.getOrDefault(BotaniaDataComponents.COOLDOWN, 0);
	}
}
