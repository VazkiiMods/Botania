/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.saveddata.SavedData;

import vazkii.botania.api.block.IWandable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.lens.LensPiston;
import vazkii.botania.common.network.PacketBotaniaEffect;

import javax.annotation.Nonnull;

import java.util.*;

public class BlockPistonRelay extends BlockMod implements IWandable {

	// Currently active binding attempts
	public final Map<UUID, GlobalPos> activeBindingAttempts = new HashMap<>();

	private final Set<GlobalPos> removeQueue = new HashSet<>();
	private final Set<GlobalPos> checkedCoords = new HashSet<>();
	private final Map<GlobalPos, Integer> coordsToCheck = new HashMap<>();

	public BlockPistonRelay(Properties builder) {
		super(builder);
		ServerTickEvents.END_SERVER_TICK.register(this::tickEnd);
	}

	@Override
	public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!world.isClientSide) {
			mapCoords(world.dimension(), pos, 2);
		}
	}

	private void mapCoords(ResourceKey<Level> type, BlockPos pos, int time) {
		coordsToCheck.put(GlobalPos.of(type, pos), time);
	}

	private void decrCoords(GlobalPos key) {
		int time = getTimeInCoords(key);

		if (time <= 0) {
			removeQueue.add(key);
		} else {
			coordsToCheck.merge(key, -1, Integer::sum);
		}
	}

	private int getTimeInCoords(GlobalPos key) {
		return coordsToCheck.getOrDefault(key, 0);
	}

	private BlockEntity getTeAt(GlobalPos key) {
		MinecraftServer server = Botania.currentServer;
		if (server != null) {
			Level world = server.getLevel(key.dimension());
			if (world != null) {
				return world.getBlockEntity(key.pos());
			}
		}
		return null;
	}

	@Override
	public boolean onUsedByWand(Player player, ItemStack stack, Level world, BlockPos pos, Direction side) {
		if (world.isClientSide) {
			return false;
		}

		if (player == null || player.isShiftKeyDown()) {
			popResource(world, pos, new ItemStack(this));
			world.destroyBlock(pos, false);
		} else {
			GlobalPos clicked = GlobalPos.of(world.dimension(), pos.immutable());
			if (ItemTwigWand.getBindMode(stack)) {
				activeBindingAttempts.put(player.getUUID(), clicked);
				world.playSound(null, pos, ModSounds.ding, SoundSource.BLOCKS, 0.5F, 1F);
			} else {
				BlockPos dest = WorldData.get(world).mapping.get(pos);
				if (dest != null) {
					PacketBotaniaEffect.sendNearby(world, pos, PacketBotaniaEffect.EffectType.PARTICLE_BEAM,
							pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
							dest.getX(), dest.getY(), dest.getZ());
				}
			}
		}

		return true;
	}

	public static class WorldData extends SavedData {

		private static final String ID = "PistonRelayPairs";
		public final Map<BlockPos, BlockPos> mapping = new HashMap<>();

		public WorldData(@Nonnull CompoundTag cmp) {
			ListTag list = cmp.getList("list", 11);
			for (int i = 0; i < list.size(); i += 2) {
				Tag from = list.get(i);
				Tag to = list.get(i + 1);
				BlockPos fromPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, from).result().get().getFirst();
				BlockPos toPos = BlockPos.CODEC.decode(NbtOps.INSTANCE, to).result().get().getFirst();

				mapping.put(fromPos, toPos);
			}
		}

		@Nonnull
		@Override
		public CompoundTag save(@Nonnull CompoundTag cmp) {
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

	public void tickEnd(MinecraftServer server) {
		for (GlobalPos s : coordsToCheck.keySet()) {
			ServerLevel world = server.getLevel(s.dimension());
			WorldData data = WorldData.get(world);

			decrCoords(s);
			if (checkedCoords.contains(s)) {
				continue;
			}

			BlockState state = world.getBlockState(s.pos());
			if (state.is(Blocks.MOVING_PISTON)) {
				boolean sticky = PistonType.STICKY == state.getValue(MovingPistonBlock.TYPE);
				Direction dir = ((PistonMovingBlockEntity) getTeAt(s)).getMovementDirection();

				if (getTimeInCoords(s) == 0) {
					BlockPos newPos;

					// Put the relay back, or drop it
					{
						int x = s.pos().getX(), y = s.pos().getY(), z = s.pos().getZ();
						BlockPos pos = s.pos();
						if (world.isEmptyBlock(pos.relative(dir))) {
							world.setBlockAndUpdate(pos.relative(dir), ModBlocks.pistonRelay.defaultBlockState());
						} else {
							ItemStack stack = new ItemStack(ModBlocks.pistonRelay);
							world.addFreshEntity(new ItemEntity(world, x + dir.getStepX(), y + dir.getStepY(), z + dir.getStepZ(), stack));
						}
						checkedCoords.add(s);
						newPos = pos.relative(dir);
					}

					// Move the linked block and update the mapping
					if (data.mapping.containsKey(s.pos())) {
						BlockPos destPos = data.mapping.get(s.pos());

						BlockState srcState = world.getBlockState(destPos);
						BlockEntity tile = world.getBlockEntity(destPos);

						if (!sticky && tile == null && srcState.getPistonPushReaction() == PushReaction.NORMAL && srcState.getDestroySpeed(world, destPos) != -1 && !srcState.isAir()) {
							Material destMat = world.getBlockState(destPos.relative(dir)).getMaterial();
							if (world.isEmptyBlock(destPos.relative(dir)) || destMat.isReplaceable()) {
								world.setBlockAndUpdate(destPos, Blocks.AIR.defaultBlockState());
								world.setBlockAndUpdate(destPos.relative(dir), LensPiston.unWaterlog(srcState));
								data.mapping.put(s.pos(), destPos.relative(dir));
							}
						}

						destPos = data.mapping.get(s.pos());
						data.mapping.remove(s.pos());
						data.mapping.put(newPos, destPos);
						data.setDirty();
					}
				}
			}
		}

		coordsToCheck.keySet().removeAll(removeQueue);
		checkedCoords.removeAll(removeQueue);
		removeQueue.clear();
	}
}
