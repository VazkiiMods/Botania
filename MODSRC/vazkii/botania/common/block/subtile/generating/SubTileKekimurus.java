/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jun 8, 2014, 6:26:37 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileKekimurus extends SubTileGenerating {

	@Override
	public void onUpdate() {
		super.onUpdate();

		int mana = 640;

		if(getMaxMana() - this.mana >= mana && !supertile.getWorldObj().isRemote && supertile.getWorldObj().getTotalWorldTime() % 50 == 0) {
			int range = 5;
			for(int i = 0; i < range * 2 + 1; i++)
				for(int j = 0; j < range * 2 + 1; j++)
					for(int k = 0; k < range * 2 + 1; k++) {
						int x = supertile.xCoord + i - range;
						int y = supertile.yCoord + j - range;
						int z = supertile.zCoord + k - range;
						Block block = supertile.getWorldObj().getBlock(x, y, z);
						if(block instanceof BlockCake) {
							int meta = supertile.getWorldObj().getBlockMetadata(x, y, z) + 1;
							if(meta == 6)
								supertile.getWorldObj().setBlockToAir(x, y, z);
							else supertile.getWorldObj().setBlockMetadataWithNotify(x, y, z, meta, 1 | 2);

							supertile.getWorldObj().playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
							this.mana += mana;
							sync();
							return;
						}
					}
		}
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
		return 4200;
	}

}
