/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [01/11/2015, 19:05:57 (GMT)]
 */
package vazkii.botania.api.lexicon.multiblock.component;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * A multiblock component that matches any non air blocks that have a collision box.
 */
public class AnyComponent extends MultiblockComponent {

	public AnyComponent(BlockPos relPos, IBlockState state) {
		super(relPos, state);
	}

	@Override
	public boolean matches(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return !block.isAir(world, pos) && block.getCollisionBoundingBox(world, pos, world.getBlockState(pos)) != null;
	}

}
