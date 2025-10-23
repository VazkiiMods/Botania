/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.PhantomInkableBlock;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.annotations.SoftImplement;
import vazkii.botania.common.block.PlatformBlock;

import java.util.Optional;

public class PlatformBlockEntity extends BotaniaBlockEntity implements Wandable, PhantomInkableBlock {
	private static final String TAG_CAMO = "camo";

	@Nullable
	private BlockState camoState;

	public PlatformBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.PLATFORM, pos, state);
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
		if (player != null) {
			if (getBlockState().getBlock() instanceof PlatformBlock platform && platform.requireCreativeInteractions()
					&& !player.isCreative()) {
				return false;
			}

			if (getCamoState() == null || player.isShiftKeyDown()) {
				swapSelfAndPass(this, true);
			} else {
				swapSurroudings(this, false);
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean onPhantomInked(@Nullable Player player, ItemStack stack, Direction side) {
		if (camoState == Blocks.BARRIER.defaultBlockState()) {
			return false;
		}
		if (!level.isClientSide) {
			if (player == null || !player.hasInfiniteMaterials()) {
				stack.shrink(1);
			}
			setCamoState(Blocks.BARRIER.defaultBlockState());
			level.gameEvent(null, GameEvent.BLOCK_CHANGE, getBlockPos());
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
		return true;
	}

	@Nullable
	public BlockState getCamoState() {
		return camoState;
	}

	public void setCamoState(@Nullable BlockState state) {
		this.camoState = state;

		if (level != null) {
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
			if (!level.isClientSide) {
				level.blockUpdated(worldPosition, getBlockState().getBlock());
				setChanged();
			}
		}
	}

	private void swapSelfAndPass(PlatformBlockEntity tile, boolean empty) {
		swap(tile, empty);
		swapSurroudings(tile, empty);
	}

	private void swapSurroudings(PlatformBlockEntity tile, boolean empty) {
		Block variant = getBlockState().getBlock();
		for (Direction dir : Direction.values()) {
			BlockPos pos = tile.getBlockPos().relative(dir);
			Optional<PlatformBlockEntity> tileAt = level.getBlockEntity(pos, BotaniaBlockEntities.PLATFORM);
			if (tileAt.isPresent() && tileAt.get().getBlockState().getBlock() == variant
					&& (tileAt.get().getCamoState() != null) == empty) {
				swapSelfAndPass(tileAt.get(), empty);
			}
		}
	}

	private void swap(PlatformBlockEntity tile, boolean empty) {
		tile.setCamoState(empty ? null : getCamoState());
	}

	@Override
	public void writePacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		if (getCamoState() != null) {
			cmp.put(TAG_CAMO, NbtUtils.writeBlockState(getCamoState()));
		}
	}

	@Override
	public void readPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		HolderGetter<Block> holderGetter = this.level != null ? this.level.holderLookup(Registries.BLOCK) : BuiltInRegistries.BLOCK.asLookup();
		BlockState state = NbtUtils.readBlockState(holderGetter, cmp.getCompound(TAG_CAMO));
		if (state.isAir()) {
			state = null;
		}
		setCamoState(state);
		if (level != null && level.isClientSide) {
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
		}
	}

	@SoftImplement("RenderDataBlockEntity")
	public Object getRenderData() {
		return new PlatformData(this);
	}

	public record PlatformData(BlockPos pos, @Nullable BlockState state) {
		public PlatformData(PlatformBlockEntity tile) {
			this(tile.getBlockPos().immutable(), tile.camoState);
		}
	}
}
