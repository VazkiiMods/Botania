/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

public class BlockPool extends BlockModWaterloggable implements ITileEntityProvider, IWandHUD, IWandable {
	private static final VoxelShape SLAB = makeCuboidShape(0, 0, 0, 16, 8, 16);
	private static final VoxelShape CUTOUT = makeCuboidShape(1, 1, 1, 15, 8, 15);
	private static final VoxelShape REAL_SHAPE = VoxelShapes.combineAndSimplify(SLAB, CUTOUT, IBooleanFunction.ONLY_FIRST);

	public enum Variant {
		DEFAULT,
		CREATIVE,
		DILUTED,
		FABULOUS
	}

	public final Variant variant;

	public BlockPool(Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return REAL_SHAPE;
	}

	@Override
	public List<ItemStack> getDrops(@Nonnull BlockState state, LootContext.Builder builder) {
		if (builder.get(LootParameters.BLOCK_ENTITY) instanceof TilePool
				&& ((TilePool) builder.get(LootParameters.BLOCK_ENTITY)).fragile) {
			return Collections.emptyList();
		} else {
			return super.getDrops(state, builder);
		}
	}

	@Nonnull
	@Override
	public ActionResultType onBlockActivated(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult hit) {
		TileEntity te = world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		if (stack.getItem() instanceof DyeItem && te instanceof TilePool) {
			DyeColor color = ((DyeItem) stack.getItem()).getDyeColor();
			if (color != ((TilePool) te).getColor()) {
				((TilePool) te).setColor(color);
				stack.shrink(1);
				return ActionResultType.SUCCESS;
			}
		}
		return super.onBlockActivated(state, world, pos, player, hand, hit);
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TilePool();
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof ItemEntity) {
			TilePool tile = (TilePool) world.getTileEntity(pos);
			tile.collideEntityItem((ItemEntity) entity);
		}
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		if (variant == Variant.FABULOUS) {
			return BlockRenderType.ENTITYBLOCK_ANIMATED;
		} else {
			return BlockRenderType.MODEL;
		}
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		TilePool pool = (TilePool) world.getTileEntity(pos);
		return TilePool.calculateComparatorLevel(pool.getCurrentMana(), pool.manaCap);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc, World world, BlockPos pos) {
		((TilePool) world.getTileEntity(pos)).renderHUD(ms, mc);
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TilePool) world.getTileEntity(pos)).onWanded(player);
		return true;
	}
}
