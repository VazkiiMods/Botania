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
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileKekimurus extends SubTileGenerating {

	private static final int RANGE = 5;

	@Override
	public void onUpdate() {
		super.onUpdate();

		int mana = 1800;

		if(getMaxMana() - this.mana >= mana && !supertile.getWorldObj().isRemote && ticksExisted % 80 == 0) {
			for(int i = 0; i < RANGE * 2 + 1; i++)
				for(int j = 0; j < RANGE * 2 + 1; j++)
					for(int k = 0; k < RANGE * 2 + 1; k++) {
						int x = supertile.xCoord + i - RANGE;
						int y = supertile.yCoord + j - RANGE;
						int z = supertile.zCoord + k - RANGE;
						Block block = supertile.getWorldObj().getBlock(x, y, z);
						if(block instanceof BlockCake) {
							int meta = supertile.getWorldObj().getBlockMetadata(x, y, z) + 1;
							if(meta == 6)
								supertile.getWorldObj().setBlockToAir(x, y, z);
							else supertile.getWorldObj().setBlockMetadataWithNotify(x, y, z, meta, 1 | 2);

							supertile.getWorldObj().playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
							supertile.getWorldObj().playSoundEffect(supertile.xCoord, supertile.yCoord, supertile.zCoord, "random.eat", 1F, 0.5F + (float) Math.random() * 0.5F);
							this.mana += mana;
							sync();
							return;
						}
					}
		}
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toChunkCoordinates(), RANGE);
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
