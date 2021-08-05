/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlockCorporeaCrystalCube extends BlockModWaterloggable implements EntityBlock, IWandable {

	private static final VoxelShape SHAPE = box(3.0, 0, 3.0, 13.0, 16, 13.0);

	public BlockCorporeaCrystalCube(BlockBehaviour.Properties builder) {
		super(builder);
	}

	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player player) {
		if (!world.isClientSide) {
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getBlockEntity(pos);
			cube.doRequest(player.isShiftKeyDown());
		}
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.isEmpty()) {
			if (stack.getItem() == ModItems.twigWand && player.isShiftKeyDown()) {
				return InteractionResult.PASS;
			}
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getBlockEntity(pos);
			if (cube.locked) {
				if (!world.isClientSide) {
					player.displayClientMessage(new TranslatableComponent("botaniamisc.crystalCubeLocked"), false);
				}
			} else {
				cube.setRequestTarget(stack);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean onUsedByWand(Player player, ItemStack stack, Level world, BlockPos pos, Direction side) {
		if (player == null || player.isShiftKeyDown()) {
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getBlockEntity(pos);
			cube.locked = !cube.locked;
			if (!world.isClientSide) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(cube);
			}
			return true;
		}
		return false;
	}

	@Nonnull
	@Override
	public TileCorporeaBase newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TileCorporeaCrystalCube(pos, state);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return ((TileCorporeaCrystalCube) world.getBlockEntity(pos)).getComparatorValue();
	}
}
