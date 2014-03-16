/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 16, 2014, 11:15:42 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

public class SubTileNightshade extends SubTileDaybloom {
	
	@Override
	public int getDelayBetweenPassiveGeneration() {
		return super.getDelayBetweenPassiveGeneration() * 2;
	}
	
	@Override
	public boolean canGeneratePassively() {
		System.out.println(!super.canGeneratePassively());
		return !super.canGeneratePassively();
	}
	
	@Override
	public int getColor() {
		return 0x3D2A90;
	}

}
