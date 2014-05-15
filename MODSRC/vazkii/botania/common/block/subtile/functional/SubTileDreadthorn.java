/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 15, 2014, 4:25:24 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.functional;

import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.lexicon.LexiconData;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;

public class SubTileDreadthorn extends SubTileBellethorn {

	@Override
	public int getColor() {
		return 0x260B45;
	}
	
	@Override
	public IEntitySelector getSelector() {
		return new IEntitySelector() {

			@Override
			public boolean isEntityApplicable(Entity var1) {
				return var1 instanceof EntityAnimal && ((EntityAnimal) var1).getGrowingAge() == 0;
			}
			
		};
	}
	
	@Override
	public int getManaCost() {
		return 30;
	}
	
	@Override
	public LexiconEntry getEntry() {
		return LexiconData.dreadthorne;
	}
	
}
