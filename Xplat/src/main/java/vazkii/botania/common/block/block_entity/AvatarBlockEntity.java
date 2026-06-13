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
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.UnknownNullability;

import vazkii.botania.api.block.Avatar;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.block.AvatarBlock;

public class AvatarBlockEntity extends SimpleInventoryBlockEntity implements Avatar, ManaReceiver {
	private static final int MAX_MANA = 6400;

	private static final String TAG_MANA = "mana";

	private int mana;

	public AvatarBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.AVATAR, pos, state, true);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, AvatarBlockEntity self) {
		ItemStack stack = self.getItemHandler().getItem(0);
		if (!stack.isEmpty()) {
			var wieldable = AvatarWieldable.LOOKUP.find(stack, self);
			if (wieldable != null) {
				wieldable.onAvatarUpdate((ServerLevel) level, pos, self);
			}
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt(TAG_MANA, mana);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		mana = tag.getInt(TAG_MANA);
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(1) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		};
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (level != null && !level.isClientSide()) {
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
		}
	}

	@Override
	public boolean isFull() {
		return mana >= MAX_MANA;
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.min(3 * MAX_MANA, this.mana + mana);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return !getItemHandler().getItem(0).isEmpty();
	}

	@Override
	@UnknownNullability
	public Level getManaReceiverLevel() {
		return getLevel();
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return getBlockPos();
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public SlotAccess getHeldItemSlot() {
		return SlotAccess.forContainer(getItemHandler(), 0);
	}

	@Override
	public Direction getAvatarFacing() {
		return getBlockState().getValue(AvatarBlock.FACING);
	}

	@Override
	public boolean isEnabled() {
		return !getBlockState().getValue(AvatarBlock.POWERED);
	}

	@Override
	public AvatarBlockEntity getSelf() {
		return this;
	}
}
