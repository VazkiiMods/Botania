/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.entity.EntityManaBurst;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

public class BlockPool extends BlockModWaterloggable implements BlockEntityProvider, IWandHUD, IWandable {
	private static final VoxelShape SLAB = createCuboidShape(0, 0, 0, 16, 8, 16);
	private static final VoxelShape CUTOUT = createCuboidShape(1, 1, 1, 15, 8, 15);
	private static final VoxelShape REAL_SHAPE = VoxelShapes.combineAndSimplify(SLAB, CUTOUT, BooleanBiFunction.ONLY_FIRST);

	public enum Variant {
		DEFAULT,
		CREATIVE,
		DILUTED,
		FABULOUS
	}

	public final Variant variant;

	public BlockPool(Variant v, Settings builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return REAL_SHAPE;
	}

	@Override
	public List<ItemStack> getDroppedStacks(@Nonnull BlockState state, LootContext.Builder builder) {
		if (builder.getNullable(LootContextParameters.BLOCK_ENTITY) instanceof TilePool
				&& ((TilePool) builder.getNullable(LootContextParameters.BLOCK_ENTITY)).fragile) {
			return Collections.emptyList();
		} else {
			return super.getDroppedStacks(state, builder);
		}
	}

	@Nonnull
	@Override
	public ActionResult onUse(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockHitResult hit) {
		BlockEntity te = world.getBlockEntity(pos);
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() instanceof DyeItem && te instanceof TilePool) {
			DyeColor color = ((DyeItem) stack.getItem()).getColor();
			if (color != ((TilePool) te).getColor()) {
				((TilePool) te).setColor(color);
				stack.decrement(1);
				return ActionResult.SUCCESS;
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		if (context.getEntity() instanceof EntityManaBurst) {
			// Sometimes the pool's collision box is too thin for bursts shot straight up.
			return SLAB;
		} else {
			return super.getCollisionShape(state, world, pos, context);
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TilePool();
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof ItemEntity) {
			TilePool tile = (TilePool) world.getBlockEntity(pos);
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
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		TilePool pool = (TilePool) world.getBlockEntity(pos);
		return TilePool.calculateComparatorLevel(pool.getCurrentMana(), pool.manaCap);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, MinecraftClient mc, World world, BlockPos pos) {
		((TilePool) world.getBlockEntity(pos)).renderHUD(ms, mc);
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TilePool) world.getBlockEntity(pos)).onWanded(player);
		return true;
	}
}
