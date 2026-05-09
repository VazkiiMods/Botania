/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import it.unimi.dsi.fastutil.ints.IntList;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.saveddata.SavedData;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.common.helper.ForcePushHelper;
import vazkii.botania.common.item.lens.ForceLens;
import vazkii.botania.network.EffectType;
import vazkii.botania.network.clientbound.BotaniaEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;

public class ForceRelayBlock extends BotaniaBlock {

	public final Map<UUID, GlobalPos> activeBindingAttempts = new HashMap<>();

	public ForceRelayBlock(Properties builder) {
		super(builder.pushReaction(PushReaction.PUSH_ONLY));
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!world.isClientSide) {
			var data = WorldData.get(world);

			Direction movementContextDirection = ForcePushHelper.getMovementContextDirection();
			if (isMoving && (movementContextDirection != null || newState.is(Blocks.MOVING_PISTON))) {
				var pistonDirection = movementContextDirection != null
						? movementContextDirection
						: newState.getValue(MovingPistonBlock.FACING);
				// if being moved as part of a retracting sticky piston's block structure, reverse movement direction
				var moveDirection = ForcePushHelper.isExtendingMovementContext() ? pistonDirection : pistonDirection.getOpposite();

				var destPos = data.mapping.get(pos);
				if (destPos != null) {
					BlockPos newSrcPos = pos.relative(moveDirection);

					{
						// Move source side of our binding along
						data.mapping.remove(pos);
						data.mapping.put(newSrcPos, destPos);
						data.setDirty();
					}

					if (!newState.is(Blocks.MOVING_PISTON) || newState.getValue(MovingPistonBlock.TYPE) == PistonType.DEFAULT) {
						// Move the actual bound blocks
						if (ForceLens.moveBlocks(world, destPos.relative(moveDirection.getOpposite()), moveDirection, pos)) {
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

	public static Wandable createWandable(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be, Direction side) {
		return new ForceRelayWandable(level, pos);
	}

	public record ForceRelayWandable(Level level, BlockPos pos) implements Wandable {
		@Override
		public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
			if (player == null || level.isClientSide) {
				return false;
			}

			var data = WorldData.get(level);
			if (XplatAbstractions.INSTANCE.isDevEnvironment()) {
				BotaniaAPI.LOGGER.info("PistonRelay pairs");
				for (var e : data.mapping.entrySet()) {
					BotaniaAPI.LOGGER.info("{} -> {}", e.getKey(), e.getValue());
				}
			}
			BlockPos dest = data.mapping.get(pos);
			if (dest != null) {
				XplatAbstractions.INSTANCE.sendToNear(level, pos, new BotaniaEffectPacket(EffectType.PARTICLE_BEAM,
						pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
						IntList.of(dest.getX(), dest.getY(), dest.getZ())));
			}

			return true;
		}
	}

	public static WandBindable createWandBindable(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be, Direction side) {
		return new ForceRelayBindable(level, pos);
	}

	public record ForceRelayBindable(Level level, BlockPos sourcePos) implements WandBindable {
		@Override
		public boolean bindTo(Player player, ItemStack wand, BlockPos targetPos, Direction side) {
			if (!level.isClientSide) {
				addBinding(level, sourcePos, targetPos);

				XplatAbstractions.INSTANCE.sendToNear(level, targetPos,
						new BotaniaEffectPacket(EffectType.PARTICLE_BEAM,
								sourcePos.getX() + 0.5, sourcePos.getY() + 0.5, sourcePos.getZ() + 0.5,
								IntList.of(targetPos.getX(), targetPos.getY(), targetPos.getZ())));
			}
			level.playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.ding, SoundSource.PLAYERS, 1F, 1F);
			return true;
		}

	}

	public static void addBinding(Level level, BlockPos sourcePos, BlockPos targetPos) {
		if (level.isClientSide) {
			BotaniaAPI.LOGGER.warn("Tried to bind Force Relay on client: {}/{}", sourcePos, targetPos);
			return;
		}
		WorldData data = WorldData.get(level);
		data.mapping.put(sourcePos, targetPos.immutable());
		data.setDirty();
	}

	public static class WorldData extends SavedData {

		private static final String ID = "PistonRelayPairs";
		public static final Factory<WorldData> FACTORY = new Factory<>(WorldData::new, WorldData::new, DataFixTypes.LEVEL);
		public final Map<BlockPos, BlockPos> mapping = new HashMap<>();

		public WorldData() {
			// initialize with empty data
		}

		public WorldData(CompoundTag cmp, HolderLookup.Provider registries) {
			ListTag list = cmp.getList("list", Tag.TAG_INT_ARRAY);
			for (int i = 0; i < list.size(); i += 2) {
				Tag from = list.get(i);
				Tag to = list.get(i + 1);
				BlockPos fromPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, from).result().orElseThrow().getFirst();
				BlockPos toPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, to).result().orElseThrow().getFirst();

				mapping.put(fromPos, toPos);
			}
		}

		@Override
		public CompoundTag save(CompoundTag cmp, HolderLookup.Provider registries) {
			ListTag list = new ListTag();
			for (Map.Entry<BlockPos, BlockPos> e : mapping.entrySet()) {
				Tag from = BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, e.getKey()).result().orElseThrow();
				Tag to = BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue()).result().orElseThrow();
				list.add(from);
				list.add(to);
			}
			cmp.put("list", list);
			return cmp;
		}

		public static WorldData get(Level world) {
			return ((ServerLevel) world).getDataStorage().computeIfAbsent(FACTORY, ID);
		}
	}
}
