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
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.block_entity.RadiusDescriptor;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;

import java.util.Optional;

public class KekimurusBlockEntity extends GeneratingFlowerBlockEntity {
	private static final int RANGE = 5;
	public static final int MANA_PER_SLICE = 1800;

	public KekimurusBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.KEKIMURUS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide || getMaxMana() - this.getMana() < MANA_PER_SLICE || !shouldUpdateThisTick()) {
			return;
		}

		BlockPos effectivePos = getEffectivePos();
		Optional<BlockPos> cakePos = BlockPos.findClosestMatch(effectivePos, RANGE, RANGE,
				pos -> getLevel().getBlockState(pos).getBlock() instanceof CakeBlock);
		if (cakePos.isPresent()) {
			BlockPos pos = cakePos.get();
			BlockState state = getLevel().getBlockState(pos);
			int nextSlicesEaten = state.getValue(CakeBlock.BITES) + 1;
			if (nextSlicesEaten > CakeBlock.MAX_BITES) {
				getLevel().removeBlock(pos, false);
				getLevel().gameEvent(null, GameEvent.BLOCK_DESTROY, pos);
			} else {
				getLevel().setBlockAndUpdate(pos, state.setValue(CakeBlock.BITES, nextSlicesEaten));
			}

			getLevel().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
			getLevel().gameEvent(null, GameEvent.EAT, effectivePos);
			//Usage of vanilla sound event: Subtitle is "Eating", generic sounds are meant to be reused.
			getLevel().playSound(null, effectivePos, SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 1F, 0.5F + (float) Math.random() * 0.5F);
			Vec3 offset = getLevel().getBlockState(effectivePos).getOffset(getLevel(), effectivePos).add(0.5, 0.75, 0.5);
			((ServerLevel) getLevel()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(state.getBlock())),
					effectivePos.getX() + offset.x, effectivePos.getY() + offset.y, effectivePos.getZ() + offset.z,
					5, 0.1, 0.1, 0.1, 0.03);
			addMana(MANA_PER_SLICE);
		}
	}

	@Override
	protected int getUpdateInterval() {
		return 80;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0x935D28;
	}

	@Override
	public int getMaxMana() {
		return 9001;
	}

}
