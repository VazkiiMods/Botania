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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.WorldEvent;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;

public class BlockPistonRelay extends BlockMod implements IWandable, ILexiconable {

	public static Map<String, String> playerPositions = new HashMap();
	public static Map<String, String> mappedPositions = new HashMap();

	static List<String> removeThese = new ArrayList();
	static List<String> checkedCoords = new ArrayList();
	static Map<String, Integer> coordsToCheck = new HashMap();

	public BlockPistonRelay() {
		super(Material.gourd);
		setBlockName(LibBlockNames.PISTON_RELAY);
		setHardness(2F);
		setResistance(10F);
		setStepSound(soundTypeMetal);

		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 0;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
		mapCoords(par1World.provider.dimensionId, par2, par3, par4, 2);
	}

	public static String getCoordsAsString(int world, int x, int y, int z) {
		return world + ":" + x + ":" + y + ":" + z;
	}

	static void mapCoords(int world, int x, int y, int z, int time) {
		String coords = getCoordsAsString(world, x, y, z);
		coordsToCheck.put(coords, time);
	}

	static void decrCoords(String key) {
		int time = getTimeInCoords(key);

		if(time <= 0)
			removeThese.add(key);
		else coordsToCheck.put(key, time - 1);
	}

	static int getTimeInCoords(String key) {
		return coordsToCheck.get(key);
	}

	static Block getBlockAt(String key) {
		MinecraftServer server = MinecraftServer.getServer();
		if(server == null)
			return Blocks.air;

		String[] tokens = key.split(":");
		int worldId = Integer.parseInt(tokens[0]), x = Integer.parseInt(tokens[1]), y = Integer.parseInt(tokens[2]), z = Integer.parseInt(tokens[3]);
		World world = server.worldServerForDimension(worldId);
		return world.getBlock(x, y, z);
	}

	static int getBlockMetaAt(String key) {
		MinecraftServer server = MinecraftServer.getServer();
		if(server == null)
			return 0;

		String[] tokens = key.split(":");
		int worldId = Integer.parseInt(tokens[0]), x = Integer.parseInt(tokens[1]), y = Integer.parseInt(tokens[2]), z = Integer.parseInt(tokens[3]);
		World world = server.worldServerForDimension(worldId);
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		if(player == null)
			return false;

		if(!player.isSneaking()) {
			playerPositions.put(player.getCommandSenderName(), getCoordsAsString(world.provider.dimensionId, x, y, z));
			world.playSoundEffect(x, y, z, "botania:ding", 0.5F, 1F);
		} else {
			dropBlockAsItem(world, x, y, z, new ItemStack(this));
			world.setBlockToAir(x, y, z);
			if(!world.isRemote)
				world.playAuxSFX(2001, x, y , z, Block.getIdFromBlock(this));
		}

		return true;
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		WorldData.get(event.world);
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event) {
		WorldData.get(event.world).markDirty();
	}

	public static class WorldData extends WorldSavedData {

		private static final String ID = "PistonRelayPairs";

		public WorldData(String id) {
			super(id);
		}

		@Override
		public void readFromNBT(NBTTagCompound nbttagcompound) {
			mappedPositions.clear();

			Collection<String> tags = nbttagcompound.func_150296_c();
			for(String key : tags) {
				NBTBase tag = nbttagcompound.getTag(key);
				if(tag instanceof NBTTagString) {
					String value = ((NBTTagString) tag).func_150285_a_();

					mappedPositions.put(key, value);
				}
			}
		}

		@Override
		public void writeToNBT(NBTTagCompound nbttagcompound) {
			for(String s : mappedPositions.keySet())
				nbttagcompound.setString(s, mappedPositions.get(s));
		}

		public static WorldData get(World world) {
			if(world.mapStorage == null)
				return null;

			WorldData data = (WorldData) world.mapStorage.loadData(WorldData.class, ID);

			if (data == null) {
				data = new WorldData(ID);
				data.markDirty();
				world.mapStorage.setData(ID, data);
			}
			return data;
		}
	}

	@SubscribeEvent
	public void tickEnd(TickEvent event) {
		if(event.type == Type.SERVER && event.phase == Phase.END) {
			List<String> coordsToCheckCopy = new ArrayList(coordsToCheck.keySet());
			for(String s : coordsToCheckCopy) {
				decrCoords(s);
				if(checkedCoords.contains(s))
					continue;

				Block block = getBlockAt(s);
				if(block == Blocks.piston_extension) {
					int meta = getBlockMetaAt(s);
					boolean sticky = (meta & 8) == 8;
					ForgeDirection dir = ForgeDirection.getOrientation(meta & ~8);

					MinecraftServer server = MinecraftServer.getServer();

					if(server != null && getTimeInCoords(s) == 0) {
						String newPos;

						{
							String[] tokens = s.split(":");
							int worldId = Integer.parseInt(tokens[0]), x = Integer.parseInt(tokens[1]), y = Integer.parseInt(tokens[2]), z = Integer.parseInt(tokens[3]);
							World world = server.worldServerForDimension(worldId);
							if(world.isAirBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ))
								world.setBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, ModBlocks.pistonRelay);
							else if(!world.isRemote) {
								ItemStack stack = new ItemStack(ModBlocks.pistonRelay);
								world.spawnEntityInWorld(new EntityItem(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, stack));
							}
							checkedCoords.add(s);
							newPos = getCoordsAsString(world.provider.dimensionId, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
						}

						if(mappedPositions.containsKey(s)) {
							String pos = mappedPositions.get(s);
							String[] tokens = pos.split(":");
							int worldId = Integer.parseInt(tokens[0]), x = Integer.parseInt(tokens[1]), y = Integer.parseInt(tokens[2]), z = Integer.parseInt(tokens[3]);
							World world = server.worldServerForDimension(worldId);

							Block srcBlock = world.getBlock(x, y, z);
							int srcMeta = world.getBlockMetadata(x, y, z);
							TileEntity tile = world.getTileEntity(x, y, z);
							Material mat = srcBlock.getMaterial();

							if(!sticky && tile == null && mat.getMaterialMobility() == 0 && srcBlock.getBlockHardness(world, x, y, z) != -1 && !srcBlock.isAir(world, x, y, z)) {
								Material destMat = world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ).getMaterial();
								if(world.isAirBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) || destMat.isReplaceable()) {
									world.setBlock(x, y, z, Blocks.air);
									world.setBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, srcBlock, srcMeta, 1 | 2);
									mappedPositions.put(s, getCoordsAsString(world.provider.dimensionId, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ));
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

		// ConcurrentModificationException failsafe
		ArrayList<String> remove = new ArrayList(removeThese);
		for(String s : remove) {
			coordsToCheck.remove(s);
			if(checkedCoords.contains(s))
				checkedCoords.remove(s);
		}
		removeThese.clear();
	}

	public void save(World world) {
		WorldData data = WorldData.get(world);
		if(data != null)
			data.markDirty();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.pistonRelay;
	}
}
