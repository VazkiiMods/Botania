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
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.component.BotaniaDataComponents;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.BotaniaConfig;

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
			setChanged();
			return;
		}

		if (ticksExisted % 4 == 0) {
			if (getMaxMana() - getMana() >= MANA_PER_LEAF && eatLeaves()) {
				if (!getBlockState().getValue(BotaniaStateProperties.GENERATING)) {
					level.setBlock(getBlockPos(),
							getBlockState()
									.setValue(BotaniaStateProperties.GENERATING, true)
									.setValue(BotaniaStateProperties.ON_COOLDOWN, false),
							Block.UPDATE_CLIENTS);
				}
				return;
			} else if (getBlockState().getValue(BotaniaStateProperties.GENERATING)) {
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

	private boolean eatLeaves() {
		Map<BlockPos, Float> coordsMap = new HashMap<>();
		Random rng = new Random();
		BlockPos pos = getEffectivePos();

		for (BlockPos pos_ : BlockPos.betweenClosed(pos.offset(-RANGE, 0, -RANGE),
				pos.offset(RANGE, RANGE_Y, RANGE))) {
			BlockState state = level.getBlockState(pos_);
			if (state.is(BotaniaTags.Blocks.MUNCHDEW_CONSUMABLE)) {
				for (Direction dir : Direction.values()) {
					if (level.isEmptyBlock(pos_.relative(dir))) {
						coordsMap.put(pos_.immutable(), (state.hasProperty(LeavesBlock.DISTANCE)
								? state.getValue(LeavesBlock.DISTANCE) : 1) + 2.0f * rng.nextFloat());
						break;
					}
				}
			}
		}

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

		BlockPos breakCoords = coords.get(level.getRandom().nextInt(coords.size()));
		BlockState state = level.getBlockState(breakCoords);
		level.removeBlock(breakCoords, false);
		if (BotaniaConfig.common().blockBreakParticles()) {
			level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, breakCoords, Block.getId(state));
			Vec3 offset = level.getBlockState(pos).getOffset(level, pos).add(0.5, 0.75, 0.5);
			((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(state.getBlock())),
					pos.getX() + offset.x, pos.getY() + offset.y, pos.getZ() + offset.z,
					5, 0.1, 0.1, 0.1, 0.03);
		}
		level.gameEvent(null, GameEvent.BLOCK_DESTROY, breakCoords);
		addMana(MANA_PER_LEAF);
		// TODO: only actually sync if a player might look at the mana amount?
		sync();
		return true;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.writeToPacketNBT(cmp, registries);

		cmp.putInt(TAG_COOLDOWN, cooldown);
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.readFromPacketNBT(cmp, registries);

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
