/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 28, 2015, 3:23:15 PM (GMT)]
 */
package vazkii.botania.api.lexicon.multiblock.component;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.state.BotaniaStateProps;

/**
 * A multiblock component that matches any botania flower.
 */
public class FlowerComponent extends ColorSwitchingComponent {

	public FlowerComponent(BlockPos relPos, Block block) {
		super(relPos, block, BotaniaStateProps.COLOR);
	}

	@Override
	public boolean matches(World world, BlockPos pos) {
		return BotaniaAPI.internalHandler.isBotaniaFlower(world, pos);
	}

	@Override
	public MultiblockComponent copy() {
		return new FlowerComponent(relPos, state.getBlock());
	}

}
