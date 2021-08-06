/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.mixin.AccessorItemEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileOpenCrate extends TileExposedSimpleInventory {
	public TileOpenCrate(BlockPos pos, BlockState state) {
		this(ModTiles.OPEN_CRATE, pos, state);
	}

	protected TileOpenCrate(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(1);
	}

	public static void serverTick(Level level, BlockPos worldPosition, BlockState state, TileOpenCrate self) {
		boolean redstone = level.hasNeighborSignal(worldPosition);

		if (self.canEject()) {
			ItemStack stack = self.getItemHandler().getItem(0);
			if (!stack.isEmpty()) {
				self.eject(stack, redstone);
			}
		}
	}

	public boolean canEject() {
		float width = EntityType.ITEM.getWidth();
		float height = EntityType.ITEM.getHeight();

		double ejectX = worldPosition.getX() + 0.5;
		double ejectY = worldPosition.getY() - height;
		double ejectZ = worldPosition.getZ() + 0.5;
		AABB itemBB = new AABB(ejectX - width / 2, ejectY, ejectZ - width / 2, ejectX + width / 2, ejectY + height, ejectZ + width / 2);
		return level.noCollision(itemBB);
	}

	public void eject(ItemStack stack, boolean redstone) {
		double ejectY = worldPosition.getY() - EntityType.ITEM.getHeight();
		ItemEntity item = new ItemEntity(level, worldPosition.getX() + 0.5, ejectY, worldPosition.getZ() + 0.5, stack);
		item.setDeltaMovement(Vec3.ZERO);
		if (redstone) {
			((AccessorItemEntity) item).setAge(-200);
		}

		getItemHandler().setItem(0, ItemStack.EMPTY);
		level.addFreshEntity(item);
	}

	@Override
	public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
		return false;
	}
}
