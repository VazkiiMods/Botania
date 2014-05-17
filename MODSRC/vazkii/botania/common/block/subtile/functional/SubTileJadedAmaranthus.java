/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 3, 2014, 10:31:29 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileJadedAmaranthus extends SubTileFunctional {

	private static final int COST = 100;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(redstoneSignal > 0)
			return;

		if(mana >= COST && !supertile.getWorldObj().isRemote && supertile.getWorldObj().getTotalWorldTime() % 30 == 0) {
			int range = 4;
			int x = supertile.xCoord - range + supertile.getWorldObj().rand.nextInt(range * 2 + 1);
			int y = supertile.yCoord + range;
			int z = supertile.zCoord - range + supertile.getWorldObj().rand.nextInt(range * 2 + 1);

			for(int i = 0; i < range * 2; i++) {
				Block block = supertile.getWorldObj().getBlock(x, y, z);
				if((block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland) && (supertile.getWorldObj().isAirBlock(x, y + 1, z) || supertile.getWorldObj().getBlock(x, y + 1, z).isReplaceable(supertile.getWorldObj(), x, y + 1, z))) {
					int color = supertile.getWorldObj().rand.nextInt(16);
					if(ModBlocks.flower.canBlockStay(supertile.getWorldObj(), x, y + 1, z)) {
						supertile.getWorldObj().playAuxSFX(2001, x, y + 1, z, Block.getIdFromBlock(ModBlocks.flower) + (color << 12));
						supertile.getWorldObj().setBlock(x, y + 1, z, ModBlocks.flower, color, 1 | 2);
					}

					mana -= COST;
					sync();

					break;
				}

				y--;
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
	public LexiconEntry getEntry() {
		return LexiconData.jadedAmaranthus;
	}

	@Override
	public int getMaxMana() {
		return COST;
	}

}
