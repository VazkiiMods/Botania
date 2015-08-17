/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 18, 2015, 8:30:00 PM (GMT)]
 */
package vazkii.botania.common.block.decor.walls;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockPrismarineWall extends BlockModWall {

	public BlockPrismarineWall() {
		super(ModBlocks.prismarine, 0);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypeStone);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.prismarine;
	}

}
