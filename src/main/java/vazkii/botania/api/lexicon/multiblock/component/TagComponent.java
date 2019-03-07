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
import net.minecraft.block.state.IBlockState;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;

public class TagComponent extends MultiblockComponent {

	private final Tag<Block> tag;

	public TagComponent(BlockPos relPos, Block block, Tag<Block> tag) {
		super(relPos, block.getDefaultState());
		this.tag = tag;
	}

	@Override
	public IBlockState getBlockState() {
		if (tag.getAllElements().isEmpty()) {
			return super.getBlockState();
		} else {
			Block[] arr = tag.getAllElements().toArray(new Block[0]);
			int idx = (int) (BotaniaAPI.internalHandler.getWorldElapsedTicks() / 20) % arr.length;
			return arr[idx].getDefaultState();
		}
	}

	@Override
	public boolean matches(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock().isIn(tag);
	}

	@Override
	public MultiblockComponent copy() {
		return new TagComponent(getRelativePosition(), getBlockState().getBlock(), tag);
	}

}
