/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 20, 2014, 4:57:36 PM (GMT)]
 */
package vazkii.botania.common.block;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
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
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.PistonType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BlockPistonRelay extends BlockMod implements IWandable {

	// Currently active binding attempts
	public final Map<UUID, GlobalPos> activeBindingAttempts = new HashMap<>();

	// Bindings
	public final Map<GlobalPos, GlobalPos> mappedPositions = new HashMap<>();

	private final Set<GlobalPos> removeQueue = new HashSet<>();
	private final Set<GlobalPos> checkedCoords = new HashSet<>();
	private final Map<GlobalPos, Integer> coordsToCheck = new HashMap<>();

	public BlockPistonRelay(Properties builder) {
		super(builder);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean canEntitySpawn(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, EntityType<?> type) {
		return false;
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if(!world.isRemote)
			mapCoords(world.getDimension().getType(), pos, 2);
	}

	private void mapCoords(DimensionType type, BlockPos pos, int time) {
		coordsToCheck.put(GlobalPos.of(type, pos), time);
	}

	private void decrCoords(GlobalPos key) {
		int time = getTimeInCoords(key);

		if(time <= 0)
			removeQueue.add(key);
		else coordsToCheck.merge(key, -1, Integer::sum);
	}

	private int getTimeInCoords(GlobalPos key) {
		return coordsToCheck.getOrDefault(key, 0);
	}

	private Block getBlockAt(GlobalPos key) {
		return getStateAt(key).getBlock();
	}

	private BlockState getStateAt(GlobalPos key) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if(server == null)
			return Blocks.AIR.getDefaultState();
		return server.getWorld(key.getDimension()).getBlockState(key.getPos());
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		if(player == null || world.isRemote)
			return false;

		if(!player.isSneaking()) {
			GlobalPos clicked = GlobalPos.of(world.getDimension().getType(), pos.toImmutable());
			if(ItemTwigWand.getBindMode(stack)) {
				activeBindingAttempts.put(player.getUniqueID(), clicked);
				world.playSound(null, pos, ModSounds.ding, SoundCategory.BLOCKS, 0.5F, 1F);
			} else {
				GlobalPos dest = mappedPositions.get(clicked);
				if (dest != null) {
					PacketHandler.sendToNearby(world, pos,
							new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.PARTICLE_BEAM,
									pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
									dest.getPos().getX(), dest.getPos().getY(), dest.getPos().getZ()));
				}
			}
		} else {
			spawnAsEntity(world, pos, new ItemStack(this));
			world.destroyBlock(pos, false);
		}

		return true;
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		if(!event.getWorld().getWorld().isRemote)
			WorldData.get(event.getWorld().getWorld());
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		if(!event.getWorld().getWorld().isRemote)
			WorldData.get(event.getWorld().getWorld()).markDirty();
	}

	public static class WorldData extends WorldSavedData {

		private static final String ID = "PistonRelayPairs";

		public WorldData() {
			super(ID);
		}

		@Override
		public void read(@Nonnull CompoundNBT cmp) {
			((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.clear();

			ListNBT list = cmp.getList("list", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < list.size(); i += 2) {
				CompoundNBT from = list.getCompound(i);
				CompoundNBT to = list.getCompound(i + 1);
				GlobalPos fromPos = GlobalPos.deserialize(new Dynamic<>(NBTDynamicOps.INSTANCE, from));
				GlobalPos toPos = GlobalPos.deserialize(new Dynamic<>(NBTDynamicOps.INSTANCE, to));

				((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.put(fromPos, toPos);
			}
		}

		@Nonnull
		@Override
		public CompoundNBT write(@Nonnull CompoundNBT cmp) {
			ListNBT list = new ListNBT();
			for (Map.Entry<GlobalPos, GlobalPos> e : ((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.entrySet()) {
				list.add(e.getKey().serialize(NBTDynamicOps.INSTANCE));
				list.add(e.getValue().serialize(NBTDynamicOps.INSTANCE));
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

	@SubscribeEvent
	public void tickEnd(TickEvent.ServerTickEvent event) {
		if(event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END) {
			for(GlobalPos s : coordsToCheck.keySet()) {
				decrCoords(s);
				if(checkedCoords.contains(s))
					continue;

				Block block = getBlockAt(s);
				if(block == Blocks.MOVING_PISTON) {
					BlockState state = getStateAt(s);
					boolean sticky = PistonType.STICKY == state.get(MovingPistonBlock.TYPE);
					Direction dir = state.get(MovingPistonBlock.FACING);

					MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

					if(server != null && getTimeInCoords(s) == 0) {
						GlobalPos newPos;

						// Put the relay back, or drop it
						{
							int x = s.getPos().getX(), y = s.getPos().getY(), z = s.getPos().getZ();
							BlockPos pos = s.getPos();
							World world = server.getWorld(s.getDimension());
							if(world.isAirBlock(pos.offset(dir)))
								world.setBlockState(pos.offset(dir), ModBlocks.pistonRelay.getDefaultState());
							else if(!world.isRemote) {
								ItemStack stack = new ItemStack(ModBlocks.pistonRelay);
								world.addEntity(new ItemEntity(world, x + dir.getXOffset(), y + dir.getYOffset(), z + dir.getZOffset(), stack));
							}
							checkedCoords.add(s);
							newPos = GlobalPos.of(world.getDimension().getType(), pos.offset(dir));
						}

						// Move the linked block and update the mapping
						if(mappedPositions.containsKey(s)) {
							GlobalPos dest = mappedPositions.get(s);
							BlockPos destPos = dest.getPos();
							World world = server.getWorld(dest.getDimension());

							BlockState srcState = world.getBlockState(destPos);
							TileEntity tile = world.getTileEntity(destPos);

							if(!sticky && tile == null && srcState.getPushReaction() == PushReaction.NORMAL && srcState.getBlockHardness(world, destPos) != -1 && !srcState.isAir(world, destPos)) {
								Material destMat = world.getBlockState(destPos.offset(dir)).getMaterial();
								if(world.isAirBlock(destPos.offset(dir)) || destMat.isReplaceable()) {
									world.setBlockState(destPos, Blocks.AIR.getDefaultState());
									world.setBlockState(destPos.offset(dir), srcState);
									mappedPositions.put(s, GlobalPos.of(world.getDimension().getType(), destPos.offset(dir)));
								}
							}

							dest  = mappedPositions.get(s);
							mappedPositions.remove(s);
							mappedPositions.put(newPos, dest);
							save(world);
						}
					}
				}
			}
		}

		coordsToCheck.keySet().removeAll(removeQueue);
		checkedCoords.removeAll(removeQueue);
		removeQueue.clear();
	}

	private void save(World world) {
		WorldData data = WorldData.get(world);
		if(data != null)
			data.markDirty();
	}
}
