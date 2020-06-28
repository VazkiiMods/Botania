/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MovingPistonBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.PistonType;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

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

	public BlockPistonRelay(Properties builder) {
		super(builder);
		MinecraftForge.EVENT_BUS.addListener(this::tickEnd);
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if (!world.isRemote) {
			mapCoords(world.func_234923_W_(), pos, 2);
		}
	}

	private void mapCoords(RegistryKey<World> type, BlockPos pos, int time) {
		coordsToCheck.put(GlobalPos.func_239648_a_(type, pos), time);
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

	private TileEntity getTeAt(GlobalPos key) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server == null) {
			return null;
		}
		return server.getWorld(key.func_239646_a_()).getTileEntity(key.getPos());
	}

	private BlockState getStateAt(GlobalPos key) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server == null) {
			return Blocks.AIR.getDefaultState();
		}
		return server.getWorld(key.func_239646_a_()).getBlockState(key.getPos());
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		if (world.isRemote) {
			return false;
		}

		if (player == null || player.isSneaking()) {
			spawnAsEntity(world, pos, new ItemStack(this));
			world.destroyBlock(pos, false);
		} else {
			GlobalPos clicked = GlobalPos.func_239648_a_(world.func_234923_W_(), pos.toImmutable());
			if (ItemTwigWand.getBindMode(stack)) {
				activeBindingAttempts.put(player.getUniqueID(), clicked);
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

	public static class WorldData extends WorldSavedData {

		private static final String ID = "PistonRelayPairs";
		public final Map<BlockPos, BlockPos> mapping = new HashMap<>();

		public WorldData() {
			super(ID);
		}

		@Override
		public void read(@Nonnull CompoundNBT cmp) {
			mapping.clear();

			ListNBT list = cmp.getList("list", Constants.NBT.TAG_INT_ARRAY);
			for (int i = 0; i < list.size(); i += 2) {
				INBT from = list.get(i);
				INBT to = list.get(i + 1);
				BlockPos fromPos = BlockPos.field_239578_a_.decode(NBTDynamicOps.INSTANCE, from).result().get().getFirst();
				BlockPos toPos = BlockPos.field_239578_a_.decode(NBTDynamicOps.INSTANCE, to).result().get().getFirst();

				mapping.put(fromPos, toPos);
			}
		}

		@Nonnull
		@Override
		public CompoundNBT write(@Nonnull CompoundNBT cmp) {
			ListNBT list = new ListNBT();
			for (Map.Entry<BlockPos, BlockPos> e : mapping.entrySet()) {
				INBT from = BlockPos.field_239578_a_.encodeStart(NBTDynamicOps.INSTANCE, e.getKey()).result().get();
				INBT to = BlockPos.field_239578_a_.encodeStart(NBTDynamicOps.INSTANCE, e.getValue()).result().get();
				list.add(from);
				list.add(to);
			}
			cmp.put("list", list);
			return cmp;
		}

		public static WorldData get(World world) {
			WorldData data = ((ServerWorld) world).getSavedData().get(WorldData::new, ID);
			if (data == null) {
				data = new WorldData();
				data.markDirty();
				((ServerWorld) world).getSavedData().set(data);
			}
			return data;
		}
	}

	public void tickEnd(TickEvent.ServerTickEvent event) {
		if (event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END) {
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			for (GlobalPos s : coordsToCheck.keySet()) {
				ServerWorld world = server.getWorld(s.func_239646_a_());
				WorldData data = WorldData.get(world);

				decrCoords(s);
				if (checkedCoords.contains(s)) {
					continue;
				}

				BlockState state = getStateAt(s);
				if (state.getBlock() == Blocks.MOVING_PISTON) {
					boolean sticky = PistonType.STICKY == state.get(MovingPistonBlock.TYPE);
					Direction dir = ((PistonTileEntity) getTeAt(s)).getMotionDirection();

					if (getTimeInCoords(s) == 0) {
						BlockPos newPos;

						// Put the relay back, or drop it
						{
							int x = s.getPos().getX(), y = s.getPos().getY(), z = s.getPos().getZ();
							BlockPos pos = s.getPos();
							if (world.isAirBlock(pos.offset(dir))) {
								world.setBlockState(pos.offset(dir), ModBlocks.pistonRelay.getDefaultState());
							} else {
								ItemStack stack = new ItemStack(ModBlocks.pistonRelay);
								world.addEntity(new ItemEntity(world, x + dir.getXOffset(), y + dir.getYOffset(), z + dir.getZOffset(), stack));
							}
							checkedCoords.add(s);
							newPos = pos.offset(dir);
						}

						// Move the linked block and update the mapping
						if (data.mapping.containsKey(s.getPos())) {
							BlockPos destPos = data.mapping.get(s.getPos());

							BlockState srcState = world.getBlockState(destPos);
							TileEntity tile = world.getTileEntity(destPos);

							if (!sticky && tile == null && srcState.getPushReaction() == PushReaction.NORMAL && srcState.getBlockHardness(world, destPos) != -1 && !srcState.isAir(world, destPos)) {
								Material destMat = world.getBlockState(destPos.offset(dir)).getMaterial();
								if (world.isAirBlock(destPos.offset(dir)) || destMat.isReplaceable()) {
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
