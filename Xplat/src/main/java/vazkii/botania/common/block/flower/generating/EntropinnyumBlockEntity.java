/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;

public class EntropinnyumBlockEntity extends GeneratingFlowerBlockEntity {
	private static final int RANGE = 12;
	private static final int EXPLODE_EFFECT_EVENT = 0;
	private static final int ANGRY_EFFECT_EVENT = 1;

	private static boolean trackTntEntities = false;
	private static final List<PrimedTnt> trackedTntEntities = new ArrayList<>();

	public EntropinnyumBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.ENTROPINNYUM, pos, state);
	}

	/**
	 * A force relay, force lens mana burst or vanilla piston is about to start moving blocks.
	 */
	public static void startTrackingTntEntities() {
		assert (!trackTntEntities && trackedTntEntities.isEmpty());
		trackTntEntities = true;
	}

	/**
	 * A TNT entity just spawned. Check it for potentially unethical spawning methods.
	 * 
	 * @param entity The TNT entity.
	 */
	public static void addTrackedTntEntity(PrimedTnt entity) {
		if (trackTntEntities) {
			trackedTntEntities.add(entity);
		}
	}

	/**
	 * A force relay, force lens mana burst,or vanilla piston finished converting all blocks into moving block entities.
	 */
	public static void endTrackingTntEntitiesAndCheck() {
		assert (trackTntEntities);
		trackTntEntities = false;
		for (final var tnt : trackedTntEntities) {
			checkUnethical(tnt);
		}
		trackedTntEntities.clear();
	}

	private static void checkUnethical(PrimedTnt entity) {
		BlockPos center = entity.blockPosition();
		if (!entity.getLevel().isLoaded(center)) {
			return;
		}

		BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
		for (final var dir : Direction.values()) {
			blockPos.setWithOffset(center, dir);
			if (!entity.getLevel().isLoaded(blockPos)) {
				continue;
			}

			final var blockState = entity.getLevel().getBlockState(blockPos);
			if (blockState.getBlock() == Blocks.MOVING_PISTON && blockState.hasBlockEntity()) {
				final var blockEntity = entity.getLevel().getBlockEntity(blockPos);
				if (blockEntity instanceof PistonMovingBlockEntity movingBlockEntity
						&& movingBlockEntity.getMovementDirection() == dir
						&& movingBlockEntity.getMovedState().getBlock() instanceof TntBlock) {
					// found a moving block that marks the destination of a TNT block moving away from the TNT entity
					XplatAbstractions.INSTANCE.ethicalComponent(entity).markUnethical();
					break;
				}
			}
		}
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide && getMana() == 0) {
			List<PrimedTnt> tnts = getLevel().getEntitiesOfClass(PrimedTnt.class, new AABB(getEffectivePos().offset(-RANGE, -RANGE, -RANGE), getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1)));
			for (PrimedTnt tnt : tnts) {
				FluidState fluid = getLevel().getFluidState(tnt.blockPosition());
				if (tnt.getFuse() == 1 && tnt.isAlive() && fluid.isEmpty()) {
					boolean unethical = XplatAbstractions.INSTANCE.ethicalComponent(tnt).isUnethical();
					tnt.playSound(unethical ? BotaniaSounds.entropinnyumAngry : BotaniaSounds.entropinnyumHappy, 1F, (1F + (getLevel().random.nextFloat() - getLevel().random.nextFloat()) * 0.2F) * 0.7F);
					tnt.discard();
					addMana(unethical ? 3 : getMaxMana());
					sync();

					getLevel().blockEvent(getBlockPos(), getBlockState().getBlock(), unethical ? ANGRY_EFFECT_EVENT : EXPLODE_EFFECT_EVENT, tnt.getId());
					break;
				}
			}
		}
	}

	@Override
	public boolean triggerEvent(int event, int param) {
		if (event == EXPLODE_EFFECT_EVENT) {
			if (getLevel().isClientSide && getLevel().getEntity(param) instanceof PrimedTnt) {
				Entity e = getLevel().getEntity(param);

				for (int i = 0; i < 50; i++) {
					SparkleParticleData data = SparkleParticleData.sparkle((float) (Math.random() * 0.65F + 1.25F), 1F, (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 12);
					level.addParticle(data, e.getX() + Math.random() * 4 - 2, e.getY() + Math.random() * 4 - 2, e.getZ() + Math.random() * 4 - 2, 0, 0, 0);
				}

				getLevel().addParticle(ParticleTypes.EXPLOSION_EMITTER, e.getX(), e.getY(), e.getZ(), 1D, 0D, 0D);
			}
			return true;
		} else if (event == ANGRY_EFFECT_EVENT) {
			if (getLevel().isClientSide && getLevel().getEntity(param) instanceof PrimedTnt) {
				Entity e = getLevel().getEntity(param);

				for (int i = 0; i < 50; i++) {
					level.addParticle(ParticleTypes.ANGRY_VILLAGER, e.getX() + Math.random() * 4 - 2, e.getY() + Math.random() * 4 - 2, e.getZ() + Math.random() * 4 - 2, 0, 0, 0);
				}
			}

			return true;
		} else {
			return super.triggerEvent(event, param);
		}
	}

	@Override
	public int getColor() {
		return 0xcb0000;
	}

	@Override
	public int getMaxMana() {
		return 6500;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

}
