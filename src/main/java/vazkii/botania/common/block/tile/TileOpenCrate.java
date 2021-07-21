/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import vazkii.botania.mixin.AccessorItemEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileOpenCrate extends TileExposedSimpleInventory implements ITickableTileEntity {
	public TileOpenCrate() {
		this(ModTiles.OPEN_CRATE);
	}

	public TileOpenCrate(TileEntityType<?> type) {
		super(type);
	}

	@Override
	protected Inventory createItemHandler() {
		return new Inventory(1);
	}

	@Override
	public void tick() {
		if (world.isRemote) {
			return;
		}

		boolean redstone = world.isBlockPowered(pos);

		if (canEject()) {
			ItemStack stack = getItemHandler().getStackInSlot(0);
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
		AxisAlignedBB itemBB = new AxisAlignedBB(ejectX - width / 2, ejectY, ejectZ - width / 2, ejectX + width / 2, ejectY + height, ejectZ + width / 2);
		return world.hasNoCollisions(itemBB);
	}

	public void eject(ItemStack stack, boolean redstone) {
		double ejectY = pos.getY() - EntityType.ITEM.getHeight();
		ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, ejectY, pos.getZ() + 0.5, stack);
		item.setMotion(Vector3d.ZERO);
		if (redstone) {
			((AccessorItemEntity) item).setAge(-200);
		}

		getItemHandler().setInventorySlotContents(0, ItemStack.EMPTY);
		world.addEntity(item);
	}

	@Override
	public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
		return false;
	}
}
