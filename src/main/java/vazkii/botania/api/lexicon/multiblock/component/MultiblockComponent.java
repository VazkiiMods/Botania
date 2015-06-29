/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 27, 2015, 2:42:32 PM (GMT)]
 */
package vazkii.botania.api.lexicon.multiblock.component;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

/**
 * A component of a multiblock, the normal one
 * is just a block.
 */
public class MultiblockComponent {

	public ChunkCoordinates relPos;
	public final Block block;
	public final int meta;

	public MultiblockComponent(ChunkCoordinates relPos, Block block, int meta) {
		this.relPos = relPos;
		this.block = block;
		this.meta = meta;
	}

	public ChunkCoordinates getRelativePosition() {
		return relPos;
	}

	public Block getBlock() {
		return block;
	}

	public int getMeta() {
		return meta;
	}

	public boolean matches(World world, int x, int y, int z) {
		return world.getBlock(x, y, z) == getBlock() && (meta == -1 || world.getBlockMetadata(x, y, z) == meta);
	}

	public ItemStack[] getMaterials() {
		return new ItemStack[] { new ItemStack(block, 1, meta) };
	}

	public void rotate(double angle) {
		double x = relPos.posX;
		double z = relPos.posZ;
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);

		double xn = x * cos - z * sin;
		double zn = x * sin + z * cos;
		relPos = new ChunkCoordinates((int) Math.round(xn), relPos.posY, (int) Math.round(zn));
	}

	public MultiblockComponent copy() {
		return new MultiblockComponent(relPos, block, meta);
	}
}
