/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.helper;

import IItemHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InvWithLocation {

	private final IItemHandler handler;
	private final World world;
	private final BlockPos pos;

	public InvWithLocation(IItemHandler itemHandler, World world, BlockPos pos) {
		this.handler = itemHandler;
		this.world = world;
		this.pos = pos;
	}

	@Override
	public int hashCode() {
		return 31 * getHandler().hashCode() ^ getWorld().hashCode() ^ getPos().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof InvWithLocation
				&& getHandler().equals(((InvWithLocation) o).getHandler())
				&& getWorld() == ((InvWithLocation) o).getWorld()
				&& getPos().equals(((InvWithLocation) o).getPos());
	}

	public IItemHandler getHandler() {
		return handler;
	}

	public World getWorld() {
		return world;
	}

	public BlockPos getPos() {
		return pos;
	}
}
