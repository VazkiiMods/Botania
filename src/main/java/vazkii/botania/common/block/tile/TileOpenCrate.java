/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import vazkii.botania.mixin.AccessorItemEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileOpenCrate extends TileExposedSimpleInventory implements Tickable {
	public TileOpenCrate() {
		this(ModTiles.OPEN_CRATE);
	}

	public TileOpenCrate(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	protected SimpleInventory createItemHandler() {
		return new SimpleInventory(1);
	}

	@Override
	public void tick() {
		if (world.isClient) {
			return;
		}

		boolean redstone = false;
		for (Direction dir : Direction.values()) {
			int redstoneSide = world.getEmittedRedstonePower(pos.offset(dir), dir);
			if (redstoneSide > 0) {
				redstone = true;
				break;
			}
		}

		if (canEject()) {
			ItemStack stack = getItemHandler().getStack(0);
			if (!stack.isEmpty()) {
				eject(stack, redstone);
			}
		}
	}

	public boolean canEject() {
		float width = EntityType.ITEM.getWidth();
		float height = EntityType.ITEM.getHeight();

		double ejectX = pos.getX() + 0.5;
		double ejectY = pos.getY() - height;
		double ejectZ = pos.getZ() + 0.5;
		Box itemBB = new Box(ejectX - width / 2, ejectY, ejectZ - width / 2, ejectX + width / 2, ejectY + height, ejectZ + width / 2);
		return world.doesNotCollide(itemBB);
	}

	public void eject(ItemStack stack, boolean redstone) {
		double ejectY = pos.getY() - EntityType.ITEM.getHeight();
		ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, ejectY, pos.getZ() + 0.5, stack);
		item.setVelocity(Vec3d.ZERO);
		if (redstone) {
			((AccessorItemEntity) item).setAge(-200);
		}

		getItemHandler().setStack(0, ItemStack.EMPTY);
		world.spawnEntity(item);
	}

	@Override
	public boolean canExtract(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
		return false;
	}
}
