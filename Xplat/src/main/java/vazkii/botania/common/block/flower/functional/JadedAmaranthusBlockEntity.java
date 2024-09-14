/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.xplat.BotaniaConfig;

public class JadedAmaranthusBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int COST = 100;
	final int RANGE = 4;

	public JadedAmaranthusBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.JADED_AMARANTHUS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || redstoneSignal > 0) {
			return;
		}

		if (ticksExisted % 30 == 0 && getMana() >= COST) {
			BlockPos pos = new BlockPos(
					getEffectivePos().getX() - RANGE + getLevel().random.nextInt(RANGE * 2 + 1),
					getEffectivePos().getY() + RANGE,
					getEffectivePos().getZ() - RANGE + getLevel().random.nextInt(RANGE * 2 + 1)
			);

			BlockPos up = pos.above();

			for (int i = 0; i < RANGE * 2; i++) {
				DyeColor color = DyeColor.byId(getLevel().random.nextInt(16));
				BlockState flower = BotaniaBlocks.getFlower(color).defaultBlockState();

				if (getLevel().isEmptyBlock(up) && flower.canSurvive(getLevel(), up)) {
					if (BotaniaConfig.common().blockBreakParticles()) {
						getLevel().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, up, Block.getId(flower));
					}
					getLevel().setBlockAndUpdate(up, flower);
					getLevel().gameEvent(null, GameEvent.BLOCK_PLACE, up);
					addMana(-COST);
					sync();

					break;
				}

				up = pos;
				pos = pos.below();
			}
		}
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
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
