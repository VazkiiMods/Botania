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
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.core.ExtendedShapeContext;
import vazkii.botania.common.entity.EntityManaBurst;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockPrism extends BlockModWaterloggable implements EntityBlock, IManaTrigger, IManaCollisionGhost, IWandHUD {
	private static final VoxelShape SHAPE = box(4, 0, 4, 12, 16, 12);

	public BlockPrism(Properties builder) {
		super(builder);
		registerDefaultState(defaultBlockState()
				.setValue(BlockStateProperties.POWERED, false)
				.setValue(BotaniaStateProps.HAS_LENS, false));
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		if (ExtendedShapeContext.getEntity(context) instanceof EntityManaBurst) {
			// Expose the shape so bursts can actually collide with us
			// they will still go through the prism via IManaCollisionGhost
			return SHAPE;
		} else {
			return super.getCollisionShape(state, world, pos, context);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		if (state.getValue(BlockStateProperties.POWERED)) {
			redstoneParticlesInShape(state, world, pos, rand);
		}
	}

	public static void redstoneParticlesInShape(BlockState state, Level world, BlockPos pos, Random rand) {
		if (rand.nextBoolean()) {
			AABB localBox = state.getShape(world, pos).bounds();
			double x = pos.getX() + localBox.minX + rand.nextDouble() * (localBox.maxX - localBox.minX);
			double y = pos.getY() + localBox.minY + rand.nextDouble() * (localBox.maxY - localBox.minY);
			double z = pos.getZ() + localBox.minZ + rand.nextDouble() * (localBox.maxZ - localBox.minZ);
			world.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0, 0, 0);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.POWERED, BotaniaStateProps.HAS_LENS);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof TilePrism)) {
			return InteractionResult.PASS;
		}

		TilePrism prism = (TilePrism) tile;
		ItemStack lens = prism.getItemHandler().getItem(0);
		ItemStack heldItem = player.getItemInHand(hand);
		boolean isHeldItemLens = !heldItem.isEmpty() && heldItem.getItem() instanceof ILens;

		if (lens.isEmpty() && isHeldItemLens) {
			if (!player.getAbilities().instabuild) {
				player.setItemInHand(hand, ItemStack.EMPTY);
			}

			prism.getItemHandler().setItem(0, heldItem.copy());
		} else if (!lens.isEmpty()) {
			player.getInventory().placeItemBackInInventory(player.level, lens);
			prism.getItemHandler().setItem(0, ItemStack.EMPTY);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		boolean power = world.getBestNeighborSignal(pos) > 0 || world.getBestNeighborSignal(pos.above()) > 0;
		return this.defaultBlockState().setValue(BlockStateProperties.POWERED, power);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getBestNeighborSignal(pos) > 0 || world.getBestNeighborSignal(pos.above()) > 0;
		boolean powered = state.getValue(BlockStateProperties.POWERED);

		if (!world.isClientSide) {
			if (power && !powered) {
				world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, true));
			} else if (!power && powered) {
				world.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, false));
			}
		}
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory) {
				Containers.dropContents(world, pos, ((TileSimpleInventory) be).getItemHandler());
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new TilePrism(pos, state);
	}

	@Override
	public void onBurstCollision(IManaBurst burst, Level world, BlockPos pos) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TilePrism) {
			((TilePrism) tile).onBurstCollision(burst);
		}
	}

	@Override
	public boolean isGhost(BlockState state, Level world, BlockPos pos) {
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(PoseStack ms, Minecraft mc, Level world, BlockPos pos) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TilePrism) {
			ItemStack lens = ((TilePrism) te).getItem(0);
			if (!lens.isEmpty()) {
				Component lensName = lens.getHoverName();
				int width = 16 + mc.font.width(lensName) / 2;
				int x = mc.getWindow().getGuiScaledWidth() / 2 - width;
				int y = mc.getWindow().getGuiScaledHeight() / 2;

				mc.font.drawShadow(ms, lensName, x + 20, y + 5, -1);
				mc.getItemRenderer().renderAndDecorateItem(lens, x, y);
			}
		}
	}
}
