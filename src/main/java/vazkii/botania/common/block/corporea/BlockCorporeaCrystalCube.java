/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.corporea.TileCorporeaBase;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlockCorporeaCrystalCube extends BlockModWaterloggable implements BlockEntityProvider, IWandable {

	private static final VoxelShape SHAPE = createCuboidShape(3.0, 0, 3.0, 13.0, 16, 13.0);

	public BlockCorporeaCrystalCube(AbstractBlock.Settings builder) {
		super(builder);
	}

	@Override
	public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		if (!world.isClient) {
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getBlockEntity(pos);
			cube.doRequest(player.isSneaking());
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (!stack.isEmpty()) {
			if (stack.getItem() == ModItems.twigWand && player.isSneaking()) {
				return ActionResult.PASS;
			}
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getBlockEntity(pos);
			if (cube.locked) {
				if (!world.isClient) {
					player.sendMessage(new TranslatableText("botaniamisc.crystalCubeLocked"), false);
				}
			} else {
				cube.setRequestTarget(stack);
			}
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		if (player == null || player.isSneaking()) {
			TileCorporeaCrystalCube cube = (TileCorporeaCrystalCube) world.getBlockEntity(pos);
			cube.locked = !cube.locked;
			if (!world.isClient) {
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(cube);
			}
			return true;
		}
		return false;
	}

	@Nonnull
	@Override
	public TileCorporeaBase createBlockEntity(@Nonnull BlockView world) {
		return new TileCorporeaCrystalCube();
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ((TileCorporeaCrystalCube) world.getBlockEntity(pos)).getComparatorValue();
	}
}
