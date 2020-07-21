/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;
import vazkii.botania.common.block.ModSubtiles;

public class SubTileKekimurus extends TileEntityGeneratingFlower {
	private static final int RANGE = 5;

	public SubTileKekimurus() {
		super(ModSubtiles.KEKIMURUS);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getWorld().isClient) {
			return;
		}

		int mana = 1800;

		if (getMaxMana() - this.getMana() >= mana && !getWorld().isClient && ticksExisted % 80 == 0) {
			for (int i = 0; i < RANGE * 2 + 1; i++) {
				for (int j = 0; j < RANGE * 2 + 1; j++) {
					for (int k = 0; k < RANGE * 2 + 1; k++) {
						BlockPos pos = getEffectivePos().add(i - RANGE, j - RANGE, k - RANGE);
						BlockState state = getWorld().getBlockState(pos);
						Block block = state.getBlock();
						if (block instanceof CakeBlock) {
							int nextSlicesEaten = state.get(CakeBlock.BITES) + 1;
							if (nextSlicesEaten > 6) {
								getWorld().removeBlock(pos, false);
							} else {
								getWorld().setBlockState(pos, state.with(CakeBlock.BITES, nextSlicesEaten));
							}

							getWorld().syncWorldEvent(2001, pos, Block.getRawIdFromState(state));
							getWorld().playSound(null, getEffectivePos(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 1F, 0.5F + (float) Math.random() * 0.5F);
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
