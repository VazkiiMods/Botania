/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.saveddata.SavedData;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.item.WandOfTheForestItem;
import vazkii.botania.common.item.lens.ForceLens;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.PacketBotaniaEffect;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.*;

public class ForceRelayBlock extends BotaniaBlock {

	public final Map<UUID, GlobalPos> activeBindingAttempts = new HashMap<>();

	public ForceRelayBlock(Properties builder) {
		super(builder);
	}

	@Override
	public PushReaction getPistonPushReaction(@NotNull BlockState blockState) {
		return PushReaction.PUSH_ONLY;
	}

	@Override
	public void onRemove(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
		if (!world.isClientSide) {
			var data = WorldData.get(world);

			if (isMoving && newState.is(Blocks.MOVING_PISTON)) {
				var moveDirection = newState.getValue(MovingPistonBlock.FACING);

				var destPos = data.mapping.get(pos);
				if (destPos != null) {
					BlockPos newSrcPos = pos.relative(moveDirection);

					{
						// Move source side of our binding along
						data.mapping.remove(pos);
						data.mapping.put(newSrcPos, destPos);
						data.setDirty();
					}

					if (newState.getValue(MovingPistonBlock.TYPE) == PistonType.DEFAULT) {
						// Move the actual bound blocks
						if (ForceLens.moveBlocks(world, destPos.relative(moveDirection.getOpposite()), moveDirection)) {
							// Move dest side of our binding
							data.mapping.put(newSrcPos, data.mapping.get(newSrcPos).relative(moveDirection));
						}
					}
				}
			} else {
				if (data.mapping.remove(pos) != null) {
					data.setDirty();
				}
			}
		}
	}

	public boolean onUsedByWand(Player player, ItemStack stack, Level world, BlockPos pos) {
		if (world.isClientSide) {
			return false;
		}

		if (player == null || player.isShiftKeyDown()) {
			world.destroyBlock(pos, true);
		} else {
			GlobalPos clicked = GlobalPos.of(world.dimension(), pos.immutable());
			if (WandOfTheForestItem.getBindMode(stack)) {
				activeBindingAttempts.put(player.getUUID(), clicked);
				world.playSound(null, pos, BotaniaSounds.ding, SoundSource.BLOCKS, 0.5F, 1F);
			} else {
				var data = WorldData.get(world);
				if (IXplatAbstractions.INSTANCE.isDevEnvironment()) {
					BotaniaAPI.LOGGER.info("PistonRelay pairs");
					for (var e : data.mapping.entrySet()) {
						BotaniaAPI.LOGGER.info("{} -> {}", e.getKey(), e.getValue());
					}
				}
				BlockPos dest = data.mapping.get(pos);
				if (dest != null) {
					IXplatAbstractions.INSTANCE.sendToNear(world, pos, new PacketBotaniaEffect(EffectType.PARTICLE_BEAM,
							pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
							dest.getX(), dest.getY(), dest.getZ()));
				}
			}
		}

		return true;
	}

	public static class WorldData extends SavedData {

		private static final String ID = "PistonRelayPairs";
		public final Map<BlockPos, BlockPos> mapping = new HashMap<>();

		public WorldData(@NotNull CompoundTag cmp) {
			ListTag list = cmp.getList("list", Tag.TAG_INT_ARRAY);
			for (int i = 0; i < list.size(); i += 2) {
				Tag from = list.get(i);
				Tag to = list.get(i + 1);
				BlockPos fromPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, from).result().get().getFirst();
				BlockPos toPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, to).result().get().getFirst();

				mapping.put(fromPos, toPos);
			}
		}

		@NotNull
		@Override
		public CompoundTag save(@NotNull CompoundTag cmp) {
			ListTag list = new ListTag();
			for (Map.Entry<BlockPos, BlockPos> e : mapping.entrySet()) {
				Tag from = BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, e.getKey()).result().get();
				Tag to = BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue()).result().get();
				list.add(from);
				list.add(to);
			}
			cmp.put("list", list);
			return cmp;
		}

		public static WorldData get(Level world) {
			WorldData data = ((ServerLevel) world).getDataStorage().get(WorldData::new, ID);
			if (data == null) {
				data = new WorldData(new CompoundTag());
				data.setDirty();
				((ServerLevel) world).getDataStorage().set(ID, data);
			}
			return data;
		}
	}
}
