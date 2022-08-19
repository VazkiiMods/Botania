/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 7, 2015, 6:14:18 PM (GMT)]
 */
package vazkii.botania.common.world;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import vazkii.botania.client.render.world.SkyblockSkyRenderer;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileManaFlame;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public final class SkyblockWorldEvents {

	private static final String TAG_MADE_ISLAND = "Botania-MadeIsland";
	private static final String TAG_HAS_OWN_ISLAND = "Botania-HasOwnIsland";
	private static final String TAG_ISLAND_X = "Botania-IslandX";
	private static final String TAG_ISLAND_Y = "Botania-IslandY";
	private static final String TAG_ISLAND_Z = "Botania-IslandZ";

	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event) {
		if(event.entityLiving instanceof EntityPlayer && !event.entity.worldObj.isRemote) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			NBTTagCompound data = player.getEntityData();
			if(!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
				data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());

			NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
			if(player.ticksExisted > 3 && !persist.getBoolean(TAG_MADE_ISLAND)) {
				World world = player.worldObj;
				if(WorldTypeSkyblock.isWorldSkyblock(world)) {
					ChunkCoordinates coords = world.getSpawnPoint();
					if(world.getBlock(coords.posX, coords.posY - 4, coords.posZ) != Blocks.bedrock && world.provider.dimensionId == 0)
						spawnPlayer(player, coords.posX, coords.posY, coords.posZ, false);
				}


				persist.setBoolean(TAG_MADE_ISLAND, true);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(WorldTypeSkyblock.isWorldSkyblock(event.world)) {
			ItemStack equipped = event.entityPlayer.getCurrentEquippedItem();
			if(event.action == Action.RIGHT_CLICK_BLOCK && equipped == null && event.entityPlayer.isSneaking()) {
				Block block = event.world.getBlock(event.x, event.y, event.z);
				if(block == Blocks.grass || block == Blocks.dirt) {
					if(event.world.isRemote)
						event.entityPlayer.swingItem();
					else {
						event.world.playSoundEffect(event.x + 0.5, event.y + 0.5, event.z + 0.5, block.stepSound.getBreakSound(), block.stepSound.getVolume() * 0.4F, block.stepSound.getPitch() + (float) (Math.random() * 0.2 - 0.1));
						if(Math.random() < 0.8)
							event.entityPlayer.dropPlayerItemWithRandomChoice(new ItemStack(ModItems.manaResource, 1, 21), false);
					}
				}
			} else if(equipped != null && equipped.getItem() == Items.bowl && event.action == Action.RIGHT_CLICK_BLOCK && !event.world.isRemote) {
				MovingObjectPosition movingobjectposition = ToolCommons.raytraceFromEntity(event.world, event.entityPlayer, true, 4.5F);

				if(movingobjectposition != null) {
					if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !event.world.isRemote) {
						int i = movingobjectposition.blockX;
						int j = movingobjectposition.blockY;
						int k = movingobjectposition.blockZ;

						if(event.world.getBlock(i, j, k).getMaterial() == Material.water) {
							--equipped.stackSize;

							if(equipped.stackSize <= 0)
								event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.currentItem, new ItemStack(ModItems.waterBowl));
							else event.entityPlayer.dropPlayerItemWithRandomChoice(new ItemStack(ModItems.waterBowl), false);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onDrops(HarvestDropsEvent event) {
		if(WorldTypeSkyblock.isWorldSkyblock(event.world) && event.block == Blocks.tallgrass) {
			ItemStack stackToRemove = null;
			for(ItemStack stack : event.drops)
				if(stack.getItem() == Items.wheat_seeds && event.world.rand.nextInt(4) == 0) {
					stackToRemove = stack;
					break;
				}

			if(stackToRemove != null) {
				event.drops.remove(stackToRemove);
				event.drops.add(new ItemStack(event.world.rand.nextBoolean() ? Items.pumpkin_seeds : Items.melon_seeds));
			}
		}
	}
	
	public static void spawnPlayer(EntityPlayer player, int x, int y, int z, boolean fabricated) {
		NBTTagCompound data = player.getEntityData();
		if(!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
			data.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
		NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

		final boolean test = false;

		if(test || !persist.getBoolean(TAG_HAS_OWN_ISLAND)) {
			createSkyblock(player.worldObj, x, y, z);

			if(player instanceof EntityPlayerMP) {
				EntityPlayerMP pmp = (EntityPlayerMP) player;
				pmp.setPositionAndUpdate(x + 0.5, y + 1.6, z + 0.5);
				pmp.setSpawnChunk(new ChunkCoordinates(x, y, z), true);
				player.inventory.addItemStackToInventory(new ItemStack(ModItems.lexicon));
			}

			if(fabricated) {
				persist.setBoolean(TAG_HAS_OWN_ISLAND, true);
				persist.setDouble(TAG_ISLAND_X, player.posX);
				persist.setDouble(TAG_ISLAND_Y, player.posY);
				persist.setDouble(TAG_ISLAND_Z, player.posZ);
			}
		} else {
			double posX = persist.getDouble(TAG_ISLAND_X);
			double posY = persist.getDouble(TAG_ISLAND_Y);
			double posZ = persist.getDouble(TAG_ISLAND_Z);

			if(player instanceof EntityPlayerMP) {
				EntityPlayerMP pmp = (EntityPlayerMP) player;
				pmp.setPositionAndUpdate(posX, posY, posZ);
			}
		}
	}

	public static void createSkyblock(World world, int x, int y, int z) {
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 4; j++)
				for(int k = 0; k < 3; k++)
					world.setBlock(x - 1 + i, y - 1 - j, z - 1 + k, j == 0 ? Blocks.grass : Blocks.dirt);
		world.setBlock(x - 1, y - 2, z, Blocks.flowing_water);
		world.setBlock(x + 1, y + 2, z + 1, ModBlocks.manaFlame);
		((TileManaFlame) world.getTileEntity(x + 1, y + 2, z + 1)).setColor(new Color(70 + world.rand.nextInt(185), 70 + world.rand.nextInt(185), 70 + world.rand.nextInt(185)).getRGB());

		int[][] rootPositions = new int[][] {
				{ -1, -3, -1 },
				{ -2, -4, -1 },
				{ -2, -4, -2 },
				{ +1, -4, -1 },
				{ +1, -5, -1 },
				{ +2, -5, -1 },
				{ +2, -6, +0 },
				{ +0, -4, +2 },
				{ +0, -5, +2 },
				{ +0, -5, +3 },
				{ +0, -6, +3 },
		};
		for(int[] root : rootPositions)
			world.setBlock(x + root[0], y + root[1], z + root[2], ModBlocks.root);

		world.setBlock(x, y - 4, z, Blocks.bedrock);
	}

}
