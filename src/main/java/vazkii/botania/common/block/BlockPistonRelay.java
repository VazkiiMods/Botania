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
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.PistonType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
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
	public int quantityDropped(IBlockState state, @Nonnull Random random) {
		return 0;
	}

	@Override
	public void onReplaced(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState newState, boolean isMoving) {
		// todo 1.13 utilize isMoving?
		if(!world.isRemote)
			mapCoords(world.getDimension().getId(), pos, 2);
	}

	private void mapCoords(int world, BlockPos pos, int time) {
		coordsToCheck.put(new DimWithPos(world, pos), time);
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

	private IBlockState getStateAt(DimWithPos key) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if(server == null)
			return Blocks.AIR.getDefaultState();
		return server.getWorld(key.dim).getBlockState(key.blockPos);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		if(player == null || world.isRemote)
			return false;

		if(!player.isSneaking()) {
			playerPositions.put(player.getUniqueID(), new DimWithPos(world.getDimension().getId(), pos));
			world.playSound(null, pos, ModSounds.ding, SoundCategory.BLOCKS, 0.5F, 1F);
		} else {
			spawnAsEntity(world, pos, new ItemStack(this));
			world.removeBlock(pos);
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

		public WorldData(String id) {
			super(id);
		}

		@Override
		public void read(@Nonnull NBTTagCompound nbttagcompound) {
			((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.clear();

			Collection<String> tags = nbttagcompound.keySet();
			for(String key : tags) {
				INBTBase tag = nbttagcompound.getTag(key);
				if(tag instanceof NBTTagString) {
					String value = tag.getString();

					((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.put(DimWithPos.fromString(key), DimWithPos.fromString(value));
				}
			}
		}

		@Nonnull
		@Override
		public NBTTagCompound write(@Nonnull NBTTagCompound nbttagcompound) {
			for(DimWithPos s : ((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.keySet())
				nbttagcompound.setString(s.toString(), ((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.get(s).toString());
			return nbttagcompound;
		}

		public static WorldData get(World world) {
			if(world.getMapStorage() == null)
				return null;

			WorldData data = world.getMapStorage().getOrLoadData(WorldData::new, ID);

			if (data == null) {
				data = new WorldData(ID);
				data.markDirty();
				world.getMapStorage().setData(ID, data);
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
					IBlockState state = getStateAt(s);
					boolean sticky = PistonType.STICKY == state.get(BlockPistonMoving.TYPE);
					EnumFacing dir = state.get(BlockPistonMoving.FACING);

					MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

					if(server != null && getTimeInCoords(s) == 0) {
						DimWithPos newPos;

						{
							int worldId = s.dim, x = s.blockPos.getX(), y = s.blockPos.getY(), z = s.blockPos.getZ();
							BlockPos pos = s.blockPos;
							World world = server.getWorld(worldId);
							if(world.isAirBlock(pos.offset(dir)))
								world.setBlockState(pos.offset(dir), ModBlocks.pistonRelay.getDefaultState());
							else if(!world.isRemote) {
								ItemStack stack = new ItemStack(ModBlocks.pistonRelay);
								world.spawnEntity(new EntityItem(world, x + dir.getXOffset(), y + dir.getYOffset(), z + dir.getZOffset(), stack));
							}
							checkedCoords.add(s);
							newPos = new DimWithPos(world.getDimension().getId(), pos.offset(dir));
						}

						if(mappedPositions.containsKey(s)) {
							DimWithPos pos = mappedPositions.get(s);
							int worldId = pos.dim;
							BlockPos pos2 = pos.blockPos;
							World world = server.getWorld(worldId);

							IBlockState srcState = world.getBlockState(pos2);
							TileEntity tile = world.getTileEntity(pos2);
							Material mat = srcState.getMaterial();

							if(!sticky && tile == null && mat.getPushReaction() == EnumPushReaction.NORMAL && srcState.getBlockHardness(world, pos2) != -1 && !srcState.getBlock().isAir(srcState, world, pos2)) {
								Material destMat = world.getBlockState(pos2.offset(dir)).getMaterial();
								if(world.isAirBlock(pos2.offset(dir)) || destMat.isReplaceable()) {
									world.setBlockState(pos2, Blocks.AIR.getDefaultState());
									world.setBlockState(pos2.offset(dir), srcState, 1 | 2);
									mappedPositions.put(s, new DimWithPos(world.getDimension().getId(), pos2.offset(dir)));
								}
							}

							pos = mappedPositions.get(s);
							mappedPositions.remove(s);
							mappedPositions.put(newPos, pos);
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

	public void save(World world) {
		WorldData data = WorldData.get(world);
		if(data != null)
			data.markDirty();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.pistonRelay;
	}

	public static class DimWithPos {
		public final int dim;
		public final BlockPos blockPos;

		public DimWithPos(int dim, BlockPos pos) {
			this.dim = dim;
			blockPos = pos;
		}

		@Override
		public int hashCode() {
			return 31 * dim ^ blockPos.hashCode();
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof DimWithPos
					&& dim == ((DimWithPos) o).dim
					&& blockPos.equals(((DimWithPos) o).blockPos);
		}

		@Override
		public String toString() {
			return dim + ":" + blockPos.getX() + ":" + blockPos.getY() + ":" + blockPos.getZ();
		}

		public static DimWithPos fromString(String s) {
			String[] split = s.split(":");
			return new DimWithPos(Integer.parseInt(split[0]), new BlockPos(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])));
		}

	}

}
