/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.Optional;

public class JadedAmaranthusBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int COST = 100;
	final int RANGE = 4;

	public JadedAmaranthusBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.JADED_AMARANTHUS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide() || isPowered() || getMana() < COST || !shouldUpdateThisTick()) {
			return;
		}
		Optional<HolderSet.Named<Block>> flowersTag =
				BuiltInRegistries.BLOCK.getTag(BotaniaTags.Blocks.SMALL_MYSTICAL_FLOWERS);
		if (flowersTag.flatMap(holders -> holders.stream().findAny()).isEmpty()) {
			// missing or empty flowers tag
			return;
		}

		BlockPos pos = new BlockPos(
				getEffectivePos().getX() - RANGE + getLevel().getRandom().nextInt(RANGE * 2 + 1),
				getEffectivePos().getY() + RANGE,
				getEffectivePos().getZ() - RANGE + getLevel().getRandom().nextInt(RANGE * 2 + 1)
		);

		BlockPos up = pos.above();

		for (int i = 0; i < RANGE * 2; i++) {
			var randomFlower = flowersTag.get().getRandomElement(getLevel().getRandom());
			if (randomFlower.isEmpty()) {
				// shouldn't be possible, but just in case
				continue;
			}
			BlockState flower = randomFlower.get().value().defaultBlockState();
			if (getLevel().isEmptyBlock(up) && flower.canSurvive(getLevel(), up)) {
				if (BotaniaConfig.common().blockBreakParticles()) {
					getLevel().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, up, Block.getId(flower));
				}
				getLevel().setBlockAndUpdate(up, flower);
				getLevel().gameEvent(null, GameEvent.BLOCK_PLACE, up);
				addMana(-COST);

				break;
			}

			up = pos;
			pos = pos.below();
		}
	}

	@Override
	protected int getUpdateInterval() {
		return 30;
	}

	@Override
	public int getColor() {
		return 0x961283;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return COST;
	}

}
