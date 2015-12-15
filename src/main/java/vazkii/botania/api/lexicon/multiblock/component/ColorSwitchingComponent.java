/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 27, 2015, 7:20:09 PM (GMT)]
 */
package vazkii.botania.api.lexicon.multiblock.component;

import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;

/**
 * A multiblock component that switches through the 16 colors of the
 * minecraft spectrum. This can be used for flowers, for example.
 */
public class ColorSwitchingComponent extends MultiblockComponent {

	public ColorSwitchingComponent(ChunkCoordinates relPos, Block block) {
		super(relPos, block, -1);
	}

	@Override
	public int getMeta() {
		return (int) (BotaniaAPI.internalHandler.getWorldElapsedTicks() / 20) % 16;
	}

	@Override
	public boolean matches(World world, int x, int y, int z) {
		return world.getBlock(x, y, z) == getBlock();
	}

	@Override
	public MultiblockComponent copy() {
		return new ColorSwitchingComponent(relPos, block);
	}

}
