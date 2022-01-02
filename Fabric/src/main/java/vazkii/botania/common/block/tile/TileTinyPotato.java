/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Nameable;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.ModStats;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Locale;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class TileTinyPotato extends TileExposedSimpleInventory implements Nameable {
	private static final String TAG_NAME = "name";
	private static final int JUMP_EVENT = 0;

	public int jumpTicks = 0;
	public Component name = new TextComponent("");
	private int nextDoIt = 0;

	public TileTinyPotato(BlockPos pos, BlockState state) {
		super(ModTiles.TINY_POTATO, pos, state);
	}

	public void interact(Player player, InteractionHand hand, ItemStack stack, Direction side) {
		int index = side.get3DDataValue();
		if (index >= 0) {
			ItemStack stackAt = getItemHandler().getItem(index);
			if (!stackAt.isEmpty() && stack.isEmpty()) {
				player.setItemInHand(hand, stackAt);
				getItemHandler().setItem(index, ItemStack.EMPTY);
			} else if (!stack.isEmpty()) {
				ItemStack copy = stack.split(1);

				if (stack.isEmpty()) {
					player.setItemInHand(hand, stackAt);
				} else if (!stackAt.isEmpty()) {
					player.getInventory().placeItemBackInInventory(stackAt);
				}

				getItemHandler().setItem(index, copy);
			}
		}

		if (!level.isClientSide) {
			jump();

			if (name.getString().toLowerCase(Locale.ROOT).trim().endsWith("shia labeouf") && nextDoIt == 0) {
				nextDoIt = 40;
				level.playSound(null, worldPosition, ModSounds.doit, SoundSource.BLOCKS, 1F, 1F);
			}

			for (int i = 0; i < inventorySize(); i++) {
				ItemStack stackAt = getItemHandler().getItem(i);
				if (!stackAt.isEmpty() && stackAt.is(ModBlocks.tinyPotato.asItem())) {
					player.sendMessage(new TextComponent("Don't talk to me or my son ever again."), Util.NIL_UUID);
					return;
				}
			}

			player.awardStat(ModStats.TINY_POTATOES_PETTED);
			PlayerHelper.grantCriterion((ServerPlayer) player, prefix("main/tiny_potato_pet"), "code_triggered");
		}
	}

	private void jump() {
		if (jumpTicks == 0) {
			level.blockEvent(getBlockPos(), getBlockState().getBlock(), JUMP_EVENT, 20);
		}
	}

	@Override
	public boolean triggerEvent(int id, int param) {
		if (id == JUMP_EVENT) {
			jumpTicks = param;
			return true;
		} else {
			return super.triggerEvent(id, param);
		}
	}

	public static void commonTick(Level level, BlockPos pos, BlockState state, TileTinyPotato self) {
		if (self.jumpTicks > 0) {
			self.jumpTicks--;
		}

		if (!level.isClientSide) {
			if (level.random.nextInt(100) == 0) {
				self.jump();
			}
			if (self.nextDoIt > 0) {
				self.nextDoIt--;
			}
		}
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (level != null && !level.isClientSide) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		super.writePacketNBT(cmp);
		cmp.putString(TAG_NAME, Component.Serializer.toJson(name));
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		super.readPacketNBT(cmp);
		name = Component.Serializer.fromJson(cmp.getString(TAG_NAME));
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(6);
	}

	@Nonnull
	@Override
	public Component getName() {
		return new TranslatableComponent(ModBlocks.tinyPotato.getDescriptionId());
	}

	@Nullable
	@Override
	public Component getCustomName() {
		return name.getString().isEmpty() ? null : name;
	}

	@Nonnull
	@Override
	public Component getDisplayName() {
		return hasCustomName() ? getCustomName() : getName();
	}
}
