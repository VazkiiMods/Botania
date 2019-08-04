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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MovingPistonBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.PistonType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class BlockPistonRelay extends BlockMod implements IWandable, ILexiconable {

	// Currently active binding attempts
	public final Map<UUID, DimWithPos> playerPositions = new HashMap<>();

	// Bindings
	public final Map<DimWithPos, DimWithPos> mappedPositions = new HashMap<>();

	private final Set<DimWithPos> removeQueue = new HashSet<>();
	private final Set<DimWithPos> checkedCoords = new HashSet<>();
	private final Map<DimWithPos, Integer> coordsToCheck = new HashMap<>();

	public BlockPistonRelay(Properties builder) {
		super(builder);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
		if(!world.isRemote)
			mapCoords(world.getDimension().getType(), pos, 2);
	}

	private void mapCoords(DimensionType type, BlockPos pos, int time) {
		coordsToCheck.put(new DimWithPos(type, pos), time);
	}

	private void decrCoords(DimWithPos key) {
		int time = getTimeInCoords(key);

		if(time <= 0)
			removeQueue.add(key);
		else coordsToCheck.merge(key, -1, Integer::sum);
	}

	private int getTimeInCoords(DimWithPos key) {
		return coordsToCheck.getOrDefault(key, 0);
	}

	private Block getBlockAt(DimWithPos key) {
		return getStateAt(key).getBlock();
	}

	private BlockState getStateAt(DimWithPos key) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if(server == null)
			return Blocks.AIR.getDefaultState();
		return server.getWorld(key.dim).getBlockState(key.blockPos);
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		if(player == null || world.isRemote)
			return false;

		if(!player.isSneaking()) {
			playerPositions.put(player.getUniqueID(), new DimWithPos(world.getDimension().getType(), pos));
			world.playSound(null, pos, ModSounds.ding, SoundCategory.BLOCKS, 0.5F, 1F);
		} else {
			spawnAsEntity(world, pos, new ItemStack(this));
			world.removeBlock(pos, false);
			world.playEvent(2001, pos, Block.getStateId(getDefaultState()));
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
		public void read(@Nonnull CompoundNBT nbttagcompound) {
			((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.clear();

			Collection<String> tags = nbttagcompound.keySet();
			for(String key : tags) {
				INBT tag = nbttagcompound.get(key);
				if(tag instanceof StringNBT) {
					String value = tag.getString();
					DimWithPos from = DimWithPos.fromString(key);
					DimWithPos to = DimWithPos.fromString(value);

					if(from != null && to != null)
						((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.put(from, to);
				}
			}
		}

		@Nonnull
		@Override
		public CompoundNBT write(@Nonnull CompoundNBT nbttagcompound) {
			for(DimWithPos s : ((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.keySet())
				nbttagcompound.putString(s.toString(), ((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.get(s).toString());
			return nbttagcompound;
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
		if(event.type == Type.SERVER && event.phase == Phase.END) {
			for(DimWithPos s : coordsToCheck.keySet()) {
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
						DimWithPos newPos;

						// Put the relay back, or drop it
						{
							int x = s.blockPos.getX(), y = s.blockPos.getY(), z = s.blockPos.getZ();
							BlockPos pos = s.blockPos;
							World world = server.getWorld(s.dim);
							if(world.isAirBlock(pos.offset(dir)))
								world.setBlockState(pos.offset(dir), ModBlocks.pistonRelay.getDefaultState());
							else if(!world.isRemote) {
								ItemStack stack = new ItemStack(ModBlocks.pistonRelay);
								world.addEntity(new ItemEntity(world, x + dir.getXOffset(), y + dir.getYOffset(), z + dir.getZOffset(), stack));
							}
							checkedCoords.add(s);
							newPos = new DimWithPos(world.getDimension().getType(), pos.offset(dir));
						}

						// Move the linked block and update the mapping
						if(mappedPositions.containsKey(s)) {
							DimWithPos dest = mappedPositions.get(s);
							BlockPos destPos = dest.blockPos;
							World world = server.getWorld(dest.dim);

							BlockState srcState = world.getBlockState(destPos);
							TileEntity tile = world.getTileEntity(destPos);

							if(!sticky && tile == null && srcState.getPushReaction() == PushReaction.NORMAL && srcState.getBlockHardness(world, destPos) != -1 && !srcState.isAir(world, destPos)) {
								Material destMat = world.getBlockState(destPos.offset(dir)).getMaterial();
								if(world.isAirBlock(destPos.offset(dir)) || destMat.isReplaceable()) {
									world.setBlockState(destPos, Blocks.AIR.getDefaultState());
									world.setBlockState(destPos.offset(dir), srcState, 1 | 2);
									mappedPositions.put(s, new DimWithPos(world.getDimension().getType(), destPos.offset(dir)));
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

		for(DimWithPos s : removeQueue) {
			coordsToCheck.remove(s);
			checkedCoords.remove(s);
		}
		removeQueue.clear();
	}

	private void save(World world) {
		WorldData data = WorldData.get(world);
		if(data != null)
			data.markDirty();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, PlayerEntity player, ItemStack lexicon) {
		return LexiconData.pistonRelay;
	}

	public static class DimWithPos {
		public final DimensionType dim;
		public final BlockPos blockPos;

		public DimWithPos(DimensionType dim, BlockPos pos) {
			this.dim = dim;
			blockPos = pos;
		}

		@Override
		public int hashCode() {
			return 31 * dim.hashCode() ^ blockPos.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof DimWithPos
					&& dim.equals(((DimWithPos) o).dim)
					&& blockPos.equals(((DimWithPos) o).blockPos);
		}

		@Override
		public String toString() {
			return dim.toString() + "|" + blockPos.getX() + "|" + blockPos.getY() + "|" + blockPos.getZ();
		}

		@Nullable
		public static DimWithPos fromString(String s) {
			String[] split = s.split("\\|");
			ResourceLocation id = ResourceLocation.tryCreate(split[0]);
			if(id != null) {
				DimensionType type = DimensionType.byName(id);
				if(type != null) {
					return new DimWithPos(type, new BlockPos(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])));
				}
			}
			return null;
		}

	}

}
