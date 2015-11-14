/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 17, 2014, 12:05:37 AM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileClayconia extends SubTileFunctional {

	private static final int RANGE = 5;
	private static final int RANGE_Y = 3;

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(!supertile.getWorld().isRemote && ticksExisted % 5 == 0) {
			int manaCost = 80;
			if(mana >= manaCost) {
				BlockPos coords = getCoordsToPut();
				if(coords != null) {
					supertile.getWorld().setBlockToAir(coords.posX, coords.posY, coords.posZ);
					if(ConfigHandler.blockBreakParticles)
						supertile.getWorld().playAuxSFX(2001, coords.posX, coords.posY, coords.posZ, Block.getIdFromBlock(Block.getBlockFromName("sand")));
					EntityItem item = new EntityItem(supertile.getWorld(), coords.posX + 0.5, coords.posY + 0.5, coords.posZ + 0.5, new ItemStack(Items.clay_ball));
					supertile.getWorld().spawnEntityInWorld(item);
					mana -= manaCost;
				}
			}
		}
	}

	public BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList();

		for(int i = -RANGE; i < RANGE + 1; i++)
			for(int j = -RANGE_Y; j < RANGE_Y + 1; j++)
				for(int k = -RANGE; k < RANGE + 1; k++) {
					int x = supertile.xCoord + i;
					int y = supertile.yCoord + j;
					int z = supertile.zCoord + k;
					Block block = supertile.getWorld().getBlock(x, y, z);
					if(block == Block.getBlockFromName("sand"))
						possibleCoords.add(new BlockPos(x, y, z));
				}

		if(possibleCoords.isEmpty())
			return null;
		return possibleCoords.get(supertile.getWorld().rand.nextInt(possibleCoords.size()));
	}

	@Override
	public RadiusDescriptor getRadius() {
		return new RadiusDescriptor.Square(toBlockPos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0x7B8792;
	}

	@Override
	public int getMaxMana() {
		return 640;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.clayconia;
	}

}
