/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.ExtendedShapeContext;
import vazkii.botania.common.entity.EntityManaBurst;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

public class BlockPool extends BlockModWaterloggable implements EntityBlock, IWandHUD, IWandable {
	private static final VoxelShape REAL_SHAPE;
	private static final VoxelShape BURST_SHAPE;
	static {
		VoxelShape slab = box(0, 0, 0, 16, 8, 16);
		VoxelShape cutout = box(1, 1, 1, 15, 8, 15);
		VoxelShape cutoutBurst = box(1, 6, 1, 15, 8, 15);
		BURST_SHAPE = Shapes.join(slab, cutoutBurst, BooleanOp.ONLY_FIRST);
		REAL_SHAPE = Shapes.join(slab, cutout, BooleanOp.ONLY_FIRST);
	}

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
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return REAL_SHAPE;
	}

	@Override
	public List<ItemStack> getDrops(@Nonnull BlockState state, LootContext.Builder builder) {
		if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof TilePool
				&& ((TilePool) builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY)).fragile) {
			return Collections.emptyList();
		} else {
			return super.getDrops(state, builder);
		}
	}

	@Nonnull
	@Override
	public InteractionResult use(@Nonnull BlockState state, Level world, @Nonnull BlockPos pos, Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
		BlockEntity te = world.getBlockEntity(pos);
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof DyeItem && te instanceof TilePool) {
			DyeColor color = ((DyeItem) stack.getItem()).getDyeColor();
			if (color != ((TilePool) te).getColor()) {
				((TilePool) te).setColor(color);
				stack.shrink(1);
				return InteractionResult.SUCCESS;
			}
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (ExtendedShapeContext.getEntity(context) instanceof EntityManaBurst) {
			// Sometimes the pool's collision box is too thin for bursts shot straight up.
			return BURST_SHAPE;
		} else {
			return super.getCollisionShape(state, world, pos, context);
		}
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TilePool(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, ModTiles.POOL, level.isClientSide ? TilePool::clientTick : TilePool::serverTick);
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (entity instanceof ItemEntity) {
			TilePool tile = (TilePool) world.getBlockEntity(pos);
			tile.collideEntityItem((ItemEntity) entity);
		}
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		if (variant == Variant.FABULOUS) {
			return RenderShape.ENTITYBLOCK_ANIMATED;
		} else {
			return RenderShape.MODEL;
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		TilePool pool = (TilePool) world.getBlockEntity(pos);
		return TilePool.calculateComparatorLevel(pool.getCurrentMana(), pool.manaCap);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(PoseStack ms, Minecraft mc, Level world, BlockPos pos) {
		((TilePool) world.getBlockEntity(pos)).renderHUD(ms, mc);
	}

	@Override
	public boolean onUsedByWand(Player player, ItemStack stack, Level world, BlockPos pos, Direction side) {
		((TilePool) world.getBlockEntity(pos)).onWanded(player);
		return true;
	}
}
