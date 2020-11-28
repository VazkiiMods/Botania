/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.math.Direction;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.ModStats;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class TileTinyPotato extends TileExposedSimpleInventory implements Tickable, Nameable {
	private static final String TAG_NAME = "name";
	private static final int JUMP_EVENT = 0;

	public int jumpTicks = 0;
	public Text name = new LiteralText("");
	private int nextDoIt = 0;

	public TileTinyPotato() {
		super(ModTiles.TINY_POTATO);
	}

	public void interact(PlayerEntity player, Hand hand, ItemStack stack, Direction side) {
		int index = side.getId();
		if (index >= 0) {
			ItemStack stackAt = getItemHandler().getStack(index);
			if (!stackAt.isEmpty() && stack.isEmpty()) {
				player.setStackInHand(hand, stackAt);
				getItemHandler().setStack(index, ItemStack.EMPTY);
			} else if (!stack.isEmpty()) {
				ItemStack copy = stack.split(1);

				if (stack.isEmpty()) {
					player.setStackInHand(hand, stackAt);
				} else if (!stackAt.isEmpty()) {
					player.inventory.offerOrDrop(player.world, stackAt);
				}

				getItemHandler().setStack(index, copy);
			}
		}

		if (!world.isClient) {
			jump();

			if (name.getString().toLowerCase().trim().endsWith("shia labeouf") && nextDoIt == 0) {
				nextDoIt = 40;
				world.playSound(null, pos, ModSounds.doit, SoundCategory.BLOCKS, 1F, 1F);
			}

			for (int i = 0; i < inventorySize(); i++) {
				ItemStack stackAt = getItemHandler().getStack(i);
				if (!stackAt.isEmpty() && stackAt.getItem() == ModBlocks.tinyPotato.asItem()) {
					player.sendSystemMessage(new LiteralText("Don't talk to me or my son ever again."), Util.NIL_UUID);
					return;
				}
			}

			player.incrementStat(ModStats.TINY_POTATOES_PETTED);
			PlayerHelper.grantCriterion((ServerPlayerEntity) player, prefix("main/tiny_potato_pet"), "code_triggered");
		}
	}

	private void jump() {
		if (jumpTicks == 0) {
			world.addSyncedBlockEvent(getPos(), getCachedState().getBlock(), JUMP_EVENT, 20);
		}
	}

	@Override
	public boolean onSyncedBlockEvent(int id, int param) {
		if (id == JUMP_EVENT) {
			jumpTicks = param;
			return true;
		} else {
			return super.onSyncedBlockEvent(id, param);
		}
	}

	@Override
	public void tick() {
		if (jumpTicks > 0) {
			jumpTicks--;
		}

		if (!world.isClient) {
			if (world.random.nextInt(100) == 0) {
				jump();
			}
			if (nextDoIt > 0) {
				nextDoIt--;
			}
		}
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isClient) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp) {
		super.writePacketNBT(cmp);
		cmp.putString(TAG_NAME, Text.Serializer.toJson(name));
	}

	@Override
	public void readPacketNBT(CompoundTag cmp) {
		super.readPacketNBT(cmp);
		name = Text.Serializer.fromJson(cmp.getString(TAG_NAME));
	}

	@Override
	protected SimpleInventory createItemHandler() {
		return new SimpleInventory(6);
	}

	@Nonnull
	@Override
	public Text getName() {
		return new TranslatableText(ModBlocks.tinyPotato.getTranslationKey());
	}

	@Nullable
	@Override
	public Text getCustomName() {
		return name.getString().isEmpty() ? null : name;
	}

	@Nonnull
	@Override
	public Text getDisplayName() {
		return hasCustomName() ? getCustomName() : getName();
	}
}
