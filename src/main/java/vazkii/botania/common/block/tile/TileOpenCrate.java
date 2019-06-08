/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 4, 2014, 12:51:05 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class TileOpenCrate extends TileSimpleInventory implements ITickableTileEntity {

	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.OPEN_CRATE)
	public static TileEntityType<?> TYPE;

	public TileOpenCrate() {
		this(TYPE);
	}

	public TileOpenCrate(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public void tick() {
		if (world.isRemote)
			return;

		boolean redstone = false;
		for(Direction dir : Direction.values()) {
			int redstoneSide = world.getRedstonePower(pos.offset(dir), dir);
			if(redstoneSide > 0) {
				redstone = true;
				break;
			}
		}

		if(canEject()) {
			ItemStack stack = itemHandler.getStackInSlot(0);
			if(!stack.isEmpty())
				eject(stack, redstone);
		}
	}

	public boolean canEject() {
		return world.isCollisionBoxesEmpty(null, new AxisAlignedBB(pos.down()));
	}

	public void eject(ItemStack stack, boolean redstone) {
		ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5, stack);
		item.motionX = 0;
		item.motionY = 0;
		item.motionZ = 0;
		if (redstone)
			item.age = -200;

		itemHandler.setStackInSlot(0, ItemStack.EMPTY);
		world.spawnEntity(item);
	}

	public boolean onWanded(World world, PlayerEntity player, ItemStack stack) {
		return false;
	}

	public int getSignal() {
		return 0;
	}
}
