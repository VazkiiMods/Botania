/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 8, 2014, 6:26:37 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileKekimurus extends SubTileGenerating {

	private static final int RANGE = 5;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (supertile.getWorld().isRemote)
			return;

		int mana = 1800;

		if(getMaxMana() - this.mana >= mana && !supertile.getWorld().isRemote && ticksExisted % 80 == 0) {
			for(int i = 0; i < RANGE * 2 + 1; i++)
				for(int j = 0; j < RANGE * 2 + 1; j++)
					for(int k = 0; k < RANGE * 2 + 1; k++) {
						BlockPos pos = supertile.getPos().add(i - RANGE, j - RANGE, k - RANGE);
						IBlockState state = supertile.getWorld().getBlockState(pos);
						Block block = state.getBlock();
						if(block instanceof BlockCake) {
							int nextSlicesEaten = state.getValue(BlockCake.BITES) + 1;
							if(nextSlicesEaten > 6)
								supertile.getWorld().setBlockToAir(pos);
							else supertile.getWorld().setBlockState(pos, state.withProperty(BlockCake.BITES, nextSlicesEaten), 1 | 2);

							supertile.getWorld().playEvent(2001, pos, Block.getStateId(state));
							supertile.getWorld().playSound(null, supertile.getPos(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 1F, 0.5F + (float) Math.random() * 0.5F);
							this.mana += mana;
							sync();
							return;
						}
					}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.kekimurus;
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
