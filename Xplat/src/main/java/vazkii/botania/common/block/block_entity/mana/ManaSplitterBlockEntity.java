/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntity;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;

public class ManaSplitterBlockEntity extends BotaniaBlockEntity implements ManaReceiver {
	private final List<ManaReceiver> validPools = new ArrayList<>();

	public ManaSplitterBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.DISTRIBUTOR, pos, state);
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, ManaSplitterBlockEntity self) {
		self.validPools.clear();
		for (Direction dir : Direction.Plane.HORIZONTAL) {
			BlockPos pos = worldPosition.relative(dir);
			if (level.hasChunkAt(pos)) {
				var receiver = XplatAbstractions.INSTANCE.findManaReceiver(level, pos, dir.getOpposite());
				if (receiver instanceof ManaPool) {
					if (!receiver.isFull()) {
						self.validPools.add(receiver);
					}
				}
			}
		}
	}

	@Override
	public Level getManaReceiverLevel() {
		return getLevel();
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return getBlockPos();
	}

	@Override
	public int getCurrentMana() {
		return 0;
	}

	@Override
	public boolean isFull() {
		return validPools.isEmpty();
	}

	@Override
	public void receiveMana(int mana) {
		int tiles = validPools.size();
		if (tiles != 0) {
			int manaForEach = mana / tiles;
			for (ManaReceiver pool : validPools) {
				pool.receiveMana(manaForEach);
			}
		}
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return !isFull();
	}
}
