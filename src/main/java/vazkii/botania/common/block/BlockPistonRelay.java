/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.block.enums.PistonType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.lens.LensPiston;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;

import java.util.*;

public class BlockPistonRelay extends BlockMod implements IWandable {

	// Currently active binding attempts
	public final Map<UUID, GlobalPos> activeBindingAttempts = new HashMap<>();

	private final Set<GlobalPos> removeQueue = new HashSet<>();
	private final Set<GlobalPos> checkedCoords = new HashSet<>();
	private final Map<GlobalPos, Integer> coordsToCheck = new HashMap<>();

	public BlockPistonRelay(Settings builder) {
		super(builder);
		MinecraftForge.EVENT_BUS.addListener(this::tickEnd);
	}

	@Override
	public void onStateReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!world.isClient) {
			mapCoords(world.getRegistryKey(), pos, 2);
		}
	}

	private void mapCoords(RegistryKey<World> type, BlockPos pos, int time) {
		coordsToCheck.put(GlobalPos.create(type, pos), time);
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
		Object game = FabricLoader.getInstance().getGameInstance();
		if (game instanceof MinecraftServer) {
			MinecraftServer server = (MinecraftServer) game;
			World world = server.getWorld(key.getDimension());
			if (world != null) {
				return world.getBlockEntity(key.getPos());
			}
		}
		return null;
	}

	private BlockState getStateAt(GlobalPos key) {
		Object game = FabricLoader.getInstance().getGameInstance();
		if (game instanceof MinecraftServer) {
			MinecraftServer server = (MinecraftServer) game;
			World world = server.getWorld(key.getDimension());
			if (world != null) {
				return world.getBlockState(key.getPos());
			}
		}
		return Blocks.AIR.getDefaultState();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		if (world.isClient) {
			return false;
		}

		if (player == null || player.isSneaking()) {
			dropStack(world, pos, new ItemStack(this));
			world.breakBlock(pos, false);
		} else {
			GlobalPos clicked = GlobalPos.create(world.getRegistryKey(), pos.toImmutable());
			if (ItemTwigWand.getBindMode(stack)) {
				activeBindingAttempts.put(player.getUuid(), clicked);
				world.playSound(null, pos, ModSounds.ding, SoundCategory.BLOCKS, 0.5F, 1F);
			} else {
				BlockPos dest = WorldData.get(world).mapping.get(pos);
				if (dest != null) {
					PacketHandler.sendToNearby(world, pos,
							new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.PARTICLE_BEAM,
									pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
									dest.getX(), dest.getY(), dest.getZ()));
				}
			}
		}

		return true;
	}

	public static class WorldData extends PersistentState {

		private static final String ID = "PistonRelayPairs";
		public final Map<BlockPos, BlockPos> mapping = new HashMap<>();

		public WorldData() {
			super(ID);
		}

		@Override
		public void fromTag(@Nonnull CompoundTag cmp) {
			mapping.clear();

			ListTag list = cmp.getList("list", 11);
			for (int i = 0; i < list.size(); i += 2) {
				Tag from = list.get(i);
				Tag to = list.get(i + 1);
				BlockPos fromPos = BlockPos.field_25064.decode(NbtOps.INSTANCE, from).result().get().getFirst();
				BlockPos toPos = BlockPos.field_25064.decode(NbtOps.INSTANCE, to).result().get().getFirst();

				mapping.put(fromPos, toPos);
			}
		}

		@Nonnull
		@Override
		public CompoundTag toTag(@Nonnull CompoundTag cmp) {
			ListTag list = new ListTag();
			for (Map.Entry<BlockPos, BlockPos> e : mapping.entrySet()) {
				Tag from = BlockPos.field_25064.encodeStart(NbtOps.INSTANCE, e.getKey()).result().get();
				Tag to = BlockPos.field_25064.encodeStart(NbtOps.INSTANCE, e.getValue()).result().get();
				list.add(from);
				list.add(to);
			}
			cmp.put("list", list);
			return cmp;
		}

		public static WorldData get(World world) {
			WorldData data = ((ServerWorld) world).getPersistentStateManager().get(WorldData::new, ID);
			if (data == null) {
				data = new WorldData();
				data.markDirty();
				((ServerWorld) world).getPersistentStateManager().set(data);
			}
			return data;
		}
	}

	public void tickEnd(TickEvent.ServerTickEvent event) {
		if (event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END) {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			for (GlobalPos s : coordsToCheck.keySet()) {
				ServerWorld world = server.getWorld(s.getDimension());
				WorldData data = WorldData.get(world);

				decrCoords(s);
				if (checkedCoords.contains(s)) {
					continue;
				}

				BlockState state = getStateAt(s);
				if (state.getBlock() == Blocks.MOVING_PISTON) {
					boolean sticky = PistonType.STICKY == state.get(PistonExtensionBlock.TYPE);
					Direction dir = ((PistonBlockEntity) getTeAt(s)).getMovementDirection();

					if (getTimeInCoords(s) == 0) {
						BlockPos newPos;

						// Put the relay back, or drop it
						{
							int x = s.getPos().getX(), y = s.getPos().getY(), z = s.getPos().getZ();
							BlockPos pos = s.getPos();
							if (world.isAir(pos.offset(dir))) {
								world.setBlockState(pos.offset(dir), ModBlocks.pistonRelay.getDefaultState());
							} else {
								ItemStack stack = new ItemStack(ModBlocks.pistonRelay);
								world.spawnEntity(new ItemEntity(world, x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ(), stack));
							}
							checkedCoords.add(s);
							newPos = pos.offset(dir);
						}

						// Move the linked block and update the mapping
						if (data.mapping.containsKey(s.getPos())) {
							BlockPos destPos = data.mapping.get(s.getPos());

							BlockState srcState = world.getBlockState(destPos);
							BlockEntity tile = world.getBlockEntity(destPos);

							if (!sticky && tile == null && srcState.getPistonBehavior() == PistonBehavior.NORMAL && srcState.getHardness(world, destPos) != -1 && !srcState.isAir()) {
								Material destMat = world.getBlockState(destPos.offset(dir)).getMaterial();
								if (world.isAir(destPos.offset(dir)) || destMat.isReplaceable()) {
									world.setBlockState(destPos, Blocks.AIR.getDefaultState());
									world.setBlockState(destPos.offset(dir), LensPiston.unWaterlog(srcState));
									data.mapping.put(s.getPos(), destPos.offset(dir));
								}
							}

							destPos = data.mapping.get(s.getPos());
							data.mapping.remove(s.getPos());
							data.mapping.put(newPos, destPos);
							data.markDirty();
						}
					}
				}
			}
		}

		coordsToCheck.keySet().removeAll(removeQueue);
		checkedCoords.removeAll(removeQueue);
		removeQueue.clear();
	}
}
