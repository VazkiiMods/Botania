/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.mixin.AccessorRecipeManager;

import javax.annotation.Nonnull;

public class BlockTerraPlate extends BlockModWaterloggable implements EntityBlock {

	private static final VoxelShape SHAPE = box(0, 0, 0, 16, 3, 16);

	public BlockTerraPlate(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.isEmpty() && usesItem(stack, world)) {
			if (!world.isClientSide) {
				ItemStack target = stack.split(1);
				ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, target);
				item.setPickUpDelay(40);
				item.setDeltaMovement(Vec3.ZERO);
				world.addFreshEntity(item);
			}

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	private static boolean usesItem(ItemStack stack, Level world) {
		for (Recipe<?> value : ((AccessorRecipeManager) world.getRecipeManager()).botania_getAll(ModRecipeTypes.TERRA_PLATE_TYPE).values()) {
			for (Ingredient i : value.getIngredients()) {
				if (i.test(stack)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isPathfindable(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, PathComputationType type) {
		return false;
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockGetter world) {
		return new TileTerraPlate();
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		TileTerraPlate plate = (TileTerraPlate) world.getBlockEntity(pos);
		return plate.getComparatorLevel();
	}

}
