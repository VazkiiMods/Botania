/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 31, 2014, 7:49:43 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ChestGenHooks;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.SubTileFunctional;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileLoonuim extends SubTileFunctional {

	private static final int COST = 20000;

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(redstoneSignal == 0 && supertile.getWorldObj().getTotalWorldTime() % 200 == 0 && mana >= COST) {
			Random rand = supertile.getWorldObj().rand;
			ItemStack stack = ChestGenHooks.getOneItem(ChestGenHooks.DUNGEON_CHEST, rand);
			EntityItem entity = new EntityItem(supertile.getWorldObj(), supertile.xCoord - 3 + rand.nextInt(7) , supertile.yCoord + 1, supertile.zCoord - 3 + rand.nextInt(7), stack);
			entity.motionX = 0;
			entity.motionY = 0;
			entity.motionZ = 0;

			if(!supertile.getWorldObj().isRemote)
				supertile.getWorldObj().spawnEntityInWorld(entity);

			mana -= COST;
			sync();
		}
	}

	@Override
	public int getColor() {
		return 0x274A00;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.loonium;
	}

	@Override
	public int getMaxMana() {
		return COST;
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

}
