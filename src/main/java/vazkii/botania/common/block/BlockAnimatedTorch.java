/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.item.IHourglassTrigger;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.tile.TileAnimatedTorch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockAnimatedTorch extends BlockModWaterloggable implements IWandable, IManaTrigger, IHourglassTrigger, IWandHUD {

	private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 4, 16);

	public BlockAnimatedTorch(Properties builder) {
		super(builder);
	}

	@Override
	public ActionResultType onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
		if (hand == Hand.MAIN_HAND && playerIn.isSneaking() && playerIn.getHeldItem(hand).isEmpty()) {
			((TileAnimatedTorch) worldIn.getTileEntity(pos)).handRotate();
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		((TileAnimatedTorch) world.getTileEntity(pos)).onPlace(entity);
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		if (!burst.isFake()) {
			((TileAnimatedTorch) world.getTileEntity(pos)).toggle();
		}
	}

	@Override
	public void onTriggeredByHourglass(World world, BlockPos pos, TileEntity hourglass) {
		((TileAnimatedTorch) world.getTileEntity(pos)).toggle();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TileAnimatedTorch) world.getTileEntity(pos)).onWanded();
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		((TileAnimatedTorch) world.getTileEntity(pos)).renderHUD(mc);
	}

	@Override
	public boolean canProvidePower(BlockState state) {
		return true;
	}

	@Override
	public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return getWeakPower(blockState, blockAccess, pos, side);
	}

	@Override
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		TileAnimatedTorch tile = (TileAnimatedTorch) blockAccess.getTileEntity(pos);

		if (tile.rotating) {
			return 0;
		}

		if (TileAnimatedTorch.SIDES[tile.side] == side) {
			return 15;
		}

		return 0;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileAnimatedTorch();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public void onPlayerDestroy(IWorld world, BlockPos pos, BlockState state) {
		// TE is already gone so best we can do is just notify everyone
		world.notifyNeighbors(pos, this);
		super.onPlayerDestroy(world, pos, state);
	}

}
