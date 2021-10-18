/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;

public class SubTileKekimurus extends TileEntityGeneratingFlower {
	private static final int RANGE = 5;

	public SubTileKekimurus(BlockPos pos, BlockState state) {
		super(ModSubtiles.KEKIMURUS, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			return;
		}

		int mana = 1800;

		if (getMaxMana() - this.getMana() >= mana && !getLevel().isClientSide && ticksExisted % 80 == 0) {
			for (int i = 0; i < RANGE * 2 + 1; i++) {
				for (int j = 0; j < RANGE * 2 + 1; j++) {
					for (int k = 0; k < RANGE * 2 + 1; k++) {
						BlockPos pos = getEffectivePos().offset(i - RANGE, j - RANGE, k - RANGE);
						BlockState state = getLevel().getBlockState(pos);
						Block block = state.getBlock();
						if (block instanceof CakeBlock) {
							int nextSlicesEaten = state.getValue(CakeBlock.BITES) + 1;
							if (nextSlicesEaten > 6) {
								getLevel().removeBlock(pos, false);
							} else {
								getLevel().setBlockAndUpdate(pos, state.setValue(CakeBlock.BITES, nextSlicesEaten));
							}

							getLevel().levelEvent(2001, pos, Block.getId(state));
							//Usage of vanilla sound event: Subtitle is "Eating", generic sounds are meant to be reused.
							getLevel().playSound(null, getEffectivePos(), SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 1F, 0.5F + (float) Math.random() * 0.5F);
							addMana(mana);
							sync();
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
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
