/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
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

public class BlockAnimatedTorch extends BlockModWaterloggable implements BlockEntityProvider, IWandable, IManaTrigger, IHourglassTrigger, IWandHUD {

	private static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 4, 16);

	public BlockAnimatedTorch(Settings builder) {
		super(builder);
	}

	@Override
	public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockHitResult hit) {
		if (hand == Hand.MAIN_HAND && playerIn.isSneaking() && playerIn.getStackInHand(hand).isEmpty()) {
			((TileAnimatedTorch) worldIn.getBlockEntity(pos)).handRotate();
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		((TileAnimatedTorch) world.getBlockEntity(pos)).onPlace(entity);
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		if (!burst.isFake()) {
			((TileAnimatedTorch) world.getBlockEntity(pos)).toggle();
		}
	}

	@Override
	public void onTriggeredByHourglass(World world, BlockPos pos, BlockEntity hourglass) {
		((TileAnimatedTorch) world.getBlockEntity(pos)).toggle();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TileAnimatedTorch) world.getBlockEntity(pos)).onWanded();
		return true;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void renderHUD(MatrixStack ms, MinecraftClient mc, World world, BlockPos pos) {
		((TileAnimatedTorch) world.getBlockEntity(pos)).renderHUD(ms, mc);
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getStrongRedstonePower(BlockState blockState, BlockView blockAccess, BlockPos pos, Direction side) {
		return getWeakRedstonePower(blockState, blockAccess, pos, side);
	}

	@Override
	public int getWeakRedstonePower(BlockState blockState, BlockView blockAccess, BlockPos pos, Direction side) {
		TileAnimatedTorch tile = (TileAnimatedTorch) blockAccess.getBlockEntity(pos);

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
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileAnimatedTorch();
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		// TE is already gone so best we can do is just notify everyone
		world.updateNeighbors(pos, this);
		super.onBroken(world, pos, state);
	}

}
