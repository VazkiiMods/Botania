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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlockEnchanter extends BlockMod implements BlockEntityProvider, IWandable, IWandHUD {

	public BlockEnchanter(Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BotaniaStateProps.ENCHANTER_DIRECTION, Direction.Axis.X));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(BotaniaStateProps.ENCHANTER_DIRECTION);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileEnchanter();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		TileEnchanter enchanter = (TileEnchanter) world.getBlockEntity(pos);
		ItemStack stack = player.getStackInHand(hand);
		if (!stack.isEmpty() && stack.getItem() == ModItems.twigWand) {
			return ActionResult.PASS;
		}

		boolean stackEnchantable = !stack.isEmpty()
				&& stack.getItem() != Items.BOOK
				&& stack.isEnchantable()
				&& stack.getCount() == 1;

		if (enchanter.itemToEnchant.isEmpty()) {
			if (stackEnchantable) {
				enchanter.itemToEnchant = stack.copy();
				player.setStackInHand(hand, ItemStack.EMPTY);
				enchanter.sync();
			} else {
				return ActionResult.PASS;
			}
		} else if (enchanter.stage == TileEnchanter.State.IDLE) {
			player.inventory.offerOrDrop(player.world, enchanter.itemToEnchant.copy());
			enchanter.itemToEnchant = ItemStack.EMPTY;
			enchanter.sync();
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEnchanter enchanter = (TileEnchanter) world.getBlockEntity(pos);

			if (!enchanter.itemToEnchant.isEmpty()) {
				world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), enchanter.itemToEnchant));
			}

			super.onStateReplaced(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TileEnchanter) world.getBlockEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, MinecraftClient mc, World world, BlockPos pos) {
		((TileEnchanter) world.getBlockEntity(pos)).renderHUD(ms);
	}
}
