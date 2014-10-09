/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 10, 2014, 9:44:02 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.LexiconData;

public class SubTileThermalily extends SubTileHydroangeas {

	@Override
	public int getColor(){
		return 0xD03C00;
	}

	@Override
	public LexiconEntry getEntry() {
		return LexiconData.thermalily;
	}

	@Override
	public void doBurnParticles() {
		Botania.proxy.wispFX(supertile.getWorldObj(), supertile.xCoord + 0.55 + Math.random() * 0.2 - 0.1, supertile.yCoord + 0.9 + Math.random() * 0.2 - 0.1, supertile.zCoord + 0.5, 0.7F, 0.05F, 0.05F, (float) Math.random() / 6, (float) -Math.random() / 60);
	}

	@Override
	public boolean isPassiveFlower() {
		return false;
	}

	@Override
	public Block getBlockToSearchFor() {
		return Blocks.lava;
	}

	@Override
	public Block getBlockToSearchBelow() {
		return ConfigHandler.thermalilyObsidian ? Blocks.obsidian : null;
	}
	
	@Override
	public void playSound() {
		supertile.getWorldObj().playSoundEffect(supertile.xCoord, supertile.yCoord, supertile.zCoord, "botania:thermalily", 0.2F, 1F);
	}

	@Override
	public int getDelayBetweenPassiveGeneration() {
		return 1;
	}

	@Override
	public int getBurnTime() {
		return 3500;
	}

	@Override
	public int getValueForPassiveGeneration() {
		return 2;
	}

	@Override
	public int getMaxMana() {
		return 250;
	}

}
