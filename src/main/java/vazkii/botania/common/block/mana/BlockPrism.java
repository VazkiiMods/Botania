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
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.IManaCollisionGhost;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileSimpleInventory;
import vazkii.botania.common.block.tile.mana.TilePrism;

import javax.annotation.Nonnull;

public class BlockPrism extends BlockModWaterloggable implements BlockEntityProvider, IManaTrigger, IManaCollisionGhost, IWandHUD {
	private static final VoxelShape SHAPE = createCuboidShape(4, 0, 4, 12, 16, 12);

	public BlockPrism(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState()
				.with(Properties.POWERED, false)
				.with(BotaniaStateProps.HAS_LENS, false));
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED, BotaniaStateProps.HAS_LENS);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (!(tile instanceof TilePrism)) {
			return ActionResult.PASS;
		}

		TilePrism prism = (TilePrism) tile;
		ItemStack lens = prism.getItemHandler().getStack(0);
		ItemStack heldItem = player.getStackInHand(hand);
		boolean isHeldItemLens = !heldItem.isEmpty() && heldItem.getItem() instanceof ILens;

		if (lens.isEmpty() && isHeldItemLens) {
			if (!player.abilities.creativeMode) {
				player.setStackInHand(hand, ItemStack.EMPTY);
			}

			prism.getItemHandler().setStack(0, heldItem.copy());
		} else if (!lens.isEmpty()) {
			player.inventory.offerOrDrop(player.world, lens);
			prism.getItemHandler().setStack(0, ItemStack.EMPTY);
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getReceivedRedstonePower(pos) > 0 || world.getReceivedRedstonePower(pos.up()) > 0;
		boolean powered = state.get(Properties.POWERED);

		if (!world.isClient) {
			if (power && !powered) {
				world.setBlockState(pos, state.with(Properties.POWERED, true));
			} else if (!power && powered) {
				world.setBlockState(pos, state.with(Properties.POWERED, false));
			}
		}
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof TileSimpleInventory) {
				ItemScatterer.spawn(world, pos, ((TileSimpleInventory) be).getItemHandler());
			}
			super.onStateReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TilePrism();
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, BlockPos pos) {
		BlockEntity tile = world.getBlockEntity(pos);
		if (tile instanceof TilePrism) {
			((TilePrism) tile).onBurstCollision(burst);
		}
	}

	@Override
	public boolean isGhost(BlockState state, World world, BlockPos pos) {
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, MinecraftClient mc, World world, BlockPos pos) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TilePrism) {
			ItemStack lens = ((TilePrism) te).getStack(0);
			if (!lens.isEmpty()) {
				Text lensName = lens.getName();
				int width = 16 + mc.textRenderer.getWidth(lensName) / 2;
				int x = mc.getWindow().getScaledWidth() / 2 - width;
				int y = mc.getWindow().getScaledHeight() / 2;

				mc.textRenderer.drawWithShadow(ms, lensName, x + 20, y + 5, -1);
				mc.getItemRenderer().renderInGuiWithOverrides(lens, x, y);
			}
		}
	}
}
