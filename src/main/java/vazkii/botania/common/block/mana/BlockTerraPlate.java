/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlockTerraPlate extends BlockModWaterloggable implements BlockEntityProvider {

	private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 3, 16);

	public BlockTerraPlate(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (!stack.isEmpty()
				&& (stack.getItem() == ModItems.manaSteel || stack.getItem() == ModItems.manaPearl || stack.getItem() == ModItems.manaDiamond)) {
			if (!world.isClient) {
				ItemStack target = stack.split(1);
				ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, target);
				item.setPickupDelay(40);
				item.setVelocity(Vec3d.ZERO);
				world.spawnEntity(item);
			}

			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public boolean canPathfindThrough(@Nonnull BlockState state, @Nonnull BlockView world, @Nonnull BlockPos pos, NavigationType type) {
		return false;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileTerraPlate();
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		TileTerraPlate plate = (TileTerraPlate) world.getBlockEntity(pos);
		int val = (int) ((double) plate.getCurrentMana() / (double) TileTerraPlate.MAX_MANA * 15.0);
		if (plate.getCurrentMana() > 0) {
			val = Math.max(val, 1);
		}

		return val;
	}

}
